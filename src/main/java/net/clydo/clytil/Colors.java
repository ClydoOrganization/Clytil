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
import org.jetbrains.annotations.Nullable;

/**
 * ARGB int color math: channel access, blending, HSB conversion and
 * sRGB/linear conversion. Hex string formatting lives in {@link Hexes}.
 */
@UtilityClass
public class Colors {
    private final int ALPHA_SHIFT = 24;
    private final int RED_SHIFT = 16;
    private final int GREEN_SHIFT = 8;
    private final int BLUE_SHIFT = 0;

    private final int CHANNEL_MASK = 0xFF;
    private final int ALPHA_MASK = CHANNEL_MASK << ALPHA_SHIFT;
    private final int RGB_MASK = ~ALPHA_MASK;

    public final int MAX_CHANNEL = 255;
    public final float MAX_CHANNEL_F = 255.0f;
    private final double ROUNDING_OFFSET = 0.5;
    private final float ROUNDING_OFFSET_F = 0.5f;

    private final float HUE_SECTORS = 6.0f;
    private final float GREEN_HUE_OFFSET = 2.0f;
    private final float BLUE_HUE_OFFSET = 4.0f;
    private final int HSB_COMPONENTS = 3;

    private final double LUMINANCE_RED = 0.299;
    private final double LUMINANCE_GREEN = 0.587;
    private final double LUMINANCE_BLUE = 0.114;
    private final int LIGHT_LUMINANCE_THRESHOLD = 128;

    private final float DEFAULT_SHADE_FACTOR = 0.7f;

    private final int RED_MASK = CHANNEL_MASK << RED_SHIFT;
    private final int BLUE_MASK = CHANNEL_MASK << BLUE_SHIFT;
    private final int ALPHA_GREEN_MASK = ALPHA_MASK | CHANNEL_MASK << GREEN_SHIFT;

    private final int MEAN_OF_TWO = 2;
    private final int MEAN_OF_FOUR = 4;

    private final float GREYSCALE_RED = 0.3f;
    private final float GREYSCALE_GREEN = 0.59f;
    private final float GREYSCALE_BLUE = 0.11f;

    private final int LINEAR_CHANNEL_DEPTH = 1024;
    private final float MAX_LINEAR_CHANNEL_F = LINEAR_CHANNEL_DEPTH - 1;
    private final float SRGB_LINEAR_THRESHOLD = 0.04045f;
    private final float LINEAR_SRGB_THRESHOLD = 0.0031308f;
    private final float SRGB_LINEAR_SLOPE = 12.92f;
    private final double SRGB_OFFSET = 0.055;
    private final double SRGB_SCALE = 1.055;
    private final double SRGB_GAMMA = 2.4;

    public int lerp(
            final float delta,
            final int start,
            final int end
    ) {
        return start + Maths.floor(delta * (float) (end - start));
    }

    public int lerp(
            final double delta,
            final int start,
            final int end
    ) {
        return start + Maths.floor(delta * (double) (end - start));
    }

    @UtilityClass
    public class ARGB32 {
        public final int BLACK = 0xFF000000;
        public final int BLUE = 0xFF0000FF;
        public final int DARK_GRAY = 0xFF404040;
        public final int GRAY = 0xFF808080;
        public final int GREEN = 0xFF00FF00;
        public final int LIGHT_GRAY = 0xFFC0C0C0;
        public final int ORANGE = 0xFFFFC800;
        public final int PHOENIX = 0xFFFF5a00;
        public final int RED = 0xFFFF0000;
        public final int TRANSPARENT = 0x00000000;
        public final int WHITE = 0xFFFFFFFF;
        public final int YELLOW = 0xFFFFFF00;

        private final short[] SRGB_TO_LINEAR = createSrgbToLinear();
        private final byte[] LINEAR_TO_SRGB = createLinearToSrgb();

        public int alpha(
                final int argb
        ) {
            return argb >>> ALPHA_SHIFT;
        }

        public int red(
                final int argb
        ) {
            return argb >> RED_SHIFT & CHANNEL_MASK;
        }

        public int green(
                final int argb
        ) {
            return argb >> GREEN_SHIFT & CHANNEL_MASK;
        }

        public int blue(
                final int argb
        ) {
            return argb & CHANNEL_MASK;
        }

        public float alphaFloat(
                final int argb
        ) {
            return alpha(argb) / MAX_CHANNEL_F;
        }

        public float redFloat(
                final int argb
        ) {
            return red(argb) / MAX_CHANNEL_F;
        }

        public float greenFloat(
                final int argb
        ) {
            return green(argb) / MAX_CHANNEL_F;
        }

        public float blueFloat(
                final int argb
        ) {
            return blue(argb) / MAX_CHANNEL_F;
        }

        public int alpha(
                final int argb,
                final int alpha
        ) {
            return (argb & ~(CHANNEL_MASK << ALPHA_SHIFT)) | (alpha << ALPHA_SHIFT);
        }

        public int red(
                final int argb,
                final int red
        ) {
            return (argb & ~(CHANNEL_MASK << RED_SHIFT)) | (red << RED_SHIFT);
        }

        public int green(
                final int argb,
                final int green
        ) {
            return (argb & ~(CHANNEL_MASK << GREEN_SHIFT)) | (green << GREEN_SHIFT);
        }

        public int blue(
                final int argb,
                final int blue
        ) {
            return (argb & ~(CHANNEL_MASK << BLUE_SHIFT)) | (blue << BLUE_SHIFT);
        }

        public int alpha(
                final int argb,
                final float alpha
        ) {
            return alpha(argb, toChannel(alpha));
        }

        public int red(
                final int argb,
                final float red
        ) {
            return red(argb, toChannel(red));
        }

        public int green(
                final int argb,
                final float green
        ) {
            return green(argb, toChannel(green));
        }

        public int blue(
                final int argb,
                final float blue
        ) {
            return blue(argb, toChannel(blue));
        }

        public int color(
                final int red,
                final int green,
                final int blue,
                final int alpha
        ) {
            return alpha << ALPHA_SHIFT | red << RED_SHIFT | green << GREEN_SHIFT | blue;
        }

        public int color(
                final int red,
                final int green,
                final int blue
        ) {
            return color(red, green, blue, MAX_CHANNEL);
        }

        public int color(
                final int argb,
                final boolean opaque
        ) {
            return opaque ? ALPHA_MASK | argb : argb;
        }

        public int color(
                final float red,
                final float green,
                final float blue,
                final float alpha
        ) {
            return color(
                    toChannel(red),
                    toChannel(green),
                    toChannel(blue),
                    toChannel(alpha)
            );
        }

        public int color(
                final float red,
                final float green,
                final float blue
        ) {
            return color(
                    toChannel(red),
                    toChannel(green),
                    toChannel(blue),
                    MAX_CHANNEL
            );
        }

        public int multiply(
                final int firstColor,
                final int secondColor
        ) {
            if (firstColor == WHITE) {
                return secondColor;
            }
            if (secondColor == WHITE) {
                return firstColor;
            }

            return color(
                    red(firstColor) * red(secondColor) / MAX_CHANNEL,
                    green(firstColor) * green(secondColor) / MAX_CHANNEL,
                    blue(firstColor) * blue(secondColor) / MAX_CHANNEL,
                    alpha(firstColor) * alpha(secondColor) / MAX_CHANNEL
            );
        }

        public int lerp(
                final float delta,
                final int start,
                final int end
        ) {
            return color(
                    Colors.lerp(delta, red(start), red(end)),
                    Colors.lerp(delta, green(start), green(end)),
                    Colors.lerp(delta, blue(start), blue(end)),
                    Colors.lerp(delta, alpha(start), alpha(end))
            );
        }

        public int lerp(
                final double delta,
                final int start,
                final int end
        ) {
            return color(
                    Colors.lerp(delta, red(start), red(end)),
                    Colors.lerp(delta, green(start), green(end)),
                    Colors.lerp(delta, blue(start), blue(end)),
                    Colors.lerp(delta, alpha(start), alpha(end))
            );
        }

        public int transparent(
                final int argb
        ) {
            return argb & RGB_MASK;
        }

        public int opaque(
                final int argb
        ) {
            return argb | ALPHA_MASK;
        }

        public int toAbgr(
                final int argb
        ) {
            return argb & ALPHA_GREEN_MASK
                    | (argb & RED_MASK) >> RED_SHIFT
                    | (argb & BLUE_MASK) << RED_SHIFT;
        }

        public int fromAbgr(
                final int abgr
        ) {
            return toAbgr(abgr);
        }

        public int reverseColor(
                final int argb
        ) {
            return (argb & ALPHA_MASK) | (~argb & RGB_MASK);
        }

        /**
         * Determines whether an ARGB color is considered light based on its perceived luminance.
         *
         * @param argb The ARGB color as an integer (0xAARRGGBB).
         * @return {@code true} if the color is light, {@code false} if dark.
         */
        public boolean isLight(
                final int argb
        ) {
            val r = red(argb);
            val g = green(argb);
            val b = blue(argb);

            // Standard luminance formula using human eye sensitivity
            val luminance = LUMINANCE_RED * r + LUMINANCE_GREEN * g + LUMINANCE_BLUE * b;

            // Threshold of 128 gives decent contrast; adjust if needed
            return luminance >= LIGHT_LUMINANCE_THRESHOLD;
        }

        public int HSBtoRGB(
                final float hue,
                final float saturation,
                final float brightness
        ) {
            var r = 0;
            var g = 0;
            var b = 0;
            if (saturation == 0) {
                r = g = b = toChannelF(brightness);
            } else {
                val h = (hue - (float) Math.floor(hue)) * HUE_SECTORS;
                val f = h - (float) Math.floor(h);
                val p = brightness * (1.0f - saturation);
                val q = brightness * (1.0f - saturation * f);
                val t = brightness * (1.0f - (saturation * (1.0f - f)));
                switch ((int) h) {
                    case 0:
                        r = toChannelF(brightness);
                        g = toChannelF(t);
                        b = toChannelF(p);
                        break;
                    case 1:
                        r = toChannelF(q);
                        g = toChannelF(brightness);
                        b = toChannelF(p);
                        break;
                    case 2:
                        r = toChannelF(p);
                        g = toChannelF(brightness);
                        b = toChannelF(t);
                        break;
                    case 3:
                        r = toChannelF(p);
                        g = toChannelF(q);
                        b = toChannelF(brightness);
                        break;
                    case 4:
                        r = toChannelF(t);
                        g = toChannelF(p);
                        b = toChannelF(brightness);
                        break;
                    case 5:
                        r = toChannelF(brightness);
                        g = toChannelF(p);
                        b = toChannelF(q);
                        break;
                }
            }
            return ALPHA_MASK | (r << RED_SHIFT) | (g << GREEN_SHIFT) | (b << BLUE_SHIFT);
        }

        public float[] RGBtoHSB(
                final int r,
                final int g,
                final int b,
                @Nullable final float[] hsbvals
        ) {
            val result = hsbvals == null ? new float[HSB_COMPONENTS] : hsbvals;
            val cmax = Math.max(r, Math.max(g, b));
            val cmin = Math.min(r, Math.min(g, b));

            val brightness = ((float) cmax) / MAX_CHANNEL_F;
            val saturation = cmax != 0
                    ? ((float) (cmax - cmin)) / ((float) cmax)
                    : 0.0f;
            var hue = 0.0f;
            if (saturation != 0) {
                val redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
                val greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
                val bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
                if (r == cmax)
                    hue = bluec - greenc;
                else if (g == cmax)
                    hue = GREEN_HUE_OFFSET + redc - bluec;
                else
                    hue = BLUE_HUE_OFFSET + greenc - redc;
                hue = hue / HUE_SECTORS;
                if (hue < 0)
                    hue = hue + 1.0f;
            }
            result[0] = hue;
            result[1] = saturation;
            result[2] = brightness;
            return result;
        }

        public int adjustColor(
                final int rgb,
                final float hue,
                final float saturation,
                final float brightness
        ) {
            // Extract RGB components
            val r = red(rgb);
            val g = green(rgb);
            val b = blue(rgb);

            // Calculate the min and max of RGB components
            val max = Math.max(r, Math.max(g, b));
            val min = Math.min(r, Math.min(g, b));
            val delta = (float) (max - min);

            // Calculate brightness
            var brightnessCurrent = max / MAX_CHANNEL_F;

            // Calculate saturation
            var saturationCurrent = (max == 0) ? 0 : delta / max;

            // Calculate hue
            var hueCurrent = 0.0f;
            if (delta != 0) {
                if (max == r) {
                    hueCurrent = ((g - b) / delta + (g < b ? HUE_SECTORS : 0)) / HUE_SECTORS;
                } else if (max == g) {
                    hueCurrent = ((b - r) / delta + GREEN_HUE_OFFSET) / HUE_SECTORS;
                } else {
                    hueCurrent = ((r - g) / delta + BLUE_HUE_OFFSET) / HUE_SECTORS;
                }
            }

            // Apply the adjustments
            if (hue >= 0) {
                hueCurrent = hue;
            }
            if (saturation >= 0) {
                saturationCurrent = saturation;
            }
            if (brightness >= 0) {
                brightnessCurrent = brightness;
            }

            // Convert HSB back to RGB
            int ri, gi, bi;
            if (saturationCurrent == 0) {
                ri = gi = bi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
            } else {
                val h = hueCurrent * HUE_SECTORS;
                val hInt = (int) h;
                val f = h - hInt;
                val p = brightnessCurrent * (1.0f - saturationCurrent);
                val q = brightnessCurrent * (1.0f - saturationCurrent * f);
                val t = brightnessCurrent * (1.0f - saturationCurrent * (1.0f - f));

                switch (hInt) {
                    case 0:
                        ri = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        gi = Math.round(t * MAX_CHANNEL_F);
                        bi = Math.round(p * MAX_CHANNEL_F);
                        break;
                    case 1:
                        ri = Math.round(q * MAX_CHANNEL_F);
                        gi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        bi = Math.round(p * MAX_CHANNEL_F);
                        break;
                    case 2:
                        ri = Math.round(p * MAX_CHANNEL_F);
                        gi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        bi = Math.round(t * MAX_CHANNEL_F);
                        break;
                    case 3:
                        ri = Math.round(p * MAX_CHANNEL_F);
                        gi = Math.round(q * MAX_CHANNEL_F);
                        bi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        break;
                    case 4:
                        ri = Math.round(t * MAX_CHANNEL_F);
                        gi = Math.round(p * MAX_CHANNEL_F);
                        bi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        break;
                    default: // case 5
                        ri = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        gi = Math.round(p * MAX_CHANNEL_F);
                        bi = Math.round(q * MAX_CHANNEL_F);
                        break;
                }
            }

            // Return the new RGB color with full opacity (alpha = 255)
            return color(ri, gi, bi, MAX_CHANNEL);
        }

        public int adjustBrightness(
                final int rgb,
                final float hue,
                final float saturation,
                final float brightness
        ) {
            // Extract RGB components
            val r = red(rgb);
            val g = green(rgb);
            val b = blue(rgb);

            // Calculate the min and max of RGB components
            val max = Math.max(r, Math.max(g, b));
            val min = Math.min(r, Math.min(g, b));
            val delta = (float) (max - min);

            // Calculate brightness
            var brightnessCurrent = max / MAX_CHANNEL_F;

            // Calculate saturation
            var saturationCurrent = (max == 0) ? 0 : delta / max;

            // Calculate hue
            var hueCurrent = 0.0f;
            if (delta != 0) {
                if (max == r) {
                    hueCurrent = ((g - b) / delta + (g < b ? HUE_SECTORS : 0)) / HUE_SECTORS;
                } else if (max == g) {
                    hueCurrent = ((b - r) / delta + GREEN_HUE_OFFSET) / HUE_SECTORS;
                } else {
                    hueCurrent = ((r - g) / delta + BLUE_HUE_OFFSET) / HUE_SECTORS;
                }
            }

            // Apply the adjustments
            if (hue >= 0) {
                hueCurrent = hue;
            }
            if (saturation >= 0) {
                saturationCurrent = saturation;
            }
            if (brightness >= 0) {
                brightnessCurrent = brightness;
            }

            // Convert HSB back to RGB
            int ri, gi, bi;
            if (saturationCurrent == 0) {
                ri = gi = bi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
            } else {
                val h = hueCurrent * HUE_SECTORS;
                val hInt = (int) h;
                val f = h - hInt;
                val p = brightnessCurrent * (1.0f - saturationCurrent);
                val q = brightnessCurrent * (1.0f - saturationCurrent * f);
                val t = brightnessCurrent * (1.0f - saturationCurrent * (1.0f - f));

                switch (hInt) {
                    case 0:
                        ri = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        gi = Math.round(t * MAX_CHANNEL_F);
                        bi = Math.round(p * MAX_CHANNEL_F);
                        break;
                    case 1:
                        ri = Math.round(q * MAX_CHANNEL_F);
                        gi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        bi = Math.round(p * MAX_CHANNEL_F);
                        break;
                    case 2:
                        ri = Math.round(p * MAX_CHANNEL_F);
                        gi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        bi = Math.round(t * MAX_CHANNEL_F);
                        break;
                    case 3:
                        ri = Math.round(p * MAX_CHANNEL_F);
                        gi = Math.round(q * MAX_CHANNEL_F);
                        bi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        break;
                    case 4:
                        ri = Math.round(t * MAX_CHANNEL_F);
                        gi = Math.round(p * MAX_CHANNEL_F);
                        bi = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        break;
                    default: // case 5
                        ri = Math.round(brightnessCurrent * MAX_CHANNEL_F);
                        gi = Math.round(p * MAX_CHANNEL_F);
                        bi = Math.round(q * MAX_CHANNEL_F);
                        break;
                }
            }

            // Return the new RGB color with full opacity (alpha = 255)
            return color(ri, gi, bi, MAX_CHANNEL);
        }

        public int darker(
                final int argb
        ) {
            return darker(argb, DEFAULT_SHADE_FACTOR);
        }

        public int darker(
                final int argb,
                final float factor
        ) {
            val red = red(argb);
            val green = green(argb);
            val blue = blue(argb);
            val alpha = alpha(argb);

            return color(
                    Math.max((int) (red * factor), 0),
                    Math.max((int) (green * factor), 0),
                    Math.max((int) (blue * factor), 0),
                    alpha
            );
        }

        public int brighter(
                final int argb
        ) {
            return brighter(argb, DEFAULT_SHADE_FACTOR);
        }

        public int brighter(
                final int argb,
                final float factor
        ) {
            var red = (float) red(argb);
            var green = (float) green(argb);
            var blue = (float) blue(argb);
            val alpha = alpha(argb);

            /* From 2D group:
             * 1. black.brighter() should return grey
             * 2. applying brighter to blue will always return blue, brighter
             * 3. non-pure color (non-zero rgb) will eventually return white
             */
            val i = (int) (1.0 / (1.0 - factor));
            if (red == 0 && green == 0 && blue == 0) {
                return color(i, i, i, alpha);
            }
            if (red > 0 && red < i) red = i;
            if (green > 0 && green < i) green = i;
            if (blue > 0 && blue < i) blue = i;

            return color(
                    Math.min((int) (red / factor), MAX_CHANNEL),
                    Math.min((int) (green / factor), MAX_CHANNEL),
                    Math.min((int) (blue / factor), MAX_CHANNEL),
                    alpha
            );
        }

        public int transparency(
                final int argb
        ) {
            val alpha = alpha(argb);

            if (alpha == MAX_CHANNEL) {
                return Transparency.OPAQUE;
            } else if (alpha == 0x0) {
                return Transparency.BITMASK;
            } else {
                return Transparency.TRANSLUCENT;
            }
        }

        public float srgbToLinearChannel(
                final int srgb
        ) {
            return SRGB_TO_LINEAR[srgb] / MAX_LINEAR_CHANNEL_F;
        }

        public int linearToSrgbChannel(
                final float linear
        ) {
            return LINEAR_TO_SRGB[Maths.floor(linear * MAX_LINEAR_CHANNEL_F)] & CHANNEL_MASK;
        }

        public int meanLinear(
                final int first,
                final int second,
                final int third,
                final int fourth
        ) {
            return color(
                    linearChannelMean(red(first), red(second), red(third), red(fourth)),
                    linearChannelMean(green(first), green(second), green(third), green(fourth)),
                    linearChannelMean(blue(first), blue(second), blue(third), blue(fourth)),
                    (alpha(first) + alpha(second) + alpha(third) + alpha(fourth)) / MEAN_OF_FOUR
            );
        }

        public int linearLerp(
                final float delta,
                final int start,
                final int end
        ) {
            return color(
                    LINEAR_TO_SRGB[Colors.lerp(delta, SRGB_TO_LINEAR[red(start)], SRGB_TO_LINEAR[red(end)])] & CHANNEL_MASK,
                    LINEAR_TO_SRGB[Colors.lerp(delta, SRGB_TO_LINEAR[green(start)], SRGB_TO_LINEAR[green(end)])] & CHANNEL_MASK,
                    LINEAR_TO_SRGB[Colors.lerp(delta, SRGB_TO_LINEAR[blue(start)], SRGB_TO_LINEAR[blue(end)])] & CHANNEL_MASK,
                    Colors.lerp(delta, alpha(start), alpha(end))
            );
        }

        public int average(
                final int first,
                final int second
        ) {
            return color(
                    (red(first) + red(second)) / MEAN_OF_TWO,
                    (green(first) + green(second)) / MEAN_OF_TWO,
                    (blue(first) + blue(second)) / MEAN_OF_TWO,
                    (alpha(first) + alpha(second)) / MEAN_OF_TWO
            );
        }

        public int addRgb(
                final int first,
                final int second
        ) {
            return color(
                    Math.min(red(first) + red(second), MAX_CHANNEL),
                    Math.min(green(first) + green(second), MAX_CHANNEL),
                    Math.min(blue(first) + blue(second), MAX_CHANNEL),
                    alpha(first)
            );
        }

        public int subtractRgb(
                final int first,
                final int second
        ) {
            return color(
                    Math.max(red(first) - red(second), 0),
                    Math.max(green(first) - green(second), 0),
                    Math.max(blue(first) - blue(second), 0),
                    alpha(first)
            );
        }

        public int multiplyAlpha(
                final int argb,
                final float alphaMultiplier
        ) {
            if (argb == TRANSPARENT || alphaMultiplier <= 0.0f) {
                return TRANSPARENT;
            }

            return alphaMultiplier >= 1.0f
                    ? argb
                    : withAlpha(alphaFloat(argb) * alphaMultiplier, argb);
        }

        public int scaleRGB(
                final int argb,
                final float scale
        ) {
            return scaleRGB(argb, scale, scale, scale);
        }

        public int scaleRGB(
                final int argb,
                final float scaleRed,
                final float scaleGreen,
                final float scaleBlue
        ) {
            return color(
                    Maths.clamp((int) (red(argb) * scaleRed), 0, MAX_CHANNEL),
                    Maths.clamp((int) (green(argb) * scaleGreen), 0, MAX_CHANNEL),
                    Maths.clamp((int) (blue(argb) * scaleBlue), 0, MAX_CHANNEL),
                    alpha(argb)
            );
        }

        public int scaleRGB(
                final int argb,
                final int scale
        ) {
            return color(
                    (int) Maths.clamp((long) red(argb) * scale / MAX_CHANNEL, 0L, MAX_CHANNEL),
                    (int) Maths.clamp((long) green(argb) * scale / MAX_CHANNEL, 0L, MAX_CHANNEL),
                    (int) Maths.clamp((long) blue(argb) * scale / MAX_CHANNEL, 0L, MAX_CHANNEL),
                    alpha(argb)
            );
        }

        public int greyscale(
                final int argb
        ) {
            val grey = (int) (red(argb) * GREYSCALE_RED + green(argb) * GREYSCALE_GREEN + blue(argb) * GREYSCALE_BLUE);
            return color(grey, grey, grey, alpha(argb));
        }

        public int alphaBlend(
                final int destination,
                final int source
        ) {
            val destinationAlpha = alpha(destination);
            val sourceAlpha = alpha(source);
            if (sourceAlpha == MAX_CHANNEL) {
                return source;
            }
            if (sourceAlpha == 0) {
                return destination;
            }

            val alpha = sourceAlpha + destinationAlpha * (MAX_CHANNEL - sourceAlpha) / MAX_CHANNEL;
            return color(
                    alphaBlendChannel(alpha, sourceAlpha, red(destination), red(source)),
                    alphaBlendChannel(alpha, sourceAlpha, green(destination), green(source)),
                    alphaBlendChannel(alpha, sourceAlpha, blue(destination), blue(source)),
                    alpha
            );
        }

        public int withAlpha(
                final int alpha,
                final int rgb
        ) {
            return alpha << ALPHA_SHIFT | rgb & RGB_MASK;
        }

        public int withAlpha(
                final float alpha,
                final int rgb
        ) {
            return withAlpha(as8BitChannel(alpha), rgb);
        }

        public int composeColor(
                final int rgb,
                final int alpha
        ) {
            return (rgb & RGB_MASK) | (alpha & ALPHA_MASK);
        }

        public int white(
                final int alpha
        ) {
            return withAlpha(alpha, RGB_MASK);
        }

        public int white(
                final float alpha
        ) {
            return withAlpha(alpha, RGB_MASK);
        }

        public int black(
                final int alpha
        ) {
            return alpha << ALPHA_SHIFT;
        }

        public int black(
                final float alpha
        ) {
            return as8BitChannel(alpha) << ALPHA_SHIFT;
        }

        public int gray(
                final float brightness
        ) {
            val channel = as8BitChannel(brightness);
            return color(channel, channel, channel);
        }

        public int as8BitChannel(
                final float value
        ) {
            return Maths.floor(value * MAX_CHANNEL_F);
        }

        public int setBrightness(
                final int argb,
                final float brightness
        ) {
            val hsb = RGBtoHSB(red(argb), green(argb), blue(argb), null);
            return withAlpha(alpha(argb), HSBtoRGBRounded(hsb[0], hsb[1], brightness));
        }

        private int HSBtoRGBRounded(
                final float hue,
                final float saturation,
                final float brightness
        ) {
            if (saturation == 0.0f) {
                val channel = Math.round(brightness * MAX_CHANNEL_F);
                return color(channel, channel, channel);
            }

            val segment = (hue - (float) Math.floor(hue)) * HUE_SECTORS;
            val offset = segment - (float) Math.floor(segment);
            val primary = brightness * (1.0f - saturation);
            val secondary = brightness * (1.0f - saturation * offset);
            val tertiary = brightness * (1.0f - saturation * (1.0f - offset));
            val full = Math.round(brightness * MAX_CHANNEL_F);
            val p = Math.round(primary * MAX_CHANNEL_F);
            val q = Math.round(secondary * MAX_CHANNEL_F);
            val t = Math.round(tertiary * MAX_CHANNEL_F);

            return switch ((int) segment) {
                case 0 -> color(full, t, p);
                case 1 -> color(q, full, p);
                case 2 -> color(p, full, t);
                case 3 -> color(p, q, full);
                case 4 -> color(t, p, full);
                case 5 -> color(full, p, q);
                default -> color(0, 0, 0);
            };
        }

        private int linearChannelMean(
                final int first,
                final int second,
                final int third,
                final int fourth
        ) {
            val linear = (SRGB_TO_LINEAR[first] + SRGB_TO_LINEAR[second] + SRGB_TO_LINEAR[third] + SRGB_TO_LINEAR[fourth]) / MEAN_OF_FOUR;
            return LINEAR_TO_SRGB[linear] & CHANNEL_MASK;
        }

        private int alphaBlendChannel(
                final int resultAlpha,
                final int sourceAlpha,
                final int destination,
                final int source
        ) {
            return (source * sourceAlpha + destination * (resultAlpha - sourceAlpha)) / resultAlpha;
        }

        private short[] createSrgbToLinear() {
            val lookup = new short[MAX_CHANNEL + 1];
            for (var i = 0; i < lookup.length; i++) {
                lookup[i] = (short) Math.round(computeSrgbToLinear(i / MAX_CHANNEL_F) * MAX_LINEAR_CHANNEL_F);
            }
            return lookup;
        }

        private byte[] createLinearToSrgb() {
            val lookup = new byte[LINEAR_CHANNEL_DEPTH];
            for (var i = 0; i < lookup.length; i++) {
                lookup[i] = (byte) Math.round(computeLinearToSrgb(i / MAX_LINEAR_CHANNEL_F) * MAX_CHANNEL_F);
            }
            return lookup;
        }

        private float computeSrgbToLinear(
                final float value
        ) {
            return value >= SRGB_LINEAR_THRESHOLD
                    ? (float) Math.pow((value + SRGB_OFFSET) / SRGB_SCALE, SRGB_GAMMA)
                    : value / SRGB_LINEAR_SLOPE;
        }

        private float computeLinearToSrgb(
                final float value
        ) {
            return value >= LINEAR_SRGB_THRESHOLD
                    ? (float) (SRGB_SCALE * Math.pow(value, 1.0 / SRGB_GAMMA) - SRGB_OFFSET)
                    : SRGB_LINEAR_SLOPE * value;
        }

        private int toChannel(
                final float value
        ) {
            return (int) (value * MAX_CHANNEL + ROUNDING_OFFSET);
        }

        private int toChannelF(
                final float value
        ) {
            return (int) (value * MAX_CHANNEL_F + ROUNDING_OFFSET_F);
        }
    }

    @UtilityClass
    public class ABGR32 {
        public int alpha(
                final int argb
        ) {
            return argb >>> ALPHA_SHIFT;
        }

        public int red(
                final int argb
        ) {
            return argb & CHANNEL_MASK;
        }

        public int green(
                final int argb
        ) {
            return argb >> GREEN_SHIFT & CHANNEL_MASK;
        }

        public int blue(
                final int argb
        ) {
            return argb >> RED_SHIFT & CHANNEL_MASK;
        }

        public int toArgb(
                final int abgr
        ) {
            val alpha = alpha(abgr);
            val red = red(abgr);
            val green = green(abgr);
            val blue = blue(abgr);

            return (alpha << ALPHA_SHIFT) | (red << RED_SHIFT) | (green << GREEN_SHIFT) | blue;
        }
    }

    @UtilityClass
    public class Transparency {
        //255
        public final int OPAQUE = 1;
        //0
        //BITMASK/TRANSPARENT
        public final int BITMASK = 2;
        //0-255
        public final int TRANSLUCENT = 3;
    }
}
