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
 * Copyright (C) 2026 ClydoNetwork
 */

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Thread pool helpers: named {@link ThreadFactory} creation, cached/fixed/work-stealing pool
 * factories, shutdown, and thread and task naming.
 */
@UtilityClass
public class Threads {

    /**
     * Creates a thread factory naming and configuring threads after the given arguments.
     *
     * @param nameFormat a {@link String#format(String, Object...)} pattern
     *                   taking an incrementing int, e.g. {@code "worker-%d"}
     * @param handler    uncaught exception handler, or {@code null} for the default
     * @return the thread factory
     */
    public ThreadFactory threadFactory(
            @NotNull final String nameFormat,
            @Nullable final Thread.UncaughtExceptionHandler handler
    ) {
        Validates.require(nameFormat, "nameFormat");

        val counter = new AtomicInteger();
        return runnable -> {
            val thread = new Thread(runnable, String.format(nameFormat, counter.getAndIncrement()));
            if (handler != null) {
                thread.setUncaughtExceptionHandler(unwrapping(handler));
            }
            return thread;
        };
    }

    /**
     * Creates a cached (unbounded, elastic) thread pool.
     *
     * @param nameFormat thread name pattern, see {@link #threadFactory(String, Thread.UncaughtExceptionHandler)}
     * @return the pool
     */
    public ExecutorService newCachedPool(
            @NotNull final String nameFormat,
            @Nullable final Thread.UncaughtExceptionHandler handler
    ) {
        return Executors.newCachedThreadPool(threadFactory(nameFormat, handler));
    }

    /**
     * Creates a fixed thread pool.
     *
     * @param nameFormat thread name pattern, see {@link #threadFactory(String, Thread.UncaughtExceptionHandler)}
     * @param threads    number of threads, must be positive
     * @return the pool
     */
    public ExecutorService newFixedPool(
            @NotNull final String nameFormat,
            final int threads,
            @Nullable final Thread.UncaughtExceptionHandler handler
    ) {
        Validates.requirePositive(threads, "threads");
        return Executors.newFixedThreadPool(threads, threadFactory(nameFormat, handler));
    }

    /**
     * Creates a work-stealing {@link ForkJoinPool} in FIFO (async) mode, suited to many small
     * independent tasks.
     *
     * @param nameFormat thread name pattern, see {@link #threadFactory(String, Thread.UncaughtExceptionHandler)}
     * @param threads    parallelism, must be positive
     * @return the pool
     */
    public ForkJoinPool newWorkStealingPool(
            @NotNull final String nameFormat,
            final int threads,
            @Nullable final Thread.UncaughtExceptionHandler handler
    ) {
        Validates.require(nameFormat, "nameFormat");
        Validates.requirePositive(threads, "threads");

        val counter = new AtomicInteger();
        return new ForkJoinPool(
                threads,
                pool -> {
                    val thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                    thread.setName(String.format(nameFormat, counter.getAndIncrement()));
                    return thread;
                },
                handler == null ? null : unwrapping(handler),
                true
        );
    }

    /**
     * Returns a worker count for a background pool: one less than the available processors, so
     * the calling thread keeps a core, clamped to {@code [1, max]}.
     */
    public int backgroundThreadCount(
            final int max
    ) {
        Validates.requirePositive(max, "max");

        return Maths.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, max);
    }

    /**
     * Shuts {@code executor} down and waits up to {@code timeout} for its tasks to finish, then
     * interrupts any still running.
     *
     * @return whether the executor finished within the timeout
     */
    public boolean shutdownAndAwait(
            @NotNull final ExecutorService executor,
            final long timeout,
            @NotNull final TimeUnit unit
    ) {
        Validates.require(executor, "executor");
        Validates.require(unit, "unit");

        executor.shutdown();
        try {
            if (executor.awaitTermination(timeout, unit)) {
                return true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdownNow();
        return false;
    }

    /**
     * Runs {@code runnable} with the current thread renamed to {@code name}, which shows up in
     * thread dumps and profilers, then restores the old name.
     */
    public void runNamed(
            @NotNull final String name,
            @NotNull final Runnable runnable
    ) {
        Validates.require(name, "name");
        Validates.require(runnable, "runnable");

        val thread = Thread.currentThread();
        val oldName = thread.getName();
        thread.setName(name);
        try {
            runnable.run();
        } finally {
            thread.setName(oldName);
        }
    }

    /**
     * Wraps {@code runnable} so its {@code toString()} returns {@code name}, which makes tasks
     * readable in logs and executor queues.
     */
    public @NotNull Runnable named(
            @NotNull final Runnable runnable,
            @NotNull final String name
    ) {
        Validates.require(runnable, "runnable");
        Validates.require(name, "name");

        return new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    /**
     * Wraps {@code supplier} so its {@code toString()} returns {@code name}.
     */
    public <T> @NotNull Supplier<T> named(
            @NotNull final Supplier<T> supplier,
            @NotNull final String name
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(name, "name");

        return new Supplier<>() {
            @Override
            public T get() {
                return supplier.get();
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    private Thread.UncaughtExceptionHandler unwrapping(
            @NotNull final Thread.UncaughtExceptionHandler handler
    ) {
        return (thread, exception) -> handler.uncaughtException(thread, Futures.unwrap(exception));
    }

}
