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

package net.clydo.clytil.list;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class DelegatingCollection<E> implements Collection<E>, Serializable {

    private Collection<E> delegate;

    public DelegatingCollection() {
        super();
    }

    public DelegatingCollection(Collection<E> collection) {
        this.delegate = Objects.requireNonNull(collection, "collection must not be null");
    }

    public Collection<E> delegate() {
        return this.delegate;
    }

    @Override
    public boolean add(E e) {
        return this.delegate.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.delegate.remove(o);
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
    public boolean contains(Object o) {
        return this.delegate.contains(o);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.delegate.iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return this.delegate.toArray();
    }

    @Override
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return this.delegate.toArray(a);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return this.delegate.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.delegate.containsAll(c);
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super E> filter) {
        return this.delegate.removeIf(filter);
    }

    @Override
    public String toString() {
        return this.delegate.toString();
    }

}

