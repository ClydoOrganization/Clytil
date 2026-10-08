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

package net.clydo.clytil.cache.memoize;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A {@link LinkedHashMap} in access order that drops its least recently used entry once it holds
 * more than {@code maxSize}. Not thread-safe.
 */
final class LruMap<K, V> extends LinkedHashMap<K, V> {

    private final int maxSize;

    LruMap(
            final int maxSize
    ) {
        super(Math.min(16, maxSize + 1), 0.75f, true);
        this.maxSize = maxSize;
    }

    int maxSize() {
        return this.maxSize;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        return this.size() > this.maxSize;
    }

}
