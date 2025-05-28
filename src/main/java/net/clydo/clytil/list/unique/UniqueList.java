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
import net.clydo.clytil.list.DelegatingList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public abstract class UniqueList<E> extends DelegatingList<E> {

    protected UniqueList(final List<E> list) {
        super(list);
    }

    @Override
    public boolean add(E e) {
        val size = this.size();

        this.add(size, e);

        return size != this.size();
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return this.addAll(this.size(), c);
    }

}
