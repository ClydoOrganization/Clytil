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

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
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

    public static final List<Class<?>> NUMERICS = Inits.of(() ->
            List.copyOf(
                    Stream.of(BYTE, SHORT, INT, FLOAT, LONG, DOUBLE)
                            .flatMap(List::stream)
                            .toList()
            )
    );

    public static final List<Class<?>> INTEGERS = Inits.of(() ->
            List.copyOf(
                    Stream.of(BYTE, SHORT, INT, LONG)
                            .flatMap(List::stream)
                            .toList()
            )
    );

    public static final List<Class<?>> FLOATS = Inits.of(() ->
            List.copyOf(
                    Stream.of(FLOAT, DOUBLE)
                            .flatMap(List::stream)
                            .toList()
            )
    );

    public Class<?> getValueType(@NotNull final Member member) {
        Validates.requireParam(member, "member");

        if (member instanceof Field field) {
            return field.getType();
        } else if (member instanceof Method method) {
            return method.getReturnType();
        }

        throw new IllegalArgumentException(
                "Unsupported member type: " + member.getClass().getName() + ". Expected Field or Method."
        );
    }

    public <T> boolean is(Class<T> type, Class<?> expected) {
        return type == expected;
    }

    public <T> boolean isString(Class<T> type) {
        return type == String.class;
    }

    public <T> boolean isBigDecimal(Class<T> type) {
        return Numbers.isBigDecimal(type);
    }

    public <T> boolean isBigInteger(Class<T> type) {
        return Numbers.isBigInteger(type);
    }

    public <T> boolean isLong(Class<T> type) {
        return Numbers.isLong(type);
    }

    public <T> boolean isDouble(Class<T> type) {
        return Numbers.isDouble(type);
    }

    public <T> boolean isFloat(Class<T> type) {
        return Numbers.isFloat(type);
    }

    public <T> boolean isInteger(Class<T> type) {
        return Numbers.isInteger(type);
    }

    public <T> boolean isShort(Class<T> type) {
        return Numbers.isShort(type);
    }

    public <T> boolean isByte(Class<T> type) {
        return Numbers.isByte(type);
    }

}
