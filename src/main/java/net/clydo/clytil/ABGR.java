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

@UtilityClass
public class ABGR {

    private final int CHANNEL_MASK = 0xFF;
    private final int ALPHA_SHIFT = 24;
    private final int BLUE_SHIFT = 16;
    private final int GREEN_SHIFT = 8;
    private final int BLUE_MASK = 0x00FF0000;
    private final int GREEN_MASK = 0x0000FF00;
    private final int RED_MASK = 0x000000FF;

    public int alpha(
            final int abgr
    ) {
        return ARGB.alpha(abgr);
    }

    public int red(
            final int abgr
    ) {
        return abgr & CHANNEL_MASK;
    }

    public int green(
            final int abgr
    ) {
        return abgr >> GREEN_SHIFT & CHANNEL_MASK;
    }

    public int blue(
            final int abgr
    ) {
        return abgr >> BLUE_SHIFT & CHANNEL_MASK;
    }

    public float alphaFloat(
            final int abgr
    ) {
        return ARGB.toFloat(alpha(abgr));
    }

    public float redFloat(
            final int abgr
    ) {
        return ARGB.toFloat(red(abgr));
    }

    public float greenFloat(
            final int abgr
    ) {
        return ARGB.toFloat(green(abgr));
    }

    public float blueFloat(
            final int abgr
    ) {
        return ARGB.toFloat(blue(abgr));
    }

    public int color(
            final int red,
            final int green,
            final int blue,
            final int alpha
    ) {
        return alpha << ALPHA_SHIFT
                | (blue & CHANNEL_MASK) << BLUE_SHIFT
                | (green & CHANNEL_MASK) << GREEN_SHIFT
                | red & CHANNEL_MASK;
    }

    public int color(
            final int red,
            final int green,
            final int blue
    ) {
        return color(red, green, blue, ARGB.MAX_CHANNEL);
    }

    public int color(
            final float red,
            final float green,
            final float blue,
            final float alpha
    ) {
        return color(ARGB.toByte(red), ARGB.toByte(green), ARGB.toByte(blue), ARGB.toByte(alpha));
    }

    public int color(
            final float red,
            final float green,
            final float blue
    ) {
        return color(ARGB.toByte(red), ARGB.toByte(green), ARGB.toByte(blue));
    }

    public int withAlpha(
            final int abgr,
            final int alpha
    ) {
        return ARGB.withAlpha(abgr, alpha);
    }

    public int withRed(
            final int abgr,
            final int red
    ) {
        return abgr & ~RED_MASK | red & CHANNEL_MASK;
    }

    public int withGreen(
            final int abgr,
            final int green
    ) {
        return abgr & ~GREEN_MASK | (green & CHANNEL_MASK) << GREEN_SHIFT;
    }

    public int withBlue(
            final int abgr,
            final int blue
    ) {
        return abgr & ~BLUE_MASK | (blue & CHANNEL_MASK) << BLUE_SHIFT;
    }

    public int opaque(
            final int abgr
    ) {
        return ARGB.opaque(abgr);
    }

    public int transparent(
            final int abgr
    ) {
        return ARGB.transparent(abgr);
    }

    public int toArgb(
            final int abgr
    ) {
        return ARGB.toAbgr(abgr);
    }

}
