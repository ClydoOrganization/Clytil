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

package net.clydo.clytil.map;

import net.clydo.clytil.Validates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DelegatingMap<K, V> implements Map<K, V>, Serializable {

    private Map<K, V> delegate;

    public DelegatingMap() {
        super();
    }

    public DelegatingMap(@NotNull final Map<K, V> map) {
        this.delegate = Validates.require(map, "map");
    }

    public Map<K, V> delegate() {
        return this.delegate;
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable final Object key) {
        return this.delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nullable final Object value) {
        return this.delegate.containsValue(value);
    }

    @Override
    public V get(@Nullable final Object key) {
        return this.delegate.get(key);
    }

    @Override
    public @Nullable V put(K key, V value) {
        return this.delegate.put(key, value);
    }

    @Override
    public V remove(@Nullable final Object key) {
        return this.delegate.remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        this.delegate.putAll(m);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public @NotNull Set<K> keySet() {
        return this.delegate.keySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        return this.delegate.values();
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return this.delegate.entrySet();
    }

}
