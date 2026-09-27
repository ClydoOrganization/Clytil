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

package net.clydo.clytil.data;

import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

/**
 * A lookup from an identifier to a value: the minimal common shape of
 * registries, caches and resource managers.
 *
 * @param <I> identifier type
 * @param <R> value type
 */
public interface Provider<I, R> {

    /**
     * @param identifier the identifier to probe
     * @return {@code true} if this provider can supply {@code identifier}
     */
    boolean has(
            @NotNull I identifier
    );

    /**
     * @param identifier the identifier to look up
     * @return the value, or {@code null} if unavailable
     */
    @Nullable R get(
            @NotNull I identifier
    );

    /**
     * Wraps this provider, mapping every value it supplies.
     *
     * @param mapper mapping applied to supplied values
     * @return a mapped view of this provider
     */
    @Contract(value = "_ -> new", pure = true)
    default <T> @NotNull Provider<I, T> map(
            @NotNull final BiFunction<I, R, T> mapper
    ) {
        val source = this;

        return new Provider<>() {

            @Override
            public boolean has(
                    @NotNull final I identifier
            ) {
                return source.has(identifier);
            }

            @Override
            public @Nullable T get(
                    @NotNull final I identifier
            ) {
                val resource = source.get(identifier);
                return resource != null ? mapper.apply(identifier, resource) : null;
            }

        };
    }

    /**
     * @param <I> identifier type
     * @param <R> value type
     * @return a provider that never supplies anything
     */
    @Contract(value = "-> new", pure = true)
    static <I, R> @NotNull Provider<I, R> empty() {
        return new Provider<>() {

            @Override
            public boolean has(
                    @NotNull final I identifier
            ) {
                return false;
            }

            @Override
            public @Nullable R get(
                    @NotNull final I identifier
            ) {
                return null;
            }

        };
    }

    /**
     * Chains providers: the first one supplying a value wins.
     *
     * @param providers providers to chain, in priority order
     * @param <I>       identifier type
     * @param <R>       value type
     * @return a provider consulting every given provider in order
     */
    @SafeVarargs
    @Contract(value = "_ -> new", pure = true)
    static <I, R> @NotNull Provider<I, R> compose(
            @NotNull final Provider<I, R>... providers
    ) {
        val chain = List.of(providers);

        return new Provider<>() {

            @Override
            public boolean has(
                    @NotNull final I identifier
            ) {
                for (val provider : chain) {
                    if (provider.has(identifier)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public @Nullable R get(
                    @NotNull final I identifier
            ) {
                for (val provider : chain) {
                    val resource = provider.get(identifier);
                    if (resource != null) {
                        return resource;
                    }
                }
                return null;
            }

        };
    }

}
