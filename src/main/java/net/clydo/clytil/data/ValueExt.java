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
 * Copyright (C) 2024-2026 ClydoNetwork
 */

package net.clydo.clytil.data;

import net.clydo.clytil.Validates;
import net.clydo.clytil.iface.Resettable;
import net.clydo.clytil.reflect.FieldValue;
import net.clydo.clytil.reflect.MethodInvoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ValueExt<V> extends Value<V>, DefaultValue<V>, Resettable {

    static <T> @NotNull ValueExt<T> fromLambda(
            @NotNull final Setter<T> setter,
            @NotNull final Getter<T> getter
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return new Lambda<>(setter, getter);
    }

    static <V> @NotNull ValueExt<V> fromLambda(
            @Nullable final V defaultValue,
            @NotNull final Setter<V> setter,
            @NotNull final Getter<V> getter
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return new Lambda<>(defaultValue, setter, getter);
    }

    static <V> @NotNull ValueExt<V> fromField(
            @NotNull final FieldValue<Object, V> fieldValue,
            @Nullable final Object owner
    ) {
        Validates.require(fieldValue, "field");

        return new Field<>(fieldValue, owner);
    }

    static <V> @NotNull ValueExt<V> fromField(
            @Nullable final V defaultValue,
            @NotNull final FieldValue<Object, V> fieldValue,
            @Nullable final Object owner
    ) {
        Validates.require(fieldValue, "field");

        return new Field<>(defaultValue, fieldValue, owner);
    }

    static <V> @NotNull ValueExt<V> fromMethod(
            @NotNull final MethodInvoker<Object, V> setter,
            @NotNull final MethodInvoker<Object, V> getter,
            @Nullable final Object owner
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return new Method<>(setter, getter, owner);
    }

    static <V> @NotNull ValueExt<V> fromMethod(
            @Nullable final V defaultValue,
            @NotNull final MethodInvoker<Object, V> setter,
            @NotNull final MethodInvoker<Object, V> getter,
            @Nullable final Object owner
    ) {
        Validates.require(setter, "setter");
        Validates.require(getter, "getter");

        return new Method<>(defaultValue, setter, getter, owner);
    }

    static <V> @NotNull ValueExt<V> of(
            @Nullable final V defaultValue
    ) {
        return new Simple<>(defaultValue);
    }

    static <V> @NotNull ValueExt<V> constant(
            @Nullable final V value
    ) {
        return new Constant<>(value);
    }

    abstract class Abstract<V> implements ValueExt<V> {

        private final V defaultValue;

        private Abstract(final V defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public V getDefault() {
            return this.defaultValue;
        }

        @Override
        public void reset() {
            this.set(this.defaultValue);
        }

    }

    final class Lambda<V> extends Abstract<V> {

        private final Setter<V> setter;
        private final Getter<V> getter;

        private Lambda(
                final V defaultValue,
                @NotNull final Setter<V> setter,
                @NotNull final Getter<V> getter
        ) {
            super(defaultValue);
            this.setter = setter;
            this.getter = getter;
        }

        private Lambda(
                @NotNull final Setter<V> setter,
                @NotNull final Getter<V> getter
        ) {
            this(getter.get(), setter, getter);
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
                final V defaultValue,
                @NotNull final FieldValue<Object, V> field,
                @Nullable final Object owner
        ) {
            super(defaultValue);
            this.field = field;
            this.owner = owner;
        }

        private Field(
                @NotNull final FieldValue<Object, V> field,
                @Nullable final Object owner
        ) {
            this(field.get(owner), field, owner);
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
                final V defaultValue,
                @NotNull final MethodInvoker<Object, V> setter,
                @NotNull final MethodInvoker<Object, V> getter,
                @Nullable final Object owner
        ) {
            super(defaultValue);
            this.setter = setter;
            this.getter = getter;
            this.owner = owner;
        }

        private Method(
                @NotNull final MethodInvoker<Object, V> setter,
                @NotNull final MethodInvoker<Object, V> getter,
                @Nullable final Object owner
        ) {
            this(getter.invoke(owner), setter, getter, owner);
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

    final class Simple<V> extends Abstract<V> {

        private V value;

        private Simple(final V defaultValue) {
            super(defaultValue);
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

    final class Constant<V> extends Abstract<V> {

        private Constant(final V value) {
            super(value);
        }

        @Override
        public void set(final V value) {
            throw new UnsupportedOperationException(
                    "You can't set the value of a ConstantValueExt"
            );
        }

        @Override
        public V get() {
            return this.getDefault();
        }

    }

}
