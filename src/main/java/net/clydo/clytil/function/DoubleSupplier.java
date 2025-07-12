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

package net.clydo.clytil.function;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface DoubleSupplier extends java.util.function.DoubleSupplier {

    double get();

    /**
     * @deprecated This method is deprecated and will be removed in a future release.
     * Use {@link #get()} instead.
     */
    @Deprecated(forRemoval = true)
    @Override
    default double getAsDouble() {
        return this.get();
    }

    @Contract(pure = true)
    static @NotNull DoubleSupplier of(
            @NotNull final Supplier<Double> supplier
    ) {
        return supplier::get;
    }

    @Contract(pure = true)
    static @NotNull DoubleSupplier ofConst(final double value) {
        return () -> value;
    }

}
