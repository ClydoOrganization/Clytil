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

package net.clydo.clytil.geom;

import org.jetbrains.annotations.NotNull;

public interface Geometry<G extends Geometry<G>> extends Point<G>, Size<G> {

    default float x1() {
        return this.x();
    }

    default float y1() {
        return this.y();
    }

    default float x2() {
        return this.x() + this.width();
    }

    default float y2() {
        return this.y() + this.height();
    }

    default float left() {
        return this.x();
    }

    default float top() {
        return this.y();
    }

    default float right() {
        return this.x() + this.width();
    }

    default float bottom() {
        return this.y() + this.height();
    }

    @NotNull
    default G x1(
            final float x
    ) {
        return this.x(x);
    }

    @NotNull
    default G y1(
            final float y
    ) {
        return this.y(y);
    }

    @NotNull
    default G x2(
            final float x
    ) {
        return this.width(x - this.x());
    }

    @NotNull
    default G y2(
            final float y
    ) {
        return this.height(y - this.y());
    }

    @NotNull
    default G left(
            final float x
    ) {
        return this.x(x);
    }

    @NotNull
    default G top(
            final float y
    ) {
        return this.y(y);
    }

    @NotNull
    default G right(
            final float x
    ) {
        return this.width(x - this.x());
    }

    @NotNull
    default G bottom(
            final float y
    ) {
        return this.height(y - this.y());
    }

    @NotNull
    default G horizontal(
            final float x
    ) {
        return this.x1(x).x2(x);
    }

    @NotNull
    default G vertical(
            final float y
    ) {
        return this.y1(y).y2(y);
    }

    @NotNull
    default G horizontal(
            final float x1,
            final float x2
    ) {
        return this.x1(x1).x2(x2);
    }

    @NotNull
    default G vertical(
            final float y1,
            final float y2
    ) {
        return this.y1(y1).y2(y2);
    }

    @NotNull
    default G geometry(
            final float x1,
            final float y1,
            final float x2,
            final float y2
    ) {
        return this.x1(x1).y1(y1).x2(x2).y2(y2);
    }

    @NotNull
    default G bounds(
            final float x,
            final float y,
            final float width,
            final float height
    ) {
        return this.x(x).y(y).width(width).height(height);
    }

    @NotNull
    default G bounds(
            final float point,
            final float size
    ) {
        return this.point(point).size(size);
    }

    @NotNull
    default G bounds(
            final float value
    ) {
        return this.point(value).size(value);
    }

}
