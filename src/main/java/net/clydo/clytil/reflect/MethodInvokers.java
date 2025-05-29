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

import java.lang.reflect.Method;

@UtilityClass
public class MethodInvokers {

    public <O, R> MethodInvoker<O, R> of(
            @NotNull final String clazz,
            @NotNull final String name,
            @NotNull final Class<?>... argTypes
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");
        Validates.requireParam(argTypes, "argTypes");

        try {
            return MethodInvokers.of(
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

        return MethodInvokers.of(clazz, method);
    }

    public <O, R> MethodInvoker<O, R> of(
            @NotNull final Class<?> clazz,
            @NotNull final Method method
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(method, "method");

        method.setAccessible(true);

        return (owner, args) -> Methods.get(clazz, method, owner, args);
    }

}
