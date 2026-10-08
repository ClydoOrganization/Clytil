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

import java.util.function.BiFunction;

/**
 * A {@link BiFunction} that caches its results, created by {@link Memoize}.
 *
 * @param <T> first argument type
 * @param <U> second argument type
 * @param <R> result type
 */
public interface MemoizedBiFunction<T, U, R> extends BiFunction<T, U, R> {

    /**
     * Returns whether a live result for these arguments is cached.
     */
    boolean isCached(T first, U second);

    /**
     * Drops the cached result for these arguments, so the next call recomputes it.
     */
    void invalidate(T first, U second);

    /**
     * Drops every cached result.
     */
    void clear();

    /**
     * Returns how many results are cached.
     */
    int size();

}
