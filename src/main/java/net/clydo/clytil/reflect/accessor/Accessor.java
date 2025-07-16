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

package net.clydo.clytil.reflect.accessor;

import net.clydo.clytil.Validates;
import net.clydo.clytil.reflect.FieldValue;
import net.clydo.clytil.reflect.MethodInvoker;
import org.jetbrains.annotations.NotNull;

public interface Accessor<O, V> extends AccessorSetter<O, V>, AccessorGetter<O, V> {

    @NotNull
    static <O, V> Accessor<O, V> fromLambda(
            @NotNull final AccessorSetter<O, V> setter,
            @NotNull final AccessorGetter<O, V> getter
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return new Accessor<>() {

            @Override
            public void set(
                    final O owner,
                    final V value
            ) {
                setter.set(owner, value);
            }

            @Override
            public V get(
                    final O owner
            ) {
                return getter.get(owner);
            }

        };
    }

    @NotNull
    static <O, V> Accessor<O, V> fromField(
            @NotNull final FieldValue<O, V> field
    ) {
        Validates.require(field, "field");

        return fromLambda(
                field::set,
                field::get
        );
    }

    @NotNull
    static <O, V> Accessor<O, V> fromMethod(
            @NotNull final MethodInvoker<Object, V> setter,
            @NotNull final MethodInvoker<Object, V> getter
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return fromLambda(
                setter::invoke,
                getter::invoke
        );
    }

}
