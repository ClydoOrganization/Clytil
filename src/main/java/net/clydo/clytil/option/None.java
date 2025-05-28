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

package net.clydo.clytil.option;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class None<T> extends Option<T> {

    private static final None<?> NONE = new None<>();

    protected static <T> @NotNull None<T> of() {
        return NONE.cast();
    }

    //

    protected None() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> @NotNull None<U> cast() {
        return (None<U>) NONE;
    }

//    @Override
//    public <U> @NotNull Option<U> cast(@NotNull Class<U> type) {
//        Objects.requireNonNull(type, "type must not be null");
//
//        return Options.none();
//    }

    @Override
    public @NotNull Option<T> ifSome(@NotNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action must not be null");

        return this;
    }

    @Override
    public @NotNull Option<T> ifNone(@NotNull Runnable action) {
        Objects.requireNonNull(action, "action must not be null");

        action.run();

        return this;
    }

    @Override
    public @NotNull Option<T> ifSomeOrElse(@NotNull Consumer<? super T> action, @NotNull Runnable noneAction) {
        return this.ifNone(noneAction);
    }

    @Override
    public @NotNull Option<T> ifNoneOrElse(@NotNull Runnable action, @NotNull Consumer<? super T> someAction) {
        return this.ifNone(action);
    }

    @Override
    public @NotNull Option<T> filter(@NotNull Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public @NotNull <U> Option<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        return Options.none();
    }

    @Override
    public @NotNull <U> Option<U> flatMap(@NotNull Function<? super T, ? extends Option<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        return Options.none();
    }

    @Override
    public @NotNull Option<T> or(@NotNull Option<? extends T> other) {
        Objects.requireNonNull(other, "other must not be null");

        return other.cast();
    }

    @Override
    public @NotNull Option<T> orFlat(@Nullable T other) {
        return Options.of(other);
    }

    @Override
    public @NotNull Option<T> orGet(@NotNull Supplier<? extends @NotNull Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return Options.castOrThrow(supplier.get());
    }

    @Override
    public @NotNull Option<T> orGetFlat(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return Options.castOrThrow(
                Options.of(supplier.get())
        );
    }

    //

    @Override
    public T orElse(@NotNull Option<? extends T> other) {
        Objects.requireNonNull(other, "other must not be null");

        return other.orNull();
    }

    @Override
    public T orElseFlat(@Nullable T other) {
        return other;
    }

    @Override
    public T orElseGet(@NotNull Supplier<? extends @NotNull Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return this.orElse(supplier.get());
    }

    @Override
    public T orElseGetFlat(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return supplier.get();
    }

    //

    @Override
    public @Nullable T orNull() {
        return null;
    }

    @Override
    public @NotNull T orThrow() {
        throw new NoSuchElementException("None has no value");
    }

    @Override
    public <X extends Throwable> @NotNull T orThrow(@NotNull Supplier<? extends X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    @Override
    public <X extends Throwable> @NotNull T orThrowIf(@NotNull Predicate<? super @Nullable T> condition, @NotNull Supplier<? extends @NotNull X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    @Override
    public @NotNull Option<T> flatten() {
        return this;
    }

    @Override
    public boolean contains(@Nullable final T value) {
        return value == null;
    }

    @Override
    public @NotNull Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public boolean isSome() {
        return false;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(null);
    }

    @Override
    public String toString() {
        return "Option.none()";
    }

}
