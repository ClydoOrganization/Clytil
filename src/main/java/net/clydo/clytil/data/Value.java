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
 * Copyright (C) 2025-2026 ClydoNetwork
 */

package net.clydo.clytil.data;

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

        return new Lambda<>(setter, getter);
    }

    static <V> @NotNull Value<V> fromField(
            @NotNull final FieldValue<Object, V> fieldValue,
            @Nullable final Object owner
    ) {
        Validates.require(fieldValue, "field");

        return new Field<>(fieldValue, owner);
    }

    static <V> @NotNull Value<V> fromMethod(
            @NotNull final MethodInvoker<Object, V> setter,
            @NotNull final MethodInvoker<Object, V> getter,
            @Nullable final Object owner
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return new Method<>(setter, getter, owner);
    }

    static <V> @NotNull Value<V> of(
            @Nullable final V defaultValue
    ) {
        return new Simple<>(defaultValue);
    }

    static <V> @NotNull Value<V> constant(
            @Nullable final V value
    ) {
        return new Constant<>(value);
    }

    abstract class Abstract<V> implements Value<V> {
    }

    final class Lambda<V> extends Abstract<V> {

        private final Setter<V> setter;
        private final Getter<V> getter;

        private Lambda(
                @NotNull final Setter<V> setter,
                @NotNull final Getter<V> getter
        ) {
            this.setter = setter;
            this.getter = getter;
        }

        @Override
        public void set(final V value) {
            this.setter.set(value);
        }

        @Override
        public V get() {
            return this.getter.get();
        }

    }

    final class Field<V> extends Abstract<V> {

        private final FieldValue<Object, V> field;
        private final Object owner;

        private Field(
                @NotNull final FieldValue<Object, V> field,
                @Nullable final Object owner
        ) {
            this.field = field;
            this.owner = owner;
        }

        @Override
        public void set(final V value) {
            this.field.set(this.owner, value);
        }

        @Override
        public V get() {
            return this.field.get(this.owner);
        }

    }

    final class Method<V> extends Abstract<V> {

        private final MethodInvoker<Object, V> setter;
        private final MethodInvoker<Object, V> getter;
        private final Object owner;

        private Method(
                @NotNull final MethodInvoker<Object, V> setter,
                @NotNull final MethodInvoker<Object, V> getter,
                @Nullable final Object owner
        ) {
            this.setter = setter;
            this.getter = getter;
            this.owner = owner;
        }

        @Override
        public void set(final V value) {
            this.setter.invoke(this.owner, value);
        }

        @Override
        public V get() {
            return this.getter.invoke(this.owner);
        }

    }

    final class Constant<V> extends Abstract<V> {

        private final V value;

        private Constant(final V value) {
            this.value = value;
        }

        @Override
        public void set(final V value) {
            throw new UnsupportedOperationException(
                    "You can't set the value of a ConstantValueExt"
            );
        }

        @Override
        public V get() {
            return this.value;
        }

    }

    final class Simple<V> extends Abstract<V> {

        private V value;

        private Simple(final V defaultValue) {
            this.value = defaultValue;
        }

        @Override
        public void set(final V value) {
            this.value = value;
        }

        @Override
        public V get() {
            return this.value;
        }

    }
}
