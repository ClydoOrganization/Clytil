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

package net.clydo.clytil.tuple.pair;

import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public abstract class Pair<F, S> implements Map.Entry<F, S> {

    public abstract F first();

    public abstract S second();

    public final F key() {
        return this.first();
    }

    public final S value() {
        return this.second();
    }

    @Contract(pure = true)
    @Override
    public final @Nullable F getKey() {
        return this.first();
    }

    @Contract(pure = true)
    @Override
    public final @Nullable S getValue() {
        return this.second();
    }

    //

    public abstract Pair<F, S> first(final F first);

    public abstract Pair<F, S> second(final S second);

    public Pair<F, S> key(final F key) {
        return this.first(key);
    }

    public Pair<F, S> value(final S value) {
        return this.second(value);
    }

    public F setKey(F key) {
        val keyBefore = this.getKey();
        this.first(key);
        return keyBefore;
    }

    @Override
    public S setValue(S value) {
        val valueBefore = this.getValue();
        this.second(value);
        return valueBefore;
    }

    //

//    public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
//        return (Comparator<Map.Entry<K, V>> & Serializable)
//                (c1, c2) -> c1.getKey().compareTo(c2.getKey());
//    }
//
//    public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
//        return (Comparator<Map.Entry<K, V>> & Serializable)
//                (c1, c2) -> c1.getValue().compareTo(c2.getValue());
//    }
//
//    public static <K, V> Comparator<Pair<K, V>> comparingByKey(Comparator<? super K> cmp) {
//        Objects.requireNonNull(cmp);
//        return (Comparator<Map.Entry<K, V>> & Serializable)
//                (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
//    }
//
//    public static <K, V> Comparator<Pair<K, V>> comparingByValue(Comparator<? super V> cmp) {
//        Objects.requireNonNull(cmp);
//        return (Comparator<Map.Entry<K, V>> & Serializable)
//                (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Pair<?, ?> other) {
            return Objects.equals(this.first(), other.first()) && Objects.equals(this.second(), other.second());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.first()) ^ Objects.hashCode(this.second());
    }

    @Override
    public String toString() {
        return "(" + this.first() + ", " + this.second() + ")";
    }

    public String toString(final String format) {
        return String.format(format, this.first(), this.second());
    }

}
