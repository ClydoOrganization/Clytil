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

import java.util.Iterator;
import java.util.Objects;

public abstract class DelegatingUntypedIterator<I, O> implements Iterator<O> {

    private final Iterator<I> iterator;

    protected DelegatingUntypedIterator(final Iterator<I> iterator) {
        this.iterator = Objects.requireNonNull(iterator, "iterator");
    }

    protected Iterator<I> getIterator() {
        return this.iterator;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public void remove() {
        this.iterator.remove();
    }

}
