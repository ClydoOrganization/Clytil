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

    @Nullable
    public Object defaultValue(
            @NotNull final Class<?> type
    ) {
        Validates.require(type, "type");

        if (!type.isPrimitive()) {
            return null;
        }

        if (type == boolean.class) {
            return Boolean.FALSE;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == char.class) {
            return (char) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == float.class) {
            return 0F;
        }
        if (type == double.class) {
            return 0D;
        }
        if (type == long.class) {
            return 0L;
        }

        return null;
    }
}
