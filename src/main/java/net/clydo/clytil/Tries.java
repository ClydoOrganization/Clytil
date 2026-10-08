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
import net.clydo.clytil.iface.*;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Runs code that may throw and swallows the failure, falling back to a default.
 * <p>
 * Nullability follows the arguments: {@code getOr(supplier, "x")} is non-null when the supplier
 * is, while {@link #get} and {@link #apply} are always nullable since failure yields {@code null}.
 * <p>
 * Fatal {@link VirtualMachineError}s (out of memory, stack overflow) are rethrown, and an
 * {@link InterruptedException} restores the thread's interrupt flag before being swallowed.
 */
@UtilityClass
@NotNullByDefault
public class Tries {

    // ---------- Runnables ----------

    /**
     * Runs {@code runnable}, ignoring any failure.
     *
     * @return {@code true} if it completed, {@code false} if it threw
     */
    public <X extends Throwable> boolean run(
            final XRunnable<X> runnable
    ) {
        Validates.require(runnable, "runnable");

        try {
            runnable.run();
            return true;
        } catch (Throwable throwable) {
            swallow(throwable);
            return false;
        }
    }

    /**
     * Runs {@code runnable}, running {@code otherwise} instead if it throws.
     *
     * @return {@code true} if {@code runnable} completed, {@code false} if it threw
     */
    public <X extends Throwable> boolean runOr(
            final XRunnable<X> runnable,
            final Runnable otherwise
    ) {
        Validates.require(runnable, "runnable");
        Validates.require(otherwise, "otherwise");

        try {
            runnable.run();
            return true;
        } catch (Throwable throwable) {
            swallow(throwable);
            otherwise.run();
            return false;
        }
    }

    // ---------- Suppliers ----------

    /**
     * Returns {@code supplier}'s value, or {@code null} if it throws.
     */
    public <T extends @Nullable Object, X extends Throwable> @Nullable T get(
            final XSupplier<? extends T, X> supplier
    ) {
        Validates.require(supplier, "supplier");

        try {
            return supplier.get();
        } catch (Throwable throwable) {
            swallow(throwable);
            return null;
        }
    }

    /**
     * Returns {@code supplier}'s value, or {@code fallback} if it throws.
     */
    public <T extends @Nullable Object, X extends Throwable> T getOr(
            final XSupplier<? extends T, X> supplier,
            final T fallback
    ) {
        Validates.require(supplier, "supplier");

        try {
            return supplier.get();
        } catch (Throwable throwable) {
            swallow(throwable);
            return fallback;
        }
    }

    /**
     * Returns {@code supplier}'s value, or the value of {@code fallback} if it throws. The fallback
     * is only called on failure.
     */
    public <T extends @Nullable Object, X extends Throwable> T getOrElse(
            final XSupplier<? extends T, X> supplier,
            final Supplier<? extends T> fallback
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(fallback, "fallback");

        try {
            return supplier.get();
        } catch (Throwable throwable) {
            swallow(throwable);
            return fallback.get();
        }
    }

    /**
     * Returns {@code supplier}'s value, or {@code recovery} applied to what it threw.
     */
    public <T extends @Nullable Object, X extends Throwable> T recover(
            final XSupplier<? extends T, X> supplier,
            final Function<? super Throwable, ? extends T> recovery
    ) {
        Validates.require(supplier, "supplier");
        Validates.require(recovery, "recovery");

        try {
            return supplier.get();
        } catch (Throwable throwable) {
            swallow(throwable);
            return recovery.apply(throwable);
        }
    }

    /**
     * Returns {@code supplier}'s value as an {@link Optional}, empty if it is {@code null} or throws.
     */
    public <T, X extends Throwable> Optional<T> optional(
            final XSupplier<? extends @Nullable T, X> supplier
    ) {
        Validates.require(supplier, "supplier");

        try {
            return Optional.ofNullable(supplier.get());
        } catch (Throwable throwable) {
            swallow(throwable);
            return Optional.empty();
        }
    }

    // ---------- Functions ----------

    /**
     * Returns {@code function} applied to {@code value}, or {@code null} if it throws.
     */
    public <T extends @Nullable Object, R extends @Nullable Object, X extends Throwable> @Nullable R apply(
            final T value,
            final XFunction<? super T, ? extends R, X> function
    ) {
        Validates.require(function, "function");

        try {
            return function.apply(value);
        } catch (Throwable throwable) {
            swallow(throwable);
            return null;
        }
    }

    /**
     * Returns {@code function} applied to {@code value}, or {@code fallback} if it throws.
     */
    public <T extends @Nullable Object, R extends @Nullable Object, X extends Throwable> R applyOr(
            final T value,
            final XFunction<? super T, ? extends R, X> function,
            final R fallback
    ) {
        Validates.require(function, "function");

        try {
            return function.apply(value);
        } catch (Throwable throwable) {
            swallow(throwable);
            return fallback;
        }
    }

    // ---------- Consumers ----------

    /**
     * Passes {@code value} to {@code consumer}, ignoring any failure.
     *
     * @return {@code true} if it completed, {@code false} if it threw
     */
    public <T extends @Nullable Object, X extends Throwable> boolean accept(
            final T value,
            final XConsumer<? super T, X> consumer
    ) {
        Validates.require(consumer, "consumer");

        try {
            consumer.accept(value);
            return true;
        } catch (Throwable throwable) {
            swallow(throwable);
            return false;
        }
    }

    // ---------- Predicates ----------

    /**
     * Tests {@code value} with {@code predicate}, treating a failure as {@code false}.
     */
    public <T extends @Nullable Object, X extends Throwable> boolean test(
            final T value,
            final XPredicate<? super T, X> predicate
    ) {
        Validates.require(predicate, "predicate");

        try {
            return predicate.test(value);
        } catch (Throwable throwable) {
            swallow(throwable);
            return false;
        }
    }

    /**
     * Rethrows {@code throwable} if it is a fatal {@link VirtualMachineError}, and restores the
     * thread's interrupt flag if it is an {@link InterruptedException}.
     */
    private void swallow(
            final Throwable throwable
    ) {
        if (throwable instanceof VirtualMachineError error) {
            throw error;
        }

        if (throwable instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }

}
