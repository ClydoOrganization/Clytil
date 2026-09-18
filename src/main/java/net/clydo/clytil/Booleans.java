package net.clydo.clytil;

import lombok.experimental.UtilityClass;

/**
 * Static utility methods for working with {@code boolean} and {@link Boolean} values.
 */
@UtilityClass
public class Booleans {

    private final int INT_TRUE = 1;
    private final int INT_FALSE = 0;

    private final long LONG_TRUE = 1L;
    private final long LONG_FALSE = 0L;

    private final byte BYTE_TRUE = (byte) 1;
    private final byte BYTE_FALSE = (byte) 0;

    private final String STRING_TRUE = "true";
    private final String STRING_FALSE = "false";

    /**
     * Converts a {@code boolean} to an {@code int}.
     *
     * @param value the value to convert
     * @return {@code 1} if {@code value} is {@code true}, {@code 0} otherwise
     */
    public int toInt(
            final boolean value
    ) {
        return value ? INT_TRUE : INT_FALSE;
    }

    /**
     * Converts a {@code boolean} to a {@code long}.
     *
     * @param value the value to convert
     * @return {@code 1L} if {@code value} is {@code true}, {@code 0L} otherwise
     */
    public long toLong(
            final boolean value
    ) {
        return value ? LONG_TRUE : LONG_FALSE;
    }

    /**
     * Converts a {@code boolean} to a {@code byte}.
     *
     * @param value the value to convert
     * @return {@code 1} if {@code value} is {@code true}, {@code 0} otherwise
     */
    public byte toByte(
            final boolean value
    ) {
        return value ? BYTE_TRUE : BYTE_FALSE;
    }

    /**
     * Converts a {@code boolean} to its {@code String} representation.
     *
     * @param value the value to convert
     * @return {@code "true"} or {@code "false"}
     */
    public String toString(
            final boolean value
    ) {
        return value ? STRING_TRUE : STRING_FALSE;
    }

    /**
     * Converts an {@code int} to a {@code boolean}.
     *
     * @param value the value to convert
     * @return {@code false} if {@code value == 0}, {@code true} otherwise
     */
    public boolean fromInt(
            final int value
    ) {
        return value != INT_FALSE;
    }

    /**
     * Converts a {@code long} to a {@code boolean}.
     *
     * @param value the value to convert
     * @return {@code false} if {@code value == 0L}, {@code true} otherwise
     */
    public boolean fromLong(
            final long value
    ) {
        return value != LONG_FALSE;
    }

    /**
     * Converts a {@code byte} to a {@code boolean}.
     *
     * @param value the value to convert
     * @return {@code false} if {@code value == 0}, {@code true} otherwise
     */
    public boolean fromByte(
            final byte value
    ) {
        return value != BYTE_FALSE;
    }

}
