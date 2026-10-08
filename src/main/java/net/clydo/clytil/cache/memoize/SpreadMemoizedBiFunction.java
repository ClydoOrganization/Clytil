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
 * Copyright (C) 2026 ClydoNetwork
 */

package net.clydo.clytil.cache.memoize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Memoizes a {@link BiFunction} through a one-argument {@link MemoizedFunction} keyed by both
 * arguments together.
 *
 * @param function the function being memoized
 * @param delegate the cache, keyed by both arguments
 */
record SpreadMemoizedBiFunction<T, U, R>(
        @NotNull BiFunction<? super T, ? super U, ? extends R> function,
        @NotNull MemoizedFunction<Key<T, U>, R> delegate
) implements MemoizedBiFunction<T, U, R> {

    @Contract("_, _ -> new")
    static <T, U, R> @NotNull SpreadMemoizedBiFunction<T, U, R> of(
            @NotNull final BiFunction<? super T, ? super U, ? extends R> function,
            @NotNull final Function<Function<Key<T, U>, R>, MemoizedFunction<Key<T, U>, R>> memoizer
    ) {
        return new SpreadMemoizedBiFunction<>(
                function,
                memoizer.apply(key -> function.apply(key.first(), key.second()))
        );
    }

    @Override
    public R apply(final T first, final U second) {
        return this.delegate.apply(new Key<>(first, second));
    }

    @Override
    public boolean isCached(final T first, final U second) {
        return this.delegate.isCached(new Key<>(first, second));
    }

    @Override
    public void invalidate(final T first, final U second) {
        this.delegate.invalidate(new Key<>(first, second));
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public @NotNull String toString() {
        return "Memoize.biFunction[" + this.function + ", size=" + this.size() + "]";
    }

    record Key<T, U>(@Nullable T first, @Nullable U second) {

        @Contract(pure = true)
        @Override
        public @NotNull String toString() {
            return "(" + this.first + ", " + this.second + ")";
        }

    }

}
