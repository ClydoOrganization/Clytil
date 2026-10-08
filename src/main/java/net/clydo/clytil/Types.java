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

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public class Types {

    public static final List<Class<?>> BOOLEAN = List.of(Boolean.class, boolean.class);
    public static final List<Class<?>> BYTE = List.of(Byte.class, byte.class);
    public static final List<Class<?>> SHORT = List.of(Short.class, short.class);
    public static final List<Class<?>> CHAR = List.of(Character.class, char.class);
    public static final List<Class<?>> INT = List.of(Integer.class, int.class);
    public static final List<Class<?>> FLOAT = List.of(Float.class, float.class);
    public static final List<Class<?>> LONG = List.of(Long.class, long.class);
    public static final List<Class<?>> DOUBLE = List.of(Double.class, double.class);
    public static final List<Class<?>> STRING = List.of(String.class);
    public static final List<Class<?>> VOID = List.of(Void.class, void.class);

    public static final List<Class<?>> NUMERICS = List.copyOf(
            Stream.of(BYTE, SHORT, INT, FLOAT, LONG, DOUBLE)
                    .flatMap(List::stream)
                    .toList()
    );

    public static final List<Class<?>> INTEGERS = List.copyOf(
            Stream.of(BYTE, SHORT, INT, LONG)
                    .flatMap(List::stream)
                    .toList()
    );

    public static final List<Class<?>> FLOATS = List.copyOf(
            Stream.of(FLOAT, DOUBLE)
                    .flatMap(List::stream)
                    .toList()
    );

    public Class<?> getValueType(@NotNull final Member member) {
        return getValueType(member, false);
    }

    public Class<?> getValueType(@NotNull final Member member, final boolean allowVoid) {
        Validates.require(member, "member");

        if (member instanceof Field field) {
            return field.getType();
        } else if (member instanceof Method method) {
            val parameterCount = method.getParameterCount();
            if (parameterCount == 0) {
                val returnType = method.getReturnType();
                if (allowVoid || (returnType != void.class && returnType != Void.class)) {
                    return returnType;
                }
            } else if (parameterCount == 1) {
                return method.getParameterTypes()[0];
            }
        }

        throw new IllegalArgumentException(
                "Unsupported member type: " + member.getClass().getName() +
                        ". Expected Field or Method (getter/setter)."
        );
    }

    public Void set() {
        return null;
    }

    public Type getGenericValueType(@NotNull final Member member) {
        return getGenericValueType(member, false);
    }

    public Type getGenericValueType(@NotNull final Member member, final boolean allowVoid) {
        Validates.require(member, "member");

        if (member instanceof Field field) {
            return field.getGenericType();
        } else if (member instanceof Method method) {
            val parameterCount = method.getParameterCount();
            if (parameterCount == 0) {
                val returnType = method.getGenericReturnType();
                if (allowVoid || (returnType != void.class && returnType != Void.class)) {
                    return returnType;
                }
            } else if (parameterCount == 1) {
                return method.getGenericParameterTypes()[0];
            }
        }

        throw new IllegalArgumentException(
                "Unsupported member type: " + member.getClass().getName() +
                        ". Expected Field or Method (getter/setter)."
        );
    }

    public <T> boolean is(@Nullable final Class<T> type, @Nullable final Class<?> expected) {
        return type == expected;
    }

    public <T> boolean isString(@Nullable final Class<T> type) {
        return type == String.class;
    }

    /**
     * Checks if the provided type is {@link BigDecimal}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link BigDecimal}, {@code false} otherwise
     */
    public <T> boolean isBigDecimal(@Nullable final Class<T> type) {
        return type == BigDecimal.class;
    }

    /**
     * Checks if the provided type is {@link BigInteger}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link BigInteger}, {@code false} otherwise
     */
    public <T> boolean isBigInteger(@Nullable final Class<T> type) {
        return type == BigInteger.class;
    }

    /**
     * Checks if the provided type is {@link Long}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Long}, {@code false} otherwise
     */
    public <T> boolean isLong(@Nullable final Class<T> type) {
        return type == Long.class || type == long.class;
    }

    /**
     * Checks if the provided type is {@link Double}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Double}, {@code false} otherwise
     */
    public <T> boolean isDouble(@Nullable final Class<T> type) {
        return type == Double.class || type == double.class;
    }

    /**
     * Checks if the provided type is {@link Float}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Float}, {@code false} otherwise
     */
    public <T> boolean isFloat(@Nullable final Class<T> type) {
        return type == Float.class || type == float.class;
    }

    /**
     * Checks if the provided type is {@link Integer}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Integer}, {@code false} otherwise
     */
    public <T> boolean isInteger(@Nullable final Class<T> type) {
        return type == Integer.class || type == int.class;
    }

    /**
     * Checks if the provided type is {@link Short}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Short}, {@code false} otherwise
     */
    public <T> boolean isShort(@Nullable final Class<T> type) {
        return type == Short.class || type == short.class;
    }

    /**
     * Checks if the provided type is {@link Byte}.
     *
     * @param type the class type to check
     * @param <T>  the type
     * @return {@code true} if the type is {@link Byte}, {@code false} otherwise
     */
    public <T> boolean isByte(@Nullable final Class<T> type) {
        return type == Byte.class || type == byte.class;
    }


    public @Nullable Class<?> getTypeArgument(
            @NotNull final Type type,
            final int index
    ) {
        if (!(type instanceof ParameterizedType parameterized)) {
            return null;
        }

        val arguments = parameterized.getActualTypeArguments();
        if (index < 0 || index >= arguments.length) {
            return null;
        }

        val argument = arguments[index];
        if (argument instanceof Class<?> clazz) {
            return clazz;
        }

        return argument instanceof ParameterizedType nested && nested.getRawType() instanceof Class<?> raw
                ? raw
                : null;
    }

}
