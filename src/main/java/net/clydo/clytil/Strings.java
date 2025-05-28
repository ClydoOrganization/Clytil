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
public class Strings {

    /**
     * Compares two CharSequence objects for equality.
     *
     * @param cs1 the first CharSequence, may be null
     * @param cs2 the second CharSequence, may be null
     * @return {@code true} if both CharSequences are equal, {@code false} otherwise
     */
    public boolean equals(final CharSequence cs1, final CharSequence cs2) {
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


}
