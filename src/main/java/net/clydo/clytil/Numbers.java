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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Utility class for number type casting and type checking.
 * This class provides methods to cast a given {@link Number} to various numeric types like {@link Byte}, {@link Short}, {@link Integer}, {@link Float}, {@link Double}, {@link Long}, {@link BigInteger}, and {@link BigDecimal}.
 * It also includes methods to check if a class corresponds to a specific numeric type.
 *
 * @author RezaNajafian
 */
@UtilityClass
public class Numbers {

    @SuppressWarnings("unchecked")
    public <T extends Number> T cast(Number input, T to) {
        Objects.requireNonNull(to);

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
    public <T extends Number> T cast(Number input, Class<T> toType) {
        Objects.requireNonNull(toType, "toType must not be null");

        if (input == null) {
            return null;
        } else if (Numbers.isByte(toType)) {
            return Numbers.castToByte(input);
        } else if (Numbers.isShort(toType)) {
            return Numbers.castToShort(input);
        } else if (Numbers.isInteger(toType)) {
            return Numbers.castToInteger(input);
        } else if (Numbers.isFloat(toType)) {
            return Numbers.castToFloat(input);
        } else if (Numbers.isDouble(toType)) {
            return Numbers.castToDouble(input);
        } else if (Numbers.isLong(toType)) {
            return Numbers.castToLong(input);
        } else if (Numbers.isBigInteger(toType)) {
            return Numbers.castToBigInteger(input);
        } else if (Numbers.isBigDecimal(toType)) {
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

    /**
     * Checks if the provided type is {@link BigDecimal}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link BigDecimal}, {@code false} otherwise
     */
    public <T> boolean isBigDecimal(Class<T> type) {
        return type == BigDecimal.class;
    }

    /**
     * Checks if the provided type is {@link BigInteger}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link BigInteger}, {@code false} otherwise
     */
    public <T> boolean isBigInteger(Class<T> type) {
        return type == BigInteger.class;
    }

    /**
     * Checks if the provided type is {@link Long}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Long}, {@code false} otherwise
     */
    public <T> boolean isLong(Class<T> type) {
        return type == Long.class || type == long.class;
    }

    /**
     * Checks if the provided type is {@link Double}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Double}, {@code false} otherwise
     */
    public <T> boolean isDouble(Class<T> type) {
        return type == Double.class || type == double.class;
    }

    /**
     * Checks if the provided type is {@link Float}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Float}, {@code false} otherwise
     */
    public <T> boolean isFloat(Class<T> type) {
        return type == Float.class || type == float.class;
    }

    /**
     * Checks if the provided type is {@link Integer}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Integer}, {@code false} otherwise
     */
    public <T> boolean isInteger(Class<T> type) {
        return type == Integer.class || type == int.class;
    }

    /**
     * Checks if the provided type is {@link Short}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Short}, {@code false} otherwise
     */
    public <T> boolean isShort(Class<T> type) {
        return type == Short.class || type == short.class;
    }

    /**
     * Checks if the provided type is {@link Byte}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Byte}, {@code false} otherwise
     */
    public <T> boolean isByte(Class<T> type) {
        return type == Byte.class || type == byte.class;
    }
}

