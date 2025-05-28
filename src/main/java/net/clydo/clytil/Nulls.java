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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class Nulls {

    // === Basic Null Checks ===
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean nul(Object obj) {
        return obj == null;
    }

    public static boolean non(Object obj) {
        return obj != null;
    }

    // === Defaulting ===
    public static <T> T or(T value, T fallback) {
        return value != null ? value : fallback;
    }

    public static <T> T orGet(T value, Supplier<? extends T> fallbackSupplier) {
        return value != null ? value : fallbackSupplier.get();
    }

    // === First Non-null ===
    public static <T> T firstNonNull(T a, T b) {
        return a == null ? b : a;
    }

    public static <T> T firstNonNull(T a, T b, T c) {
        return a == null ? (b == null ? c : b) : a;
    }

    public static <T> T firstNonNull(T a, T b, T c, T d) {
        return a == null ? (b == null ? (c == null ? d : c) : b) : a;
    }

    @SafeVarargs
    public static <T> T firstNonNull(T... values) {
        for (T val : values) {
            if (val != null) return val;
        }
        return null;
    }

    // === ... ===

    @Nullable
    public <T, R> R map(@Nullable T t, Function<T, R> function) {
        return t == null ? null : function.apply(t);
    }

    public <T, R> R mapOrDefault(@Nullable T t, Function<T, R> function, R r) {
        return t == null ? r : function.apply(t);
    }

    public <T, R> R mapOrElse(@Nullable T t, Function<T, R> function, Supplier<R> supplier) {
        return t == null ? supplier.get() : function.apply(t);
    }

    @Nullable
    public <T> T first(@NotNull Collection<T> collection) {
        val iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public <T> T firstOrDefault(@NotNull Collection<T> collection, T t) {
        val iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : t;
    }

    public <T> T firstOrElse(@NotNull Collection<T> collection, Supplier<T> supplier) {
        val iterator = collection.iterator();
        return iterator.hasNext() ? iterator.next() : supplier.get();
    }

    public <T> boolean isNullOrEmpty(@Nullable T[] ts) {
        return ts == null || ts.length == 0;
    }

    public boolean isNullOrEmpty(boolean @Nullable [] booleans) {
        return booleans == null || booleans.length == 0;
    }

    public boolean isNullOrEmpty(byte @Nullable [] bytes) {
        return bytes == null || bytes.length == 0;
    }

    public boolean isNullOrEmpty(char @Nullable [] chars) {
        return chars == null || chars.length == 0;
    }

    public boolean isNullOrEmpty(short @Nullable [] shorts) {
        return shorts == null || shorts.length == 0;
    }

    public boolean isNullOrEmpty(int @Nullable [] ints) {
        return ints == null || ints.length == 0;
    }

    public boolean isNullOrEmpty(long @Nullable [] longs) {
        return longs == null || longs.length == 0;
    }

    public boolean isNullOrEmpty(float @Nullable [] floats) {
        return floats == null || floats.length == 0;
    }

    public boolean isNullOrEmpty(double @Nullable [] doubles) {
        return doubles == null || doubles.length == 0;
    }

}
