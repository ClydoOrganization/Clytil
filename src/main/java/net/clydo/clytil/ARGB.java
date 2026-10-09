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

@UtilityClass
public class ARGB {

    public final int MAX_CHANNEL = 255;
    public final float MAX_CHANNEL_F = 255.0f;

    public final int ALPHA_MASK = 0xFF000000;
    public final int RED_MASK = 0x00FF0000;
    public final int GREEN_MASK = 0x0000FF00;
    public final int BLUE_MASK = 0x000000FF;
    public final int RGB_MASK = 0x00FFFFFF;

    public final int CHANNEL_MASK = 0xFF;
    public final int ALPHA_SHIFT = 24;
    public final int RED_SHIFT = 16;
    public final int GREEN_SHIFT = 8;
    public final int ALPHA_GREEN_MASK = ALPHA_MASK | GREEN_MASK;
    public final int RED_BLUE_MASK = RED_MASK | BLUE_MASK;

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
        return toFloat(alpha(argb));
    }

    public float redFloat(
            final int argb
    ) {
        return toFloat(red(argb));
    }

    public float greenFloat(
            final int argb
    ) {
        return toFloat(green(argb));
    }

    public float blueFloat(
            final int argb
    ) {
        return toFloat(blue(argb));
    }

    public int toByte(
            final float value
    ) {
        return Maths.round(value * MAX_CHANNEL_F);
    }

    public float toFloat(
            final int value
    ) {
        return value / MAX_CHANNEL_F;
    }

    public int color(
            final int red,
            final int green,
            final int blue,
            final int alpha
    ) {
        return alpha << ALPHA_SHIFT
                | (red & CHANNEL_MASK) << RED_SHIFT
                | (green & CHANNEL_MASK) << GREEN_SHIFT
                | blue & CHANNEL_MASK;
    }

    public int color(
            final int red,
            final int green,
            final int blue
    ) {
        return color(red, green, blue, MAX_CHANNEL);
    }

    public int color(
            final float red,
            final float green,
            final float blue,
            final float alpha
    ) {
        return color(toByte(red), toByte(green), toByte(blue), toByte(alpha));
    }

    public int color(
            final float red,
            final float green,
            final float blue
    ) {
        return color(toByte(red), toByte(green), toByte(blue));
    }

    public int gray(
            final int channel,
            final int alpha
    ) {
        return color(channel, channel, channel, alpha);
    }

    public int withAlpha(
            final int argb,
            final int alpha
    ) {
        return argb & RGB_MASK | alpha << ALPHA_SHIFT;
    }

    public int withRed(
            final int argb,
            final int red
    ) {
        return argb & ~RED_MASK | (red & CHANNEL_MASK) << RED_SHIFT;
    }

    public int withGreen(
            final int argb,
            final int green
    ) {
        return argb & ~GREEN_MASK | (green & CHANNEL_MASK) << GREEN_SHIFT;
    }

    public int withBlue(
            final int argb,
            final int blue
    ) {
        return argb & ~BLUE_MASK | blue & CHANNEL_MASK;
    }

    public int withAlpha(
            final int argb,
            final float alpha
    ) {
        return withAlpha(argb, toByte(alpha));
    }

    public int withRed(
            final int argb,
            final float red
    ) {
        return withRed(argb, toByte(red));
    }

    public int withGreen(
            final int argb,
            final float green
    ) {
        return withGreen(argb, toByte(green));
    }

    public int withBlue(
            final int argb,
            final float blue
    ) {
        return withBlue(argb, toByte(blue));
    }

    public int withAlphaFrom(
            final int rgb,
            final int alphaSource
    ) {
        return withAlpha(rgb, alpha(alphaSource));
    }

    public int opaque(
            final int argb
    ) {
        return argb | ALPHA_MASK;
    }

    public int transparent(
            final int argb
    ) {
        return argb & RGB_MASK;
    }

    public int multiplyAlpha(
            final int argb,
            final float multiplier
    ) {
        if (multiplier <= 0.0f || argb == Palette.TRANSPARENT) {
            return Palette.TRANSPARENT;
        }
        if (multiplier >= 1.0f) {
            return argb;
        }
        return withAlpha(argb, Maths.round(alpha(argb) * multiplier));
    }

    public int toAbgr(
            final int argb
    ) {
        return argb & ALPHA_GREEN_MASK | red(argb) | blue(argb) << RED_SHIFT;
    }

    @UtilityClass
    public class Palette {

        public final int TRANSPARENT = 0x00000000;
        public final int BLACK = 0xFF000000;
        public final int WHITE = 0xFFFFFFFF;
        public final int DARK_GRAY = 0xFF404040;
        public final int GRAY = 0xFF808080;
        public final int LIGHT_GRAY = 0xFFC0C0C0;
        public final int RED = 0xFFFF0000;
        public final int GREEN = 0xFF00FF00;
        public final int BLUE = 0xFF0000FF;
        public final int YELLOW = 0xFFFFFF00;
        public final int ORANGE = 0xFFFFC800;
        public final int PHOENIX = 0xFFFF5A00;

        public int white(
                final int alpha
        ) {
            return withAlpha(WHITE, alpha);
        }

        public int white(
                final float alpha
        ) {
            return withAlpha(WHITE, alpha);
        }

        public int black(
                final int alpha
        ) {
            return withAlpha(BLACK, alpha);
        }

        public int black(
                final float alpha
        ) {
            return withAlpha(BLACK, alpha);
        }

        public int gray(
                final float brightness
        ) {
            return ARGB.gray(toByte(brightness), MAX_CHANNEL);
        }

    }

    @UtilityClass
    public class Blending {

        // Fixed-point weight of the end color in lerp: 256 is all of it
        private final int LERP_ONE = 256;
        private final int LERP_SHIFT = 8;
        private final int LERP_ROUND = 0x00800080;

        /**
         * Blends two colors channel by channel, unpremultiplied, as a CSS color transition does.
         *
         * @param start the color at {@code 0}
         * @param end   the color at {@code 1}
         * @param delta how far from {@code start} to {@code end}; clamped to {@code [0, 1]}, so curves
         *              that overshoot never wrap a channel
         * @return the blend, rounded to the nearest channel values
         * @see #lerp(int, int, double)
         */
        public int lerp(
                final int start,
                final int end,
                final float delta
        ) {
            return lerp(start, end, (double) delta);
        }

        /**
         * Blends two colors channel by channel, unpremultiplied, as a CSS color transition does.
         *
         * <p>The blend runs in fixed point, two channels per multiply, with {@code delta} in steps of
         * {@code 1/256}: allocation-free and cheap enough to call per vertex or per frame.</p>
         *
         * @param start the color at {@code 0}
         * @param end   the color at {@code 1}
         * @param delta how far from {@code start} to {@code end}; clamped to {@code [0, 1]}, so curves
         *              that overshoot never wrap a channel
         * @return the blend, rounded to the nearest channel values
         */
        public int lerp(
                final int start,
                final int end,
                final double delta
        ) {
            if (start == end || !(delta > 0)) {
                return start;
            }
            if (delta >= 1) {
                return end;
            }
            val weight = (int) (delta * LERP_ONE + 0.5);
            val keep = LERP_ONE - weight;
            // Each 16-bit lane holds one channel times its weight, at most 255 * 256, so lanes never carry
            val redBlue = ((start & RED_BLUE_MASK) * keep + (end & RED_BLUE_MASK) * weight + LERP_ROUND) >>> LERP_SHIFT;
            val alphaGreen = (start >>> LERP_SHIFT & RED_BLUE_MASK) * keep + (end >>> LERP_SHIFT & RED_BLUE_MASK) * weight + LERP_ROUND;
            return alphaGreen & ALPHA_GREEN_MASK | redBlue & RED_BLUE_MASK;
        }

        public int multiply(
                final int first,
                final int second
        ) {
            if (first == Palette.WHITE) {
                return second;
            }
            if (second == Palette.WHITE) {
                return first;
            }
            return color(
                    multiplyChannel(red(first), red(second)),
                    multiplyChannel(green(first), green(second)),
                    multiplyChannel(blue(first), blue(second)),
                    multiplyChannel(alpha(first), alpha(second))
            );
        }

        public int average(
                final int first,
                final int second
        ) {
            return (first & second) + ((first ^ second) >>> 1 & 0x7F7F7F7F);
        }

        public int addRgb(
                final int first,
                final int second
        ) {
            return color(
                    Maths.min(red(first) + red(second), MAX_CHANNEL),
                    Maths.min(green(first) + green(second), MAX_CHANNEL),
                    Maths.min(blue(first) + blue(second), MAX_CHANNEL),
                    alpha(first)
            );
        }

        public int subtractRgb(
                final int first,
                final int second
        ) {
            return color(
                    Maths.max(red(first) - red(second), 0),
                    Maths.max(green(first) - green(second), 0),
                    Maths.max(blue(first) - blue(second), 0),
                    alpha(first)
            );
        }

        public int sourceOver(
                final int destination,
                final int source
        ) {
            val sourceAlpha = alpha(source);
            if (sourceAlpha == MAX_CHANNEL) {
                return source;
            }
            if (sourceAlpha == 0) {
                return destination;
            }

            val alpha = sourceAlpha + multiplyChannel(alpha(destination), MAX_CHANNEL - sourceAlpha);
            val destinationWeight = alpha - sourceAlpha;
            return color(
                    (red(source) * sourceAlpha + red(destination) * destinationWeight) / alpha,
                    (green(source) * sourceAlpha + green(destination) * destinationWeight) / alpha,
                    (blue(source) * sourceAlpha + blue(destination) * destinationWeight) / alpha,
                    alpha
            );
        }

        private int multiplyChannel(
                final int first,
                final int second
        ) {
            return first * second / MAX_CHANNEL;
        }

    }

    @UtilityClass
    public class Adjustments {

        public final float DEFAULT_SHADE_FACTOR = 0.7f;
        public final float LUMA_RED = 0.299f;
        public final float LUMA_GREEN = 0.587f;
        public final float LUMA_BLUE = 0.114f;
        public final float LIGHT_LUMA_THRESHOLD = 128.0f;
        // RGB channels with the low bits cleared so shifting right cannot bleed into the next channel
        public final int HALVE_RGB_MASK = 0x00FEFEFE;
        public final int QUARTER_RGB_MASK = 0x00FCFCFC;
        public final int EIGHTH_RGB_MASK = 0x00F8F8F8;

        public int darker(
                final int argb
        ) {
            return darker(argb, DEFAULT_SHADE_FACTOR);
        }

        public int darker(
                final int argb,
                final float factor
        ) {
            return scaleRgb(argb, factor);
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
            val minimum = (int) (1.0 / (1.0 - factor));
            if (transparent(argb) == 0) {
                return gray(minimum, alpha(argb));
            }
            return color(
                    brighterChannel(red(argb), minimum, factor),
                    brighterChannel(green(argb), minimum, factor),
                    brighterChannel(blue(argb), minimum, factor),
                    alpha(argb)
            );
        }

        public int invert(
                final int argb
        ) {
            return argb ^ RGB_MASK;
        }

        public int grayscale(
                final int argb
        ) {
            return gray(Maths.round(luma(argb)), alpha(argb));
        }

        public int scaleRgb(
                final int argb,
                final float scale
        ) {
            return scaleRgb(argb, scale, scale, scale);
        }

        public int scaleRgb(
                final int argb,
                final float scaleRed,
                final float scaleGreen,
                final float scaleBlue
        ) {
            return color(
                    scaleChannel(red(argb), scaleRed),
                    scaleChannel(green(argb), scaleGreen),
                    scaleChannel(blue(argb), scaleBlue),
                    alpha(argb)
            );
        }

        public int scaleRgbFixed(
                final int argb,
                final int scale
        ) {
            return color(
                    scaleChannelFixed(red(argb), scale),
                    scaleChannelFixed(green(argb), scale),
                    scaleChannelFixed(blue(argb), scale),
                    alpha(argb)
            );
        }

        public int halveRgb(
                final int argb
        ) {
            return (argb & HALVE_RGB_MASK) >> 1 | argb & ALPHA_MASK;
        }

        public int quarterRgb(
                final int argb
        ) {
            return (argb & QUARTER_RGB_MASK) >> 2 | argb & ALPHA_MASK;
        }

        public int eighthRgb(
                final int argb
        ) {
            return (argb & EIGHTH_RGB_MASK) >> 3 | argb & ALPHA_MASK;
        }

        public float luma(
                final int argb
        ) {
            return LUMA_RED * red(argb) + LUMA_GREEN * green(argb) + LUMA_BLUE * blue(argb);
        }

        public boolean isLight(
                final int argb
        ) {
            return luma(argb) >= LIGHT_LUMA_THRESHOLD;
        }

        private int brighterChannel(
                final int channel,
                final int minimum,
                final float factor
        ) {
            val raised = channel > 0 && channel < minimum ? minimum : channel;
            return Maths.min((int) (raised / factor), MAX_CHANNEL);
        }

        private int scaleChannel(
                final int channel,
                final float scale
        ) {
            return Maths.clamp((int) (channel * scale), 0, MAX_CHANNEL);
        }

        private int scaleChannelFixed(
                final int channel,
                final int scale
        ) {
            return (int) Maths.clamp((long) channel * scale / MAX_CHANNEL, 0L, MAX_CHANNEL);
        }

    }

    @UtilityClass
    public class Hsb {

        public final float SECTORS = 6.0f;
        public final float GREEN_SECTOR = 2.0f;
        public final float BLUE_SECTOR = 4.0f;
        public final float KEEP = -1.0f;
        public final int COMPONENTS = 3;

        public int toArgb(
                final float hue,
                final float saturation,
                final float brightness
        ) {
            return toArgb(hue, saturation, brightness, MAX_CHANNEL);
        }

        public int toArgb(
                final float hue,
                final float saturation,
                final float brightness,
                final int alpha
        ) {
            val full = toByte(brightness);
            if (saturation == 0.0f) {
                return gray(full, alpha);
            }

            val sector = (hue - Maths.floor(hue)) * SECTORS;
            val sectorIndex = (int) sector;
            val offset = sector - sectorIndex;
            val p = toByte(brightness * (1.0f - saturation));
            val q = toByte(brightness * (1.0f - saturation * offset));
            val t = toByte(brightness * (1.0f - saturation * (1.0f - offset)));
            return switch (sectorIndex) {
                case 1 -> color(q, full, p, alpha);
                case 2 -> color(p, full, t, alpha);
                case 3 -> color(p, q, full, alpha);
                case 4 -> color(t, p, full, alpha);
                case 5 -> color(full, p, q, alpha);
                default -> color(full, t, p, alpha);
            };
        }

        public float[] fromArgb(
                final int argb,
                final float @Nullable [] hsb
        ) {
            return fromRgb(red(argb), green(argb), blue(argb), hsb);
        }

        public float[] fromRgb(
                final int red,
                final int green,
                final int blue,
                final float @Nullable [] hsb
        ) {
            val result = hsb == null ? new float[COMPONENTS] : hsb;
            val max = Maths.max(red, green, blue);
            val min = Maths.min(red, green, blue);
            result[0] = hue(red, green, blue, max, min);
            result[1] = saturation(max, min);
            result[2] = toFloat(max);
            return result;
        }

        public float hue(
                final int argb
        ) {
            val red = red(argb);
            val green = green(argb);
            val blue = blue(argb);
            return hue(red, green, blue, Maths.max(red, green, blue), Maths.min(red, green, blue));
        }

        public float saturation(
                final int argb
        ) {
            val red = red(argb);
            val green = green(argb);
            val blue = blue(argb);
            return saturation(Maths.max(red, green, blue), Maths.min(red, green, blue));
        }

        public float brightness(
                final int argb
        ) {
            return toFloat(Maths.max(red(argb), green(argb), blue(argb)));
        }

        public int withHue(
                final int argb,
                final float hue
        ) {
            return adjust(argb, hue, KEEP, KEEP);
        }

        public int withSaturation(
                final int argb,
                final float saturation
        ) {
            return adjust(argb, KEEP, saturation, KEEP);
        }

        public int withBrightness(
                final int argb,
                final float brightness
        ) {
            return adjust(argb, KEEP, KEEP, brightness);
        }

        public int adjust(
                final int argb,
                final float hue,
                final float saturation,
                final float brightness
        ) {
            val red = red(argb);
            val green = green(argb);
            val blue = blue(argb);
            val max = Maths.max(red, green, blue);
            val min = Maths.min(red, green, blue);
            return toArgb(
                    hue >= 0.0f ? hue : hue(red, green, blue, max, min),
                    saturation >= 0.0f ? saturation : saturation(max, min),
                    brightness >= 0.0f ? brightness : toFloat(max),
                    alpha(argb)
            );
        }

        private float saturation(
                final int max,
                final int min
        ) {
            return max == 0 ? 0.0f : (max - min) / (float) max;
        }

        private float hue(
                final int red,
                final int green,
                final int blue,
                final int max,
                final int min
        ) {
            if (max == min) {
                return 0.0f;
            }

            val range = (float) (max - min);
            val redDistance = (max - red) / range;
            val greenDistance = (max - green) / range;
            val blueDistance = (max - blue) / range;
            final float sector;
            if (red == max) {
                sector = blueDistance - greenDistance;
            } else if (green == max) {
                sector = GREEN_SECTOR + redDistance - blueDistance;
            } else {
                sector = BLUE_SECTOR + greenDistance - redDistance;
            }

            val hue = sector / SECTORS;
            return hue < 0.0f ? hue + 1.0f : hue;
        }

    }

    @UtilityClass
    public class LinearRgb {

        public final int LINEAR_DEPTH = 1024;
        public final int MAX_LINEAR = LINEAR_DEPTH - 1;
        public final float MAX_LINEAR_F = MAX_LINEAR;
        public final float DECODE_THRESHOLD = 0.04045f;
        public final float ENCODE_THRESHOLD = 0.0031308f;
        public final float LINEAR_SLOPE = 12.92f;
        public final double OFFSET = 0.055;
        public final double SCALE = 1.055;
        public final double GAMMA = 2.4;

        private final short[] SRGB_TO_LINEAR = createSrgbToLinear();
        private final byte[] LINEAR_TO_SRGB = createLinearToSrgb();

        public float srgbToLinear(
                final int srgb
        ) {
            return SRGB_TO_LINEAR[srgb & CHANNEL_MASK] / MAX_LINEAR_F;
        }

        public int linearToSrgb(
                final float linear
        ) {
            return encodeLookup(Maths.floor(linear * MAX_LINEAR_F));
        }

        public int lerp(
                final int start,
                final int end,
                final float delta
        ) {
            return color(
                    lerpChannel(red(start), red(end), delta),
                    lerpChannel(green(start), green(end), delta),
                    lerpChannel(blue(start), blue(end), delta),
                    Maths.lerp(alpha(start), alpha(end), delta)
            );
        }

        public int average(
                final int first,
                final int second,
                final int third,
                final int fourth
        ) {
            return color(
                    averageChannel(red(first), red(second), red(third), red(fourth)),
                    averageChannel(green(first), green(second), green(third), green(fourth)),
                    averageChannel(blue(first), blue(second), blue(third), blue(fourth)),
                    (alpha(first) + alpha(second) + alpha(third) + alpha(fourth)) >> 2
            );
        }

        private int lerpChannel(
                final int start,
                final int end,
                final float delta
        ) {
            return encodeLookup(Maths.lerp(SRGB_TO_LINEAR[start], SRGB_TO_LINEAR[end], delta));
        }

        private int averageChannel(
                final int first,
                final int second,
                final int third,
                final int fourth
        ) {
            return encodeLookup((SRGB_TO_LINEAR[first] + SRGB_TO_LINEAR[second] + SRGB_TO_LINEAR[third] + SRGB_TO_LINEAR[fourth]) >> 2);
        }

        private int encodeLookup(
                final int linear
        ) {
            return LINEAR_TO_SRGB[Maths.clamp(linear, 0, MAX_LINEAR)] & CHANNEL_MASK;
        }

        private short[] createSrgbToLinear() {
            val lookup = new short[MAX_CHANNEL + 1];
            for (var i = 0; i < lookup.length; i++) {
                lookup[i] = (short) Math.round(decode(toFloat(i)) * MAX_LINEAR_F);
            }
            return lookup;
        }

        private byte[] createLinearToSrgb() {
            val lookup = new byte[LINEAR_DEPTH];
            for (var i = 0; i < lookup.length; i++) {
                lookup[i] = (byte) Math.round(encode(i / MAX_LINEAR_F) * MAX_CHANNEL_F);
            }
            return lookup;
        }

        private float decode(
                final float value
        ) {
            return value >= DECODE_THRESHOLD
                    ? (float) Math.pow((value + OFFSET) / SCALE, GAMMA)
                    : value / LINEAR_SLOPE;
        }

        private float encode(
                final float value
        ) {
            return value >= ENCODE_THRESHOLD
                    ? (float) (SCALE * Math.pow(value, 1.0 / GAMMA) - OFFSET)
                    : LINEAR_SLOPE * value;
        }

    }

    @UtilityClass
    public class Transparency {

        public final int OPAQUE = 1;
        public final int BITMASK = 2;
        public final int TRANSLUCENT = 3;

        public int of(
                final int argb
        ) {
            val alpha = alpha(argb);
            if (alpha == MAX_CHANNEL) {
                return OPAQUE;
            }
            return alpha == 0 ? BITMASK : TRANSLUCENT;
        }

    }

}
