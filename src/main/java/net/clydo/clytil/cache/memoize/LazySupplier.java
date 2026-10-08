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

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Computes once with double-checked locking. One volatile field holds the value: {@code null}
 * before it is computed, the {@link NullMask} sentinel for a {@code null} result. Used for
 * non-serializable delegates; see {@link SerializableLazySupplier}.
 */
final class LazySupplier<T> implements MemoizedSupplier<T> {

    private final Supplier<? extends T> supplier;
    private volatile Object value;

    LazySupplier(
            @NotNull final Supplier<? extends T> supplier
    ) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        Object current = this.value;
        if (current == null) {
            synchronized (this) {
                current = this.value;
                if (current == null) {
                    current = NullMask.mask(this.supplier.get());
                    this.value = current;
                }
            }
        }

        return NullMask.unmask(current);
    }

    @Override
    public boolean isComputed() {
        return this.value != null;
    }

    @Override
    public void reset() {
        this.value = null;
    }

    @Override
    public String toString() {
        return "Memoize.supplier[" + this.supplier + ", computed=" + this.isComputed() + "]";
    }

}
