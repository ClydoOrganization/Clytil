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

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public abstract class DelegatingList<E> extends DelegatingCollection<E> implements List<E> {

    public DelegatingList() {
        super();
    }

    public DelegatingList(final List<E> list) {
        super(list);
    }

    @Override
    public List<E> delegate() {
        return (List<E>) super.delegate();
    }

    @Override
    public boolean equals(final Object object) {
        return object == this || this.delegate().equals(object);
    }

    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }

    @Override
    public void add(int index, E element) {
        this.delegate().add(index, element);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return this.delegate().addAll(index, c);
    }

    @Override
    public E get(int index) {
        return this.delegate().get(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.delegate().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.delegate().lastIndexOf(o);
    }

    @Override
    public @NotNull ListIterator<E> listIterator() {
        return this.delegate().listIterator();
    }

    @Override
    public @NotNull ListIterator<E> listIterator(int index) {
        return this.delegate().listIterator(index);
    }

    @Override
    public E remove(int index) {
        return this.delegate().remove(index);
    }

    @Override
    public E set(int index, E element) {
        return this.delegate().set(index, element);
    }

    @Override
    public @NotNull List<E> subList(int fromIndex, int toIndex) {
        return this.delegate().subList(fromIndex, toIndex);
    }

}

