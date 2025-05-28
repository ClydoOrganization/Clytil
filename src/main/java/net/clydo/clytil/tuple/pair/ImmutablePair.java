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

package net.clydo.clytil.tuple.pair;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImmutablePair<F, S> extends Pair<F, S> {

    private final F first;
    private final S second;

    @Override
    public F first() {
        return this.first;
    }

    @Override
    public S second() {
        return this.second;
    }

    @Override
    public Pair<F, S> first(final F first) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pair<F, S> second(final S second) {
        throw new UnsupportedOperationException();
    }

}
