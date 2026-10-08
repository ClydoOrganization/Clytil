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

package net.clydo.clytil.cache.memoize;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

/**
 * Like {@link LockedMemoizedFunction}, but each value carries an expiry time and is recomputed once
 * it passes. Expired entries are dropped when looked up or counted.
 */
final class ExpiringMemoizedFunction<T, R> implements MemoizedFunction<T, R> {

    private final Map<Object, TimedValue> cache;
    private final Function<? super T, ? extends R> function;
    private final long ttlNanos;

    ExpiringMemoizedFunction(
            @NotNull final Function<? super T, ? extends R> function,
            @NotNull final Map<Object, TimedValue> cache,
            final long ttlNanos
    ) {
        this.function = function;
        this.cache = cache;
        this.ttlNanos = ttlNanos;
    }

    @Override
    public R apply(final T argument) {
        val key = NullMask.mask(argument);
        synchronized (this.cache) {
            val cached = this.cache.get(key);
            if (cached != null) {
                if (!cached.isExpired()) {
                    return NullMask.unmask(cached.value());
                }
                this.cache.remove(key);
            }
        }

        val computed = TimedValue.of(NullMask.mask(this.function.apply(argument)), this.ttlNanos);
        synchronized (this.cache) {
            this.cache.put(key, computed);
        }

        return NullMask.unmask(computed.value());
    }

    @Override
    public boolean isCached(final T argument) {
        synchronized (this.cache) {
            val cached = this.cache.get(NullMask.mask(argument));
            return cached != null && !cached.isExpired();
        }
    }

    @Override
    public void invalidate(final T argument) {
        synchronized (this.cache) {
            this.cache.remove(NullMask.mask(argument));
        }
    }

    @Override
    public void clear() {
        synchronized (this.cache) {
            this.cache.clear();
        }
    }

    @Override
    public int size() {
        synchronized (this.cache) {
            this.cache.values().removeIf(TimedValue::isExpired);
            return this.cache.size();
        }
    }

    Map<Object, TimedValue> cache() {
        return this.cache;
    }

    long ttlNanos() {
        return this.ttlNanos;
    }

    @Override
    public String toString() {
        return "Memoize.function[" + this.function + ", size=" + this.size() + "]";
    }

}
