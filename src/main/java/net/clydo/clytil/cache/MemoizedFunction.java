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

import java.util.function.Function;

/**
 * A {@link Function} that caches its results, created by {@link Memoize}.
 *
 * @param <T> argument type
 * @param <R> result type
 */
public interface MemoizedFunction<T, R> extends Function<T, R> {

    /**
     * Returns whether a live result for {@code argument} is cached.
     */
    boolean isCached(T argument);

    /**
     * Drops the cached result for {@code argument}, so the next call recomputes it.
     */
    void invalidate(T argument);

    /**
     * Drops every cached result.
     */
    void clear();

    /**
     * Returns how many results are cached.
     */
    int size();

}
