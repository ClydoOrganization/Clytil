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

import net.clydo.clytil.iface.value.FieldValue;
import net.clydo.clytil.iface.value.Value;

public interface Field<O, V> extends Value<V>, FieldValue<O, V> {

    @Override
    void set(final O owner, final V value);

    @Override
    V get(final O owner);

    @Override
    default void set(final V value) {
        this.set(null, value);
    }

    @Override
    default V get() {
        return this.get(null);
    }

    default Field<O, V> toCacheable() {
        return Fields.cacheable(this);
    }

}
