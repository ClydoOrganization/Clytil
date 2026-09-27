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

public interface Curve<R extends Curve<R>> {

    @NotNull
    R radiusTL(
            final float radius
    );

    @NotNull
    R radiusTR(
            final float radius
    );

    @NotNull
    R radiusBR(
            final float radius
    );

    @NotNull
    R radiusBL(
            final float radius
    );

    @NotNull
    default R radius(
            final float radiusTL,
            final float radiusTR,
            final float radiusBR,
            final float radiusBL
    ) {
        return this.radiusTL(radiusTL)
                .radiusTR(radiusTR)
                .radiusBR(radiusBR)
                .radiusBL(radiusBL);
    }

    @NotNull
    R radius(
            final float radius
    );

    @NotNull
    default R rounded(
            final float radius
    ) {
        return this.radius(radius).rounded(true);
    }

    @NotNull
    R rounded(
            final boolean rounded
    );

    @NotNull
    R rounded();

}
