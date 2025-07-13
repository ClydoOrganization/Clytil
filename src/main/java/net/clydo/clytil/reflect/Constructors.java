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
import lombok.val;
import net.clydo.clytil.Validates;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@UtilityClass
public class Constructors {

    public <T> Constructor<T> of(
            @NotNull final String clazz,
            @NotNull final Class<?>... parameterTypes
    ) {
        Validates.require(clazz, "class");
        Validates.require(parameterTypes, "parameterTypes");

        try {
            return (Constructor<T>) Constructors.of(
                    Class.forName(clazz),
                    parameterTypes
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find class %s!",
                            clazz
                    ), e
            );
        }
    }

    public <T> Constructor<T> of(
            @NotNull final Class<T> clazz,
            @NotNull final Class<?>... parameterTypes
    ) {
        Validates.require(clazz, "class");
        Validates.require(parameterTypes, "parameterTypes");

        val constructor = Reflects.getConstructor(clazz, parameterTypes);

        return Constructors.of(
                clazz,
                constructor
        );
    }

    public <T> Constructor<T> of(
            @NotNull final Class<T> clazz,
            @NotNull final java.lang.reflect.Constructor<T> constructor
    ) {
        Validates.require(clazz, "class");
        Validates.require(constructor, "constructor");

        constructor.setAccessible(true);

        return args -> {
            try {
                return (T) constructor.newInstance(args);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException(
                        String.format(
                                "Failed to construct %s(%s) with args [%s]!",
                                clazz,
                                Arrays.stream(constructor.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", ")),
                                Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "))
                        ), e
                );
            }
        };
    }

}
