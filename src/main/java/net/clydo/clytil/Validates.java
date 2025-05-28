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
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@UtilityClass
public class Validates {

    public static <T> @NotNull T require(
            @Nullable final T value
    ) {
        return Validates.require(value, "value must not be null");
    }

    public static <T> @NotNull T requireValue(
            @Nullable final T value,
            @NotNull final String name
    ) {
        return Validates.require(value, String.format("'%s' must not be null", name));
    }

    public static <T> @NotNull T requireParam(
            @Nullable final T value,
            @NotNull final String name
    ) {
        return Validates.require(value, String.format("param '%s' must not be null", name));
    }

    public static <T> @NotNull T requireField(
            @Nullable final T value,
            @NotNull final String name) {
        return Validates.require(value, String.format("field '%s' must not be null", name));
    }

    public static <T> @NotNull T require(
            @Nullable final T value,
            @NotNull final Supplier<String> message
    ) {
        if (value == null) {
            throw new NullPointerException(message.get());
        }
        return value;
    }

    public static <T> @NotNull T require(
            @Nullable final T value,
            @NotNull final String message
    ) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

}
