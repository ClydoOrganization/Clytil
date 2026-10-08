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
import net.clydo.clytil.Validates;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@UtilityClass
public class UniqueLists {

    @Contract(" -> new")
    public <E> @NotNull UniqueListWithoutSet<E> unsafe() {
        return UniqueLists.unsafe(new ArrayList<>());
    }

    @Contract("null -> fail; _ -> new")
    public <E> @NotNull UniqueListWithoutSet<E> unsafe(@NotNull final List<E> list) {
        Validates.require(list, "list");

        if (list.isEmpty()) {
            return new UniqueListWithoutSet<>(list);
        }

        val temp = new ArrayList<>(list);
        list.clear();
        val sl = new UniqueListWithoutSet<>(list);
        sl.addAll(temp);
        return sl;
    }

    @Contract(" -> new")
    public <E> @NotNull UniqueListWithSet<E> safe() {
        return UniqueLists.safe(new ArrayList<>(), new HashSet<>());
    }

    @Contract("null -> fail; _ -> new")
    public <E> @NotNull UniqueListWithSet<E> safe(@NotNull final List<E> list) {
        return UniqueLists.safe(list, new HashSet<>());
    }

    @Contract("null -> fail; _ -> new")
    public <E> @NotNull UniqueListWithSet<E> safe(@NotNull final Set<E> set) {
        return UniqueLists.safe(new ArrayList<>(), set);
    }

    @Contract("null, _ -> fail; _, null -> fail; _, _ -> new")
    public <E> @NotNull UniqueListWithSet<E> safe(@NotNull final List<E> list, @NotNull final Set<E> set) {
        Validates.require(list, "list");
        Validates.require(set, "set");

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
