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

import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class Array<T> {

    private static final Object[] EMPTY_ARRAY = new Object[0];

    @NotNull
    private T[] array;

    private Array(final @NotNull T[] array) {
        this.array = Objects.requireNonNull(array, "array must not be null");
    }

    public void set(final int index, final T element) {
        this.array[index] = element;
    }

    public T get(final int index) {
        return this.array[index];
    }

    @NotNull
    public T[] set(final T[] array) {
        Objects.requireNonNull(array, "array must not be null");

        val previous = this.array;
        this.array = array;
        return previous;
    }

    public Array<T> map(final Function<T, T> mapper) {
        Objects.requireNonNull(mapper, "mapper must not be null");
        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = mapper.apply(this.array[i]);
        }
        return this;
    }

    public Array<T> fill(final T element) {
        Arrays.fill(this.array, element);
        return this;
    }

    public T[] get() {
        return this.array;
    }

    public T first() {
        return this.array[0];
    }

    public int size() {
        return this.array.length;
    }

    public boolean isEmpty() {
        return this.array.length == 0;
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <T> @NotNull Array<T> of(final T... array) {
        return new Array<>(array);
    }

    public static <T> @NotNull Array<T> nul(final int size) {
        return new Array<>(Array.newArray(size));
    }

    public static <T> @NotNull Array<T> fill(final T element, final int size) {
        return Array.<T>nul(size).fill(element);
    }

    @Contract(value = "_ -> new", pure = true)
    @SuppressWarnings("unchecked")
    public static <T> T @NotNull [] newArray(int size) {
        return (T[]) new Object[size];
    }

    public static <T> @NotNull Array<T> empty() {
        return Array.nul(0);
    }

}
