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
import net.clydo.clytil.iface.CharPredicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.Normalizer;
import java.util.HexFormat;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@UtilityClass
public class Strings {

    private final HexFormat HEX = HexFormat.of();

    @Contract(pure = true)
    public boolean containsIgnoreCase(
            @Nullable final String text,
            @Nullable final String part
    ) {
        if (text == null || part == null) {
            return false;
        }

        for (var index = 0; index <= text.length() - part.length(); index++) {
            if (text.regionMatches(true, index, part, 0, part.length())) {
                return true;
            }
        }

        return false;
    }

    @Contract(value = "null -> null", pure = true)
    public @Nullable String nullIfEmpty(
            @Nullable final String text
    ) {
        return text == null || text.isEmpty()
                ? null
                : text;
    }

    /**
     * Compares two CharSequence objects for equality.
     *
     * @param cs1 the first CharSequence, may be null
     * @param cs2 the second CharSequence, may be null
     * @return {@code true} if both CharSequences are equal, {@code false} otherwise
     */
    public boolean equals(@Nullable final CharSequence cs1, @Nullable final CharSequence cs2) {
        // Reference equality check
        if (cs1 == cs2) {
            return true;
        }

        // Null check
        if (cs1 == null || cs2 == null) {
            return false;
        }

        // Length check
        if (cs1.length() != cs2.length()) {
            return false;
        }

        // Optimized path for String comparison
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }

        // Character-by-character comparison
        for (int i = 0, length = cs1.length(); i < length; i++) {
            if (cs1.charAt(i) == cs2.charAt(i)) {
                continue;
            }

            return false;
        }

        return true;
    }

    public String truncate(
            @Nullable final String str,
            final int maxLength,
            final boolean addEllipsis
    ) {
        return truncate(str, 0, maxLength, addEllipsis);
    }

    public String truncate(
            @Nullable final String str,
            final int offset,
            final int maxLength,
            final boolean addEllipsis
    ) {
        Validates.requireNonNegative(offset, "offset");
        Validates.requireNonNegative(maxLength, "maxLength");

        if (str == null) {
            return null;
        }

        if (maxLength == 0) {
            return "";
        }

        if (offset > str.length()) {
            return "";
        }

        if (str.length() - offset <= maxLength) {
            return str.substring(offset);
        }

        var endIndex = Math.min(offset + maxLength, str.length());

        // ✅ Avoid cutting surrogate pairs
        if (endIndex > offset && Character.isHighSurrogate(str.charAt(endIndex - 1))) {
            endIndex--;
        }

        if (addEllipsis && maxLength > 3 && endIndex - offset > 3) {
            return str.substring(offset, endIndex - 3) + "...";
        }

        return str.substring(
                offset,
                endIndex
        );
    }

    public String filter(
            @NotNull final String string,
            @NotNull final CharPredicate predicate
    ) {
        val stringBuilder = new StringBuilder();

        for (val c : string.toCharArray()) {
            if (predicate.test(c)) {
                stringBuilder.append(c);
            }
        }

        return stringBuilder.toString();
    }

    public int freeLength(
            @NotNull final CharSequence seq,
            final int maxLength
    ) {
        return Math.max(0, maxLength - seq.length());
    }

    /**
     * Compares a region of a CharSequence with a String, without allocating.
     *
     * @param region the region to compare against
     * @param text   the text holding the region
     * @param start  index of the first character of the region (inclusive)
     * @param end    index past the last character of the region (exclusive)
     * @return {@code true} if the region equals {@code region}
     */
    public boolean regionEquals(
            @NotNull final String region,
            @NotNull final CharSequence text,
            final int start,
            final int end
    ) {
        if (region.length() != end - start) {
            return false;
        }
        if (start < 0 || end > text.length() || start > end) {
            return false;
        }

        for (int i = 0; i < region.length(); i++) {
            if (region.charAt(i) != text.charAt(start + i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first occurrence of a character within a range, without allocating.
     *
     * @param text the text to search
     * @param c    the character to look for
     * @param from index to start at (inclusive)
     * @param end  index to stop at (exclusive)
     * @return the index of the character, or {@code -1} if not found
     */
    public int indexOf(
            @NotNull final CharSequence text,
            final char c,
            final int from,
            final int end
    ) {
        val last = Math.min(end, text.length());
        for (int i = Math.max(from, 0); i < last; i++) {
            if (text.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Escapes a code point for plain-text output: {@code \n}, {@code \r},
     * {@code \t}, {@code \'} and any other ISO control character become
     * escape sequences; everything else is returned as-is.
     *
     * @param codePoint the code point to escape
     * @return the escaped representation
     */
    public String escapeControl(
            final int codePoint
    ) {
        return switch (codePoint) {
            case '\n' -> "\\n";
            case '\r' -> "\\r";
            case '\t' -> "\\t";
            case '\'' -> "\\'";
            default -> Character.isISOControl(codePoint)
                    ? "\\u" + HEX.toHexDigits((char) codePoint)
                    : Character.toString(codePoint);
        };
    }

    private final Pattern COMBINING_MARKS = Pattern.compile("\\p{M}+");

    /**
     * Removes accents and other combining marks, so {@code "Éclair"} becomes {@code "Eclair"}.
     * Useful for searches that should ignore accents.
     *
     * @param str the text
     * @return the text without combining marks
     */
    @Contract(pure = true)
    public @NotNull String stripAccents(
            @NotNull final String str
    ) {
        return COMBINING_MARKS.matcher(Normalizer.normalize(str, Normalizer.Form.NFD)).replaceAll("");
    }

    /**
     * Replaces every char that {@code allowed} rejects with {@code '_'}, such as to build a file
     * name or identifier from user text.
     */
    @Contract(pure = true)
    public @NotNull String sanitize(
            @NotNull final String text,
            @NotNull final CharPredicate allowed
    ) {
        return sanitize(text, allowed, '_');
    }

    /**
     * Replaces every char that {@code allowed} rejects with {@code replacement}.
     */
    @Contract(pure = true)
    public @NotNull String sanitize(
            @NotNull final String text,
            @NotNull final CharPredicate allowed,
            final char replacement
    ) {
        int first = 0;
        while (first < text.length() && allowed.test(text.charAt(first))) {
            first++;
        }

        if (first == text.length()) {
            return text;
        }

        val chars = text.toCharArray();
        for (int i = first; i < chars.length; i++) {
            if (!allowed.test(chars[i])) {
                chars[i] = replacement;
            }
        }

        return new String(chars);
    }

    /**
     * Returns a consumer that passes each string to {@code consumer} with {@code prefix} in front,
     * such as to tag log lines.
     */
    @Contract(pure = true)
    public @NotNull Consumer<String> prefixed(
            @NotNull final String prefix,
            @NotNull final Consumer<? super String> consumer
    ) {
        Validates.require(prefix, "prefix");
        Validates.require(consumer, "consumer");

        return text -> consumer.accept(prefix + text);
    }

}
