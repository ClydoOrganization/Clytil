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

import java.util.function.Supplier;

/**
 * A {@link Supplier} that computes its value once and then reuses it, created by {@link Memoize}.
 *
 * @param <T> value type
 */
public interface MemoizedSupplier<T> extends Supplier<T> {

    /**
     * Returns whether a live value is cached, without computing one.
     */
    boolean isComputed();

    /**
     * Drops the cached value, so the next {@link #get()} recomputes it.
     */
    void reset();

}
