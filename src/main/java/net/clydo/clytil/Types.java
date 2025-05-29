package net.clydo.clytil;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Types {

    public <T> boolean is(Class<T> type, Class<?> expected) {
        return type == expected;
    }

    public <T> boolean isString(Class<T> type) {
        return type == String.class;
    }

    public <T> boolean isBigDecimal(Class<T> type) {
        return Numbers.isBigDecimal(type);
    }

    public <T> boolean isBigInteger(Class<T> type) {
        return Numbers.isBigInteger(type);
    }

    public <T> boolean isLong(Class<T> type) {
        return Numbers.isLong(type);
    }

    public <T> boolean isDouble(Class<T> type) {
        return Numbers.isDouble(type);
    }

    public <T> boolean isFloat(Class<T> type) {
        return Numbers.isFloat(type);
    }

    public <T> boolean isInteger(Class<T> type) {
        return Numbers.isInteger(type);
    }

    public <T> boolean isShort(Class<T> type) {
        return Numbers.isShort(type);
    }

    public <T> boolean isByte(Class<T> type) {
        return Numbers.isByte(type);
    }

}
