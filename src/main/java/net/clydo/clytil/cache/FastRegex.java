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

package net.clydo.clytil.cache;

import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public class FastRegex {

    //private static final Pattern NEVER_REGEX = Pattern.compile("$.");

    private final Map<String, Pattern> cache;
    private final Set<String> invalidRegexes;

    public FastRegex() {
        this.cache = new HashMap<>();
        this.invalidRegexes = new HashSet<>();
    }

    @Nullable
    private Pattern compilePattern(final String regex) {
        try {
            return Pattern.compile(regex);
        } catch (Exception e) {
            this.invalidRegexes.add(regex);
            //return NEVER_REGEX;
            return null;
        }
    }

    public boolean matches(CharSequence input, String regex) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(regex, "regex must not be null");

        if (this.invalidRegexes.contains(regex)) {
            return false;
        }

        val pattern = this.cache.computeIfAbsent(regex, this::compilePattern);
        return pattern != null && pattern.matcher(input).matches();
    }

    public void clear() {
        this.cache.clear();
    }

}
