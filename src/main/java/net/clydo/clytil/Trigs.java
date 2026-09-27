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

/**
 * Lookup tables for whole-degree sine and cosine.
 * Avoids recomputing {@link Math#sin(double)} / {@link Math#cos(double)}
 * for the common "angle in degrees" case.
 */
@UtilityClass
public class Trigs {

    private final int DEGREES = 360;

    private final float[] SIN_LIST = new float[DEGREES];
    private final float[] COS_LIST = new float[DEGREES];

    static {
        for (var i = 0; i < SIN_LIST.length; i++) {
            val radians = Math.toRadians(i);
            SIN_LIST[i] = (float) Math.sin(radians);
            COS_LIST[i] = (float) Math.cos(radians);
        }
    }

    /**
     * Sine of a whole-degree angle.
     *
     * @param a angle in degrees, expected within {@code 0..359}
     * @return the sine of {@code a}
     * @throws ArrayIndexOutOfBoundsException if {@code a} is outside {@code 0..359}
     */
    public double angleSin(
            final int a
    ) {
        return SIN_LIST[a];
    }

    /**
     * Cosine of a whole-degree angle.
     *
     * @param a angle in degrees, expected within {@code 0..359}
     * @return the cosine of {@code a}
     * @throws ArrayIndexOutOfBoundsException if {@code a} is outside {@code 0..359}
     */
    public double angleCos(
            final int a
    ) {
        return COS_LIST[a];
    }

}
