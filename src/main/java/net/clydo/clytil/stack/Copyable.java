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

package net.clydo.clytil.stack;

import org.jetbrains.annotations.NotNull;

/**
 * An element that can be copied, used by {@link CopyableStack}
 * to recycle popped elements instead of allocating new ones.
 *
 * @param <T> the concrete element type
 */
public interface Copyable<T> {

    /**
     * @return a copy of this element
     */
    T copy();

    /**
     * Copies the state of {@code value} into this element.
     * Default implementation does nothing.
     *
     * @param value the element to copy from
     */
    default void copyFrom(
            @NotNull final T value
    ) {
    }

}
