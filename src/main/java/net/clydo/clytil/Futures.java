/*
 * This file is part of Clytil.
 *
 * Clytil is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Clytil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Clytil. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2025 ClydoNetwork
 */

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class Futures {

    public <V> CompletableFuture<List<V>> sequence(@NotNull List<? extends CompletableFuture<V>> futures) {
        if (futures.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }

        if (futures.size() == 1) {
            return futures.get(0).thenApply(List::of);
        }

        CompletableFuture<Void> completablefuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return completablefuture.thenApply((unused) -> futures.stream().map(CompletableFuture::join).toList());
    }

    /**
     * Like {@link #sequence(List)}, but fails as soon as any future fails, without waiting for the
     * others. The result list may contain {@code null}s.
     */
    public <V> @NotNull CompletableFuture<List<V>> sequenceFailFast(
            @NotNull final List<? extends CompletableFuture<? extends V>> futures
    ) {
        Validates.require(futures, "futures");

        val failure = new CompletableFuture<List<V>>();
        return collect(futures, failure::completeExceptionally)
                .applyToEither(failure, Function.identity());
    }

    /**
     * Like {@link #sequenceFailFast(List)}, and also cancels every future when the first one fails.
     */
    public <V> @NotNull CompletableFuture<List<V>> sequenceFailFastAndCancel(
            @NotNull final List<? extends CompletableFuture<? extends V>> futures
    ) {
        Validates.require(futures, "futures");

        val failure = new CompletableFuture<List<V>>();
        return collect(futures, exception -> {
            if (failure.completeExceptionally(exception)) {
                for (val future : futures) {
                    future.cancel(true);
                }
            }
        }).applyToEither(failure, Function.identity());
    }

    /**
     * Runs a task that submits its work to an {@link Executor}, running that work on the calling
     * thread until {@code done} accepts the task's result. Useful for driving asynchronous code
     * synchronously, such as at startup or in tests. If the thread is interrupted, it stops waiting
     * and keeps its interrupt flag.
     *
     * @param task receives the executor and returns a result to watch
     * @param done tells when the result is complete
     * @return the task's result
     */
    public <T> T runUntilDone(
            @NotNull final Function<Executor, T> task,
            @NotNull final Predicate<? super T> done
    ) {
        Validates.require(task, "task");
        Validates.require(done, "done");

        val queue = new LinkedBlockingQueue<Runnable>();
        val result = task.apply(queue::add);

        while (!done.test(result)) {
            try {
                val runnable = queue.poll(100L, TimeUnit.MILLISECONDS);
                if (runnable != null) {
                    runnable.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return result;
    }

    /**
     * Like {@link #runUntilDone(Function, Predicate)}, waiting for the returned future to complete.
     */
    public <T> CompletableFuture<T> runUntilDone(
            @NotNull final Function<Executor, CompletableFuture<T>> task
    ) {
        return runUntilDone(task, CompletableFuture::isDone);
    }

    /**
     * Strips {@link CompletionException} and {@link ExecutionException} wrappers, returning the
     * exception that actually failed the future.
     */
    public @NotNull Throwable unwrap(
            @NotNull final Throwable throwable
    ) {
        Throwable current = Validates.require(throwable, "throwable");
        while ((current instanceof CompletionException || current instanceof ExecutionException)
                && current.getCause() != null) {
            current = current.getCause();
        }

        return current;
    }

    @SuppressWarnings("unchecked")
    private <V> @NotNull CompletableFuture<List<V>> collect(
            final @NotNull List<? extends CompletableFuture<? extends V>> futures,
            final @NotNull Consumer<Throwable> onFailure
    ) {
        val results = new Object[futures.size()];
        val stages = new CompletableFuture<?>[futures.size()];

        for (int i = 0; i < stages.length; i++) {
            val index = i;
            stages[i] = futures.get(i).whenComplete((result, exception) -> {
                if (exception != null) {
                    onFailure.accept(exception);
                } else {
                    results[index] = result;
                }
            });
        }

        return CompletableFuture.allOf(stages)
                .thenApply(unused -> Collections.unmodifiableList((List<V>) Arrays.asList(results)));
    }

}
