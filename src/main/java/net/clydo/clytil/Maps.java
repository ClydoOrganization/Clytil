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

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class Maps {

    /**
     * Creates an {@link EnumMap} with an entry for every constant of {@code type}.
     *
     * @param type          the enum class
     * @param valueFunction computes each constant's value
     * @return a new, mutable map
     */
    @Contract("_, _ -> new")
    public <K extends Enum<K>, V> @NotNull EnumMap<K, V> enumMap(
            @NotNull final Class<K> type,
            @NotNull final Function<? super K, ? extends V> valueFunction
    ) {
        Validates.require(type, "type");
        Validates.require(valueFunction, "valueFunction");

        val map = new EnumMap<K, V>(type);
        for (val key : type.getEnumConstants()) {
            map.put(key, valueFunction.apply(key));
        }

        return map;
    }

    /**
     * Copies {@code map} with every value passed through {@code mapper}, keeping the key order.
     * Unlike {@link java.util.stream.Collectors#toMap}, {@code null} values are allowed.
     *
     * @return a new, mutable map
     */
    @Contract("_, _ -> new")
    public <K, V, R> @NotNull Map<K, R> mapValues(
            @NotNull final Map<K, V> map,
            @NotNull final Function<? super V, ? extends R> mapper
    ) {
        Validates.require(map, "map");
        Validates.require(mapper, "mapper");

        val result = new LinkedHashMap<K, R>(Math.max(16, (int) (map.size() / 0.75f) + 1));
        for (val entry : map.entrySet()) {
            result.put(entry.getKey(), mapper.apply(entry.getValue()));
        }

        return result;
    }

    /**
     * Returns an unmodifiable copy of {@code map} with {@code key} set to {@code value}.
     */
    @Contract("_, _, _ -> new")
    public <K, V> @NotNull Map<K, V> with(
            @NotNull final Map<? extends K, ? extends V> map,
            final K key,
            final V value
    ) {
        Validates.require(map, "map");

        val copy = new LinkedHashMap<K, V>(map);
        copy.put(key, value);
        return Collections.unmodifiableMap(copy);
    }

    /**
     * Returns an unmodifiable copy of {@code map} without {@code key}.
     */
    @Contract("_, _ -> new")
    public <K, V> @NotNull Map<K, V> without(
            @NotNull final Map<? extends K, ? extends V> map,
            @Nullable final Object key
    ) {
        Validates.require(map, "map");

        val copy = new LinkedHashMap<K, V>(map);
        copy.remove(key);
        return Collections.unmodifiableMap(copy);
    }

}
