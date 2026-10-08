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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Set;

@UtilityClass
public class Uris {

    /**
     * Web pages: {@code http} and {@code https}.
     */
    public final Set<String> WEB_SCHEMES = Set.of("http", "https");

    /**
     * Encrypted transports: {@code https}, {@code wss}, {@code ftps} and {@code sftp}.
     */
    public final Set<String> SECURE_SCHEMES = Set.of("https", "wss", "ftps", "sftp");

    /**
     * WebSockets: {@code ws} and {@code wss}.
     */
    public final Set<String> WEBSOCKET_SCHEMES = Set.of("ws", "wss");

    /**
     * File transfer: {@code ftp}, {@code ftps} and {@code sftp}.
     */
    public final Set<String> FTP_SCHEMES = Set.of("ftp", "ftps", "sftp");

    /**
     * Links that are generally safe to open from untrusted text: web pages plus {@code mailto}.
     * Leaves out schemes that can run code or read local data, such as {@code file},
     * {@code javascript} and {@code data}.
     */
    public final Set<String> LINK_SCHEMES = Set.of("http", "https", "mailto");

    /**
     * Parses a URI from an untrusted source, such as user input or a downloaded file, accepting
     * only {@code http} and {@code https}.
     *
     * @throws URISyntaxException if it does not parse, or has another or no scheme
     */
    public @NotNull URI parseUntrusted(
            @NotNull final String uri
    ) throws URISyntaxException {
        return parseUntrusted(uri, WEB_SCHEMES);
    }

    /**
     * Parses a URI from an untrusted source, accepting only the given schemes (ignoring case).
     *
     * @throws URISyntaxException if it does not parse, or has another or no scheme
     */
    public @NotNull URI parseUntrusted(
            @NotNull final String uri,
            @NotNull final Collection<String> allowedSchemes
    ) throws URISyntaxException {
        Validates.require(uri, "uri");
        Validates.require(allowedSchemes, "allowedSchemes");

        val parsed = new URI(uri);
        val scheme = parsed.getScheme();
        if (scheme == null) {
            throw new URISyntaxException(uri, "Missing scheme");
        }

        if (!hasScheme(parsed, allowedSchemes)) {
            throw new URISyntaxException(uri, "Unsupported scheme '" + scheme + "'");
        }

        return parsed;
    }

    /**
     * Returns whether {@code uri} uses {@code http} or {@code https}.
     */
    @Contract(pure = true)
    public boolean isWeb(
            @NotNull final URI uri
    ) {
        return hasScheme(uri, WEB_SCHEMES);
    }

    /**
     * Returns whether {@code uri} uses an encrypted transport, one of {@link #SECURE_SCHEMES}.
     */
    @Contract(pure = true)
    public boolean isSecure(
            @NotNull final URI uri
    ) {
        return hasScheme(uri, SECURE_SCHEMES);
    }

    /**
     * Returns whether {@code uri} uses the {@code file} scheme.
     */
    @Contract(pure = true)
    public boolean isFile(
            @NotNull final URI uri
    ) {
        Validates.require(uri, "uri");

        return "file".equalsIgnoreCase(uri.getScheme());
    }

    /**
     * Returns {@code uri} as a string, with a {@code file} URI in the {@code file:///path} form
     * that browsers and desktop tools expect. {@link java.io.File#toURI()} gives the shorter
     * {@code file:/path}, which some of them reject. URIs that already have the slashes, relative
     * file URIs and other schemes are returned unchanged.
     */
    @Contract(pure = true)
    public @NotNull String toExternalString(
            @NotNull final URI uri
    ) {
        val text = Validates.require(uri, "uri").toString();
        if (!isFile(uri)) {
            return text;
        }

        val path = text.substring("file:".length());
        return path.startsWith("/") && !path.startsWith("//")
                ? "file://" + path
                : text;
    }

    /**
     * Returns whether {@code uri} uses one of {@code schemes}, ignoring case.
     */
    @Contract(pure = true)
    public boolean hasScheme(
            @NotNull final URI uri,
            @NotNull final Collection<String> schemes
    ) {
        Validates.require(uri, "uri");
        Validates.require(schemes, "schemes");

        val scheme = uri.getScheme();
        if (scheme == null) {
            return false;
        }

        for (val candidate : schemes) {
            if (candidate.equalsIgnoreCase(scheme)) {
                return true;
            }
        }

        return false;
    }

}
