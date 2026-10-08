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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/**
 * Utility class for number type casting and type checking.
 * This class provides methods to cast a given {@link Number} to various numeric types like {@link Byte}, {@link Short}, {@link Integer}, {@link Float}, {@link Double}, {@link Long}, {@link BigInteger}, and {@link BigDecimal}.
 *
 * @author RezaNajafian
 */
@UtilityClass
public class Numbers {

    @SuppressWarnings("unchecked")
    public <T extends Number> T cast(@Nullable final Number input, @NotNull final T to) {
        Validates.require(to, "to");

        return Numbers.cast(input, (Class<? extends T>) to.getClass());
    }

    /**
     * Casts a {@link Number} to a specified numeric type.
     *
     * @param input  the number to be cast
     * @param toType the target type to cast the number to
     * @param <T>    the target type, which extends {@link Number}
     * @return the casted number of type {@code T}, or {@code null} if the input is {@code null}
     * @throws IllegalArgumentException if the input cannot be cast to the specified type
     */
    public <T extends Number> T cast(@Nullable final Number input, @NotNull final Class<T> toType) {
        Validates.require(toType, "toType");

        if (input == null) {
            return null;
        } else if (Types.isByte(toType)) {
            return Numbers.castToByte(input);
        } else if (Types.isShort(toType)) {
            return Numbers.castToShort(input);
        } else if (Types.isInteger(toType)) {
            return Numbers.castToInteger(input);
        } else if (Types.isFloat(toType)) {
            return Numbers.castToFloat(input);
        } else if (Types.isDouble(toType)) {
            return Numbers.castToDouble(input);
        } else if (Types.isLong(toType)) {
            return Numbers.castToLong(input);
        } else if (Types.isBigInteger(toType)) {
            return Numbers.castToBigInteger(input);
        } else if (Types.isBigDecimal(toType)) {
            return Numbers.castToBigDecimal(input);
        } else {
            throw new IllegalArgumentException("Cannot cast " + input + " to " + toType);
        }
    }

    /**
     * Casts a {@link Number} to a {@link BigDecimal}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link BigDecimal}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToBigDecimal(@NotNull Number anyNumber) {
        return (T) new BigDecimal(anyNumber.toString());
    }

    /**
     * Casts a {@link Number} to a {@link BigInteger}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link BigInteger}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToBigInteger(@NotNull Number anyNumber) {
        return (T) new BigInteger(anyNumber.toString());
    }

    /**
     * Casts a {@link Number} to a {@link Long}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link Long}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToLong(@NotNull Number anyNumber) {
        return (T) Long.valueOf(anyNumber.longValue());
    }

    /**
     * Casts a {@link Number} to a {@link Double}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link Double}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToDouble(@NotNull Number anyNumber) {
        return (T) Double.valueOf(anyNumber.doubleValue());
    }

    /**
     * Casts a {@link Number} to a {@link Float}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link Float}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToFloat(@NotNull Number anyNumber) {
        return (T) Float.valueOf(anyNumber.floatValue());
    }

    /**
     * Casts a {@link Number} to an {@link Integer}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link Integer}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToInteger(@NotNull Number anyNumber) {
        return (T) Integer.valueOf(anyNumber.intValue());
    }

    /**
     * Casts a {@link Number} to a {@link Short}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link Short}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToShort(@NotNull Number anyNumber) {
        return (T) Short.valueOf(anyNumber.shortValue());
    }

    /**
     * Casts a {@link Number} to a {@link Byte}.
     *
     * @param anyNumber the number to be cast
     * @param <T>       the target type, which extends {@link Number}
     * @return the casted {@link Byte}
     */
    @SuppressWarnings("unchecked")
    public <T extends Number> @NotNull T castToByte(@NotNull Number anyNumber) {
        return (T) Byte.valueOf(anyNumber.byteValue());
    }

    public <N extends Number> @NotNull N clamp(
            @NotNull final N value,
            @NotNull final N min,
            @NotNull final N max
    ) {
        return Numbers.cast(Maths.clamp(value.doubleValue(), min.doubleValue(), max.doubleValue()), value);
    }

    /**
     * Formats a size in bytes the way file lists show it, in binary units: {@code 512 B},
     * {@code 24 KB}, {@code 2.4 MB}, {@code 1.1 GB}.
     *
     * @param bytes the size, not negative
     * @return the text
     */
    public @NotNull String formatBytes(
            final long bytes
    ) {
        Validates.requireNonNegative(bytes, "bytes");
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return Math.round(bytes / 1024.0) + " KB";
        }
        if (bytes < 1024L * 1024 * 1024) {
            return String.format(Locale.ROOT, "%.1f MB", bytes / (1024.0 * 1024));
        }
        return String.format(Locale.ROOT, "%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

}
