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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HexFormat;
import java.util.regex.Pattern;

/**
 * Hex string helpers: validation, formatting and parsing of
 * {@code #RRGGBB} / {@code #RRGGBBAA} (ARGB int) values.
 *
 * <p>Color math itself lives in {@link Colors}.</p>
 */
@UtilityClass
public class Hexes {

    private final Pattern RGB_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6})$");
    private final Pattern RGBA_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{8})$");

    private final HexFormat HEX = HexFormat.of().withUpperCase();
    private final HexFormat HEX_LOWER = HexFormat.of();

    private final int RGB_HEX_DIGITS = 6;
    private final int ARGB_HEX_DIGITS = 8;

    private final int ALPHA_SHIFT = 24;
    private final int CHANNEL_MASK = 0xFF;
    private final int ALPHA_MASK = CHANNEL_MASK << ALPHA_SHIFT;

    private final int HEX_RADIX = 16;
    private final int OPAQUE = 0xFF000000;

    /**
     * Checks whether a string is a {@code #RRGGBB} color.
     *
     * @param str the string to check
     * @return {@code true} if the string matches {@code #RRGGBB}
     */
    public boolean isValidRGBHexColor(
            @NotNull final String str
    ) {
        return RGB_PATTERN.matcher(str).matches();
    }

    /**
     * Checks whether a string is a {@code #RRGGBBAA} color.
     *
     * @param str the string to check
     * @return {@code true} if the string matches {@code #RRGGBBAA}
     */
    public boolean isValidRGBAHexColor(
            @NotNull final String str
    ) {
        return RGBA_PATTERN.matcher(str).matches();
    }

    /**
     * Checks whether a string is a {@code #RRGGBB} or {@code #RRGGBBAA} color.
     *
     * @param str the string to check
     * @return {@code true} if the string is a supported hex color
     */
    public boolean isValidHexString(
            @NotNull final String str
    ) {
        return isValidRGBHexColor(str) || isValidRGBAHexColor(str);
    }

    /**
     * Formats an ARGB int as upper-case hex digits.
     *
     * @param color  the ARGB color
     * @param opaque whether the alpha channel should be forced opaque
     * @return hex digits without a prefix
     */
    public String toHexString(
            final int color,
            final boolean opaque
    ) {
        return toHexString("", color, opaque);
    }

    /**
     * Formats an ARGB int as upper-case hex digits with a prefix.
     * Leading zero digits of the alpha channel are trimmed unless
     * {@code opaque} is {@code true}.
     *
     * @param prefix prefix to prepend, e.g. {@code "#"}
     * @param color  the ARGB color
     * @param opaque whether the alpha channel should be forced opaque
     * @return prefixed hex digits
     */
    public String toHexString(
            @NotNull final String prefix,
            final int color,
            final boolean opaque
    ) {
        if (opaque) {
            return prefix + HEX.toHexDigits(color | ALPHA_MASK);
        }

        val digits = HEX.toHexDigits(color);
        var start = 0;
        while (start < ARGB_HEX_DIGITS - RGB_HEX_DIGITS && digits.charAt(start) == '0') {
            start++;
        }
        return prefix + digits.substring(start);
    }

    /**
     * Formats an ARGB int as lower-case hex digits with a prefix.
     *
     * @param prefix prefix to prepend, e.g. {@code "#"}
     * @param color  the ARGB color
     * @param opaque whether the alpha channel should be forced opaque
     * @return prefixed lower-case hex digits
     */
    public String toLowerCaseHexString(
            @NotNull final String prefix,
            final int color,
            final boolean opaque
    ) {
        return prefix + HEX_LOWER.toHexDigits(color | (opaque ? ALPHA_MASK : 0));
    }

    /**
     * Parses hex digits into an int, returning a default on failure.
     *
     * @param hexString     the hex digits, may be {@code null}
     * @param defaultValue  value returned when parsing fails
     * @return the parsed value, or {@code defaultValue}
     */
    public int parseHexString(
            @Nullable final String hexString,
            final int defaultValue
    ) {
        if (hexString == null || hexString.isEmpty()) {
            return defaultValue;
        }

        try {
            return HexFormat.fromHexDigits(hexString);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * Parses hex digits behind a prefix into an int, returning a default on failure.
     *
     * @param prefix        the expected prefix, e.g. {@code "#"}
     * @param hexString     the full string, may be {@code null}
     * @param defaultValue  value returned when parsing fails
     * @return the parsed value, or {@code defaultValue}
     */
    public int parseHexString(
            @NotNull final String prefix,
            @Nullable final String hexString,
            final int defaultValue
    ) {
        if (hexString == null || !hexString.startsWith(prefix) || hexString.length() == prefix.length()) {
            return defaultValue;
        }

        try {
            return HexFormat.fromHexDigits(hexString, prefix.length(), hexString.length());
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * Parses a range of hex digits from a CharSequence into an ARGB int.
     * A leading {@code #} is not accepted; exactly 6 or 8 digits are required.
     * 6 digits are treated as fully opaque.
     *
     * @param text         the text to read from
     * @param start        index of the first digit (inclusive)
     * @param end          index past the last digit (exclusive)
     * @param defaultValue value returned when the range is not valid hex
     * @return the parsed ARGB value, or {@code defaultValue}
     */
    public int parseHexString(
            @NotNull final CharSequence text,
            final int start,
            final int end,
            final int defaultValue
    ) {
        val digits = end - start;
        if (digits != RGB_HEX_DIGITS && digits != ARGB_HEX_DIGITS) {
            return defaultValue;
        }
        if (start < 0 || end > text.length() || start > end) {
            return defaultValue;
        }

        var value = 0;
        for (int i = start; i < end; i++) {
            val digit = Character.digit(text.charAt(i), HEX_RADIX);
            if (digit < 0) {
                return defaultValue;
            }
            value = value << 4 | digit;
        }
        return ((digits == RGB_HEX_DIGITS ? OPAQUE : 0) | value);
    }

}
