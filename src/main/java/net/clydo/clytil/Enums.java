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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
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

    @Contract("_, _ -> new")
    public <T extends Enum<T>> @NotNull Set<T> unmodifiableSet(
            @NotNull final Class<T> clazz,
            @NotNull final Collection<? extends T> values
    ) {
        val set = EnumSet.noneOf(Validates.require(clazz, "clazz"));
        set.addAll(values);
        return Collections.unmodifiableSet(set);
    }

    /**
     * Returns every constant of the enum except the given ones.
     *
     * @return a new, mutable set
     */
    @SafeVarargs
    @Contract("_, _ -> new")
    public <T extends Enum<T>> @NotNull EnumSet<T> allExcept(
            @NotNull final T excluded,
            @NotNull final T... moreExcluded
    ) {
        Validates.require(excluded, "excluded");

        return EnumSet.complementOf(EnumSet.of(excluded, moreExcluded));
    }

    @Contract(pure = true)
    public <T extends Enum<T>> @NotNull List<T> constants(
            @NotNull final Class<T> clazz
    ) {
        return List.of(Validates.require(clazz, "clazz").getEnumConstants());
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

}
