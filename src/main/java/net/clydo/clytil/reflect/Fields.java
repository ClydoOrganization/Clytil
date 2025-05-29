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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;

@SuppressWarnings("unchecked")
@UtilityClass
public class Fields {

    public <V> void set(
            @NotNull final Class<?> clazz,
            @NotNull final java.lang.reflect.Field field,
            @Nullable final Object owner,
            @Nullable final V value
    ) {
        if (owner == null && !Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException(
                    String.format(
                            "Owner cannot be null for non-static field %s::%s",
                            clazz, field.getName()
                    )
            );
        }

        try {
            field.set(owner, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to set %s::%s to %s on %s!",
                            clazz, field.getName(), value, owner
                    ), e
            );
        }
    }

    public <V> @NotNull V get(
            @NotNull final Class<?> clazz,
            @NotNull final java.lang.reflect.Field field,
            @Nullable final Object owner
    ) {
        if (owner == null && !Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException(
                    String.format(
                            "Owner cannot be null for non-static field %s::%s",
                            clazz, field.getName()
                    )
            );
        }

        try {
            return (V) field.get(owner);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to get %s::%s on %s!",
                            clazz, field.getName(), owner
                    ), e
            );
        }
    }

}
