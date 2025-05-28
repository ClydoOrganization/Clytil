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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class Option<T> {

    @NotNull
    public abstract <U> Option<U> cast();

//    @NotNull
//    public abstract <U> Option<U> cast(@NotNull Class<U> type);

    //

    public final boolean isNone() {
        return !this.isSome();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean isSome();

    //

    @NotNull
    public abstract Option<T> ifSome(@NotNull Consumer<? super @NotNull T> action);

    @NotNull
    public abstract Option<T> ifNone(@NotNull Runnable action);

    @NotNull
    public abstract Option<T> ifSomeOrElse(@NotNull Consumer<? super @NotNull T> action, @NotNull Runnable noneAction);

    @NotNull
    public abstract Option<T> ifNoneOrElse(@NotNull Runnable action, @NotNull Consumer<? super @NotNull T> someAction);

    //

    @NotNull
    public abstract Option<T> filter(@NotNull Predicate<? super @NotNull T> predicate);

    @NotNull
    public abstract <U> Option<U> map(@NotNull Function<? super @NotNull T, ? extends U> mapper);

    @NotNull
    public abstract <U> Option<U> flatMap(@NotNull Function<? super @NotNull T, ? extends Option<? extends U>> mapper);

    //

    @NotNull
    public abstract Option<T> or(@NotNull Option<? extends T> other);

    @NotNull
    public abstract Option<T> orFlat(@Nullable T other);

    @NotNull
    public abstract Option<T> orGet(@NotNull Supplier<? extends @NotNull Option<? extends T>> supplier);

    @NotNull
    public abstract Option<T> orGetFlat(@NotNull Supplier<? extends T> supplier);

    //

    public abstract T orElse(@NotNull Option<? extends T> other);

    public abstract T orElseFlat(@Nullable T other);

    public abstract T orElseGet(@NotNull Supplier<? extends @NotNull Option<? extends T>> supplier);

    public abstract T orElseGetFlat(@NotNull Supplier<? extends T> supplier);

    //

    @Nullable
    public abstract T orNull();

    @NotNull
    public abstract T orThrow();

    @NotNull
    public abstract <X extends Throwable> T orThrow(@NotNull Supplier<? extends @NotNull X> exceptionSupplier) throws X;

    @NotNull
    public abstract <X extends Throwable> T orThrowIf(@NotNull Predicate<? super @Nullable T> condition, @NotNull Supplier<? extends @NotNull X> exceptionSupplier) throws X;

    //

    @NotNull
    public abstract Option<T> flatten();

    public abstract boolean contains(@Nullable T value);

    //

    @NotNull
    public abstract Stream<T> stream();

    public java.util.Optional<T> toJava() {
        return java.util.Optional.ofNullable(this.orNull());
    }

    @Override
    public abstract boolean equals(@Nullable Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

}
