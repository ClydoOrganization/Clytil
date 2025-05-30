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

package net.clydo.clytil.iface;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@FunctionalInterface
public interface Condition {

    /**
     * Evaluates this condition.
     *
     * @return true if the condition is met, false otherwise
     */
    boolean test();

    /**
     * Returns a condition that is the logical negation of this condition.
     *
     * @return a negated condition
     */
    default Condition negate() {
        return () -> !this.test();
    }

    /**
     * Returns a composed condition that represents a short-circuiting logical AND of this condition and another.
     *
     * @param other the other condition to evaluate
     * @return a new condition representing this AND other
     */
    default Condition and(
            @NotNull final Condition other
    ) {
        Objects.requireNonNull(other);
        return () -> this.test() && other.test();
    }

    /**
     * Returns a composed condition that represents a short-circuiting logical OR of this condition and another.
     *
     * @param other the other condition to evaluate
     * @return a new condition representing this OR other
     */
    default Condition or(
            @NotNull final Condition other
    ) {
        Objects.requireNonNull(other);
        return () -> this.test() || other.test();
    }

    /**
     * Returns a condition that always evaluates to true.
     *
     * @return a condition that always returns true
     */
    @Contract(pure = true)
    static @NotNull Condition alwaysTrue() {
        return () -> true;
    }


    /**
     * Returns a condition that always evaluates to false.
     *
     * @return a condition that always returns false
     */
    @Contract(pure = true)
    static @NotNull Condition alwaysFalse() {
        return () -> false;
    }

    /**
     * Returns a condition based on a given boolean value.
     *
     * @param value the boolean value
     * @return a condition that returns the given value
     */
    @Contract(pure = true)
    static @NotNull Condition of(final boolean value) {
        return () -> value;
    }

}
