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
import net.clydo.clytil.str.Substring;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Texts {

    public Substring getNextWord(
            @NotNull final CharSequence text,
            final int fromIndex,
            final boolean skipOverSpaces
    ) {
        if (text.isEmpty()) {
            return Substring.EMPTY;
        }

        var position = FastMaths.clamp(fromIndex, 0, text.length() - 1);

        while (position < text.length() && !Character.isWhitespace(text.charAt(position))) {
            position++;
        }

        if (skipOverSpaces) {
            while (position < text.length() && Character.isWhitespace(text.charAt(position))) {
                position++;
            }
        }

        val endIndex = Texts.getWordEndIndex(text, position);
        return Substring.of(position, endIndex);
    }

    public Substring getPreviousWord(
            @NotNull final CharSequence text,
            final int fromIndex,
            final boolean skipOverSpaces
    ) {
        if (text.isEmpty()) {
            return Substring.EMPTY;
        }

        var position = FastMaths.clamp(fromIndex, 0, text.length() - 1);

        if (skipOverSpaces) {
            while (position > 0 && Character.isWhitespace(text.charAt(position - 1))) {
                position--;
            }
        }

        while (position > 0 && !Character.isWhitespace(text.charAt(position - 1))) {
            position--;
        }

        val endIndex = Texts.getWordEndIndex(text, position);
        return Substring.of(position, endIndex);
    }

    public int getWordEndIndex(
            @NotNull final CharSequence text,
            final int fromIndex
    ) {
        var index = fromIndex;

        while (index < text.length() && !Character.isWhitespace(text.charAt(index))) {
            index++;
        }

        return index;
    }

}
