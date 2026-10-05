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

package net.clydo.clytil;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@UtilityClass
public class FileExtensions {

    @Contract(pure = true)
    public @NotNull String normalize(
            @NotNull final String extension
    ) {
        val trimmed = extension.trim().toLowerCase(Locale.ROOT);
        return trimmed.startsWith(".")
                ? trimmed.substring(1)
                : trimmed;
    }

    @Contract(pure = true)
    public @NotNull List<String> normalizeAll(
            @NotNull final Collection<String> extensions
    ) {
        return extensions.stream()
                .map(FileExtensions::normalize)
                .toList();
    }

    @Contract(pure = true)
    public boolean matches(
            @NotNull final Path path,
            @NotNull final Collection<String> extensions
    ) {
        if (extensions.isEmpty()) {
            return true;
        }

        val fileName = path.getFileName();
        if (fileName == null) {
            return false;
        }

        val name = fileName.toString().toLowerCase(Locale.ROOT);
        for (val extension : extensions) {
            if (name.endsWith("." + FileExtensions.normalize(extension))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the extension of a file name: what follows its last dot, as written. A name whose
     * only dot is its first character, such as {@code ".gitignore"}, has none.
     *
     * @param fileName the file name, such as {@code "photo.PNG"}
     * @return the extension without its dot, such as {@code "PNG"}, or an empty string
     */
    @Contract(pure = true)
    public @NotNull String of(
            @NotNull final String fileName
    ) {
        val dot = fileName.lastIndexOf('.');
        return dot > 0 ? fileName.substring(dot + 1) : "";
    }

}
