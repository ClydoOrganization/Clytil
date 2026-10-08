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

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Combinators for {@link Predicate}s. The combined predicates short-circuit, and the varargs and
 * collection forms copy their input, so later changes to it do not affect the result.
 */
@UtilityClass
public class Predicates {

    @Contract(pure = true)
    public <T> @NotNull Predicate<T> alwaysTrue() {
        return value -> true;
    }

    @Contract(pure = true)
    public <T> @NotNull Predicate<T> alwaysFalse() {
        return value -> false;
    }

    /**
     * Returns the negation of {@code predicate}.
     */
    @Contract(pure = true)
    public <T> @NotNull Predicate<T> not(
            @NotNull final Predicate<? super T> predicate
    ) {
        Validates.require(predicate, "predicate");

        return value -> !predicate.test(value);
    }

    /**
     * Returns a predicate that passes when every given predicate passes; with none, it always passes.
     */
    @SafeVarargs
    public <T> @NotNull Predicate<T> allOf(
            @NotNull final Predicate<? super T>... predicates
    ) {
        Validates.require(predicates, "predicates");

        return all(predicates.clone());
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull Predicate<T> allOf(
            @NotNull final Collection<? extends Predicate<? super T>> predicates
    ) {
        Validates.require(predicates, "predicates");

        return all((Predicate<? super T>[]) predicates.toArray(Predicate[]::new));
    }

    /**
     * Returns a predicate that passes when any given predicate passes; with none, it never passes.
     */
    @SafeVarargs
    public <T> @NotNull Predicate<T> anyOf(
            @NotNull final Predicate<? super T>... predicates
    ) {
        Validates.require(predicates, "predicates");

        return any(predicates.clone());
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull Predicate<T> anyOf(
            @NotNull final Collection<? extends Predicate<? super T>> predicates
    ) {
        Validates.require(predicates, "predicates");

        return any((Predicate<? super T>[]) predicates.toArray(Predicate[]::new));
    }

    /**
     * Returns a predicate that passes when no given predicate passes; with none, it always passes.
     */
    @SafeVarargs
    public <T> @NotNull Predicate<T> noneOf(
            @NotNull final Predicate<? super T>... predicates
    ) {
        return not(anyOf(predicates));
    }

    public <T> @NotNull Predicate<T> noneOf(
            @NotNull final Collection<? extends Predicate<? super T>> predicates
    ) {
        return not(anyOf(predicates));
    }

    /**
     * Combines {@code predicates}, which the caller has already copied and keeps no reference to.
     */
    private <T> @NotNull Predicate<T> all(
            final Predicate<? super T> @NotNull [] predicates
    ) {
        return switch (predicates.length) {
            case 0 -> alwaysTrue();
            case 1 -> narrow(predicates[0]);
            case 2 -> {
                val first = predicates[0];
                val second = predicates[1];
                yield value -> first.test(value) && second.test(value);
            }
            default -> value -> {
                for (val predicate : predicates) {
                    if (!predicate.test(value)) {
                        return false;
                    }
                }

                return true;
            };
        };
    }

    /**
     * Combines {@code predicates}, which the caller has already copied and keeps no reference to.
     */
    private <T> @NotNull Predicate<T> any(
            final Predicate<? super T> @NotNull [] predicates
    ) {
        return switch (predicates.length) {
            case 0 -> alwaysFalse();
            case 1 -> narrow(predicates[0]);
            case 2 -> {
                val first = predicates[0];
                val second = predicates[1];
                yield value -> first.test(value) || second.test(value);
            }
            default -> value -> {
                for (val predicate : predicates) {
                    if (predicate.test(value)) {
                        return true;
                    }
                }

                return false;
            };
        };
    }

    @SuppressWarnings("unchecked")
    private <T> Predicate<T> narrow(
            @NotNull final Predicate<? super T> predicate
    ) {
        return (Predicate<T>) predicate;
    }

}
