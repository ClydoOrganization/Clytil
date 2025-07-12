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

@UtilityClass
public class AsciiChars {

    public char toLowerCase(final char ch) {
        return (ch >= 'A' && ch <= 'Z') ? (char) (ch | 0x20) : ch;
    }

    public char toUpperCase(final char ch) {
        return (ch >= 'a' && ch <= 'z') ? (char) (ch & ~0x20) : ch;
    }

    public boolean isDigit(final char ch) {
        return ch >= '0' && ch <= '9';
    }

    public boolean isLetter(final char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
    }

    public boolean isLowerCase(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public boolean isUpperCase(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public boolean isWhitespace(final char ch) {
        // \t \n \v \f \r
        return ch == ' ' || (ch >= '\t' && ch <= '\r');
    }

    public boolean isPrintable(final char ch) {
        return ch >= 0x20 && ch < 0x7F;
    }

    public boolean equalsIgnoreCase(final char a, final char b) {
        return toLowerCase(a) == toLowerCase(b);
    }

    public int digitToInt(final char ch) {
        return isDigit(ch) ? (ch - '0') : -1;
    }

    public int hexDigitToInt(final char ch) {
        if (ch >= '0' && ch <= '9')
            return ch - '0';
        if (ch >= 'A' && ch <= 'F')
            return ch - 'A' + 10;
        if (ch >= 'a' && ch <= 'f')
            return ch - 'a' + 10;
        return -1;
    }

    public boolean isHexDigit(final char ch) {
        return hexDigitToInt(ch) != -1;
    }

}
