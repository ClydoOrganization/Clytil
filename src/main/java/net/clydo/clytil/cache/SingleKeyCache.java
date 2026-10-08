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
 * Copyright (C) 2026 ClydoNetwork
 */

package net.clydo.clytil.cache;

import net.clydo.clytil.Validates;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

/**
 * Remembers the value computed for the most recent key, for code that asks for the same key many
 * times in a row. Not thread-safe.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class SingleKeyCache<K, V> {

    private final Function<? super K, ? extends V> computer;
    private boolean filled;
    private K lastKey;
    private V lastValue;

    public SingleKeyCache(
            @NotNull final Function<? super K, ? extends V> computer
    ) {
        this.computer = Validates.require(computer, "computer");
    }

    /**
     * Returns the value for {@code key}, recomputing it only when {@code key} differs from the
     * previous one.
     */
    public V get(
            final K key
    ) {
        if (!this.filled || !Objects.equals(this.lastKey, key)) {
            this.lastValue = this.computer.apply(key);
            this.lastKey = key;
            this.filled = true;
        }

        return this.lastValue;
    }

    /**
     * Forgets the cached key and value.
     */
    public void clear() {
        this.filled = false;
        this.lastKey = null;
        this.lastValue = null;
    }

}
