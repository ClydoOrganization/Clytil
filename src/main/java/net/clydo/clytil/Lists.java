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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.random.RandomGenerator;

@UtilityClass
public class Lists {

    public <T> T last(
            @NotNull final List<T> list
    ) {
        return list.get(list.size() - 1);
    }

    /**
     * Appends every element of {@code from} to {@code to}, in order.
     * Uses indexed access when both are lists.
     *
     * @param from the source elements
     * @param to   the target collection
     * @param <T>  element type
     */
    public <T> void copy(
            @NotNull final List<T> from,
            @NotNull final Collection<T> to
    ) {
        for (int i = 0, size = from.size(); i < size; i++) {
            to.add(from.get(i));
        }
    }

    /**
     * Filters out {@code null} elements.
     *
     * @param list the source list
     * @param <T>  element type
     * @return a new list containing only non-null elements
     */
    public <T> @NotNull List<T> nonNull(
            @NotNull final List<T> list
    ) {
        return list.stream()
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Picks the highest-priority element that is both available and allowed.
     *
     * @param available     elements to choose from
     * @param priorityOrder candidates ordered from highest to lowest priority
     * @param allowed       optional filter; {@code null} or empty allows everything
     * @param fallback      returned when nothing matches
     * @param <T>           element type
     * @return the first priority element found, or {@code fallback}
     */
    public <T> T firstMatch(
            @NotNull final Collection<T> available,
            @NotNull final List<T> priorityOrder,
            @Nullable final Set<T> allowed,
            @NotNull final T fallback
    ) {
        if (available.isEmpty()) {
            return fallback;
        }

        val filtered = (allowed == null || allowed.isEmpty())
                ? available
                : available.stream().filter(allowed::contains).toList();

        if (filtered.isEmpty()) {
            return fallback;
        }

        for (val candidate : priorityOrder) {
            if (filtered.contains(candidate)) {
                return candidate;
            }
        }

        return fallback;
    }

    // ---------- Copies ----------

    /**
     * Returns an unmodifiable copy of {@code list} with {@code element} appended.
     */
    @Contract("_, _ -> new")
    public <T> @NotNull List<T> with(
            @NotNull final List<? extends T> list,
            final T element
    ) {
        val copy = new ArrayList<T>(list.size() + 1);
        copy.addAll(list);
        copy.add(element);
        return Collections.unmodifiableList(copy);
    }

    /**
     * Returns an unmodifiable copy of {@code list} with {@code elements} appended.
     */
    @SafeVarargs
    @Contract("_, _ -> new")
    public <T> @NotNull List<T> with(
            @NotNull final List<? extends T> list,
            final T @NotNull ... elements
    ) {
        val copy = new ArrayList<T>(list.size() + elements.length);
        copy.addAll(list);
        Collections.addAll(copy, elements);
        return Collections.unmodifiableList(copy);
    }

    /**
     * Returns an unmodifiable copy of {@code list} with {@code element} prepended.
     */
    @Contract("_, _ -> new")
    public <T> @NotNull List<T> withFirst(
            final T element,
            @NotNull final List<? extends T> list
    ) {
        val copy = new ArrayList<T>(list.size() + 1);
        copy.add(element);
        copy.addAll(list);
        return Collections.unmodifiableList(copy);
    }

    // ---------- Cycling ----------

    /**
     * Returns the element after {@code current}, wrapping from the last element to the first.
     * When {@code current} is {@code null} or not in the list, returns the first element.
     *
     * @throws NoSuchElementException if the list is empty
     */
    public <T> T next(
            @NotNull final List<T> list,
            @Nullable final T current
    ) {
        requireNotEmpty(list);

        val index = current == null ? -1 : list.indexOf(current);
        return list.get(index < 0 || index == list.size() - 1 ? 0 : index + 1);
    }

    /**
     * Returns the element before {@code current}, wrapping from the first element to the last.
     * When {@code current} is {@code null} or not in the list, returns the last element.
     *
     * @throws NoSuchElementException if the list is empty
     */
    public <T> T previous(
            @NotNull final List<T> list,
            @Nullable final T current
    ) {
        requireNotEmpty(list);

        val index = current == null ? -1 : list.indexOf(current);
        return list.get(index <= 0 ? list.size() - 1 : index - 1);
    }

    // ---------- Randomness ----------

    /**
     * Returns a random element of {@code list}.
     *
     * @throws NoSuchElementException if the list is empty
     */
    public <T> T random(
            @NotNull final List<T> list,
            @NotNull final RandomGenerator random
    ) {
        requireNotEmpty(list);

        return list.get(random.nextInt(list.size()));
    }

    /**
     * Returns a random element of {@code list}, or an empty {@link Optional} if it is empty or the
     * chosen element is {@code null}.
     */
    public <T> @NotNull Optional<T> randomOptional(
            @NotNull final List<T> list,
            @NotNull final RandomGenerator random
    ) {
        return list.isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(list.get(random.nextInt(list.size())));
    }

    /**
     * Shuffles {@code list} in place (Fisher–Yates). Accepts any {@link RandomGenerator}, unlike
     * {@link Collections#shuffle(List, java.util.Random)}.
     */
    public <T> void shuffle(
            @NotNull final List<T> list,
            @NotNull final RandomGenerator random
    ) {
        for (int i = list.size() - 1; i > 0; i--) {
            val j = random.nextInt(i + 1);
            list.set(i, list.set(j, list.get(i)));
        }
    }

    /**
     * Returns a shuffled, mutable copy of {@code elements}.
     */
    @Contract("_, _ -> new")
    public <T> @NotNull List<T> shuffledCopy(
            @NotNull final Collection<? extends T> elements,
            @NotNull final RandomGenerator random
    ) {
        val copy = new ArrayList<T>(elements);
        shuffle(copy, random);
        return copy;
    }

    // ---------- Index lookups ----------

    private final int LINEAR_LOOKUP_THRESHOLD = 8;

    /**
     * Returns a function giving each element's index in a snapshot of {@code list}, or {@code -1},
     * like {@link List#indexOf(Object)}. Long lists are indexed into a hash map, so repeated
     * lookups are O(1).
     */
    @Contract("_ -> new")
    public <T> @NotNull ToIntFunction<T> indexLookup(
            @NotNull final List<? extends T> list
    ) {
        val copy = new ArrayList<T>(list);
        if (copy.size() < LINEAR_LOOKUP_THRESHOLD) {
            return copy::indexOf;
        }

        val indexes = new HashMap<T, Integer>(copy.size() * 2);
        for (int i = 0; i < copy.size(); i++) {
            indexes.putIfAbsent(copy.get(i), i);
        }

        return element -> indexes.getOrDefault(element, -1);
    }

    /**
     * Like {@link #indexLookup(List)}, but matches elements by identity ({@code ==}) instead of
     * {@link Object#equals(Object)}.
     */
    @Contract("_ -> new")
    public <T> @NotNull ToIntFunction<T> identityIndexLookup(
            @NotNull final List<? extends T> list
    ) {
        val copy = new ArrayList<T>(list);
        if (copy.size() < LINEAR_LOOKUP_THRESHOLD) {
            return element -> {
                for (int i = 0; i < copy.size(); i++) {
                    if (copy.get(i) == element) {
                        return i;
                    }
                }

                return -1;
            };
        }

        val indexes = new IdentityHashMap<T, Integer>(copy.size());
        for (int i = 0; i < copy.size(); i++) {
            indexes.putIfAbsent(copy.get(i), i);
        }

        return element -> indexes.getOrDefault(element, -1);
    }

    private void requireNotEmpty(
            final @NotNull List<?> list
    ) {
        if (list.isEmpty()) {
            throw new NoSuchElementException("list is empty");
        }
    }

}
