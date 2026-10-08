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

package net.clydo.clytil.cache;

import lombok.experimental.UtilityClass;
import net.clydo.clytil.Validates;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Wraps functions and suppliers so each result is computed once and then reused.
 * <p>
 * Every wrapper is thread-safe, accepts {@code null} arguments, and caches {@code null} results.
 * A computation that throws caches nothing, so the next call tries again. The returned
 * {@link MemoizedFunction}, {@link MemoizedBiFunction} and {@link MemoizedSupplier} let callers
 * drop cached results.
 * <p>
 * Unbounded caches never evict, so use them only for small, bounded argument sets; otherwise pass
 * a {@code maxSize}, a time-to-live, or use {@link #weakKeys(Function)}.
 */
@UtilityClass
public class Memoize {

    // ---------- Functions ----------

    /**
     * Memoizes {@code function} with an unbounded cache. Each result is computed exactly once, but
     * the function must not call its own memoized wrapper; use {@link #recursive(BiFunction)} for
     * that.
     */
    @Contract("_ -> new")
    public <T, R> @NotNull MemoizedFunction<T, R> function(
            @NotNull final Function<? super T, ? extends R> function
    ) {
        Validates.require(function, "function");

        return new ConcurrentMemoizedFunction<>(function);
    }

    /**
     * Memoizes {@code function}, keeping at most {@code maxSize} results and evicting the least
     * recently used one first. Under contention, a result may be computed more than once.
     */
    @Contract("_, _ -> new")
    public <T, R> @NotNull MemoizedFunction<T, R> function(
            @NotNull final Function<? super T, ? extends R> function,
            final int maxSize
    ) {
        Validates.require(function, "function");
        Validates.requirePositive(maxSize, "maxSize");

        return new LockedMemoizedFunction<>(function, new LruMap<>(maxSize));
    }

    /**
     * Memoizes {@code function}, recomputing each result once it is older than {@code ttl}. Under
     * contention, a result may be computed more than once.
     */
    @Contract("_, _ -> new")
    public <T, R> @NotNull MemoizedFunction<T, R> function(
            @NotNull final Function<? super T, ? extends R> function,
            @NotNull final Duration ttl
    ) {
        Validates.require(function, "function");

        return new ExpiringMemoizedFunction<>(function, new HashMap<>(), toNanos(ttl));
    }

    /**
     * Memoizes {@code function}, keeping at most {@code maxSize} results, evicting the least
     * recently used one first, and recomputing each result once it is older than {@code ttl}.
     */
    @Contract("_, _, _ -> new")
    public <T, R> @NotNull MemoizedFunction<T, R> function(
            @NotNull final Function<? super T, ? extends R> function,
            final int maxSize,
            @NotNull final Duration ttl
    ) {
        Validates.require(function, "function");
        Validates.requirePositive(maxSize, "maxSize");

        return new ExpiringMemoizedFunction<>(function, new LruMap<>(maxSize), toNanos(ttl));
    }

    /**
     * Memoizes {@code function}, holding its arguments weakly: a result is dropped once nothing
     * else references its argument. Suited to caches keyed by {@link Class} or other objects that
     * should be free to be garbage collected. Arguments are compared with {@code equals}, and a
     * result that references its own argument keeps it alive.
     */
    @Contract("_ -> new")
    public <T, R> @NotNull MemoizedFunction<T, R> weakKeys(
            @NotNull final Function<? super T, ? extends R> function
    ) {
        Validates.require(function, "function");

        return new LockedMemoizedFunction<>(function, new WeakHashMap<>());
    }

    /**
     * Memoizes a function that calls itself. The body receives the memoized function to recurse
     * through, so every intermediate result is cached too:
     * <pre>{@code
     * MemoizedFunction<Integer, Long> fib = Memoize.recursive((self, n) ->
     *         n < 2 ? n : self.apply(n - 1) + self.apply(n - 2));
     * }</pre>
     * Under contention, a result may be computed more than once.
     */
    @Contract("_ -> new")
    public <T, R> @NotNull MemoizedFunction<T, R> recursive(
            @NotNull final BiFunction<? super Function<T, R>, ? super T, ? extends R> body
    ) {
        Validates.require(body, "body");

        return new RecursiveMemoizedFunction<>(body);
    }

    // ---------- BiFunctions ----------

    /**
     * Memoizes {@code function} with an unbounded cache.
     *
     * @see #function(Function)
     */
    @Contract("_ -> new")
    public <T, U, R> @NotNull MemoizedBiFunction<T, U, R> biFunction(
            @NotNull final BiFunction<? super T, ? super U, ? extends R> function
    ) {
        Validates.require(function, "function");

        return new SpreadMemoizedBiFunction<>(function, Memoize::function);
    }

    /**
     * Memoizes {@code function}, keeping at most {@code maxSize} results.
     *
     * @see #function(Function, int)
     */
    @Contract("_, _ -> new")
    public <T, U, R> @NotNull MemoizedBiFunction<T, U, R> biFunction(
            @NotNull final BiFunction<? super T, ? super U, ? extends R> function,
            final int maxSize
    ) {
        Validates.require(function, "function");
        Validates.requirePositive(maxSize, "maxSize");

        return new SpreadMemoizedBiFunction<>(function, spread -> function(spread, maxSize));
    }

    /**
     * Memoizes {@code function}, recomputing each result once it is older than {@code ttl}.
     *
     * @see #function(Function, Duration)
     */
    @Contract("_, _ -> new")
    public <T, U, R> @NotNull MemoizedBiFunction<T, U, R> biFunction(
            @NotNull final BiFunction<? super T, ? super U, ? extends R> function,
            @NotNull final Duration ttl
    ) {
        Validates.require(function, "function");
        Validates.require(ttl, "ttl");

        return new SpreadMemoizedBiFunction<>(function, spread -> function(spread, ttl));
    }

    // ---------- Suppliers ----------

    /**
     * Memoizes {@code supplier}: it is called once, on the first {@code get()}, and again only
     * after {@link MemoizedSupplier#reset()}.
     */
    @Contract("_ -> new")
    public <T> @NotNull MemoizedSupplier<T> supplier(
            @NotNull final Supplier<? extends T> supplier
    ) {
        Validates.require(supplier, "supplier");

        return new LazySupplier<>(supplier);
    }

    /**
     * Memoizes {@code supplier}, calling it again once its value is older than {@code ttl}, such as
     * to cache a remote setting for a minute.
     */
    @Contract("_, _ -> new")
    public <T> @NotNull MemoizedSupplier<T> supplier(
            @NotNull final Supplier<? extends T> supplier,
            @NotNull final Duration ttl
    ) {
        Validates.require(supplier, "supplier");

        return new ExpiringMemoizedSupplier<>(supplier, toNanos(ttl));
    }

    // ---------- Internals ----------

    private long toNanos(
            @NotNull final Duration ttl
    ) {
        Validates.require(ttl, "ttl");
        if (ttl.isNegative() || ttl.isZero()) {
            throw new IllegalArgumentException("'ttl' must be positive");
        }

        try {
            return ttl.toNanos();
        } catch (ArithmeticException e) {
            return Long.MAX_VALUE;
        }
    }

}
