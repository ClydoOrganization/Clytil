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
 * Copyright (C) 2024-2025 ClydoNetwork
 */

package net.clydo.clytil.iface.value;

import lombok.val;
import net.clydo.clytil.Validates;
import net.clydo.clytil.iface.Resettable;
import net.clydo.clytil.reflect.FieldValue;
import net.clydo.clytil.reflect.MethodInvoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ValueExt<V> extends Value<V>, DefaultValue<V>, Resettable {

    @Override
    default void reset() {
        this.set(this.def());
    }

    static <T> @NotNull ValueExt<T> fromLambda(
            final @NotNull Setter<T> setter,
            final @NotNull Getter<T> getter
    ) {
        return fromLambda(getter.get(), setter, getter);
    }

    static <V> @NotNull ValueExt<V> fromLambda(
            final V defaultValue,
            @NotNull final Setter<V> setter,
            @NotNull final Getter<V> getter
    ) {
        Validates.requireParam(setter, "setter");
        Validates.requireParam(getter, "getter");

        return new ValueExt<>() {

            @Override
            public void set(final V value) {
                setter.set(value);
            }

            @Override
            public V get() {
                return getter.get();
            }

            @Override
            public V def() {
                return defaultValue;
            }

        };
    }

    static <V> @NotNull ValueExt<V> fromField(
            @NotNull final FieldValue<Object, V> fieldValue,
            @Nullable final Object owner
    ) {
        Validates.requireParam(fieldValue, "field");

        return fromLambda(
                value -> fieldValue.set(owner, value),
                () -> fieldValue.get(owner)
        );
    }

    static <V> @NotNull ValueExt<V> fromField(
            final V defaultValue,
            @NotNull final FieldValue<Object, V> fieldValue,
            @Nullable final Object owner
    ) {
        Validates.requireParam(fieldValue, "field");

        return fromLambda(
                defaultValue,
                value -> fieldValue.set(owner, value),
                () -> fieldValue.get(owner)
        );
    }

    static <V> @NotNull ValueExt<V> fromMethod(
            @NotNull final MethodInvoker<Object, V> setter,
            @NotNull final MethodInvoker<Object, V> getter,
            @Nullable final Object owner
    ) {
        Validates.requireParam(setter, "setter");
        Validates.requireParam(getter, "getter");

        return fromLambda(
                value -> setter.invoke(owner, value),
                () -> getter.invoke(owner)
        );
    }

    static <V> @NotNull ValueExt<V> fromMethod(
            final V defaultValue,
            @NotNull final MethodInvoker<Object, V> setter,
            @NotNull final MethodInvoker<Object, V> getter,
            @Nullable final Object owner
    ) {
        Validates.requireParam(setter, "setter");
        Validates.requireParam(getter, "getter");

        return fromLambda(
                defaultValue,
                value -> setter.invoke(owner, value),
                () -> getter.invoke(owner)
        );
    }

    static <V> @NotNull ValueExt<V> of(
            final V initialValue
    ) {
        val holder = new Object[]{initialValue};

        //noinspection unchecked
        return fromLambda(
                initialValue,
                value -> holder[0] = value,
                () -> (V) holder[0]
        );
    }

    static <V> @NotNull ValueExt<V> constant(
            @Nullable final V value
    ) {
        return fromLambda(
                value,
                (v) -> {
                    throw new UnsupportedOperationException("You can't set the value of a ConstValueExt");
                },
                () -> value
        );
    }

}
