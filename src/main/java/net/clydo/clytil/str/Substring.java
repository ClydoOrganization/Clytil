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

package net.clydo.clytil.str;

import lombok.Value;
import lombok.experimental.Accessors;
import lombok.val;
import net.clydo.clytil.FastMaths;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Accessors(fluent = true)
@Value(staticConstructor = "of")
public class Substring {

    public static final Substring EMPTY = Substring.of(0, 0);

    int beginIndex;
    int endIndex;

    @Contract(pure = true)
    public @NotNull @Unmodifiable String slice(
            @NotNull final String origin
    ) {
        return origin.substring(this.beginIndex, this.endIndex);
    }

    @Contract(pure = true)
    public @NotNull @Unmodifiable String sliceTo(
            @NotNull final String origin,
            final int endIndex
    ) {
        return origin.substring(this.beginIndex, endIndex);
    }

    @Contract(pure = true)
    public @NotNull @Unmodifiable CharSequence slice(
            @NotNull final CharSequence origin
    ) {
        return origin.subSequence(this.beginIndex, this.endIndex);
    }

    @Contract(pure = true)
    public @NotNull @Unmodifiable CharSequence sliceTo(
            @NotNull final CharSequence origin,
            final int endIndex
    ) {
        return origin.subSequence(this.beginIndex, endIndex);
    }

    public String replace(
            @NotNull final String origin,
            @NotNull final String replacement
    ) {
        return new StringBuilder(origin)
                .replace(this.beginIndex, this.endIndex, replacement)
                .toString();
    }

    public int beginIndex(
            @NotNull final Substring range
    ) {
        return FastMaths.clamp(this.beginIndex(), range.beginIndex(), range.endIndex());
    }

    public int endIndex(
            @NotNull final Substring range
    ) {
        return FastMaths.clamp(this.endIndex(), range.beginIndex(), range.endIndex());
    }

    public int length() {
        return this.endIndex - this.beginIndex;
    }

    public static Substring full(
            @NotNull final String origin
    ) {
        return Substring.of(0, origin.length());
    }

    public static @NotNull @Unmodifiable List<Substring> of(
            @NotNull final List<String> parts
    ) {
        val result = new ArrayList<Substring>();

        var beginIndex = 0;
        for (val part : parts) {
            val length = part.length();
            val endIndex = beginIndex + length;
            result.add(Substring.of(beginIndex, endIndex));
            beginIndex = endIndex;
        }

        return result;
    }

    public static @NotNull @Unmodifiable List<Substring> visit(
            @NotNull final List<String> parts,
            @NotNull final Consumer<Substring> consumer
    ) {
        val result = new ArrayList<Substring>();

        var beginIndex = 0;
        for (val part : parts) {
            val length = part.length();
            val endIndex = beginIndex + length;
            consumer.accept(Substring.of(beginIndex, endIndex));
            beginIndex = endIndex;
        }

        return result;
    }

}
