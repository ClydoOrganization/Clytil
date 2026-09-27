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

package net.clydo.clytil.iface;

import net.clydo.clytil.ARGB;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ColorProvider<R extends ColorProvider<R>> {

    @NotNull
    R color(
            final int red,
            final int green,
            final int blue,
            final int alpha
    );

    default @NotNull R color(
            @NotNull final ColorSupplier supplier
    ) {
        return color(supplier.color());
    }

    default @NotNull R color(
            @NotNull final ColorSupplier color,
            final int alpha
    ) {
        return color(color.color(), alpha);
    }

    default @NotNull R color(
            final int color
    ) {
        return color(
                ARGB.red(color),
                ARGB.green(color),
                ARGB.blue(color),
                ARGB.alpha(color)
        );
    }

    default @NotNull R color(
            final int rgb,
            final int alpha
    ) {
        return color(
                ARGB.red(rgb),
                ARGB.green(rgb),
                ARGB.blue(rgb),
                alpha
        );
    }

    default @NotNull R color(
            final int red,
            final int green,
            final int blue
    ) {
        return color(red, green, blue, ARGB.MAX_CHANNEL);
    }

}
