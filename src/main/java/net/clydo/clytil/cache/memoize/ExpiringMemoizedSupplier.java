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
 * Copyright (C) 2026 ClydoNetwork
 */

package net.clydo.clytil.cache.memoize;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Like {@link LazySupplier}, but recomputes once the value is older than its time-to-live.
 */
final class ExpiringMemoizedSupplier<T> implements MemoizedSupplier<T> {

    private final Supplier<? extends T> supplier;
    private final long ttlNanos;
    private volatile TimedValue value;

    ExpiringMemoizedSupplier(
            @NotNull final Supplier<? extends T> supplier,
            final long ttlNanos
    ) {
        this.supplier = supplier;
        this.ttlNanos = ttlNanos;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        TimedValue current = this.value;
        if (current == null || current.isExpired()) {
            synchronized (this) {
                current = this.value;
                if (current == null || current.isExpired()) {
                    current = TimedValue.of(this.supplier.get(), this.ttlNanos);
                    this.value = current;
                }
            }
        }

        return (T) current.value();
    }

    @Override
    public boolean isComputed() {
        val current = this.value;
        return current != null && !current.isExpired();
    }

    @Override
    public void reset() {
        this.value = null;
    }

    long ttlNanos() {
        return this.ttlNanos;
    }

    @Override
    public String toString() {
        return "Memoize.supplier[" + this.supplier + ", computed=" + this.isComputed() + "]";
    }

}
