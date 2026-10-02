package net.clydo.clytil.iface;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@FunctionalInterface
public interface IoSupplier<T> {

    T get() throws IOException;

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final Path path
    ) {
        return () -> Files.newInputStream(path);
    }

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final Path path,
            @NotNull final OpenOption @NotNull ... options
    ) {
        return () -> Files.newInputStream(path, options);
    }

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final ZipFile zipFile,
            @NotNull final ZipEntry zipEntry
    ) {
        return () -> zipFile.getInputStream(zipEntry);
    }

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final byte @NotNull [] bytes
    ) {
        return () -> new ByteArrayInputStream(bytes);
    }

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final ClassLoader classLoader,
            @NotNull final String resource
    ) {
        return () -> {
            final InputStream input = classLoader.getResourceAsStream(resource);

            if (input == null) {
                throw new FileNotFoundException(resource);
            }

            return input;
        };
    }

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final Class<?> type,
            @NotNull final String resource
    ) {
        return () -> {
            final InputStream input = type.getResourceAsStream(resource);

            if (input == null) {
                throw new FileNotFoundException(resource);
            }

            return input;
        };
    }

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final URL url
    ) {
        return url::openStream;
    }

    static @NotNull IoSupplier<InputStream> create(
            @NotNull final URI uri
    ) {
        return () -> uri.toURL().openStream();
    }

    static @NotNull IoSupplier<Reader> reader(
            @NotNull final Path path,
            @NotNull final Charset charset
    ) {
        return () -> Files.newBufferedReader(path, charset);
    }

    static @NotNull IoSupplier<OutputStream> output(
            @NotNull final Path path,
            @NotNull final OpenOption @NotNull ... options
    ) {
        return () -> Files.newOutputStream(path, options);
    }

    static @NotNull IoSupplier<Writer> writer(
            @NotNull final Path path,
            @NotNull final Charset charset
    ) {
        return () -> Files.newBufferedWriter(path, charset);
    }

}