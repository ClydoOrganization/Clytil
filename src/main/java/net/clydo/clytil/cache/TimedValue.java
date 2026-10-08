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

package net.clydo.clytil.cache;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A cached value with the {@link System#nanoTime()} at which it expires.
 *
 * @param value     the value, possibly a {@link NullMask} sentinel
 * @param expiresAt when the value expires
 */
record TimedValue(@Nullable Object value, long expiresAt) {

    @Contract("_, _ -> new")
    static @NotNull TimedValue of(
            @Nullable final Object value,
            final long ttlNanos
    ) {
        return new TimedValue(value, System.nanoTime() + ttlNanos);
    }

    /**
     * Compares by difference, which stays correct when {@code nanoTime} wraps around.
     */
    boolean isExpired() {
        return System.nanoTime() - this.expiresAt >= 0L;
    }

}
