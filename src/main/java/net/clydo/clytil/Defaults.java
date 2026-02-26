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

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Defaults {

    public final Boolean DEFAULT_BOOLEAN = Boolean.FALSE;
    public final Character DEFAULT_CHAR = '\u0000';
    public final Byte DEFAULT_BYTE = 0;
    public final Short DEFAULT_SHORT = 0;
    public final Integer DEFAULT_INT = 0;
    public final Long DEFAULT_LONG = 0L;
    public final Float DEFAULT_FLOAT = 0f;
    public final Double DEFAULT_DOUBLE = 0d;

    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(
            @NotNull final Class<T> type
    ) {
        Validates.require(type, "type");

        if (!type.isPrimitive()) {
            return null;
        }

        if (type == boolean.class) {
            return (T) DEFAULT_BOOLEAN;
        } else if (type == char.class) {
            return (T) DEFAULT_CHAR;
        } else if (type == byte.class) {
            return (T) DEFAULT_BYTE;
        } else if (type == short.class) {
            return (T) DEFAULT_SHORT;
        } else if (type == int.class) {
            return (T) DEFAULT_INT;
        } else if (type == long.class) {
            return (T) DEFAULT_LONG;
        } else if (type == float.class) {
            return (T) DEFAULT_FLOAT;
        } else if (type == double.class) {
            return (T) DEFAULT_DOUBLE;
        }

        return null;
    }

}
