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

import java.util.Collection;
import java.util.function.Supplier;

@UtilityClass
public class Validates {

    public <T> @NotNull T require(
            @Nullable final T value,
            @NotNull final String name
    ) {
        if (value == null) {
            throw new NullPointerException(String.format("'%s' must not be null", name));
        }
        return value;
    }

    public <T> @NotNull T requireLazy(
            @Nullable final T value,
            @NotNull final Supplier<String> message
    ) {
        if (value == null) {
            throw new NullPointerException(message.get());
        }
        return value;
    }

    public <T> @NotNull T requireMsg(
            @Nullable final T value,
            @NotNull final String message
    ) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    // int versions
    public int requirePositive(
            final int value,
            @NotNull final String name
    ) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s must be positive", name));
        }

        return value;
    }

    public int requireNonNegative(
            final int value,
            @NotNull final String name
    ) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("%s must be non-negative", name));
        }

        return value;
    }

    public int requireNegative(
            final int value,
            @NotNull final String name
    ) {
        if (value >= 0) {
            throw new IllegalArgumentException(String.format("%s must be negative", name));
        }

        return value;
    }

    public int requireNonPositive(
            final int value,
            @NotNull final String name
    ) {
        if (value > 0) {
            throw new IllegalArgumentException(String.format("%s must be non-positive", name));
        }

        return value;
    }

    public int requireInRange(
            final int value,
            final int min,
            final int max,
            @NotNull final String name
    ) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s must be between %d and %d", name, min, max));
        }

        return value;
    }

    // float versions
    public float requirePositive(
            final float value,
            @NotNull final String name
    ) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s must be positive", name));
        }

        return value;
    }

    public float requireNonNegative(
            final float value,
            @NotNull final String name
    ) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("%s must be non-negative", name));
        }

        return value;
    }

    public float requireNegative(
            final float value,
            @NotNull final String name
    ) {
        if (value >= 0) {
            throw new IllegalArgumentException(String.format("%s must be negative", name));
        }

        return value;
    }

    public float requireNonPositive(
            final float value,
            @NotNull final String name
    ) {
        if (value > 0) {
            throw new IllegalArgumentException(String.format("%s must be non-positive", name));
        }

        return value;
    }

    public float requireInRange(
            final float value,
            final float min,
            final float max,
            @NotNull final String name
    ) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s must be between %f and %f", name, min, max));
        }

        return value;
    }

    // long versions
    public long requirePositive(
            final long value,
            @NotNull final String name
    ) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s must be positive", name));
        }

        return value;
    }

    public long requireNonNegative(
            final long value,
            @NotNull final String name
    ) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("%s must be non-negative", name));
        }

        return value;
    }

    public long requireNegative(
            final long value,
            @NotNull final String name
    ) {
        if (value >= 0) {
            throw new IllegalArgumentException(String.format("%s must be negative", name));
        }

        return value;
    }

    public long requireNonPositive(
            final long value,
            @NotNull final String name
    ) {
        if (value > 0) {
            throw new IllegalArgumentException(String.format("%s must be non-positive", name));
        }

        return value;
    }

    public long requireInRange(
            final long value,
            final long min,
            final long max,
            @NotNull final String name
    ) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s must be between %d and %d", name, min, max));
        }

        return value;
    }

    // double versions
    public double requirePositive(
            final double value,
            @NotNull final String name
    ) {
        if (value <= 0) {
            throw new IllegalArgumentException(String.format("%s must be positive", name));
        }

        return value;
    }

    public double requireNonNegative(
            final double value,
            @NotNull final String name
    ) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("%s must be non-negative", name));
        }

        return value;
    }

    public double requireNegative(
            final double value,
            @NotNull final String name
    ) {
        if (value >= 0) {
            throw new IllegalArgumentException(String.format("%s must be negative", name));
        }

        return value;
    }

    public double requireNonPositive(
            final double value,
            @NotNull final String name
    ) {
        if (value > 0) {
            throw new IllegalArgumentException(String.format("%s must be non-positive", name));
        }

        return value;
    }

    public double requireInRange(
            final double value,
            final double min,
            final double max,
            @NotNull final String name
    ) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("%s must be between %f and %f", name, min, max));
        }

        return value;
    }

    // collections

    public <C extends Collection<?>> @NotNull C require(
            @Nullable final C collection,
            @NotNull final String name
    ) {
        if (collection == null) {
            throw new NullPointerException(String.format("'%s' must not be null", name));
        }
        if (collection.isEmpty()) {
            throw new NullPointerException(String.format("'%s' must not be empty", name));
        }
        return collection;
    }

}
