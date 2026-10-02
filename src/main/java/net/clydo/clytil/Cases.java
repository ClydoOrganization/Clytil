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

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

import static net.clydo.clytil.Cases.Spec.*;

/**
 * Enum representing various string naming conventions (cases).
 * <p>
 * Provides methods to check if a given string conforms to a specific case, to require that it does,
 * and to convert an arbitrary string into a specific case. All operations work in a single pass
 * over ASCII characters without regex.
 * <p>
 * Conversion splits the input into words on any non-alphanumeric character and on camel-case
 * boundaries ({@code "myXMLParser"} becomes {@code my}, {@code XML}, {@code Parser}), then joins
 * them in the target style. Digits stay attached to the word they follow, even across a separator
 * ({@code "name 2"} becomes {@code name2}).
 */
@RequiredArgsConstructor
public enum Cases {

    /**
     * Lowercase words separated by underscores, e.g., "my_variable_name".
     */
    SNAKE('_', IS_LOWER, IS_LOWER | IS_DIGIT, TO_LOWER, TO_LOWER, false),

    /**
     * Uppercase words separated by underscores, e.g., "MY_VARIABLE_NAME".
     */
    SCREAMING_SNAKE('_', IS_UPPER, IS_UPPER | IS_DIGIT, TO_UPPER, TO_UPPER, false),

    /**
     * Lowercase words separated by hyphens, e.g., "my-variable-name".
     */
    KEBAB('-', IS_LOWER, IS_LOWER | IS_DIGIT, TO_LOWER, TO_LOWER, false),

    /**
     * camelCase: first word lowercase, subsequent words capitalized, e.g., "myVariableName".
     */
    CAMEL(NO_SEPARATOR, IS_LOWER, IS_ALNUM, TO_LOWER, TO_CAPITAL, false),

    /**
     * PascalCase: all words capitalized, e.g., "MyVariableName".
     */
    PASCAL(NO_SEPARATOR, IS_UPPER, IS_ALNUM, TO_CAPITAL, TO_CAPITAL, false),

    /**
     * All lowercase letters, e.g., "variable".
     * <p>
     * Conversion drops every non-letter character.
     */
    LOWER(NO_SEPARATOR, IS_LOWER, IS_LOWER, TO_LOWER, TO_LOWER, true),

    /**
     * All uppercase letters, e.g., "CONSTANT".
     * <p>
     * Conversion drops every non-letter character.
     */
    UPPER(NO_SEPARATOR, IS_UPPER, IS_UPPER, TO_UPPER, TO_UPPER, true),

    /**
     * Alphanumeric string with letters and/or digits, e.g., "Var123".
     * <p>
     * Conversion drops every non-alphanumeric character and keeps the original letter case.
     */
    ALPHANUMERIC(NO_SEPARATOR, IS_ALNUM, IS_ALNUM, KEEP, KEEP, false);

    /**
     * The character between words, or {@link Spec#NO_SEPARATOR}.
     */
    private final char separator;

    /**
     * The character classes allowed as the first character of a word.
     */
    private final int wordStart;

    /**
     * The character classes allowed after the first character of a word.
     */
    private final int wordRest;

    /**
     * How conversion transforms the first word.
     */
    private final int firstStyle;

    /**
     * How conversion transforms every word after the first.
     */
    private final int restStyle;

    /**
     * Whether conversion drops digits.
     */
    private final boolean lettersOnly;

    /**
     * Checks if the given string matches this case type.
     *
     * @param str the string to check
     * @return true if the string matches this case type, false otherwise
     */
    @Contract(pure = true)
    public boolean matches(
            @NotNull final String str
    ) {
        Validates.require(str, "str");
        val length = str.length();
        if (length == 0) {
            return false;
        }
        // fields copied to locals so the loop does not reload them on JITs that do not hoist loads
        val sep = this.separator;
        val separated = sep != NO_SEPARATOR;
        val start = this.wordStart;
        val rest = this.wordRest;
        boolean atWordStart = true;
        for (int i = 0; i < length; i++) {
            val c = str.charAt(i);
            if (separated && c == sep) {
                if (atWordStart) {
                    return false;
                }
                atWordStart = true;
                continue;
            }
            if ((AsciiChars.classOf(c) & (atWordStart ? start : rest)) == 0) {
                return false;
            }
            atWordStart = false;
        }
        return !atWordStart;
    }

    /**
     * Requires the given string to match this case type.
     *
     * @param str  the string to check
     * @param name the name of the value, used in the exception message
     * @return the given string
     * @throws IllegalArgumentException if the string does not match this case type
     */
    @Contract("_, _ -> param1")
    public @NotNull String require(
            @NotNull final String str,
            @NotNull final String name
    ) {
        Validates.require(name, "name");
        if (!this.matches(str)) {
            throw new IllegalArgumentException(String.format("%s must be in %s case but was '%s'", name, this, str));
        }
        return str;
    }

    /**
     * Converts the given string into this case type.
     * <p>
     * Returns the given instance itself when conversion would not change it.
     * <p>
     * Conversion is best-effort: input starting with a digit (e.g., "2fast"), or with
     * no letters or digits at all, may produce a result that does not {@link #matches(String) match}.
     *
     * @param str the string to convert
     * @return the string converted to this case type
     */
    @Contract(pure = true)
    public @NotNull String convert(
            @NotNull final String str
    ) {
        Validates.require(str, "str");
        val length = str.length();
        if (length == 0) {
            return str;
        }
        // fields copied to locals so the loops do not reload them on JITs that do not hoist loads
        val sep = this.separator;
        val separated = sep != NO_SEPARATOR;
        val dropDigits = this.lettersOnly;
        // output is pure ASCII, so a byte[] halves the buffer and becomes a compact string with a plain copy;
        // every separator follows at least one word character, so length * 1.5 always fits
        val out = new byte[separated ? length + (length >> 1) + 1 : length];
        int size = 0;
        // whether out[0, size) equals str[0, size); with size == length at the end, nothing changed
        boolean same = true;
        int i = 0;
        boolean first = true;
        while (i < length) {
            int prev = AsciiChars.classOf(str.charAt(i));
            if (prev == 0) {
                i++;
                continue;
            }
            val start = i++;
            val startsWithDigit = prev == IS_DIGIT;
            while (i < length) {
                val cls = AsciiChars.classOf(str.charAt(i));
                if (cls == 0 || (cls == IS_UPPER && (prev != IS_UPPER
                        || (i + 1 < length && AsciiChars.classOf(str.charAt(i + 1)) == IS_LOWER)))) {
                    break;
                }
                prev = cls;
                i++;
            }
            // a word starting with a digit joins the previous word, since no case lets a word start with one
            if (separated && !first && !startsWithDigit) {
                same &= size < length && str.charAt(size) == sep;
                out[size++] = (byte) sep;
            }
            val style = first ? this.firstStyle : this.restStyle;
            for (int k = start; k < i; k++) {
                char c = str.charAt(k);
                if (AsciiChars.isDigit(c)) {
                    if (dropDigits) {
                        continue;
                    }
                } else if (style == TO_LOWER || (style == TO_CAPITAL && k != start)) {
                    c = AsciiChars.toLowerCase(c);
                } else if (style != KEEP) {
                    c = AsciiChars.toUpperCase(c);
                }
                same &= size < length && str.charAt(size) == c;
                out[size++] = (byte) c;
            }
            first = false;
        }
        if (same && size == length) {
            return str;
        }
        return new String(out, 0, size, StandardCharsets.ISO_8859_1);
    }

    /**
     * Constants used to describe each case. Kept outside the enum so its constants can reference them.
     * <p>
     * Not a {@code @UtilityClass}, since javac cannot static-import members of one, which is also why
     * the {@link AsciiChars} classes are aliased here.
     */
    static final class Spec {
        static final char NO_SEPARATOR = '\0';

        static final int IS_LOWER = AsciiChars.CLASS_LOWER;
        static final int IS_UPPER = AsciiChars.CLASS_UPPER;
        static final int IS_DIGIT = AsciiChars.CLASS_DIGIT;
        static final int IS_ALNUM = AsciiChars.CLASS_ALPHANUMERIC;

        static final int KEEP = 0;
        static final int TO_LOWER = 1;
        static final int TO_UPPER = 2;
        static final int TO_CAPITAL = 3;

        private Spec() {
        }
    }
}
