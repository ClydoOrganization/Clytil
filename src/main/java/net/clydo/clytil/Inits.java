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

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class Inits {

    @SafeVarargs
    public <T, R> R of(
            @NotNull Supplier<T> supplier,
            @NotNull Function<? super T, ? extends R>... functions
    ) {
        val t = Inits.of(supplier);
        return Inits.of(t, functions);
    }

    @SafeVarargs
    public <T> T of(
            @NotNull Supplier<T> supplier,
            @NotNull Consumer<? super T>... consumers
    ) {
        val t = Inits.of(supplier);
        return Inits.of(t, consumers);
    }

    public <T, R> R of(
            @NotNull Supplier<T> supplier,
            @NotNull Function<? super T, ? extends R> function
    ) {
        val t = Inits.of(supplier);
        return Inits.of(t, function);
    }

    public <T> T of(
            @NotNull Supplier<T> supplier,
            @NotNull Consumer<? super T> consumer
    ) {
        val t = Inits.of(supplier);
        return Inits.of(t, consumer);
    }

    @SafeVarargs
    public <T> T of(
            T t,
            @NotNull Consumer<? super T>... consumers
    ) {
        Validates.require(consumers, "consumers");

        for (val consumer : consumers) {
            consumer.accept(t);
        }

        return t;
    }

    @SafeVarargs
    public <T, R> R of(
            T t,
            @NotNull Function<? super T, ? extends R>... functions
    ) {
        Validates.require(functions, "functions");

        R result = null;

        for (val function : functions) {
            result = function.apply(t);
        }

        return result;
    }

    public <T> T of(
            @NotNull Supplier<T> supplier
    ) {
        Validates.require(supplier, "supplier");

        return supplier.get();
    }

    public <T> T of(
            T t,
            @NotNull Consumer<? super T> consumer
    ) {
        Validates.require(consumer, "consumer");

        consumer.accept(t);
        return t;
    }

    public <T, R> R of(
            T t,
            @NotNull Function<? super T, ? extends R> function
    ) {
        Validates.require(function, "function");

        return function.apply(t);
    }

}
