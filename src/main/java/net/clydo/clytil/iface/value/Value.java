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

import net.clydo.clytil.Validates;
import org.jetbrains.annotations.NotNull;

public interface Value<V> extends Setter<V>, Getter<V> {

    static <V> @NotNull Value<V> of(
            @NotNull final Setter<V> setter,
            @NotNull final Getter<V> getter
    ) {
        Validates.requireParam(setter, "setter");
        Validates.requireParam(getter, "getter");

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

}
