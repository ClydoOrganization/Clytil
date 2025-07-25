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
import net.clydo.clytil.Numbers;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface DefaultValue<V> {

    V def();

    default <U> V safeCast(U value) {
        val def = this.def();
        if (value != null) {
            if (value instanceof Number valueNumber && def instanceof Number defaultNumber) {
                value = (U) Numbers.cast(valueNumber, defaultNumber);
            }

            try {
                return (V) value;
            } catch (ClassCastException e) {
                //LOGGER.warn("Cannot cast {} to {}", value, def);
            }
        }
        return def;
    }

    default <C extends V> C defAs(final @NotNull Class<C> type) {
        val value = this.def();
        return value != null ? type.cast(value) : null;
    }

    default <C extends V> C defAs() {
        val value = this.def();
        return value != null ? (C) value : null;
    }

    default <C> C unsafeDefAs(final @NotNull Class<C> type) {
        val value = this.def();
        return value != null ? type.cast(value) : null;
    }

    default <C> C unsafeDefAs() {
        val value = this.def();
        return value != null ? (C) value : null;
    }

}
