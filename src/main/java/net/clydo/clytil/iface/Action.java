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

package net.clydo.clytil.iface;

import net.clydo.clytil.reflect.FieldValue;
import net.clydo.clytil.reflect.MethodInvoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface Action {

    void call();

    static @NotNull <O> Action fromMethod(
            @NotNull final MethodInvoker<O, Action> methodInvoker,
            @Nullable final O owner
    ) {
        return () -> methodInvoker.invoke(owner, new Object[0]);
    }

    static @NotNull <O> Action fromField(
            @NotNull final FieldValue<O, Action> fieldValue,
            @Nullable final O owner
    ) {
        return fieldValue.get(owner);
    }

    static Action of(
            @NotNull final Action action
    ) {
        return action;
    }

}
