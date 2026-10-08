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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Swaps {@code null} for a sentinel so it can be stored where {@code null} is not allowed, such as
 * a {@link java.util.concurrent.ConcurrentHashMap}, or where {@code null} means "absent".
 */
@UtilityClass
class NullMask {

    private final Object NULL = new Object();

    @Contract(pure = true)
    @NotNull Object mask(
            @Nullable final Object value
    ) {
        return value == null ? NULL : value;
    }

    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    <T> @Nullable T unmask(
            @NotNull final Object value
    ) {
        return value == NULL ? null : (T) value;
    }

}
