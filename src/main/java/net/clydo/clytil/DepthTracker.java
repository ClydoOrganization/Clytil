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

/**
 * Guards balanced {@code push()} / {@code pop()} calls by tracking depth.
 * Useful for asserting that scopes (render passes, layouts, ...) are closed.
 */
@RequiredArgsConstructor
public final class DepthTracker {

    private final String name;
    private int depth;

    /**
     * Enters one level.
     */
    public void push() {
        this.depth++;
    }

    /**
     * Leaves one level.
     *
     * @throws IllegalStateException if there is no matching {@link #push()}
     */
    public void pop() {
        if (this.depth == 0) {
            throw new IllegalStateException(
                    this.name + ": pop() without matching push()"
            );
        }

        this.depth--;
    }

    /**
     * @return {@code true} if no level is entered
     */
    public boolean isEmpty() {
        return this.depth == 0;
    }

    /**
     * @return the current depth
     */
    public int depth() {
        return this.depth;
    }

    @Override
    public String toString() {
        return this.name + "[depth=" + this.depth + ']';
    }

}
