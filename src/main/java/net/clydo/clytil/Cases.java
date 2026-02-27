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

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Enum representing various string naming conventions (cases) and their corresponding patterns.
 * <p>
 * Provides a method to check if a given string conforms to a specific case.
 */
@RequiredArgsConstructor
public enum Cases {

    /**
     * Lowercase words separated by underscores, e.g., "my_variable_name".
     */
    SNAKE("^[a-z][a-z0-9]*(?:_[a-z][a-z0-9]*)*$"),

    /**
     * Uppercase words separated by underscores, e.g., "MY_VARIABLE_NAME".
     */
    SCREAMING_SNAKE("^[A-Z][A-Z0-9]*(?:_[A-Z][A-Z0-9]*)*$"),

    /**
     * Lowercase words separated by hyphens, e.g., "my-variable-name".
     */
    KEBAB("^[a-z][a-z0-9]*(?:-[a-z][a-z0-9]*)*$"),

    /**
     * camelCase: first word lowercase, subsequent words capitalized, e.g., "myVariableName".
     */
    CAMEL("^[a-z][a-z0-9]*(?:[A-Z][a-z0-9]*)*$"),

    /**
     * PascalCase: all words capitalized, e.g., "MyVariableName".
     */
    PASCAL("^[A-Z][a-z0-9]*(?:[A-Z][a-z0-9]*)*$"),

    /**
     * All lowercase letters, e.g., "variable".
     */
    LOWER("^[a-z]+$"),

    /**
     * All uppercase letters, e.g., "CONSTANT".
     */
    UPPER("^[A-Z]+$"),

    /**
     * Alphanumeric string with letters and/or digits, e.g., "Var123".
     */
    ALPHANUMERIC("^[A-Za-z0-9]+$");

    /**
     * The regex pattern string defining this case.
     */
    private final String regex;

    /**
     * Compiled Pattern object, initialized lazily.
     */
    private Pattern pattern;

    /**
     * Checks if the given string matches this case type.
     * <p>
     * The regex is compiled lazily on first use for efficiency.
     *
     * @param str the string to check
     * @return true if the string matches this case type, false otherwise
     */
    public boolean matches(
            @NotNull final String str
    ) {
        Validates.require(str, "str");
        if (pattern == null) {
            pattern = Pattern.compile(regex);
        }
        return pattern.matcher(str).matches();
    }
}
