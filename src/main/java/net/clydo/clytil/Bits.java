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

/**
 * Utility class for bit-level packing, unpacking, and bitmask manipulation.
 * <p>
 * Designed for efficient manipulation of specific bit ranges within a 32-bit integer.
 * Commonly used for storing multiple small values within a single integer or managing bit flags.
 *
 * <p><b>Example usage:</b>
 * <pre>{@code
 * // Packing value 5 into bits 4–7 of an integer
 * int packed = Bits.packInt(0, 5, 4, 4); // 0b00000000_00000000_00000000_01010000
 *
 * // Unpacking the same value
 * int unpacked = Bits.unpackInt(packed, 4, 4); // 5
 *
 * // Using bit flags
 * int flags = 0;
 * flags = Bits.addFlag(flags, 0b0010);         // Add flag
 * boolean has = Bits.hasFlag(flags, 0b0010);   // Check flag → true
 * flags = Bits.removeFlag(flags, 0b0010);      // Remove flag
 * flags = Bits.toggleFlag(flags, 0b0001);      // Toggle flag
 * }</pre>
 */
@UtilityClass
public class Bits {

    /**
     * Constant representing no flags set (all bits cleared).
     */
    private static final int NONE = 0;

    // ---------- Packing / Unpacking ----------

    /**
     * Packs a value into the specified bit range of a 32-bit integer.
     * Any existing bits in the target range will be replaced.
     *
     * @param target the original integer to modify
     * @param value the value to pack (should fit within {@code length} bits)
     * @param offset the starting bit position (0 = least significant bit)
     * @param length the number of bits to use for the value
     * @return the resulting integer with the packed value
     *
     * @throws IllegalArgumentException if {@code length < 0 || offset < 0 || offset + length > 32}
     */
    public static int packInt(int target, int value, int offset, int length) {
        if (length < 0 || offset < 0 || offset + length > 32)
            throw new IllegalArgumentException("Invalid offset/length combination.");

        int mask = ((1 << length) - 1) << offset;
        return (target & ~mask) | ((value << offset) & mask);
    }

    /**
     * Extracts a value from a specific bit range of a 32-bit integer.
     *
     * @param packed the packed integer
     * @param offset the starting bit position to extract from (0 = least significant bit)
     * @param length the number of bits to extract
     * @return the extracted value (right-aligned)
     *
     * @throws IllegalArgumentException if {@code length < 0 || offset < 0 || offset + length > 32}
     */
    public static int unpackInt(int packed, int offset, int length) {
        if (length < 0 || offset < 0 || offset + length > 32)
            throw new IllegalArgumentException("Invalid offset/length combination.");

        return (packed >>> offset) & ((1 << length) - 1);
    }

    // ---------- Bitmask Flag Utilities ----------

    /**
     * Returns the integer value representing no flags set (all bits cleared).
     *
     * @return an int with no bits set (zero)
     */
    public static int none() {
        return NONE;
    }

    /**
     * Returns a bitmask with a single bit set at the given index.
     * Equivalent to {@code 1 << index}.
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * public static final int BOLD = Bits.flag(0);
     * public static final int ITALIC = Bits.flag(1);
     * }</pre>
     *
     * @param index the bit index (0 = least significant bit)
     * @return an integer with only the specified bit set
     */
    public static int flag(int index) {
        if (index < 0 || index >= 32)
            throw new IllegalArgumentException("Index must be between 0 and 31.");
        return 1 << index;
    }

    /**
     * Checks if a specific flag (mask) is set in the given flags' integer.
     *
     * @param flags the flags to check
     * @param mask the bitmask to test
     * @return {@code true} if the flag is set, {@code false} otherwise
     */
    public static boolean hasFlag(int flags, int mask) {
        return (flags & mask) != 0;
    }

    /**
     * Adds (sets) a flag (bitmask) to the given flags integer.
     *
     * @param flags the original flags
     * @param mask the bitmask to add
     * @return the updated flags with the mask added
     */
    public static int addFlag(int flags, int mask) {
        return flags | mask;
    }

    /**
     * Removes (clears) a flag (bitmask) from the given flags integer.
     *
     * @param flags the original flags
     * @param mask the bitmask to remove
     * @return the updated flags with the mask removed
     */
    public static int removeFlag(int flags, int mask) {
        return flags & ~mask;
    }

    /**
     * Toggles a flag (bitmask) in the given flags' integer.
     * If the flag is set, it will be cleared; if it is clear, it will be set.
     *
     * @param flags the original flags
     * @param mask the bitmask to toggle
     * @return the updated flags with the mask toggled
     */
    public static int toggleFlag(int flags, int mask) {
        return flags ^ mask;
    }

}
