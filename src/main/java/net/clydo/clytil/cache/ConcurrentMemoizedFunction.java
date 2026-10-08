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

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Unbounded, computing each result exactly once. A hit is a lock-free {@code get}; only a miss
 * goes through {@link ConcurrentHashMap#computeIfAbsent}, with a loader built once rather than per
 * call.
 */
final class ConcurrentMemoizedFunction<T, R> implements MemoizedFunction<T, R> {

    private final Map<Object, Object> cache = new ConcurrentHashMap<>();
    private final Function<? super T, ? extends R> function;
    private final Function<Object, Object> loader;

    ConcurrentMemoizedFunction(
            @NotNull final Function<? super T, ? extends R> function
    ) {
        this.function = function;
        this.loader = key -> NullMask.mask(function.apply(NullMask.unmask(key)));
    }

    @Override
    public R apply(final T argument) {
        final Object key = NullMask.mask(argument);
        Object value = this.cache.get(key);
        if (value == null) {
            value = this.cache.computeIfAbsent(key, this.loader);
        }

        return NullMask.unmask(value);
    }

    @Override
    public boolean isCached(final T argument) {
        return this.cache.containsKey(NullMask.mask(argument));
    }

    @Override
    public void invalidate(final T argument) {
        this.cache.remove(NullMask.mask(argument));
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public int size() {
        return this.cache.size();
    }

    @Override
    public String toString() {
        return "Memoize.function[" + this.function + ", size=" + this.size() + "]";
    }

}
