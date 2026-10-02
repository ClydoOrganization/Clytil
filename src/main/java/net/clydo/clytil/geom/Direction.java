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

package net.clydo.clytil.geom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public enum Direction {

    UP(-1),
    LEFT(-1),
    DOWN(1),
    RIGHT(1);

    public static final Direction[] VALUES = values();

    private final int sign;

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }

    public boolean isVertical() {
        return this == UP || this == DOWN;
    }

    @Contract(pure = true)
    public static @Nullable Direction fromInput(
            final boolean left,
            final boolean right,
            final boolean up,
            final boolean down
    ) {
        if (left) {
            return LEFT;
        }
        if (right) {
            return RIGHT;
        }
        if (up) {
            return UP;
        }
        if (down) {
            return DOWN;
        }

        return null;
    }

}
