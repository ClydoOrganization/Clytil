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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class Option<V> {

    private static final Option<?> NONE = new Option<>(null);

    public static <T> Option<T> none() {
        return (Option<T>) NONE;
    }

    @Contract("_ -> new")
    public static <T> @NotNull Option<T> some(
            @NotNull final T value
    ) {
        Objects.requireNonNull(value, "value must not be null");

        return new Option<>(value);
    }

    public static <T> Option<T> from(
            @Nullable final T value
    ) {
        return (value != null)
                ? Option.some(value)
                : Option.none();
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType"})
    public static <T> Option<T> fromJava(@Nullable java.util.Optional<T> optional) {
        Objects.requireNonNull(optional, "optional must not be null");

        return optional.map(Option::some).orElseGet(Option::none);
    }

    //

    private V value;

    Option(
            @Nullable final V value
    ) {
        this.value = value;
    }

    //

    public final boolean isNone() {
        return this.value == null;
    }

    public boolean isSome() {
        return this.value != null;
    }

    //

    @NotNull
    public Option<V> ifSome(@NotNull Consumer<? super @NotNull V> action) {
        Objects.requireNonNull(action, "action must not be null");

        if (this.isSome()) {
            action.accept(this.value);
        }

        return this;
    }

    @NotNull
    public Option<V> ifNone(@NotNull Runnable action) {
        Objects.requireNonNull(action, "action must not be null");

        if (this.isNone()) {
            action.run();
        }

        return this;
    }

    @NotNull
    public Option<V> ifSomeOrElse(@NotNull Consumer<? super @NotNull V> action, @NotNull Runnable noneAction) {
        if (this.isNone()) {
            return this.ifNone(noneAction);
        }

        return this.ifSome(action);
    }

    @NotNull
    public Option<V> ifNoneOrElse(@NotNull Runnable action, @NotNull Consumer<? super @NotNull V> someAction) {
        if (this.isNone()) {
            return this.ifNone(action);
        }

        return this.ifSome(someAction);
    }

    //

    @NotNull
    public Option<V> filter(@NotNull Predicate<? super @NotNull V> predicate) {
        Objects.requireNonNull(predicate, "predicate must not be null");

        if (this.isNone() || predicate.test(this.value)) {
            return this;
        }

        return Option.none();
    }

    @NotNull
    public <U> Option<U> map(@NotNull Function<? super @NotNull V, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        if (this.isNone()) {
            return Option.none();
        }

        return Option.from(mapper.apply(this.value));
    }

    @NotNull
    public <U> Option<U> flatMap(@NotNull Function<? super @NotNull V, ? extends Option<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");

        if (this.isNone()) {
            return Option.none();
        }

        return Objects.requireNonNull((Option<U>) mapper.apply(this.value));
    }

    //

    @NotNull
    public Option<V> or(@NotNull Option<? extends V> other) {
        Objects.requireNonNull(other, "other must not be null");

        if (this.isNone()) {
            return (Option<V>) other;
        }

        return this;
    }

    @NotNull
    public Option<V> orFlat(@Nullable V other) {
        if (this.isSome()) {
            return Option.from(other);
        }

        return this;
    }

    @NotNull
    public Option<V> orGet(@NotNull Supplier<? extends @NotNull Option<? extends V>> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        if (this.isNone()) {
            return (Option<V>) supplier.get();
        }

        return this;
    }

    @NotNull
    public Option<V> orGetFlat(@NotNull Supplier<? extends V> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        if (this.isNone()) {
            return Option.from(supplier.get());
        }

        return this;
    }

    //

    public V orElse(@NotNull Option<? extends V> other) {
        Objects.requireNonNull(other, "other must not be null");

        if (this.isNone()) {
            return other.orNull();
        }

        return this.value;
    }

    public V orElseFlat(@Nullable V other) {
        if (this.isNone()) {
            return other;
        }

        return this.value;
    }

    public V orElseGet(@NotNull Supplier<? extends @NotNull Option<? extends V>> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        if (this.isNone()) {
            return this.orElse(supplier.get());
        }

        return this.value;
    }

    public V orElseGetFlat(@NotNull Supplier<? extends V> supplier) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        if (this.isNone()) {
            return supplier.get();
        }

        return this.value;
    }

    //

    @Nullable
    public V orNull() {
        return this.value;
    }

    @NotNull
    public V get() {
        return this.orThrow();
    }

    @NotNull
    public V orThrow() {
        if (this.isNone()) {
            throw new NoSuchElementException("None has no value");
        }

        return this.value;
    }

    @NotNull
    public <X extends Throwable> V orThrow(@NotNull Supplier<? extends @NotNull X> exceptionSupplier) throws X {
        if (this.isNone()) {
            throw exceptionSupplier.get();
        }

        return this.value;
    }

    @NotNull
    public <X extends Throwable> V orThrowIf(@NotNull Predicate<? super @Nullable V> condition, @NotNull Supplier<? extends @NotNull X> exceptionSupplier) throws X {
        if (this.isNone()) {
            throw exceptionSupplier.get();
        }

        return this.value;
    }

    //

    public void set(@Nullable final V value) {
        this.value = value;
    }

    @Nullable
    public V replace(@Nullable final V newValue) {
        val oldValue = this.value;
        this.value = newValue;
        return oldValue;
    }

    @NotNull
    public Option<V> flatten() {
        if (this.isSome()) {
            if (this.value instanceof Option<?> option) {
                return (Option<V>) option;
            }
        }

        return this;
    }

    public boolean contains(@Nullable V value) {
        return Objects.equals(this.value, value);
    }

    //

    @NotNull
    public Stream<V> stream() {
        if (this.isNone()) {
            return Stream.empty();
        }

        return Stream.of(this.value);
    }

    public java.util.Optional<V> toJava() {
        return java.util.Optional.ofNullable(this.value);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Option<?> other)) {
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
        if (this.isNone()) {
            return "Option.none()";
        }

        return "Option.some(" + this.value + ")";
    }

}
