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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Fast, locale-independent checks and conversions for ASCII characters.
 * <p>
 * Every method treats characters outside the ASCII range as neither letters nor digits.
 */
@UtilityClass
public class AsciiChars {

    /**
     * The number of ASCII characters.
     */
    public final int COUNT = 128;

    /**
     * The {@link #classOf(char) class} bit of {@code a} to {@code z}.
     */
    public final int CLASS_LOWER = 1;

    /**
     * The {@link #classOf(char) class} bit of {@code A} to {@code Z}.
     */
    public final int CLASS_UPPER = 2;

    /**
     * The {@link #classOf(char) class} bit of {@code 0} to {@code 9}.
     */
    public final int CLASS_DIGIT = 4;

    /**
     * Every {@link #classOf(char) class} bit of a letter.
     */
    public final int CLASS_LETTER = CLASS_LOWER | CLASS_UPPER;

    /**
     * Every {@link #classOf(char) class} bit of a letter or digit.
     */
    public final int CLASS_ALPHANUMERIC = CLASS_LETTER | CLASS_DIGIT;

    /**
     * The class of each ASCII character, so {@link #classOf(char)} costs one bounded array load.
     */
    private final byte[] CLASSES = classes();

    /**
     * Returns the class of the given character: {@link #CLASS_LOWER}, {@link #CLASS_UPPER},
     * {@link #CLASS_DIGIT}, or {@code 0} if it is not an ASCII letter or digit.
     * <p>
     * The classes are distinct bits, so a set of them can be tested at once,
     * e.g., {@code (classOf(ch) & CLASS_ALPHANUMERIC) != 0}.
     *
     * @param ch the character to classify
     * @return the class of the character, or {@code 0}
     */
    @Contract(pure = true)
    public int classOf(
            final char ch
    ) {
        return ch < COUNT ? CLASSES[ch] : 0;
    }

    /**
     * Converts the given character to lowercase if it is an ASCII uppercase letter.
     *
     * @param ch the character to convert
     * @return the lowercase character, or the given character if it is not {@code A} to {@code Z}
     */
    @Contract(pure = true)
    public char toLowerCase(
            final char ch
    ) {
        return isUpperCase(ch) ? (char) (ch | 0x20) : ch;
    }

    /**
     * Converts the given character to uppercase if it is an ASCII lowercase letter.
     *
     * @param ch the character to convert
     * @return the uppercase character, or the given character if it is not {@code a} to {@code z}
     */
    @Contract(pure = true)
    public char toUpperCase(
            final char ch
    ) {
        return isLowerCase(ch) ? (char) (ch & ~0x20) : ch;
    }

    /**
     * Checks if the given character is an ASCII digit.
     *
     * @param ch the character to check
     * @return true if the character is {@code 0} to {@code 9}
     */
    @Contract(pure = true)
    public boolean isDigit(
            final char ch
    ) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * Checks if the given character is an ASCII letter.
     *
     * @param ch the character to check
     * @return true if the character is {@code A} to {@code Z} or {@code a} to {@code z}
     */
    @Contract(pure = true)
    public boolean isLetter(
            final char ch
    ) {
        return isUpperCase(ch) || isLowerCase(ch);
    }

    /**
     * Checks if the given character is an ASCII letter or digit.
     *
     * @param ch the character to check
     * @return true if the character is {@code A} to {@code Z}, {@code a} to {@code z} or {@code 0} to {@code 9}
     */
    @Contract(pure = true)
    public boolean isAlphanumeric(
            final char ch
    ) {
        return classOf(ch) != 0;
    }

    /**
     * Checks if the given character is an ASCII lowercase letter.
     *
     * @param ch the character to check
     * @return true if the character is {@code a} to {@code z}
     */
    @Contract(pure = true)
    public boolean isLowerCase(
            final char ch
    ) {
        return ch >= 'a' && ch <= 'z';
    }

    /**
     * Checks if the given character is an ASCII uppercase letter.
     *
     * @param ch the character to check
     * @return true if the character is {@code A} to {@code Z}
     */
    @Contract(pure = true)
    public boolean isUpperCase(
            final char ch
    ) {
        return ch >= 'A' && ch <= 'Z';
    }

    /**
     * Checks if the given character is ASCII whitespace: space, tab, line feed,
     * vertical tab, form feed or carriage return.
     *
     * @param ch the character to check
     * @return true if the character is ASCII whitespace
     */
    @Contract(pure = true)
    public boolean isWhitespace(
            final char ch
    ) {
        // \t \n \v \f \r
        return ch == ' ' || (ch >= '\t' && ch <= '\r');
    }

    /**
     * Checks if the given character is a printable ASCII character, from space to {@code ~}.
     *
     * @param ch the character to check
     * @return true if the character is printable ASCII
     */
    @Contract(pure = true)
    public boolean isPrintable(
            final char ch
    ) {
        return ch >= 0x20 && ch < 0x7F;
    }

    /**
     * Checks if the given characters are equal, ignoring the case of ASCII letters.
     *
     * @param a the first character
     * @param b the second character
     * @return true if the characters are equal ignoring ASCII case
     */
    @Contract(pure = true)
    public boolean equalsIgnoreCase(
            final char a,
            final char b
    ) {
        return toLowerCase(a) == toLowerCase(b);
    }

    /**
     * Returns the numeric value of the given ASCII digit.
     *
     * @param ch the character to convert
     * @return the value {@code 0} to {@code 9}, or {@code -1} if the character is not an ASCII digit
     */
    @Contract(pure = true)
    public int digitToInt(
            final char ch
    ) {
        return isDigit(ch) ? (ch - '0') : -1;
    }

    /**
     * Checks if the given character is an uppercase ASCII hexadecimal letter.
     *
     * @param ch the character to check
     * @return true if the character is {@code A} to {@code F}
     */
    @Contract(pure = true)
    public boolean isUpperHexLetter(
            final char ch
    ) {
        return ch >= 'A' && ch <= 'F';
    }

    /**
     * Checks if the given character is a lowercase ASCII hexadecimal letter.
     *
     * @param ch the character to check
     * @return true if the character is {@code a} to {@code f}
     */
    @Contract(pure = true)
    public boolean isLowerHexLetter(
            final char ch
    ) {
        return ch >= 'a' && ch <= 'f';
    }

    /**
     * Returns the numeric value of the given ASCII hexadecimal digit, in either case.
     *
     * @param ch the character to convert
     * @return the value {@code 0} to {@code 15}, or {@code -1} if the character is not a hexadecimal digit
     */
    @Contract(pure = true)
    public int hexDigitToInt(
            final char ch
    ) {
        if (isDigit(ch))
            return ch - '0';
        if (isUpperHexLetter(ch))
            return ch - 'A' + 10;
        if (isLowerHexLetter(ch))
            return ch - 'a' + 10;
        return -1;
    }

    /**
     * Checks if the given character is an ASCII hexadecimal digit, in either case.
     *
     * @param ch the character to check
     * @return true if the character is {@code 0} to {@code 9}, {@code A} to {@code F} or {@code a} to {@code f}
     */
    @Contract(pure = true)
    public boolean isHexDigit(
            final char ch
    ) {
        return hexDigitToInt(ch) != -1;
    }

    /**
     * Builds the {@link #CLASSES} table.
     *
     * @return the class of each ASCII character
     */
    private byte @NotNull [] classes() {
        val classes = new byte[COUNT];
        for (char ch = 0; ch < COUNT; ch++) {
            if (isLowerCase(ch)) {
                classes[ch] = CLASS_LOWER;
            } else if (isUpperCase(ch)) {
                classes[ch] = CLASS_UPPER;
            } else if (isDigit(ch)) {
                classes[ch] = CLASS_DIGIT;
            }
        }
        return classes;
    }

}
