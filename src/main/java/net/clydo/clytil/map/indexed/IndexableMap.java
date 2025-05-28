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

package net.clydo.clytil.map.indexed;

import lombok.val;
import net.clydo.clytil.map.DelegatingMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Map implementation that maintains key insertion order and supports indexing.
 *
 * @param <K> Type of keys.
 * @param <V> Type of values.
 */
public class IndexableMap<K, V> extends DelegatingMap<K, V> {
    private final List<K> keyList;

    private IndexableMap(Map<K, V> map, List<K> keyList) {
        super(map);

        Objects.requireNonNull(map, "map must not be null");
        Objects.requireNonNull(keyList, "keyList must not be null");

        if (!map.keySet().equals(new HashSet<>(keyList))) {
            throw new IllegalArgumentException("keyList must represent the same keys as the map");
        }

        this.keyList = keyList;
    }

    /**
     * Creates a IndexableMap.
     */
    @Contract(" -> new")
    public static <K, V> @NotNull IndexableMap<K, V> create() {
        return new IndexableMap<>(new HashMap<>(), new ArrayList<>());
    }

    /**
     * Creates a IndexableMap.
     */
    @Contract("_, _ -> new")
    public static <K, V> @NotNull IndexableMap<K, V> create(Map<K, V> map, List<K> keyList) {
        return new IndexableMap<>(map, keyList);
    }

    /**
     * Creates a thread-safe IndexableMap.
     */
    @Contract(" -> new")
    public static <K, V> @NotNull IndexableMap<K, V> createThreadSafe() {
        return new IndexableMap<>(new ConcurrentHashMap<>(), Collections.synchronizedList(new ArrayList<>()));
    }

    /**
     * Retrieves a value by index.
     *
     * @param index Index of the key.
     * @return Value associated with the key at the given index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public V get(int index) {
        val key = this.getKey(index);
        return super.get(key);
    }

    /**
     * Retrieves a key by index.
     *
     * @param index Index to retrieve the key from.
     * @return The key at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public K getKey(int index) {
        if (index < 0 || index >= this.keyList.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return this.keyList.get(index);
    }

    /**
     * Retrieves the index of a key.
     *
     * @param key The key to find.
     * @return Index of the key or -1 if not found.
     */
    public int getIndexOfKey(K key) {
        return this.keyList.indexOf(key);
    }

    /**
     * Retrieves a key by its value.
     *
     * @param value The value to search for.
     * @return The key associated with the given value or null if not found.
     */
    public K getKeyByValue(V value) {
        for (K key : this.keyList) {
            if (Objects.equals(super.get(key), value)) {
                return key;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (!this.keyList.contains(key)) {
            this.keyList.add(key);
        }
        return super.put(key, value);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public V remove(Object key) {
        if (this.keyList.remove(key)) {
            return super.remove(key);
        }
        return null;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.keyList.clear();
    }

    public List<K> keyList() {
        return Collections.unmodifiableList(this.keyList);
    }

    /**
     * Sorts the keys based on a custom comparator.
     *
     * @param comparator Comparator to sort the keys.
     */
    public void sortKeys(Comparator<K> comparator) {
        this.keyList.sort(comparator);
    }

    /**
     * Swaps the positions of two keys in the key list.
     *
     * @param firstIndex  First key index.
     * @param secondIndex Second key index.
     * @throws IndexOutOfBoundsException if any index is out of bounds.
     */
    public void swapKeys(int firstIndex, int secondIndex) {
        Collections.swap(this.keyList, firstIndex, secondIndex);
    }

    /**
     * Moves a key to a new index.
     *
     * @param key      The key to move.
     * @param newIndex The new index to move the key to.
     * @throws IndexOutOfBoundsException if the new index is out of bounds.
     */
    public void moveKey(K key, int newIndex) {
        if (!this.keyList.contains(key)) {
            throw new IllegalArgumentException("Key not found: " + key);
        }
        this.keyList.remove(key);
        this.keyList.add(newIndex, key);
    }
}
