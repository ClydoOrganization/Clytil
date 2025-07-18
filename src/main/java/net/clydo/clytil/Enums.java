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
import net.clydo.clytil.option.Option;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Enums {

    public <T extends Enum<T>> Option<T> valueOf(
            @NotNull final Class<T> clazz,
            @NotNull final String name
    ) {
        Validates.require(clazz, "clazz");
        Validates.require(name, "name");

        try {
            return Option.some(Enum.valueOf(clazz, name));
        } catch (Throwable t) {
            return Option.none();
        }
    }

}
