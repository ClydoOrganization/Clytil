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

package net.clydo.clytil.tuple.triple;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImmutableTriple<F, S, T> extends Triple<F, S, T> {

    private final F first;
    private final S second;
    private final T third;

    @Override
    public F first() {
        return this.first;
    }

    @Override
    public S second() {
        return this.second;
    }

    @Override
    public T third() {
        return this.third;
    }

    @Override
    public Triple<F, S, T> first(F first) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Triple<F, S, T> second(S second) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Triple<F, S, T> third(T third) {
        throw new UnsupportedOperationException();
    }

}
