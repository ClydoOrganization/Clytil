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
import net.clydo.clytil.iface.XRunnable;
import net.clydo.clytil.iface.XSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@UtilityClass
public class Throws {

    /**
     * Returns {@code throwable} as an unchecked exception, for {@code throw Throws.unchecked(e)}:
     * a {@link RuntimeException} is returned as-is, an {@link Error} is thrown directly, and any
     * other exception is wrapped in a {@link RuntimeException}.
     */
    public @NotNull RuntimeException unchecked(
            @NotNull final Throwable throwable
    ) {
        Validates.require(throwable, "throwable");

        if (throwable instanceof RuntimeException runtime) {
            return runtime;
        }

        if (throwable instanceof Error error) {
            throw error;
        }

        return new RuntimeException(throwable);
    }

    /**
     * Returns the innermost cause of {@code throwable}, or {@code throwable} itself if it has none.
     * Stops safely at a cause cycle.
     */
    public @NotNull Throwable rootCause(
            @NotNull final Throwable throwable
    ) {
        Validates.require(throwable, "throwable");

        // Cycle check without allocating: a second pointer follows at half speed and meets the
        // first one only if the chain loops back on itself.
        Throwable current = throwable;
        Throwable trailing = throwable;
        boolean advanceTrailing = false;
        while (current.getCause() != null) {
            current = current.getCause();
            if (current == trailing) {
                break;
            }

            if (advanceTrailing) {
                trailing = trailing.getCause();
            }
            advanceTrailing = !advanceTrailing;
        }

        return current;
    }

    /**
     * Describes {@code throwable} in one line for users: its root cause's message, or the root
     * cause's {@code toString()} when it has no message.
     */
    public @NotNull String describe(
            @NotNull final Throwable throwable
    ) {
        val root = rootCause(throwable);
        val message = root.getMessage();
        return message != null ? message : root.toString();
    }

    public <X extends Throwable> void ignore(
            @NotNull final XRunnable<X> runnable
    ) {
        Validates.require(runnable, "runnable");

        try {
            runnable.run();
        } catch (Throwable ignored) {
        }
    }

    public <X extends Throwable> void ignoreOr(
            @NotNull final XRunnable<X> runnable,
            @NotNull final Runnable otherwise
    ) {
        Validates.require(runnable, "runnable");

        try {
            runnable.run();
        } catch (Throwable ignored) {
            otherwise.run();
        }
    }

    public <T, X extends Throwable> T ignoreOr(
            @NotNull final XSupplier<T, X> supplier
    ) {
        Validates.require(supplier, "supplier");

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
        Validates.require(supplier, "supplier");

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
        Validates.require(supplier, "supplier");

        try {
            return supplier.get();
        } catch (Throwable ignored) {
            return otherwise.get();
        }
    }

}
