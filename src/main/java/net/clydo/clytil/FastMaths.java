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
import lombok.val;

@UtilityClass
public class FastMaths {

    /**
     * Fast clamp for int values.
     *
     * @param value the value to clamp
     * @param min   minimum allowed value
     * @param max   maximum allowed value
     * @return clamped value in [min, max]
     */
    public int clamp(
            final int value,
            final int min,
            final int max
    ) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Fast clamp for long values.
     *
     * @param value the value to clamp
     * @param min   minimum allowed value
     * @param max   maximum allowed value
     * @return clamped value in [min, max]
     */
    public long clamp(
            final long value,
            final long min,
            final long max
    ) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Fast clamp for float values.
     *
     * @param value the value to clamp
     * @param min   minimum allowed value
     * @param max   maximum allowed value
     * @return clamped value in [min, max]
     */
    public float clamp(
            final float value,
            final float min,
            final float max
    ) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Fast clamp for double values.
     *
     * @param value the value to clamp
     * @param min   minimum allowed value
     * @param max   maximum allowed value
     * @return clamped value in [min, max]
     */
    public double clamp(
            final double value,
            final double min,
            final double max
    ) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Fast rounding of float to nearest int.
     * Works correctly for positive values.
     *
     * @param value float to round
     * @return nearest int
     */
    public int round(final float value) {
        return (int) (value >= 0.0f ? value + 0.5f : value - 0.5f);
    }

    /**
     * Fast rounding of double to nearest int.
     * Works correctly for positive values.
     *
     * @param value double to round
     * @return nearest int
     */
    public int round(final double value) {
        return (int) (value >= 0.0 ? value + 0.5 : value - 0.5);
    }

    /**
     * Fast floor of float to int.
     *
     * @param value float to floor
     * @return largest int less than or equal to value
     */
    public int floor(final float value) {
        val intVal = (int) value;
        return value < (float) intVal ? (intVal - 1) : (intVal);
    }

    /**
     * Fast ceil of float to int.
     *
     * @param value float to ceil
     * @return smallest int greater than or equal to value
     */
    public int ceil(final float value) {
        val intVal = (int) value;
        return (value > (float) intVal) ? (intVal + 1) : (intVal);
    }

    /**
     * Fast floor of double to int.
     *
     * @param value double to floor
     * @return largest int less than or equal to value
     */
    public int floor(final double value) {
        val intVal = (int) value;
        return value < (double) intVal ? (intVal - 1) : (intVal);
    }

    /**
     * Fast floor of double to long.
     *
     * @param value double to floor
     * @return largest long less than or equal to value
     */
    public long lfloor(final double value) {
        val intVal = (long) value;
        return value < (double) intVal ? (intVal - 1L) : (intVal);
    }

    /**
     * Fast ceil of double to int.
     *
     * @param value double to ceil
     * @return smallest int greater than or equal to value
     */
    public int ceil(final double value) {
        val intVal = (int) value;
        return (value > (double) intVal) ? (intVal + 1) : (intVal);
    }

}
