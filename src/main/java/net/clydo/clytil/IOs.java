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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Reads bytes from the file system or the class path, whichever matches.
 * Paths are tried as files first, then resolved against the anchor class.
 */
@UtilityClass
public class IOs {

    /**
     * Opens a stream for a file path or a class path resource.
     *
     * @param path   the path, trimmed before use
     * @param anchor class resolving class path resources
     * @return an open stream, caller closes it
     * @throws IOException if the path matches neither a file nor a resource
     */
    public @NotNull InputStream open(
            @NotNull final String path,
            @NotNull final Class<?> anchor
    ) throws IOException {
        val trimmed = path.trim();

        val file = new File(trimmed);
        if (file.isFile()) {
            return Files.newInputStream(file.toPath());
        }

        val resource = anchor.getResourceAsStream(trimmed);
        if (resource == null) {
            throw new FileNotFoundException(trimmed);
        }
        return resource;
    }

    /**
     * Reads a file path or class path resource fully into memory.
     *
     * @param path   the path, trimmed before use
     * @param anchor class resolving class path resources
     * @return the bytes
     * @throws IOException if the path cannot be read
     */
    public byte @NotNull [] readAllBytes(
            @NotNull final String path,
            @NotNull final Class<?> anchor
    ) throws IOException {
        try (val stream = open(path, anchor)) {
            return stream.readAllBytes();
        }
    }

    /**
     * Reads a file path or class path resource as UTF-8 text.
     *
     * @param path   the path, trimmed before use
     * @param anchor class resolving class path resources
     * @return the decoded text
     * @throws IOException if the path cannot be read
     */
    public @NotNull String readString(
            @NotNull final String path,
            @NotNull final Class<?> anchor
    ) throws IOException {
        return new String(readAllBytes(path, anchor), StandardCharsets.UTF_8);
    }

}
