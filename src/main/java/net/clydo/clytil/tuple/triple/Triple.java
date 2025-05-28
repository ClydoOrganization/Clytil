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

package net.clydo.clytil.tuple.triple;

import java.util.Objects;

public abstract class Triple<F, S, T> {

    public abstract F first();

    public abstract S second();

    public abstract T third();

    //

    public abstract Triple<F, S, T> first(final F first);

    public abstract Triple<F, S, T> second(final S second);

    public abstract Triple<F, S, T> third(final T third);

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

        if (obj instanceof Triple<?, ?, ?> other) {
            return Objects.equals(this.first(), other.first()) && Objects.equals(this.third(), other.third());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.first()) ^ Objects.hashCode(this.second()) ^ Objects.hashCode(this.third());
    }

    @Override
    public String toString() {
        return "(" + this.first() + ", " + this.second() + ", " + this.third() + ")";
    }

    public String toString(final String format) {
        return String.format(format, this.first(), this.second(), this.third());
    }

}
