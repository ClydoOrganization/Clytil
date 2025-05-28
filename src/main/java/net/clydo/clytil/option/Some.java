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

import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

class Some<T> extends Option<T> {

    @Contract("_ -> new")
    public static <T> @NotNull Some<T> of(@NotNull T value) {
        return new Some<>(value);
    }

    //

    @NotNull
    private final T value;

    protected Some(@NotNull T value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> @NotNull Option<U> cast() {
        return (Option<U>) this;
    }

//    @Override
//    public <U> @NotNull Option<U> cast(@NotNull Class<U> type) {
//        Objects.requireNonNull(type, "type must not be null");
//
//        return type.isInstance(this.value)
//                ? Options.ofNullable(type.cast(this.value))
//                : Options.none();
//    }

    @Override
    public @NotNull Option<T> ifSome(@NotNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action must not be null");

        action.accept(this.value);

        return this;
    }

    @Override
    public @NotNull Option<T> ifNone(@NotNull Runnable action) {
        Objects.requireNonNull(action, "action must not be null");

        return this;
    }

    @Override
    public @NotNull Option<T> ifSomeOrElse(@NotNull Consumer<? super T> action, @NotNull Runnable noneAction) {
        return this.ifSome(action);
    }

    @Override
    public @NotNull Option<T> ifNoneOrElse(@NotNull Runnable action, @NotNull Consumer<? super T> someAction) {
        return this.ifSome(someAction);
    }

    @Override
    public @NotNull Option<T> filter(@NotNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate must not be null");

        return predicate.test(this.value) ? this : Options.none();
    }

    @Override
    public @NotNull <U> Option<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        return Options.of(mapper.apply(this.value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <U> Option<U> flatMap(@NotNull Function<? super T, ? extends Option<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        return Objects.requireNonNull((Option<U>) mapper.apply(this.value));
    }

    @Override
    public @NotNull Option<T> or(@NotNull Option<? extends T> other) {
        Objects.requireNonNull(other, "other must not be null");

        return this;
    }

    @Override
    public @NotNull Option<T> orFlat(@Nullable T other) {
        return this;
    }

    @Override
    public @NotNull Option<T> orGet(@NotNull Supplier<? extends @NotNull Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return this;
    }

    @Override
    public @NotNull Option<T> orGetFlat(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return this;
    }

    //

    @Override
    public T orElse(@NotNull Option<? extends T> other) {
        Objects.requireNonNull(other, "other must not be null");

        return this.value;
    }

    @Override
    public T orElseFlat(@Nullable T other) {
        return this.value;
    }

    @Override
    public T orElseGet(@NotNull Supplier<? extends @NotNull Option<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return this.value;
    }

    @Override
    public T orElseGetFlat(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        return this.value;
    }

    //

    @Override
    public @NotNull T orNull() {
        return this.value;
    }

    @Override
    public @NotNull T orThrow() {
        return this.value;
    }

    @Override
    public <X extends Throwable> @NotNull T orThrow(@NotNull Supplier<? extends X> exceptionSupplier) {
        return this.value;
    }

    @Override
    public <X extends Throwable> @NotNull T orThrowIf(@NotNull Predicate<? super @Nullable T> condition, @NotNull Supplier<? extends @NotNull X> exceptionSupplier) throws X {
        return this.value;
    }

    @Override
    public @NotNull Option<T> flatten() {
        val value = this.value;
        if (value instanceof Option<?> option) {
            return option.cast();
        }
        return this;
    }

    @Override
    public boolean contains(@Nullable final T value) {
        return this.value.equals(value);
    }

    @Override
    public @NotNull Stream<T> stream() {
        return Stream.of(this.value);
    }

    @Override
    public boolean isSome() {
        return true;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Some<?> other)) {
            return false;
        }

        return Objects.equals(
                this.value, other.value
        );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return "Option.some(" + this.value + ")";
    }

}
