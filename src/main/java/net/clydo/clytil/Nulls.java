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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class Nulls {

    // === Basic Null Checks ===
    @Contract(value = "null -> true; !null -> false", pure = true)
    public static boolean isNull(
            @Nullable final Object obj
    ) {
        return obj == null;
    }

    @Contract(value = "null -> false; !null -> true", pure = true)
    public static boolean isNotNull(
            @Nullable final Object obj
    ) {
        return obj != null;
    }

    @Contract(value = "null -> true; !null -> false", pure = true)
    public static boolean nul(
            @Nullable final Object obj
    ) {
        return obj == null;
    }

    @Contract(value = "null -> false; !null -> true", pure = true)
    public static boolean non(
            @Nullable final Object obj
    ) {
        return obj != null;
    }

    // === Defaulting ===
    @Contract(value = "!null, _ -> param1; null, _ -> param2", pure = true)
    public static <T> T or(
            @Nullable final T value,
            @Nullable final T fallback
    ) {
        return value != null ? value : fallback;
    }

    @Contract("!null, _ -> param1")
    public static <T> T orGet(
            @Nullable final T value,
            @NotNull final Supplier<? extends T> fallbackSupplier
    ) {
        return value != null ? value : fallbackSupplier.get();
    }

    // === First Non-null ===
    @Contract(value = "!null, _ -> param1; null, _ -> param2", pure = true)
    public static <T> T firstNonNull(
            @Nullable final T a,
            @Nullable final T b
    ) {
        return a == null ? b : a;
    }

    @Contract(value = "!null, _, _ -> param1", pure = true)
    public static <T> T firstNonNull(
            @Nullable final T a,
            @Nullable final T b,
            @Nullable final T c
    ) {
        return a == null ? (b == null ? c : b) : a;
    }

    @Contract(value = "!null, _, _, _ -> param1", pure = true)
    public static <T> T firstNonNull(
            @Nullable final T a,
            @Nullable final T b,
            @Nullable final T c,
            @Nullable final T d
    ) {
        return a == null ? (b == null ? (c == null ? d : c) : b) : a;
    }

    @SafeVarargs
    @Contract(pure = true)
    public static <T> @Nullable T firstNonNull(
            final T @NotNull ... values
    ) {
        for (T val : values) {
            if (val != null) return val;
        }
        return null;
    }

    // === Mapping ===
    @Contract("null, _ -> null")
    public static <T, R> @Nullable R map(
            @Nullable final T t,
            @NotNull final Function<T, R> function
    ) {
        return t == null ? null : function.apply(t);
    }

    @Contract("null, _, _ -> param3")
    public static <T, R> R mapOrDefault(
            @Nullable final T t,
            @NotNull final Function<T, R> function,
            @Nullable final R r
    ) {
        return t == null ? r : function.apply(t);
    }

    public static <T, R> R mapOrElse(
            @Nullable final T t,
            @NotNull final Function<T, R> function,
            @NotNull final Supplier<R> supplier
    ) {
        return t == null ? supplier.get() : function.apply(t);
    }

    // === Collections ===
    public static <T> @Nullable T first(
            @NotNull final Collection<T> collection
    ) {
        val iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public static <T> T firstOrDefault(
            @NotNull final Collection<T> collection,
            @Nullable final T t
    ) {
        val iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : t;
    }

    public static <T> T firstOrElse(
            @NotNull final Collection<T> collection,
            @NotNull final Supplier<T> supplier
    ) {
        val iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : supplier.get();
    }

    // === Arrays ===
    @Contract(value = "null -> true", pure = true)
    public static <T> boolean isNullOrEmpty(
            final T @Nullable [] ts
    ) {
        return ts == null || ts.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final boolean @Nullable [] booleans
    ) {
        return booleans == null || booleans.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final byte @Nullable [] bytes
    ) {
        return bytes == null || bytes.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final char @Nullable [] chars
    ) {
        return chars == null || chars.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final short @Nullable [] shorts
    ) {
        return shorts == null || shorts.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final int @Nullable [] ints
    ) {
        return ints == null || ints.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final long @Nullable [] longs
    ) {
        return longs == null || longs.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final float @Nullable [] floats
    ) {
        return floats == null || floats.length == 0;
    }

    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(
            final double @Nullable [] doubles
    ) {
        return doubles == null || doubles.length == 0;
    }

}
