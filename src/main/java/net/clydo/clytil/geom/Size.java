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

public interface Size<R extends Size<R>> {

    float width();

    float height();

    @NotNull
    R width(
            final float width
    );

    @NotNull
    R height(
            final float height
    );

    @NotNull
    default R size(
            final float width,
            final float height
    ) {
        return this.width(width).height(height);
    }

    @NotNull
    default R size(
            final float size
    ) {
        return this.width(size).height(size);
    }

}
