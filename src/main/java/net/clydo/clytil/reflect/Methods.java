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
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@UtilityClass
public class Methods {

    public <O, R> MethodInvoker<O, R> of(
            @NotNull final String clazz,
            @NotNull final String name,
            @NotNull final Class<?>... argTypes
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");
        Validates.requireParam(argTypes, "argTypes");

        try {
            return Methods.of(
                    Class.forName(clazz),
                    name,
                    argTypes
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

    public <O, R> MethodInvoker<O, R> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Class<?>... argTypes
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");
        Validates.requireParam(argTypes, "argTypes");

        val method = Reflects.getMethod(clazz, name, argTypes);

        method.setAccessible(true);

        return (owner, args) -> {
            if (owner == null && !Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException(
                        String.format(
                                "Owner cannot be null for non-static method %s::%s",
                                clazz, name
                        )
                );
            }

            try {
                return (R) method.invoke(owner, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(
                        String.format(
                                "Failed to call %s::%s(%s) with args [%s]!",
                                clazz, name,
                                Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(", ")),
                                Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "))
                        ), e
                );
            }
        };
    }

}
