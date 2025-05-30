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
import net.clydo.clytil.iface.XRunnable;
import net.clydo.clytil.iface.XSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

@UtilityClass
public class Throws {

    public <X extends Throwable> void ignore(
            @NotNull final XRunnable<X> runnable
    ) {
        Objects.requireNonNull(runnable, "runnable must not be null");

        try {
            runnable.run();
        } catch (Throwable ignored) {
        }
    }

    public <X extends Throwable> void ignoreOr(
            @NotNull final XRunnable<X> runnable,
            @NotNull final Runnable otherwise
    ) {
        Objects.requireNonNull(runnable, "runnable must not be null");

        try {
            runnable.run();
        } catch (Throwable ignored) {
            otherwise.run();
        }
    }

    public <T, X extends Throwable> T ignoreOr(
            @NotNull final XSupplier<T, X> supplier
    ) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        try {
            return supplier.get();
        } catch (Throwable ignored) {
            return null;
        }
    }

    public <T, X extends Throwable> T ignoreOr(
            @NotNull final XSupplier<T, X> supplier,
            final T otherwise
    ) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        try {
            return supplier.get();
        } catch (Throwable ignored) {
            return otherwise;
        }
    }

    public <T, X extends Throwable> T ignoreOr(
            @NotNull final XSupplier<T, X> supplier,
            @NotNull final Supplier<T> otherwise
    ) {
        Objects.requireNonNull(supplier, "supplier must not be null");

        try {
            return supplier.get();
        } catch (Throwable ignored) {
            return otherwise.get();
        }
    }

}
