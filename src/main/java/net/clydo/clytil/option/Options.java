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

package net.clydo.clytil.option;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@UtilityClass
public class Options {

    public <T> None<T> none() {
        return None.of();
    }

    @Contract("_ -> new")
    public <T> @NotNull Option<T> some(@NotNull T value) {
        return Some.of(value);
    }

    public <T> Option<T> of(@Nullable T value) {
        return (value != null)
                ? some(value)
                : none();
    }

    @Nullable
    public <U> Option<U> cast(@Nullable Option<?> option) {
        return (option != null)
                ? option.cast()
                : null;
    }

    @NotNull
    public <U> Option<U> castSafe(@Nullable Option<?> option) {
        return (option != null)
                ? option.cast()
                : none();
    }

    @NotNull
    public <U> Option<U> castOrThrow(@NotNull Option<?> option) {
        Objects.requireNonNull(option, "option must not be null");

        return option.cast();
    }

    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
    public <T> Option<T> ofJava(@Nullable java.util.Optional<T> javaOptional) {
        if (javaOptional == null || javaOptional.isEmpty()) {
            return none();
        }

        return some(javaOptional.get());
    }

    @SuppressWarnings("OptionalAssignedToNull")
    public <T> java.util.Optional<T> toJava(Option<T> option) {
        return option != null
                ? option.toJava()
                : null;
    }

    public <T> @NotNull Collection<T> presents(final Collection<? extends Option<? extends T>> options) {
        Objects.requireNonNull(options);

        val list = new ArrayList<T>();
        for (val option : options) {
            if (!option.isSome()) {
                continue;
            }

            val t = option.orThrow();
            list.add(t);
        }
        return list;
    }

}
