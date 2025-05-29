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

    @SuppressWarnings("unchecked")
    public <T extends @NotNull Object> Constructor<T> getConstructor(
            @NotNull final Class<?> clazz,
            @NotNull final Class<?>... argTypes
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(argTypes, "argTypes");

        try {
            return (Constructor<T>) clazz.getDeclaredConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find constructor %s(%s)!",
                            clazz,
                            Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(", "))
                    ), e
            );
        }
    }

    public Method getMethod(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Class<?>... argTypes
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");
        Validates.requireParam(argTypes, "argTypes");

        try {
            return clazz.getDeclaredMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find method %s::%s(%s)!",
                            clazz, name,
                            Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(", "))
                    ), e
            );
        }
    }

    public Field getField(
            @NotNull final Class<?> clazz,
            @NotNull final String name
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");

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
