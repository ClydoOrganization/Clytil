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
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

@UtilityClass
public class Primitives {

    private final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;
    private final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE;

    static {
        val wrappers = new IdentityHashMap<Class<?>, Class<?>>(16);
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(byte.class, Byte.class);
        wrappers.put(char.class, Character.class);
        wrappers.put(short.class, Short.class);
        wrappers.put(int.class, Integer.class);
        wrappers.put(float.class, Float.class);
        wrappers.put(double.class, Double.class);
        wrappers.put(long.class, Long.class);
        wrappers.put(void.class, Void.class);

        val primitives = new IdentityHashMap<Class<?>, Class<?>>(16);
        for (val entry : wrappers.entrySet()) {
            primitives.put(entry.getValue(), entry.getKey());
        }

        PRIMITIVE_TO_WRAPPER = Collections.unmodifiableMap(wrappers);
        WRAPPER_TO_PRIMITIVE = Collections.unmodifiableMap(primitives);
    }

    public static Class<?> wrap(
            @NotNull final Class<?> type
    ) {
        Validates.require(type, "type");

        return PRIMITIVE_TO_WRAPPER.getOrDefault(type, type);
    }

    public Class<?> unwrap(
            @NotNull final Class<?> type
    ) {
        Validates.require(type, "type");

        return WRAPPER_TO_PRIMITIVE.getOrDefault(type, type);
    }

    public static Type wrap(
            @NotNull final Type type
    ) {
        Validates.require(type, "type");

        if (type instanceof Class<?> clazz) {
            return PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz);
        }
        return type;
    }

    public Type unwrap(
            @NotNull final Type type
    ) {
        Validates.require(type, "type");

        if (type instanceof Class<?> clazz) {
            return WRAPPER_TO_PRIMITIVE.getOrDefault(clazz, clazz);
        }
        return type;
    }

    public boolean isPrimitive(
            @NotNull final Class<?> type
    ) {
        Validates.require(type, "type");

        return type.isPrimitive();
    }

    public boolean isWrapper(
            @NotNull final Class<?> type
    ) {
        Validates.require(type, "type");

        return WRAPPER_TO_PRIMITIVE.containsKey(type);
    }

    private final Boolean BOOLEAN_FALSE = Boolean.FALSE;
    private final Float FLOAT_ZERO = 0F;
    private final Byte BYTE_ZERO = (byte) 0;
    private final Character CHARACTER_ZERO = (char) 0;
    private final Short SHORT_ZERO = (short) 0;
    private final Integer INT_ZERO = 0;
    private final Double DOUBLE_ZERO = 0D;
    private final Long LONG_ZERO = 0L;

    /**
     * Returns the default value of {@code type}: what an uninitialized field of that type holds,
     * such as {@code 0} for {@code int} and {@code false} for {@code boolean}. It is {@code null}
     * for reference types and {@code void}.
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable T defaultValue(
            @NotNull final Class<T> type
    ) {
        Validates.require(type, "type");

        if (!type.isPrimitive()) {
            return null;
        }

        if (type == boolean.class) {
            return (T) BOOLEAN_FALSE;
        }
        if (type == byte.class) {
            return (T) BYTE_ZERO;
        }
        if (type == char.class) {
            return (T) CHARACTER_ZERO;
        }
        if (type == short.class) {
            return (T) SHORT_ZERO;
        }
        if (type == int.class) {
            return (T) INT_ZERO;
        }
        if (type == float.class) {
            return (T) FLOAT_ZERO;
        }
        if (type == double.class) {
            return (T) DOUBLE_ZERO;
        }
        if (type == long.class) {
            return (T) LONG_ZERO;
        }

        return null;
    }

    /**
     * Returns {@code value}, or the {@link #defaultValue(Class) default value} of {@code type} when
     * {@code value} is {@code null}.
     */
    public <T> @Nullable T orDefault(
            @Nullable final T value,
            @NotNull final Class<T> type
    ) {
        Validates.require(type, "type");

        return value != null ? value : defaultValue(type);
    }

}
