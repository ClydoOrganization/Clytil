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
        return value < min ? min : (value > max ? max : value);
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
        return value < min ? min : (value > max ? max : value);
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
        return value < min ? min : (value > max ? max : value);
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
        return value < min ? min : (value > max ? max : value);
    }

    /**
     * Fast rounding of float to nearest int.
     * Works correctly for positive values.
     *
     * @param value float to round
     * @return nearest int
     */
    public int round(
            final float value
    ) {
        return (int) (value >= 0.0f ? value + 0.5f : value - 0.5f);
    }

    /**
     * Fast rounding of double to nearest int.
     * Works correctly for positive values.
     *
     * @param value double to round
     * @return nearest int
     */
    public int round(
            final double value
    ) {
        return (int) (value >= 0.0 ? value + 0.5 : value - 0.5);
    }

    /**
     * Fast floor of float to int.
     *
     * @param value float to floor
     * @return largest int less than or equal to value
     */
    public int floor(
            final float value
    ) {
        val intVal = (int) value;
        return value < (float) intVal ? (intVal - 1) : (intVal);
    }

    /**
     * Fast ceil of float to int.
     *
     * @param value float to ceil
     * @return smallest int greater than or equal to value
     */
    public int ceil(
            final float value
    ) {
        val intVal = (int) value;
        return (value > (float) intVal) ? (intVal + 1) : (intVal);
    }

    /**
     * Fast floor of double to int.
     *
     * @param value double to floor
     * @return largest int less than or equal to value
     */
    public int floor(
            final double value
    ) {
        val intVal = (int) value;
        return value < (double) intVal ? (intVal - 1) : (intVal);
    }

    /**
     * Fast floor of double to long.
     *
     * @param value double to floor
     * @return largest long less than or equal to value
     */
    public long lfloor(
            final double value
    ) {
        val intVal = (long) value;
        return value < (double) intVal ? (intVal - 1L) : (intVal);
    }

    /**
     * Fast ceil of double to int.
     *
     * @param value double to ceil
     * @return smallest int greater than or equal to value
     */
    public int ceil(
            final double value
    ) {
        val intVal = (int) value;
        return (value > (double) intVal) ? (intVal + 1) : (intVal);
    }

    /**
     * Fast ceil of double to long.
     *
     * @param value double to ceil
     * @return smallest long greater than or equal to value
     */
    public long lceil(
            final double value
    ) {
        val longVal = (long) value;
        return (value > (double) longVal) ? (longVal + 1L) : (longVal);
    }

    // ---------- Min / Max ----------
    // The float and double variants are plain comparisons, which compile to a single
    // compare-and-move. Unlike Math.min/max they do not propagate NaN or order -0.0
    // before 0.0; use Math.min/max when those semantics matter.

    /**
     * Returns the smaller of two int values.
     *
     * @param a first value
     * @param b second value
     * @return the smaller of {@code a} and {@code b}
     */
    public int min(
            final int a,
            final int b
    ) {
        return a <= b ? a : b;
    }

    /**
     * Returns the smaller of three int values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the smaller of {@code a}, {@code b} and {@code c}
     */
    public int min(
            final int a,
            final int b,
            final int c
    ) {
        return min(min(a, b), c);
    }

    /**
     * Returns the smallest element of a non-empty int array.
     *
     * @param values values to scan, must not be empty
     * @return the smallest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public int min(
            final int[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = min(result, values[i]);
        }
        return result;
    }

    /**
     * Returns the larger of two int values.
     *
     * @param a first value
     * @param b second value
     * @return the larger of {@code a} and {@code b}
     */
    public int max(
            final int a,
            final int b
    ) {
        return a >= b ? a : b;
    }

    /**
     * Returns the larger of three int values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the larger of {@code a}, {@code b} and {@code c}
     */
    public int max(
            final int a,
            final int b,
            final int c
    ) {
        return max(max(a, b), c);
    }

    /**
     * Returns the largest element of a non-empty int array.
     *
     * @param values values to scan, must not be empty
     * @return the largest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public int max(
            final int[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = max(result, values[i]);
        }
        return result;
    }

    /**
     * Returns the smaller of two long values.
     *
     * @param a first value
     * @param b second value
     * @return the smaller of {@code a} and {@code b}
     */
    public long min(
            final long a,
            final long b
    ) {
        return a <= b ? a : b;
    }

    /**
     * Returns the smaller of three long values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the smaller of {@code a}, {@code b} and {@code c}
     */
    public long min(
            final long a,
            final long b,
            final long c
    ) {
        return min(min(a, b), c);
    }

    /**
     * Returns the smallest element of a non-empty long array.
     *
     * @param values values to scan, must not be empty
     * @return the smallest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public long min(
            final long[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = min(result, values[i]);
        }
        return result;
    }

    /**
     * Returns the larger of two long values.
     *
     * @param a first value
     * @param b second value
     * @return the larger of {@code a} and {@code b}
     */
    public long max(
            final long a,
            final long b
    ) {
        return a >= b ? a : b;
    }

    /**
     * Returns the larger of three long values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the larger of {@code a}, {@code b} and {@code c}
     */
    public long max(
            final long a,
            final long b,
            final long c
    ) {
        return max(max(a, b), c);
    }

    /**
     * Returns the largest element of a non-empty long array.
     *
     * @param values values to scan, must not be empty
     * @return the largest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public long max(
            final long[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = max(result, values[i]);
        }
        return result;
    }

    /**
     * Returns the smaller of two float values. See the section note on NaN and -0.0.
     *
     * @param a first value
     * @param b second value
     * @return the smaller of {@code a} and {@code b}
     */
    public float min(
            final float a,
            final float b
    ) {
        return a <= b ? a : b;
    }

    /**
     * Returns the smaller of three float values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the smaller of {@code a}, {@code b} and {@code c}
     */
    public float min(
            final float a,
            final float b,
            final float c
    ) {
        return min(min(a, b), c);
    }

    /**
     * Returns the smallest element of a non-empty float array.
     *
     * @param values values to scan, must not be empty
     * @return the smallest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public float min(
            final float[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = min(result, values[i]);
        }
        return result;
    }

    /**
     * Returns the larger of two float values. See the section note on NaN and -0.0.
     *
     * @param a first value
     * @param b second value
     * @return the larger of {@code a} and {@code b}
     */
    public float max(
            final float a,
            final float b
    ) {
        return a >= b ? a : b;
    }

    /**
     * Returns the larger of three float values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the larger of {@code a}, {@code b} and {@code c}
     */
    public float max(
            final float a,
            final float b,
            final float c
    ) {
        return max(max(a, b), c);
    }

    /**
     * Returns the largest element of a non-empty float array.
     *
     * @param values values to scan, must not be empty
     * @return the largest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public float max(
            final float[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = max(result, values[i]);
        }
        return result;
    }

    /**
     * Returns the smaller of two double values. See the section note on NaN and -0.0.
     *
     * @param a first value
     * @param b second value
     * @return the smaller of {@code a} and {@code b}
     */
    public double min(
            final double a,
            final double b
    ) {
        return a <= b ? a : b;
    }

    /**
     * Returns the smaller of three double values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the smaller of {@code a}, {@code b} and {@code c}
     */
    public double min(
            final double a,
            final double b,
            final double c
    ) {
        return min(min(a, b), c);
    }

    /**
     * Returns the smallest element of a non-empty double array.
     *
     * @param values values to scan, must not be empty
     * @return the smallest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public double min(
            final double[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = min(result, values[i]);
        }
        return result;
    }

    /**
     * Returns the larger of two double values. See the section note on NaN and -0.0.
     *
     * @param a first value
     * @param b second value
     * @return the larger of {@code a} and {@code b}
     */
    public double max(
            final double a,
            final double b
    ) {
        return a >= b ? a : b;
    }

    /**
     * Returns the larger of three double values.
     *
     * @param a first value
     * @param b second value
     * @param c third value
     * @return the larger of {@code a}, {@code b} and {@code c}
     */
    public double max(
            final double a,
            final double b,
            final double c
    ) {
        return max(max(a, b), c);
    }

    /**
     * Returns the largest element of a non-empty double array.
     *
     * @param values values to scan, must not be empty
     * @return the largest element of {@code values}
     * @throws ArrayIndexOutOfBoundsException if {@code values} is empty
     */
    public double max(
            final double[] values
    ) {
        var result = values[0];
        for (var i = 1; i < values.length; i++) {
            result = max(result, values[i]);
        }
        return result;
    }

    // ---------- Clamping ----------

    /**
     * Clamps a float value to [0, 1].
     *
     * @param value the value to clamp
     * @return clamped value in [0, 1]
     */
    public float clamp01(
            final float value
    ) {
        return value < 0.0f ? 0.0f : (value > 1.0f ? 1.0f : value);
    }

    /**
     * Clamps a double value to [0, 1].
     *
     * @param value the value to clamp
     * @return clamped value in [0, 1]
     */
    public double clamp01(
            final double value
    ) {
        return value < 0.0 ? 0.0 : (value > 1.0 ? 1.0 : value);
    }

    /**
     * Narrows a long to an int, saturating at the int range instead of wrapping.
     *
     * @param value the value to narrow
     * @return {@code value} clamped to [{@link Integer#MIN_VALUE}, {@link Integer#MAX_VALUE}]
     */
    public int saturatedCast(
            final long value
    ) {
        return (int) clamp(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    // ---------- Interpolation ----------

    /**
     * Linearly interpolates between two float values.
     *
     * @param start value at {@code delta == 0}
     * @param end   value at {@code delta == 1}
     * @param delta interpolation factor, not clamped
     * @return interpolated value
     */
    public float lerp(
            final float start,
            final float end,
            final float delta
    ) {
        return start + delta * (end - start);
    }

    /**
     * Linearly interpolates between two double values.
     *
     * @param start value at {@code delta == 0}
     * @param end   value at {@code delta == 1}
     * @param delta interpolation factor, not clamped
     * @return interpolated value
     */
    public double lerp(
            final double start,
            final double end,
            final double delta
    ) {
        return start + delta * (end - start);
    }

    /**
     * Inverse of {@link #lerp(float, float, float)}: returns the factor at which
     * {@code value} lies between {@code start} and {@code end}.
     *
     * @param value the value to locate
     * @param start range start
     * @param end   range end, must differ from {@code start}
     * @return interpolation factor, not clamped
     */
    public float inverseLerp(
            final float value,
            final float start,
            final float end
    ) {
        return (value - start) / (end - start);
    }

    /**
     * Inverse of {@link #lerp(double, double, double)}: returns the factor at which
     * {@code value} lies between {@code start} and {@code end}.
     *
     * @param value the value to locate
     * @param start range start
     * @param end   range end, must differ from {@code start}
     * @return interpolation factor, not clamped
     */
    public double inverseLerp(
            final double value,
            final double start,
            final double end
    ) {
        return (value - start) / (end - start);
    }

    /**
     * Maps a value from one range to another.
     *
     * @param value   the value to map
     * @param fromMin source range start
     * @param fromMax source range end, must differ from {@code fromMin}
     * @param toMin   target range start
     * @param toMax   target range end
     * @return mapped value, not clamped
     */
    public double remap(
            final double value,
            final double fromMin,
            final double fromMax,
            final double toMin,
            final double toMax
    ) {
        return lerp(toMin, toMax, inverseLerp(value, fromMin, fromMax));
    }

    // ---------- Arithmetic ----------

    /**
     * Branchless absolute value of an int.
     * Like {@code Math.abs}, {@link Integer#MIN_VALUE} maps to itself.
     *
     * @param value the value
     * @return {@code |value|}
     */
    public int abs(
            final int value
    ) {
        val mask = value >> (Integer.SIZE - 1);
        return (value ^ mask) - mask;
    }

    /**
     * Branchless absolute value of a long.
     * Like {@code Math.abs}, {@link Long#MIN_VALUE} maps to itself.
     *
     * @param value the value
     * @return {@code |value|}
     */
    public long abs(
            final long value
    ) {
        val mask = value >> (Long.SIZE - 1);
        return (value ^ mask) - mask;
    }

    /**
     * Absolute value of a float. {@code -0.0f} maps to {@code 0.0f}.
     *
     * @param value the value
     * @return {@code |value|}
     */
    public float abs(
            final float value
    ) {
        return value <= 0.0f ? 0.0f - value : value;
    }

    /**
     * Absolute value of a double. {@code -0.0} maps to {@code 0.0}.
     *
     * @param value the value
     * @return {@code |value|}
     */
    public double abs(
            final double value
    ) {
        return value <= 0.0 ? 0.0 - value : value;
    }

    /**
     * Squares an int value. Overflows silently.
     *
     * @param value the value to square
     * @return {@code value * value}
     */
    public int square(
            final int value
    ) {
        return value * value;
    }

    /**
     * Squares a long value. Overflows silently.
     *
     * @param value the value to square
     * @return {@code value * value}
     */
    public long square(
            final long value
    ) {
        return value * value;
    }

    /**
     * Squares a float value.
     *
     * @param value the value to square
     * @return {@code value * value}
     */
    public float square(
            final float value
    ) {
        return value * value;
    }

    /**
     * Squares a double value.
     *
     * @param value the value to square
     * @return {@code value * value}
     */
    public double square(
            final double value
    ) {
        return value * value;
    }

    /**
     * Returns the floor of the average of two ints without intermediate overflow.
     *
     * @param a first value
     * @param b second value
     * @return {@code floor((a + b) / 2)}
     */
    public int average(
            final int a,
            final int b
    ) {
        return (a & b) + ((a ^ b) >> 1);
    }

    /**
     * Returns the floor of the average of two longs without intermediate overflow.
     *
     * @param a first value
     * @param b second value
     * @return {@code floor((a + b) / 2)}
     */
    public long average(
            final long a,
            final long b
    ) {
        return (a & b) + ((a ^ b) >> 1);
    }

    /**
     * Integer division rounding toward positive infinity.
     * Equivalent to {@code Math.ceilDiv} (Java 18+).
     *
     * @param dividend the dividend
     * @param divisor  the divisor, must not be zero
     * @return smallest int greater than or equal to {@code dividend / divisor}
     * @throws ArithmeticException if {@code divisor} is zero
     */
    public int ceilDiv(
            final int dividend,
            final int divisor
    ) {
        val quotient = dividend / divisor;
        return ((dividend ^ divisor) >= 0 && quotient * divisor != dividend) ? (quotient + 1) : (quotient);
    }

    /**
     * Integer division rounding toward positive infinity.
     * Equivalent to {@code Math.ceilDiv} (Java 18+).
     *
     * @param dividend the dividend
     * @param divisor  the divisor, must not be zero
     * @return smallest long greater than or equal to {@code dividend / divisor}
     * @throws ArithmeticException if {@code divisor} is zero
     */
    public long ceilDiv(
            final long dividend,
            final long divisor
    ) {
        val quotient = dividend / divisor;
        return ((dividend ^ divisor) >= 0L && quotient * divisor != dividend) ? (quotient + 1L) : (quotient);
    }

    // ---------- Powers of two ----------

    /**
     * Checks whether an int is a positive power of two.
     *
     * @param value the value to check
     * @return {@code true} if {@code value} is 1, 2, 4, 8, ...
     */
    public boolean isPowerOfTwo(
            final int value
    ) {
        return value > 0 && (value & (value - 1)) == 0;
    }

    /**
     * Checks whether a long is a positive power of two.
     *
     * @param value the value to check
     * @return {@code true} if {@code value} is 1, 2, 4, 8, ...
     */
    public boolean isPowerOfTwo(
            final long value
    ) {
        return value > 0L && (value & (value - 1L)) == 0L;
    }

    /**
     * Returns the smallest power of two greater than or equal to {@code value}.
     *
     * @param value the value, must be at most {@code 1 << 30}
     * @return the next power of two, or 1 if {@code value <= 1}
     */
    public int ceilPowerOfTwo(
            final int value
    ) {
        return value <= 1 ? 1 : (1 << (Integer.SIZE - Integer.numberOfLeadingZeros(value - 1)));
    }

    /**
     * Returns the smallest power of two greater than or equal to {@code value}.
     *
     * @param value the value, must be at most {@code 1L << 62}
     * @return the next power of two, or 1 if {@code value <= 1}
     */
    public long ceilPowerOfTwo(
            final long value
    ) {
        return value <= 1L ? 1L : (1L << (Long.SIZE - Long.numberOfLeadingZeros(value - 1L)));
    }

    /**
     * Returns the floor of the base-2 logarithm of a positive int.
     *
     * @param value the value, must be positive
     * @return {@code floor(log2(value))}, or -1 if {@code value} is zero
     */
    public int log2(
            final int value
    ) {
        return (Integer.SIZE - 1) - Integer.numberOfLeadingZeros(value);
    }

    /**
     * Returns the floor of the base-2 logarithm of a positive long.
     *
     * @param value the value, must be positive
     * @return {@code floor(log2(value))}, or -1 if {@code value} is zero
     */
    public int log2(
            final long value
    ) {
        return (Long.SIZE - 1) - Long.numberOfLeadingZeros(value);
    }

    // ---------- Comparison ----------

    /**
     * Checks whether two floats differ by at most {@code epsilon}.
     *
     * @param a       first value
     * @param b       second value
     * @param epsilon maximum allowed absolute difference
     * @return {@code true} if {@code |a - b| <= epsilon}
     */
    public boolean approxEquals(
            final float a,
            final float b,
            final float epsilon
    ) {
        return abs(a - b) <= epsilon;
    }

    /**
     * Checks whether two doubles differ by at most {@code epsilon}.
     *
     * @param a       first value
     * @param b       second value
     * @param epsilon maximum allowed absolute difference
     * @return {@code true} if {@code |a - b| <= epsilon}
     */
    public boolean approxEquals(
            final double a,
            final double b,
            final double epsilon
    ) {
        return abs(a - b) <= epsilon;
    }

}
