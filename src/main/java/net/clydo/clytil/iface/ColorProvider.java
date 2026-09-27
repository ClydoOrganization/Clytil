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

import net.clydo.clytil.Colors;
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
                Colors.ARGB32.red(color),
                Colors.ARGB32.green(color),
                Colors.ARGB32.blue(color),
                Colors.ARGB32.alpha(color)
        );
    }

    default @NotNull R color(
            final int rgb,
            final int alpha
    ) {
        return color(
                Colors.ARGB32.red(rgb),
                Colors.ARGB32.green(rgb),
                Colors.ARGB32.blue(rgb),
                alpha
        );
    }

    default @NotNull R color(
            final int red,
            final int green,
            final int blue
    ) {
        return color(red, green, blue, Colors.MAX_CHANNEL);
    }

}
