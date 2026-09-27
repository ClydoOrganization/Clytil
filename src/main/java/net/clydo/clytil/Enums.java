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

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.clydo.clytil.option.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class Enums {

    public <T extends Enum<T>> Option<T> valueOf(
            @NotNull final Class<T> clazz,
            @NotNull final String name
    ) {
        Validates.require(clazz, "clazz");
        Validates.require(name, "name");

        try {
            return Option.some(Enum.valueOf(clazz, name));
        } catch (Throwable t) {
            return Option.none();
        }
    }

    /**
     * Returns the names of the given enum constants.
     *
     * @param values the constants
     * @param <T>    enum type
     * @return the names, in order
     */
    public <T extends Enum<T>> String[] names(
            final T @NotNull [] values
    ) {
        return Arrays.stream(values)
                .map(Enum::name)
                .toArray(String[]::new);
    }

    /**
     * Returns the names of every constant of the given enum.
     *
     * @param clazz the enum type
     * @param <T>   enum type
     * @return the names, in declaration order
     */
    public <T extends Enum<T>> String[] names(
            @NotNull final Class<T> clazz
    ) {
        return names(clazz.getEnumConstants());
    }

    /**
     * Maps enum ordinals to their constants.
     *
     * @param values the constants
     * @param <T>    enum type
     * @return an unmodifiable ordinal-to-constant map
     */
    public <T extends Enum<T>> @NotNull Map<Integer, T> byOrdinal(
            final T @NotNull [] values
    ) {
        return Arrays.stream(values)
                .collect(Collectors.toUnmodifiableMap(Enum::ordinal, Function.identity()));
    }

    /**
     * Maps enum constants to their ordinals.
     *
     * @param values the constants
     * @param <T>    enum type
     * @return an unmodifiable constant-to-ordinal map
     */
    public <T extends Enum<T>> @NotNull Map<T, Integer> ordinals(
            final T @NotNull [] values
    ) {
        return Arrays.stream(values)
                .collect(Collectors.toUnmodifiableMap(Function.identity(), Enum::ordinal));
    }

    /**
     * Picks the highest-priority element that is both available and allowed.
     *
     * @param available     elements to choose from
     * @param priorityOrder candidates ordered from highest to lowest priority
     * @param allowed       optional filter; {@code null} or empty allows everything
     * @param fallback      returned when nothing matches
     * @param <T>           element type
     * @return the first priority element found, or {@code fallback}
     */
    public <T> T firstMatch(
            @NotNull final Collection<T> available,
            @NotNull final List<T> priorityOrder,
            @Nullable final Set<T> allowed,
            @NotNull final T fallback
    ) {
        if (available.isEmpty()) {
            return fallback;
        }

        val filtered = (allowed == null || allowed.isEmpty())
                ? available
                : available.stream().filter(allowed::contains).toList();

        if (filtered.isEmpty()) {
            return fallback;
        }

        for (val candidate : priorityOrder) {
            if (filtered.contains(candidate)) {
                return candidate;
            }
        }

        return fallback;
    }

}
