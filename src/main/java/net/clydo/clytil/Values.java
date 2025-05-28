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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class Values {

    public <T> T make(@NotNull Supplier<T> supplier) {
        return supplier.get();
    }

    public <T> T make(T t, @NotNull Consumer<? super T> consumer) {
        consumer.accept(t);
        return t;
    }

    public <T> T make(T t, @NotNull Function<? super T, ? extends T> function) {
        return function.apply(t);
    }

    public <T, R> R make(@NotNull Supplier<T> supplier, @NotNull Function<? super T, ? extends R> function) {
        val t = supplier.get();
        return function.apply(t);
    }

    public <T> T make(@NotNull Supplier<T> supplier, @NotNull Consumer<? super T> consumer) {
        val t = supplier.get();
        consumer.accept(t);
        return t;
    }

}
