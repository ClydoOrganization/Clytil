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

@UtilityClass
public class Bounds {

    /**
     * Checks if the mouse is over a rectangular area.
     *
     * @param left   The left position of the area.
     * @param top    The top position of the area.
     * @param right  The right position of the area.
     * @param bottom The bottom position of the area.
     * @param x      The X position.
     * @param y      The Y position.
     * @return True if the mouse is over the area.
     */
    public boolean contains(
            final float left,
            final float top,
            final float right,
            final float bottom,
            final double x,
            final double y
    ) {
        return x >= left
                && y >= top
                && x < right
                && y < bottom;
    }

    /**
     * Checks if the mouse is over a rectangular area.
     *
     * @param left   The left position of the area.
     * @param top    The top position of the area.
     * @param right  The right position of the area.
     * @param bottom The bottom position of the area.
     * @param x      The X position.
     * @param y      The Y position.
     * @return True if the mouse is over the area.
     */
    public boolean contains(
            final int left,
            final int top,
            final int right,
            final int bottom,
            final double x,
            final double y
    ) {
        return x >= left
                && y >= top
                && x < right
                && y < bottom;
    }

    /**
     * Checks if the mouse is over a vertical rectangular area.
     *
     * @param top        The top position of the area.
     * @param bottom     The bottom position of the area.
     * @param itemTop    The first Y position.
     * @param itemBottom The second Y position.
     * @return True if the mouse is over the area.
     */
    public boolean containsVertically(
            final float top,
            final float bottom,
            final double itemTop,
            final double itemBottom
    ) {
        return itemBottom >= top && itemTop < bottom;
    }

}
