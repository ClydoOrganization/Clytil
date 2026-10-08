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

package net.clydo.clytil.cache.wrapper;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A cache of every boxed {@link Character} value, wider than the JDK's own {@code [0, 127]}.
 */
@UtilityClass
public class CharacterCache {

    private final Character[] CACHE = new Character[0x10000];

    static {
        for (int i = 0; i < CACHE.length; i++) {
            CACHE[i] = (char) i;
        }
    }

    /**
     * Returns the cached {@link Character} for {@code value}.
     */
    @Contract(pure = true)
    public @NotNull Character get(
            final char value
    ) {
        return CACHE[value];
    }

}
