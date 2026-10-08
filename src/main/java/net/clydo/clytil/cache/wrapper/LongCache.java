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
 * A cache of boxed {@link Long} values in {@code [0, 0xFFFF]}, wider than the JDK's own
 * {@code [-128, 127]}.
 */
@UtilityClass
public class LongCache {

    private final Long[] CACHE = new Long[0x10000];

    static {
        for (int i = 0; i < CACHE.length; i++) {
            CACHE[i] = (long) i;
        }
    }

    /**
     * Returns the cached {@link Long} for {@code value}, or {@link Long#valueOf(long)} when it is
     * out of the cached range.
     */
    @Contract(pure = true)
    public @NotNull Long get(
            final long value
    ) {
        return value >= 0 && value < CACHE.length ? CACHE[(int) value] : Long.valueOf(value);
    }

}
