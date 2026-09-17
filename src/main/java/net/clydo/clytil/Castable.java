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

package net.clydo.clytil;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.*;

/**
 * Utility interface providing unchecked casting helpers.
 * <p>
 * Implementing this interface grants an instance method for casting
 * {@code this} to an arbitrary type, while the static methods provide
 * general-purpose casting utilities for values and the common functional
 * interfaces in {@code java.util.function} and {@code java.util.concurrent}.
 * <p>
 * All casts performed by this interface are unchecked and unsafe unless
 * otherwise noted (see {@link #cast(Object, Class)} for a checked
 * alternative). Misuse will result in a {@link ClassCastException} at
 * the point of use rather than at the point of casting.
 */
public interface Castable {

    /**
     * Casts {@code this} to the inferred target type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param <T> the inferred target type
     * @return this instance, cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    default <T> @NotNull T cast() {
        return (T) this;
    }

    /**
     * Casts the given value to the inferred target type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param value the value to cast, may be {@code null}
     * @param <T>   the inferred target type
     * @return the value, cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> T cast(
            @Nullable final Object value
    ) {
        return (T) value;
    }

    /**
     * Casts the given value to the specified target type, verifying the
     * cast at runtime.
     *
     * @param value the value to cast, may be {@code null}
     * @param type  the target type
     * @param <T>   the target type
     * @return the value, cast to {@code T}
     * @throws ClassCastException if {@code value} is not {@code null} and
     *                            is not an instance of {@code type}
     */
    @Contract(value = "_, _ -> param1", pure = true)
    static <T> T cast(
            @Nullable final Object value,
            @NotNull final Class<T> type
    ) {
        return type.cast(value);
    }

    /**
     * Casts the given function's return type to the inferred target type
     * {@code R}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param function the function whose return type should be cast
     * @param <T>      the function's input type
     * @param <R>      the inferred target return type
     * @return the function, with its return type cast to {@code R}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T, R> @NotNull Function<T, R> function(
            @NotNull final Function<T, ?> function
    ) {
        return (Function<T, R>) function;
    }

    /**
     * Casts the given bi-function's return type to the inferred target
     * type {@code R}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param function the bi-function whose return type should be cast
     * @param <T>      the bi-function's first input type
     * @param <U>      the bi-function's second input type
     * @param <R>      the inferred target return type
     * @return the bi-function, with its return type cast to {@code R}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T, U, R> @NotNull BiFunction<T, U, R> biFunction(
            @NotNull final BiFunction<T, U, ?> function
    ) {
        return (BiFunction<T, U, R>) function;
    }

    /**
     * Casts the given supplier's produced type to the inferred target
     * type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param supplier the supplier whose produced type should be cast
     * @param <T>      the inferred target produced type
     * @return the supplier, with its produced type cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> @NotNull Supplier<T> supplier(
            @NotNull final Supplier<?> supplier
    ) {
        return (Supplier<T>) supplier;
    }

    /**
     * Casts the given consumer's accepted type to the inferred target
     * type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     * Note that {@link Consumer} is contravariant in {@code T}, so this
     * cast is only safe if every value actually passed to the consumer
     * at runtime is compatible with its original accepted type.
     *
     * @param consumer the consumer whose accepted type should be cast
     * @param <T>      the inferred target accepted type
     * @return the consumer, with its accepted type cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> @NotNull Consumer<T> consumer(
            @NotNull final Consumer<?> consumer
    ) {
        return (Consumer<T>) consumer;
    }

    /**
     * Casts the given bi-consumer's accepted types to the inferred target
     * types {@code T} and {@code U}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     * Note that {@link BiConsumer} is contravariant in {@code T} and
     * {@code U}, so this cast is only safe if every value actually passed
     * to the bi-consumer at runtime is compatible with its original
     * accepted types.
     *
     * @param consumer the bi-consumer whose accepted types should be cast
     * @param <T>      the inferred target first accepted type
     * @param <U>      the inferred target second accepted type
     * @return the bi-consumer, with its accepted types cast to
     * {@code T} and {@code U}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T, U> @NotNull BiConsumer<T, U> biConsumer(
            @NotNull final BiConsumer<?, ?> consumer
    ) {
        return (BiConsumer<T, U>) consumer;
    }

    /**
     * Casts the given predicate's tested type to the inferred target
     * type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     * Note that {@link Predicate} is contravariant in {@code T}, so this
     * cast is only safe if every value actually tested by the predicate
     * at runtime is compatible with its original tested type.
     *
     * @param predicate the predicate whose tested type should be cast
     * @param <T>       the inferred target tested type
     * @return the predicate, with its tested type cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> @NotNull Predicate<T> predicate(
            @NotNull final Predicate<?> predicate
    ) {
        return (Predicate<T>) predicate;
    }

    /**
     * Casts the given bi-predicate's tested types to the inferred target
     * types {@code T} and {@code U}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     * Note that {@link BiPredicate} is contravariant in {@code T} and
     * {@code U}, so this cast is only safe if every value actually tested
     * by the bi-predicate at runtime is compatible with its original
     * tested types.
     *
     * @param predicate the bi-predicate whose tested types should be cast
     * @param <T>       the inferred target first tested type
     * @param <U>       the inferred target second tested type
     * @return the bi-predicate, with its tested types cast to
     * {@code T} and {@code U}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T, U> @NotNull BiPredicate<T, U> biPredicate(
            @NotNull final BiPredicate<?, ?> predicate
    ) {
        return (BiPredicate<T, U>) predicate;
    }

    /**
     * Casts the given unary operator's operand/result type to the
     * inferred target type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param operator the unary operator whose type should be cast
     * @param <T>      the inferred target type
     * @return the unary operator, with its type cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> @NotNull UnaryOperator<T> unaryOperator(
            @NotNull final UnaryOperator<?> operator
    ) {
        return (UnaryOperator<T>) operator;
    }

    /**
     * Casts the given binary operator's operand/result type to the
     * inferred target type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param operator the binary operator whose type should be cast
     * @param <T>      the inferred target type
     * @return the binary operator, with its type cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> @NotNull BinaryOperator<T> binaryOperator(
            @NotNull final BinaryOperator<?> operator
    ) {
        return (BinaryOperator<T>) operator;
    }

    /**
     * Casts the given callable's produced type to the inferred target
     * type {@code V}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     *
     * @param callable the callable whose produced type should be cast
     * @param <V>      the inferred target produced type
     * @return the callable, with its produced type cast to {@code V}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <V> @NotNull Callable<V> callable(
            @NotNull final Callable<?> callable
    ) {
        return (Callable<V>) callable;
    }

    /**
     * Casts the given comparator's compared type to the inferred target
     * type {@code T}.
     * <p>
     * This is an unchecked cast; no runtime type verification occurs.
     * Note that {@link Comparator} is contravariant in {@code T}, so this
     * cast is only safe if every value actually compared at runtime is
     * compatible with the comparator's original compared type.
     *
     * @param comparator the comparator whose compared type should be cast
     * @param <T>        the inferred target compared type
     * @return the comparator, with its compared type cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> @NotNull Comparator<T> comparator(
            @NotNull final Comparator<?> comparator
    ) {
        return (Comparator<T>) comparator;
    }

}