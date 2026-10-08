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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * File operations that retry each step and check its result, for files that another process (an
 * antivirus scanner, a sync client) may briefly lock, plus atomic writes that never leave a
 * half-written file behind. They report failure by returning {@code false} instead of throwing.
 * <p>
 * Between attempts they pause briefly, a little longer each time, and they stop retrying if the
 * thread is interrupted.
 */
@UtilityClass
public class SafeFiles {

    public final int DEFAULT_ATTEMPTS = 10;

    /**
     * The pause after the first failed attempt; each later pause grows by this much.
     */
    private final long RETRY_DELAY_MILLIS = 10L;

    // ---------- Moving ----------

    /**
     * Moves {@code from} to {@code to}, trying up to {@link #DEFAULT_ATTEMPTS} times.
     *
     * @return whether {@code to} is now a regular file
     */
    public boolean move(
            @NotNull final Path from,
            @NotNull final Path to,
            @NotNull final CopyOption... options
    ) {
        return move(from, to, DEFAULT_ATTEMPTS, options);
    }

    /**
     * Moves {@code from} to {@code to}, trying up to {@code attempts} times.
     *
     * @return whether {@code to} is now a regular file
     */
    public boolean move(
            @NotNull final Path from,
            @NotNull final Path to,
            final int attempts,
            @NotNull final CopyOption... options
    ) {
        Validates.require(from, "from");
        Validates.require(to, "to");
        Validates.requirePositive(attempts, "attempts");

        return retry(attempts, moveTask(from, to, options), isFileTask(to));
    }

    // ---------- Copying ----------

    /**
     * Copies {@code from} to {@code to}, trying up to {@link #DEFAULT_ATTEMPTS} times.
     *
     * @return whether {@code to} is now a regular file
     */
    public boolean copy(
            @NotNull final Path from,
            @NotNull final Path to,
            @NotNull final CopyOption... options
    ) {
        return copy(from, to, DEFAULT_ATTEMPTS, options);
    }

    /**
     * Copies {@code from} to {@code to}, trying up to {@code attempts} times.
     *
     * @return whether {@code to} is now a regular file
     */
    public boolean copy(
            @NotNull final Path from,
            @NotNull final Path to,
            final int attempts,
            @NotNull final CopyOption... options
    ) {
        Validates.require(from, "from");
        Validates.require(to, "to");
        Validates.requirePositive(attempts, "attempts");

        return retry(attempts, copyTask(from, to, options), isFileTask(to));
    }

    // ---------- Deleting ----------

    /**
     * Deletes {@code path} if it exists, trying up to {@link #DEFAULT_ATTEMPTS} times. A directory
     * must be empty.
     *
     * @return whether {@code path} no longer exists
     */
    public boolean delete(
            @NotNull final Path path
    ) {
        return delete(path, DEFAULT_ATTEMPTS);
    }

    /**
     * Deletes {@code path} if it exists, trying up to {@code attempts} times. A directory must be
     * empty.
     *
     * @return whether {@code path} no longer exists
     */
    public boolean delete(
            @NotNull final Path path,
            final int attempts
    ) {
        Validates.require(path, "path");
        Validates.requirePositive(attempts, "attempts");

        return retry(attempts, deleteTask(path), isGoneTask(path));
    }

    // ---------- Replacing ----------

    /**
     * Replaces {@code target} with {@code replacement}, keeping the old {@code target} at
     * {@code backup}. If the final move fails, the backup is moved back.
     *
     * @return whether {@code target} now holds the replacement
     */
    public boolean replace(
            @NotNull final Path target,
            @NotNull final Path replacement,
            @NotNull final Path backup
    ) {
        return replace(target, replacement, backup, true);
    }

    /**
     * Replaces {@code target} with {@code replacement}, keeping the old {@code target} at
     * {@code backup}. Each step is tried up to {@link #DEFAULT_ATTEMPTS} times.
     *
     * @param rollback whether to move the backup back when the final move fails
     * @return whether {@code target} now holds the replacement
     */
    public boolean replace(
            @NotNull final Path target,
            @NotNull final Path replacement,
            @NotNull final Path backup,
            final boolean rollback
    ) {
        Validates.require(target, "target");
        Validates.require(replacement, "replacement");
        Validates.require(backup, "backup");

        val backedUp = Files.exists(target);
        if (backedUp && !retry(DEFAULT_ATTEMPTS, deleteTask(backup), moveTask(target, backup), isFileTask(backup))) {
            return false;
        }

        if (!retry(DEFAULT_ATTEMPTS, deleteTask(target), isGoneTask(target))) {
            return false;
        }

        if (retry(DEFAULT_ATTEMPTS, moveTask(replacement, target), isFileTask(target))) {
            return true;
        }

        if (rollback && backedUp) {
            restore(target, backup);
        }

        return false;
    }

    /**
     * Moves {@code backup} back to {@code target}, replacing it, such as to undo
     * {@link #replace(Path, Path, Path)}.
     *
     * @return whether {@code target} is now a regular file
     */
    public boolean restore(
            @NotNull final Path target,
            @NotNull final Path backup
    ) {
        Validates.require(target, "target");
        Validates.require(backup, "backup");

        return retry(DEFAULT_ATTEMPTS, moveTask(backup, target, StandardCopyOption.REPLACE_EXISTING), isFileTask(target));
    }

    // ---------- Atomic writes ----------

    /**
     * Writes {@code text} to {@code target} as UTF-8, atomically.
     *
     * @see #write(Path, byte[])
     */
    public boolean writeString(
            @NotNull final Path target,
            @NotNull final CharSequence text
    ) {
        return writeString(target, text, StandardCharsets.UTF_8);
    }

    /**
     * Writes {@code text} to {@code target} in {@code charset}, atomically.
     *
     * @see #write(Path, byte[])
     */
    public boolean writeString(
            @NotNull final Path target,
            @NotNull final CharSequence text,
            @NotNull final Charset charset
    ) {
        Validates.require(text, "text");
        Validates.require(charset, "charset");

        return write(target, text.toString().getBytes(charset));
    }

    /**
     * Writes {@code data} to {@code target} atomically: it goes to a temporary file in the same
     * directory, is flushed to disk, and then moved over {@code target} in one step. Readers see
     * either the old content or the new, never a partial file, even if the process dies midway.
     * The parent directory is created if missing.
     *
     * @return whether {@code target} now holds {@code data}
     */
    public boolean write(
            @NotNull final Path target,
            final byte @NotNull [] data
    ) {
        Validates.require(target, "target");
        Validates.require(data, "data");

        val directory = target.toAbsolutePath().getParent();
        Path temp = null;
        try {
            Files.createDirectories(directory);
            temp = Files.createTempFile(directory, target.getFileName() + ".", ".tmp");

            try (val channel = FileChannel.open(temp, StandardOpenOption.WRITE)) {
                val buffer = ByteBuffer.wrap(data);
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                channel.force(true);
            }

            return retry(DEFAULT_ATTEMPTS, atomicMoveTask(temp, target), isFileTask(target));
        } catch (IOException e) {
            return false;
        } finally {
            if (temp != null) {
                try {
                    Files.deleteIfExists(temp);
                } catch (IOException ignored) {
                }
            }
        }
    }

    // ---------- Internals ----------

    private boolean retry(
            final int attempts,
            final Tasks.Task @NotNull ... steps
    ) {
        for (int i = 0; i < attempts; i++) {
            if (Tasks.sequence(steps)) {
                return true;
            }

            if (i + 1 < attempts && !pause((i + 1) * RETRY_DELAY_MILLIS)) {
                return false;
            }
        }

        return false;
    }

    private boolean pause(
            final long millis
    ) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Contract(pure = true)
    private @NotNull Tasks.Task moveTask(
            @NotNull final Path from,
            @NotNull final Path to,
            final CopyOption @NotNull ... options
    ) {
        return () -> {
            try {
                Files.move(from, to, options);
                return true;
            } catch (IOException e) {
                return false;
            }
        };
    }

    /**
     * Moves atomically when the file system supports it, replacing {@code to}.
     */
    @Contract(pure = true)
    private @NotNull Tasks.Task atomicMoveTask(
            @NotNull final Path from,
            @NotNull final Path to
    ) {
        return () -> {
            try {
                try {
                    Files.move(from, to, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                } catch (AtomicMoveNotSupportedException e) {
                    Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        };
    }

    @Contract(pure = true)
    private @NotNull Tasks.Task copyTask(
            @NotNull final Path from,
            @NotNull final Path to,
            final CopyOption @NotNull ... options
    ) {
        return () -> {
            try {
                Files.copy(from, to, options);
                return true;
            } catch (IOException e) {
                return false;
            }
        };
    }

    @Contract(pure = true)
    private @NotNull Tasks.Task deleteTask(
            @NotNull final Path path
    ) {
        return () -> {
            try {
                Files.deleteIfExists(path);
                return true;
            } catch (IOException e) {
                return false;
            }
        };
    }

    private @NotNull Tasks.Task isFileTask(
            @NotNull final Path path
    ) {
        return () -> Files.isRegularFile(path);
    }

    private @NotNull Tasks.Task isGoneTask(
            @NotNull final Path path
    ) {
        return () -> !Files.exists(path);
    }

}
