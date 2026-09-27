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

import org.jetbrains.annotations.NotNull;

public interface Point<P extends Point<P>> {

    float x();

    float y();

    @NotNull
    P x(
            final float x
    );

    @NotNull
    P y(
            final float y
    );

    @NotNull
    default P point(
            final float x,
            final float y
    ) {
        return this.x(x).y(y);
    }

    @NotNull
    default P point(
            final float point
    ) {
        return this.x(point).y(point);
    }

}
