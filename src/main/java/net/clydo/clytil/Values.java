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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.function.*;

/**
 * Runs functional interfaces against a value inline. Values may be {@code null}; the functions may not.
 * <p>
 * Nullability follows the arguments: a nullable value makes the function's parameter nullable, and
 * the result is nullable only if the function's result is.
 * <p>
 * {@code apply}, {@code accept} and {@code test} also have {@link Supplier} overloads that take the value from
 * the supplier. Because of them, a bare {@code null} literal is ambiguous: write {@code apply((String) null, ...)}
 * or pass a typed variable.
 */
@UtilityClass
@NotNullByDefault
public class Values {

    // ---------- Functions ----------

    public <T extends @Nullable Object, R extends @Nullable Object> R apply(
            final T value,
            final Function<? super T, ? extends R> function
    ) {
        Validates.require(function, "function");

        return function.apply(value);
    }

    /**
     * Applies each function to {@code value} in order and returns the last result, or {@code null}
     * when no functions are given.
     */
    @SafeVarargs
    public <T extends @Nullable Object, R extends @Nullable Object> @Nullable R apply(
            final T value,
            final Function<? super T, ? extends R>... functions
    ) {
        Validates.require(functions, "functions");

        R result = null;
        for (val function : functions) {
            result = function.apply(value);
        }

        return result;
    }

    public <T extends @Nullable Object, R extends @Nullable Object> R apply(
            final Supplier<? extends T> supplier,
            final Function<? super T, ? extends R> function
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(function, "function");

        return function.apply(supplier.get());
    }

    @SafeVarargs
    public <T extends @Nullable Object, R extends @Nullable Object> @Nullable R apply(
            final Supplier<? extends T> supplier,
            final Function<? super T, ? extends R>... functions
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(functions, "functions");

        val value = supplier.get();

        R result = null;
        for (val function : functions) {
            result = function.apply(value);
        }

        return result;
    }

    // ---------- BiFunctions ----------

    public <T extends @Nullable Object, U extends @Nullable Object, R extends @Nullable Object> R apply(
            final T first,
            final U second,
            final BiFunction<? super T, ? super U, ? extends R> function
    ) {
        Validates.require(function, "function");

        return function.apply(first, second);
    }

    // ---------- Consumers ----------

    @Contract("_, _ -> param1")
    public <T extends @Nullable Object> T accept(
            final T value,
            final Consumer<? super T> consumer
    ) {
        Validates.require(consumer, "consumer");

        consumer.accept(value);
        return value;
    }

    @SafeVarargs
    @Contract("_, _ -> param1")
    public <T extends @Nullable Object> T accept(
            final T value,
            final Consumer<? super T>... consumers
    ) {
        Validates.require(consumers, "consumers");

        for (val consumer : consumers) {
            consumer.accept(value);
        }

        return value;
    }

    public <T extends @Nullable Object> T accept(
            final Supplier<? extends T> supplier,
            final Consumer<? super T> consumer
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(consumer, "consumer");

        val value = supplier.get();
        consumer.accept(value);

        return value;
    }

    @SafeVarargs
    public <T extends @Nullable Object> T accept(
            final Supplier<? extends T> supplier,
            final Consumer<? super T>... consumers
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(consumers, "consumers");

        val value = supplier.get();

        for (val consumer : consumers) {
            consumer.accept(value);
        }

        return value;
    }

    // ---------- BiConsumers ----------

    public <T extends @Nullable Object, U extends @Nullable Object> void accept(
            final T first,
            final U second,
            final BiConsumer<? super T, ? super U> consumer
    ) {
        Validates.require(consumer, "consumer");

        consumer.accept(first, second);
    }

    // ---------- Predicates ----------

    public <T extends @Nullable Object> boolean test(
            final T value,
            final Predicate<? super T> predicate
    ) {
        Validates.require(predicate, "predicate");

        return predicate.test(value);
    }

    public <T extends @Nullable Object> boolean test(
            final Supplier<? extends T> supplier,
            final Predicate<? super T> predicate
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(predicate, "predicate");

        return predicate.test(supplier.get());
    }

    // ---------- Suppliers ----------

    public <T extends @Nullable Object> T get(
            final Supplier<? extends T> supplier
    ) {
        Validates.require(supplier, "supplier");

        return supplier.get();
    }

    public boolean get(
            final BooleanSupplier supplier
    ) {
        Validates.require(supplier, "supplier");

        return supplier.getAsBoolean();
    }

}
