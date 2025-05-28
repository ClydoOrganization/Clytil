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

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;

public class UniqueListWithSet<E> extends UniqueList<E> {

    private final Set<E> set;

    protected UniqueListWithSet(final List<E> list, final Set<E> set) {
        super(list);
        this.set = Objects.requireNonNull(set, "set must not be null");
    }

    @Override
    public boolean contains(Object o) {
        return this.set.contains(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new SetListIterator<>(super.iterator(), this.set);
    }

    @Override
    public boolean remove(Object o) {
        val removed = this.set.remove(o);

        if (removed) {
            super.remove(o);
        }

        return removed;
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super E> filter) {
        val removed = super.removeIf(filter);

        this.set.removeIf(filter);

        return removed;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        val temp = new ArrayList<E>();

        for (val e : c) {
            if (!this.set.add(e)) {
                continue;
            }

            temp.add(e);
        }

        return super.addAll(index, temp);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        var result = false;

        for (val name : c) {
            result |= this.remove(name);
        }

        return result;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        val retained = this.set.retainAll(c);
        if (!retained) {
            return false;
        }

        if (this.set.isEmpty()) {
            super.clear();
        } else {
            // use the set as parameter for the call to retainAll to improve performance
            super.retainAll(set);
        }
        return true;
    }

    @Override
    public void clear() {
        super.clear();
        this.set.clear();
    }

    @Override
    public E set(int index, E element) {
        val indexBefore = this.indexOf(element);
        val removed = super.set(index, element);

        if (indexBefore != -1 && indexBefore != index) {
            // the object is already in the unique list
            // (and it hasn't been swapped with itself)
            super.remove(indexBefore); // remove the duplicate by index
        }

        this.set.remove(removed); // remove the item deleted by the set
        this.set.add(element); // add the new item to the unique set

        return removed; // return the item deleted by the set
    }

    @Override
    public void add(int index, E element) {
        if (this.set.contains(element)) {
            return;
        }

        this.set.add(element);
        super.add(index, element);
    }

    @Override
    public E remove(int index) {
        val removed = super.remove(index);

        this.set.remove(removed);

        return removed;
    }

    @Override
    public @NotNull ListIterator<E> listIterator() {
        return new SetListListIterator<>(super.listIterator(), this.set);
    }

    @Override
    public @NotNull ListIterator<E> listIterator(int index) {
        return new SetListListIterator<>(super.listIterator(index), this.set);
    }

    @Override
    public @NotNull List<E> subList(int fromIndex, int toIndex) {
        val superSubList = super.subList(fromIndex, toIndex);
        val subSet = this.createSetBasedOnList(this.set, superSubList);
        return List.copyOf(new UniqueListWithSet<>(superSubList, subSet));
    }

    protected Set<E> createSetBasedOnList(final @NotNull Set<E> set, final List<E> list) {
        Set<E> subSet;
        if (set.getClass().equals(HashSet.class)) {
            subSet = new HashSet<>(list.size());
        } else {
            try {
                subSet = set.getClass().getDeclaredConstructor(set.getClass()).newInstance(set);
            } catch (final InstantiationException
                           | IllegalAccessException
                           | InvocationTargetException
                           | NoSuchMethodException ie) {
                subSet = new HashSet<>();
            }
        }
        return subSet;
    }

}
