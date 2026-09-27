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

/**
 * Horizontal and vertical alignment bit flags.
 *
 * <ul>
 * <li>{@link #ALIGN_LEFT} - align horizontally to left</li>
 * <li>{@link #ALIGN_CENTER} - align horizontally to center</li>
 * <li>{@link #ALIGN_RIGHT} - align horizontally to right</li>
 * <li>{@link #ALIGN_TOP} - align vertically to top</li>
 * <li>{@link #ALIGN_MIDDLE} - align vertically to middle</li>
 * <li>{@link #ALIGN_BOTTOM} - align vertically to bottom</li>
 * <li>{@link #ALIGN_BASELINE} - align vertically to baseline</li>
 * </ul>
 *
 * <p>Combine flags with {@code |} and extract axes with
 * {@link #getHorizontalAlign(int)} / {@link #getVerticalAlign(int)}.</p>
 */
@UtilityClass
public class Align {

    public final int
            ALIGN_LEFT = 1 << 0,
            ALIGN_CENTER = 1 << 1,
            ALIGN_RIGHT = 1 << 2,
            ALIGN_TOP = 1 << 3,
            ALIGN_MIDDLE = 1 << 4,
            ALIGN_BOTTOM = 1 << 5,
            ALIGN_BASELINE = 1 << 6,
            ALIGN_DEFAULT = ALIGN_LEFT | ALIGN_BASELINE,
            ALIGN_CENTER_MIDDLE = ALIGN_CENTER | ALIGN_MIDDLE;

    /**
     * Extracts the horizontal flags from a combined alignment.
     *
     * @param align the combined alignment
     * @return the horizontal subset of {@code align}
     */
    public int getHorizontalAlign(
            final int align
    ) {
        return align & (Align.ALIGN_LEFT | Align.ALIGN_CENTER | Align.ALIGN_RIGHT);
    }

    /**
     * Extracts the vertical flags from a combined alignment.
     *
     * @param align the combined alignment
     * @return the vertical subset of {@code align}
     */
    public int getVerticalAlign(
            final int align
    ) {
        return align & (Align.ALIGN_TOP | Align.ALIGN_MIDDLE | Align.ALIGN_BOTTOM | Align.ALIGN_BASELINE);
    }

}
