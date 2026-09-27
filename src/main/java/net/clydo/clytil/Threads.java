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

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread pool helpers: named {@link ThreadFactory} creation and
 * cached/fixed pool factories.
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
                thread.setUncaughtExceptionHandler((t, e) -> {
                    val cause = e instanceof CompletionException && e.getCause() != null
                            ? e.getCause()
                            : e;

                    handler.uncaughtException(t, cause);
                });
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

}
