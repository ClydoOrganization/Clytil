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

package net.clydo.clytil.list.unique;

import net.clydo.clytil.list.DelegatingListIterator;

import java.util.ListIterator;
import java.util.Set;

public class SetListListIterator<E> extends DelegatingListIterator<E> {

    private final Set<E> set;
    private E last = null;

    protected SetListListIterator(final ListIterator<E> it, final Set<E> set) {
        super(it);
        this.set = set;
    }

    @Override
    public E next() {
        last = super.next();
        return last;
    }

    @Override
    public E previous() {
        last = super.previous();
        return last;
    }

    @Override
    public void remove() {
        super.remove();
        set.remove(last);
        last = null;
    }

    @Override
    public void add(final E object) {
        if (!set.contains(object)) {
            super.add(object);
            set.add(object);
        }
    }

    @Override
    public void set(final E object) {
        throw new UnsupportedOperationException("ListIterator does not support set");
    }
}
