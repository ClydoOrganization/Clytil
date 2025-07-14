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

package net.clydo.clytil.reflect;

import lombok.experimental.UtilityClass;
import net.clydo.clytil.Validates;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class Reflects {

    public <T> Constructor<T> getConstructor(
            @NotNull final Class<T> clazz,
            @NotNull final Class<?>... parameterTypes
    ) {
        Validates.require(clazz, "class");
        Validates.require(parameterTypes, "parameterTypes");

        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find %s %s(%s)!",
                            parameterTypes.length == 0 ? "no-arg constructor" : "constructor",
                            clazz,
                            Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(", "))
                    ), e
            );
        }
    }

    public Method getMethod(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Class<?>... parameterTypes
    ) {
        Validates.require(clazz, "class");
        Validates.require(name, "name");
        Validates.require(parameterTypes, "parameterTypes");

        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find method %s::%s(%s)!",
                            clazz, name,
                            Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(", "))
                    ), e
            );
        }
    }

    public Field getField(
            @NotNull final Class<?> clazz,
            @NotNull final String name
    ) {
        Validates.require(clazz, "class");
        Validates.require(name, "name");

        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find field %s::%s!",
                            clazz, name
                    ), e
            );
        }
    }

}
