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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Unbounded, computing outside the map so the body can call back into itself. Under contention a
 * result may be computed more than once; the first one stored wins.
 */
final class RecursiveMemoizedFunction<T, R> implements MemoizedFunction<T, R> {

    private final Map<Object, Object> cache = new ConcurrentHashMap<>();
    private final BiFunction<? super Function<T, R>, ? super T, ? extends R> body;

    RecursiveMemoizedFunction(
            @NotNull final BiFunction<? super Function<T, R>, ? super T, ? extends R> body
    ) {
        this.body = body;
    }

    @Override
    public R apply(final T argument) {
        val key = NullMask.mask(argument);
        val cached = this.cache.get(key);
        if (cached != null) {
            return NullMask.unmask(cached);
        }

        val computed = NullMask.mask(this.body.apply(this, argument));
        val previous = this.cache.putIfAbsent(key, computed);
        return NullMask.unmask(previous != null ? previous : computed);
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
        return "Memoize.recursive[" + this.body + ", size=" + this.size() + "]";
    }

}
