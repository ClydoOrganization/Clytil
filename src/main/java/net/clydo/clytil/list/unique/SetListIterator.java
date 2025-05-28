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

import net.clydo.clytil.list.DelegatingIterator;

import java.util.Iterator;
import java.util.Set;

public class SetListIterator<E> extends DelegatingIterator<E> {

    private final Set<E> set;
    private E last = null;

    protected SetListIterator(final Iterator<E> it, final Set<E> set) {
        super(it);
        this.set = set;
    }

    @Override
    public E next() {
        this.last = super.next();
        return this.last;
    }

    @Override
    public void remove() {
        super.remove();
        this.set.remove(last);
        this.last = null;
    }

}
