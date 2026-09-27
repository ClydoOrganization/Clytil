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
 * Copyright (C) 2025 ClydoNetwork
 */

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class Lists {

    public <T> T last(
            @NotNull final List<T> list
    ) {
        return list.get(list.size() - 1);
    }

    /**
     * Appends every element of {@code from} to {@code to}, in order.
     * Uses indexed access when both are lists.
     *
     * @param from the source elements
     * @param to   the target collection
     * @param <T>  element type
     */
    public <T> void copy(
            @NotNull final List<T> from,
            @NotNull final Collection<T> to
    ) {
        for (int i = 0, size = from.size(); i < size; i++) {
            to.add(from.get(i));
        }
    }

    /**
     * Filters out {@code null} elements.
     *
     * @param list the source list
     * @param <T>  element type
     * @return a new list containing only non-null elements
     */
    public <T> @NotNull List<T> nonNull(
            @NotNull final List<T> list
    ) {
        return list.stream()
                .filter(Objects::nonNull)
                .toList();
    }

}
