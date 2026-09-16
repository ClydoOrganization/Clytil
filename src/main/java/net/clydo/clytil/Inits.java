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

import java.util.function.*;

@UtilityClass
public class Inits {

    // ---------- Functions ----------

    public <T, R> R apply(
            @NotNull final T t,
            @NotNull final Function<? super T, ? extends R> function
    ) {
        Validates.require(function, "function");

        return function.apply(t);
    }

    @SafeVarargs
    public <T, R> R apply(
            @NotNull final T t,
            @NotNull final Function<? super T, ? extends R>... functions
    ) {
        Validates.require(functions, "functions");

        R result = null;

        for (val function : functions) {
            result = function.apply(t);
        }

        return result;
    }

    public <T, R> R apply(
            @NotNull final Supplier<T> supplier,
            @NotNull final Function<? super T, ? extends R> function
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(function, "function");

        return function.apply(supplier.get());
    }

    @SafeVarargs
    public <T, R> R apply(
            @NotNull final Supplier<T> supplier,
            @NotNull final Function<? super T, ? extends R>... functions
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(functions, "functions");

        val t = supplier.get();

        R result = null;
        for (val function : functions) {
            result = function.apply(t);
        }

        return result;
    }

    // ---------- BiFunctions ----------

    public <T, U, R> R apply(
            @NotNull final T t,
            @NotNull final U u,
            @NotNull final BiFunction<? super T, ? super U, ? extends R> function
    ) {
        Validates.require(function, "function");

        return function.apply(t, u);
    }

    // ---------- Consumers ----------

    public <T> T accept(
            @NotNull final T t,
            @NotNull final Consumer<? super T> consumer
    ) {
        Validates.require(consumer, "consumer");

        consumer.accept(t);
        return t;
    }

    @SafeVarargs
    public <T> T accept(
            @NotNull final T t,
            @NotNull final Consumer<? super T>... consumers
    ) {
        Validates.require(consumers, "consumers");

        for (val consumer : consumers) {
            consumer.accept(t);
        }

        return t;
    }

    public <T> T accept(
            @NotNull final Supplier<T> supplier,
            @NotNull final Consumer<? super T> consumer
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(consumer, "consumer");

        val t = supplier.get();
        consumer.accept(t);

        return t;
    }

    @SafeVarargs
    public <T> T accept(
            @NotNull final Supplier<T> supplier,
            @NotNull final Consumer<? super T>... consumers
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(consumers, "consumers");

        val t = supplier.get();

        for (val consumer : consumers) {
            consumer.accept(t);
        }

        return t;
    }

    // ---------- BiConsumers ----------

    public <T, U> void accept(
            @NotNull final T t,
            @NotNull final U u,
            @NotNull final BiConsumer<? super T, ? super U> consumer
    ) {
        Validates.require(consumer, "consumer");

        consumer.accept(t, u);
    }

    // ---------- Predicates ----------

    public <T> boolean test(
            @NotNull final T t,
            @NotNull final Predicate<? super T> predicate
    ) {
        Validates.require(predicate, "predicate");

        return predicate.test(t);
    }

    public <T> boolean test(
            @NotNull final Supplier<T> supplier,
            @NotNull final Predicate<? super T> predicate
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(predicate, "predicate");

        return predicate.test(supplier.get());
    }

    // ---------- Suppliers ----------

    public <T> T get(
            @NotNull Supplier<T> supplier
    ) {
        Validates.require(supplier, "supplier");

        return supplier.get();
    }

    public boolean get(
            @NotNull final BooleanSupplier supplier
    ) {
        Validates.require(supplier, "supplier");

        return supplier.getAsBoolean();
    }

}