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

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class UniqueListWithoutSet<E> extends UniqueList<E> {

    protected UniqueListWithoutSet(final List<E> list) {
        super(list);
    }

    @Override
    public void add(int index, E element) {
        if (this.contains(element)) {
            return;
        }

        super.add(index, element);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        var result = false;

        for (val e : c) {
            if (this.add(e)) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public E set(int index, E element) {
        val indexBefore = this.indexOf(element);
        val set = super.set(index, element);

        if (indexBefore != -1 && indexBefore != index) {
            super.remove(indexBefore);
        }

        return set;
    }

}
