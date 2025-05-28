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

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@UtilityClass
public class UniqueLists {

    public <E> @NotNull UniqueListWithoutSet<E> unsafe() {
        return UniqueLists.unsafe(new ArrayList<>());
    }

    @Contract("null -> fail")
    public <E> @NotNull UniqueListWithoutSet<E> unsafe(final List<E> list) {
        Objects.requireNonNull(list, "list must not be null");

        if (list.isEmpty()) {
            return new UniqueListWithoutSet<>(list);
        }

        val temp = new ArrayList<>(list);
        list.clear();
        val sl = new UniqueListWithoutSet<>(list);
        sl.addAll(temp);
        return sl;
    }

    public <E> @NotNull UniqueListWithSet<E> safe() {
        return UniqueLists.safe(new ArrayList<>(), new HashSet<>());
    }

    @Contract("null -> fail")
    public <E> @NotNull UniqueListWithSet<E> safe(final List<E> list) {
        return UniqueLists.safe(list, new HashSet<>());
    }

    public <E> @NotNull UniqueListWithSet<E> safe(final Set<E> set) {
        return UniqueLists.safe(new ArrayList<>(), set);
    }

    @Contract("null, null -> fail")
    public <E> @NotNull UniqueListWithSet<E> safe(final List<E> list, final Set<E> set) {
        Objects.requireNonNull(list, "list must not be null");
        Objects.requireNonNull(set, "set must not be null");

        if (list.isEmpty()) {
            return new UniqueListWithSet<>(list, set);
        }

        val temp = new ArrayList<>(list);
        list.clear();
        val sl = new UniqueListWithSet<>(list, set);
        sl.addAll(temp);
        return sl;
    }

}
