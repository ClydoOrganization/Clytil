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

import java.lang.reflect.Modifier;
import java.util.function.Function;

@SuppressWarnings("unchecked")
@UtilityClass
public class Fields {

    public <O, T> @NotNull Field<O, T> cacheable(
            @NotNull final Field<O, T> field
    ) {
        Validates.requireParam(field, "field");

        return new Field<>() {

            private Option<T> value;

            @Override
            public void set(
                    @Nullable final O owner,
                    @Nullable final T value
            ) {
                if (this.value != null && this.value.contains(value)) {
                    return;
                }

                field.set(owner, value);
            }

            @Override
            public T get(
                    @Nullable final O owner
            ) {
                if (this.value != null) {
                    return this.value.orNull();
                }

                val value = field.get();
                this.value = Options.of(value);
                return value;
            }

        };
    }

    public <O, T> @NotNull Field<O, T> of(
            @NotNull final String clazz,
            @NotNull final String name
    ) {
        return Fields.of(clazz, name, Function.identity(), Function.identity());
    }

    public <O, T> @NotNull Field<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name
    ) {
        return Fields.of(clazz, name, Function.identity(), Function.identity());
    }

    public <O, T> @NotNull Field<O, T> of(
            @NotNull final String clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> getMapper
    ) {
        return Fields.of(clazz, name, Function.identity(), getMapper);
    }

    public <O, T> @NotNull Field<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> getMapper
    ) {
        return Fields.of(clazz, name, Function.identity(), getMapper);
    }

    public <O, T> @NotNull Field<O, T> of(
            @NotNull final String clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> setMapper,
            @NotNull final Function<T, T> getMapper
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");

        try {
            return Fields.of(Class.forName(clazz), name, setMapper, getMapper);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to find class %s!",
                            clazz
                    ), e
            );
        }
    }

    public <O, T> @NotNull Field<O, T> of(
            @NotNull final Class<?> clazz,
            @NotNull final String name,
            @NotNull final Function<T, T> setMapper,
            @NotNull final Function<T, T> getMapper
    ) {
        Validates.requireParam(clazz, "class");
        Validates.requireParam(name, "name");

        val field = Reflects.getField(clazz, name);

        return new Field<>() {

            @Override
            public void set(
                    @Nullable final O owner,
                    @Nullable T value
            ) {
                if (owner == null && !this.isStaticField(field)) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Owner cannot be null for non-static field %s::%s",
                                    clazz, name
                            )
                    );
                }

                value = setMapper.apply(value);

                try {
                    field.set(owner, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(
                            String.format(
                                    "Failed to set %s::%s to %s on %s!",
                                    clazz, name, value, owner
                            ), e
                    );
                }
            }

            @Override
            public T get(
                    @Nullable final O owner
            ) {
                if (owner == null && !this.isStaticField(field)) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Owner cannot be null for non-static field %s::%s",
                                    clazz, name
                            )
                    );
                }

                try {
                    return getMapper.apply((T) field.get(owner));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(
                            String.format(
                                    "Failed to get %s::%s on %s!",
                                    clazz, name, owner
                            ), e
                    );
                }
            }

            private boolean isStaticField(@NotNull java.lang.reflect.Field field) {
                return Modifier.isStatic(field.getModifiers());
            }

        };
    }

}
