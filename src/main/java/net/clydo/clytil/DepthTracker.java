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

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Tracks nested scope depth and validates balanced {@link #push()} and {@link #pop()} operations.
 *
 * <p>Useful for managing and validating nested scopes such as rendering,
 * transformations, layouts, and resource lifetimes.</p>
 */
@RequiredArgsConstructor
public final class DepthTracker {

    private final String name;
    private int depth;

    /**
     * Enters a new scope.
     */
    public void push() {
        this.depth++;
    }

    /**
     * Leaves the current scope.
     *
     * @throws IllegalStateException if no scope is active
     */
    public void pop() {
        if (this.depth == 0) {
            throw this.error("pop() called without an active scope");
        }

        this.depth--;
    }

    /**
     * Asserts that no scope is active.
     *
     * @throws IllegalStateException if one or more scopes are active
     */
    public void assertEmpty() {
        this.assertDepth(0);
    }

    /**
     * Asserts that at least one scope is active.
     *
     * @throws IllegalStateException if no scope is active
     */
    public void assertNotEmpty() {
        if (this.depth == 0) {
            throw this.error("expected an active scope");
        }
    }

    /**
     * Asserts that the current depth matches the expected depth.
     *
     * @param expected the expected depth
     * @throws IllegalArgumentException if {@code expected} is negative
     * @throws IllegalStateException    if the current depth differs
     */
    public void assertDepth(
            final int expected
    ) {
        this.validateDepth(expected);

        if (this.depth != expected) {
            throw this.error(
                    "expected depth " + expected + ", but was " + this.depth
            );
        }
    }

    /**
     * Asserts that the current depth is at least the specified value.
     *
     * @param minimum the minimum allowed depth
     * @throws IllegalArgumentException if {@code minimum} is negative
     * @throws IllegalStateException    if the current depth is below the minimum
     */
    public void assertAtLeast(
            final int minimum
    ) {
        this.validateDepth(minimum);

        if (this.depth < minimum) {
            throw this.error(
                    "expected depth >= " + minimum + ", but was " + this.depth
            );
        }
    }

    /**
     * @return {@code true} if no scope is active
     */
    public boolean isEmpty() {
        return this.depth == 0;
    }

    /**
     * @return {@code true} if at least one scope is active
     */
    public boolean isActive() {
        return this.depth > 0;
    }

    /**
     * @return the current scope depth
     */
    public int depth() {
        return this.depth;
    }

    private void validateDepth(
            final int depth
    ) {
        if (depth < 0) {
            throw new IllegalArgumentException(
                    "depth must not be negative: " + depth
            );
        }
    }

    @Contract(value = "_ -> new", pure = true)
    private @NotNull IllegalStateException error(
            @NotNull final String message
    ) {
        return new IllegalStateException(
                this.name + ": " + message
        );
    }

    @Override
    public String toString() {
        return this.name + "[depth=" + this.depth + ']';
    }

}