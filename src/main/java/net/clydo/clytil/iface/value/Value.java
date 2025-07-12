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

package net.clydo.clytil.iface.value;

import lombok.val;
import net.clydo.clytil.Validates;
import net.clydo.clytil.reflect.FieldValue;
import net.clydo.clytil.reflect.MethodInvoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Value<V> extends Setter<V>, Getter<V> {

    static <V> @NotNull Value<V> fromLambda(
            @NotNull final Setter<V> setter,
            @NotNull final Getter<V> getter
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return new Value<>() {

            @Override
            public void set(final V value) {
                setter.set(value);
            }

            @Override
            public V get() {
                return getter.get();
            }

        };
    }

    static <V> @NotNull Value<V> fromField(
            @NotNull final FieldValue<Object, V> fieldValue,
            @Nullable final Object owner
    ) {
        Validates.require(fieldValue, "field");

        return fromLambda(
                value -> fieldValue.set(owner, value),
                () -> fieldValue.get(owner)
        );
    }

    static <V> @NotNull Value<V> fromMethod(
            @NotNull final MethodInvoker<Object, V> setter,
            @NotNull final MethodInvoker<Object, V> getter,
            @Nullable final Object owner
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return fromLambda(
                value -> setter.invoke(owner, value),
                () -> getter.invoke(owner)
        );
    }

    static <V> @NotNull Value<V> of(
            final V initialValue
    ) {
        val holder = new Object[]{initialValue};

        //noinspection unchecked
        return fromLambda(
                value -> holder[0] = value,
                () -> (V) holder[0]
        );
    }

    static <V> @NotNull Value<V> constant(
            @Nullable final V value
    ) {
        return fromLambda(
                v -> {
                    throw new UnsupportedOperationException("You can't set the value of a ConstValue");
                },
                () -> value
        );
    }

}
