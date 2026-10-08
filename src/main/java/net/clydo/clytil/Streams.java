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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.random.RandomGenerator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Extra {@link Collector}s.
 */
@UtilityClass
public class Streams {

    /**
     * Collects {@link Map.Entry entries} into a map, keeping their encounter order. Duplicate keys
     * throw {@link IllegalStateException}.
     */
    @Contract(pure = true)
    public <K, V> @NotNull Collector<Map.Entry<? extends K, ? extends V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (first, second) -> {
                    throw new IllegalStateException("Duplicate key with values " + first + " and " + second);
                },
                LinkedHashMap::new
        );
    }

    /**
     * Collects into an {@link ArrayList}, unlike {@link java.util.stream.Stream#toList()}, which is
     * unmodifiable.
     */
    @Contract(pure = true)
    public <T> @NotNull Collector<T, ?, List<T>> toMutableList() {
        return Collectors.toCollection(ArrayList::new);
    }

    /**
     * Collects into a {@link HashSet}.
     */
    @Contract(pure = true)
    public <T> @NotNull Collector<T, ?, Set<T>> toMutableSet() {
        return Collectors.toCollection(HashSet::new);
    }

    /**
     * Collects into an {@link ArrayList} shuffled with {@code random}.
     */
    @Contract(pure = true)
    public <T> @NotNull Collector<T, ?, List<T>> toShuffledList(
            @NotNull final RandomGenerator random
    ) {
        Validates.require(random, "random");

        return Collectors.collectingAndThen(
                Collectors.toCollection(ArrayList::new),
                list -> {
                    Lists.shuffle(list, random);
                    return list;
                }
        );
    }

}
