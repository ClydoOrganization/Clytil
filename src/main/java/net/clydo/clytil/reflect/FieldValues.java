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
import net.clydo.clytil.option.Option;
import net.clydo.clytil.option.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unchecked")
@UtilityClass
public class FieldValues {

    public <O, V> @NotNull FieldValue<O, V> fromLambda(
            @NotNull final FieldSetter<O, V> setter,
            @NotNull final FieldGetter<O, V> getter
    ) {
        Validates.requireParam(setter, "setter");
        Validates.requireParam(getter, "getter");

        return new FieldValue<>() {

            @Override
            public void set(
                    @Nullable final O owner,
                    @Nullable final V value
            ) {
                setter.set(owner, value);
            }

            @Override
            public V get(
                    @Nullable final O owner
            ) {
                return getter.get(owner);
            }

        };
    }

    public <O, V> @NotNull FieldValue<O, V> cacheable(
            @NotNull final FieldValue<O, V> fieldValue
    ) {
        Validates.requireParam(fieldValue, "field");

        val holder = new Option[1];

        return fromLambda(
                (owner, value) -> {
                    val option = (Option<V>) holder[0];

                    if (option != null && option.contains(value)) {
                        return;
                    }

                    fieldValue.set(owner, value);
                },
                owner -> {
                    val option = (Option<V>) holder[0];

                    if (option != null) {
                        return option.orNull();
                    }

                    val value = fieldValue.get(owner);
                    holder[0] = Options.of(value);
                    return value;
                }
        );
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final String clazz,
            @NotNull final String name
    ) {
        return FieldValues.of(clazz, name, Function.identity(), Function.identity());
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name
    ) {
        return FieldValues.of(clazz, name, Function.identity(), Function.identity());
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final String clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> getMapper
    ) {
        return FieldValues.of(clazz, name, Function.identity(), getMapper);
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> getMapper
    ) {
        return FieldValues.of(clazz, name, Function.identity(), getMapper);
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final String clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> setMapper,
            @NotNull final Function<T, T> getMapper
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");

        try {
            return FieldValues.of(Class.forName(clazz), name, setMapper, getMapper);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find class %s!",
                            clazz
                    ), e
            );
        }
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> setMapper,
            @NotNull final Function<T, T> getMapper
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");

        val field = Reflects.getField(clazz, name);

        return FieldValues.of(clazz, field, setMapper, getMapper);
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final java.lang.reflect.Field field
    ) {
        return FieldValues.of(clazz, field, Function.identity(), Function.identity());
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final java.lang.reflect.Field field,
            @NotNull final Function<T, T> getMapper
    ) {
        return FieldValues.of(clazz, field, Function.identity(), getMapper);
    }

    public <O, T> @NotNull FieldValue<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final java.lang.reflect.Field field,
            @NotNull final Function<T, T> setMapper,
            @NotNull final Function<T, T> getMapper
    ) {
        field.setAccessible(true);

        return fromLambda(
                (owner, value) -> Fields.set(clazz, field, owner, setMapper.apply(value)),
                owner -> getMapper.apply(Fields.get(clazz, field, owner))
        );
    }

}
