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

package net.clydo.clytil.tuple;

import lombok.experimental.UtilityClass;
import net.clydo.clytil.tuple.pair.ImmutablePair;
import net.clydo.clytil.tuple.pair.MutablePair;
import net.clydo.clytil.tuple.pair.Pair;
import net.clydo.clytil.tuple.triple.ImmutableTriple;
import net.clydo.clytil.tuple.triple.MutableTriple;
import net.clydo.clytil.tuple.triple.Triple;

@UtilityClass
public class Tuples {

    public <F, S> Pair<F, S> of(final F first, final S second) {
        return new ImmutablePair<>(first, second);
    }

    public <F, S, T> Triple<F, S, T> of(final F first, final S second, final T third) {
        return new ImmutableTriple<>(first, second, third);
    }

    public <F, S> Pair<F, S> mut(final F first, final S second) {
        return new MutablePair<>(first, second);
    }

    public <F, S, T> Triple<F, S, T> mut(final F first, final S second, final T third) {
        return new MutableTriple<>(first, second, third);
    }

}
