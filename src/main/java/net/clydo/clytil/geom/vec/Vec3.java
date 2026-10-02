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

package net.clydo.clytil.geom.vec;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import org.jetbrains.annotations.NotNull;

@With
@Value
@AllArgsConstructor(staticName = "of")
public class Vec3 implements Vector<Vec3> {

    public static final Vec3 ZERO = Vec3.of(0, 0, 0);
    public static final Vec3 ONE = Vec3.of(1, 1, 1);

    double x;

    double y;

    double z;

    @Override
    public int getDimensions() {
        return 3;
    }

    @Override
    public double getComponent(
            final int axis
    ) {
        return switch (axis) {
            case 0 -> this.x;
            case 1 -> this.y;
            case 2 -> this.z;
            default -> throw new IndexOutOfBoundsException(axis);
        };
    }

    @Override
    public @NotNull Vec3 withComponent(
            final int axis,
            final double value
    ) {
        return switch (axis) {
            case 0 -> this.withX(value);
            case 1 -> this.withY(value);
            case 2 -> this.withZ(value);
            default -> throw new IndexOutOfBoundsException(axis);
        };
    }

}
