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

package net.clydo.clytil.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.clydo.clytil.iface.Identifiable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Jsons {

    @Contract(pure = true)
    public @NotNull JsonElement parse(
            @NotNull final String text
    ) {
        return JsonParser.parseString(text);
    }

    @Contract(pure = true)
    public @NotNull JsonObject parseObject(
            @NotNull final String text
    ) {
        return asObject(parse(text), "root");
    }

    @Contract(pure = true)
    public @NotNull JsonObject asObject(
            @NotNull final JsonElement element,
            @NotNull final String member
    ) {
        if (element.isJsonObject()) {
            return element.getAsJsonObject();
        }

        throw mismatch(member, "an object", element);
    }

    @Contract(pure = true)
    public @NotNull JsonArray asArray(
            @NotNull final JsonElement element,
            @NotNull final String member
    ) {
        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        }

        throw mismatch(member, "an array", element);
    }

    @Contract(pure = true)
    public @NotNull JsonPrimitive asPrimitive(
            @NotNull final JsonElement element,
            @NotNull final String member
    ) {
        if (element.isJsonPrimitive()) {
            return element.getAsJsonPrimitive();
        }

        throw mismatch(member, "a value", element);
    }

    @Contract(pure = true)
    public @NotNull String asString(
            @NotNull final JsonElement element,
            @NotNull final String member
    ) {
        if (isString(element)) {
            return element.getAsString();
        }

        throw mismatch(member, "a string", element);
    }

    @Contract(pure = true)
    public @NotNull Number asNumber(
            @NotNull final JsonElement element,
            @NotNull final String member
    ) {
        if (isNumber(element)) {
            return element.getAsNumber();
        }

        throw mismatch(member, "a number", element);
    }

    @Contract(pure = true)
    public boolean asBoolean(
            @NotNull final JsonElement element,
            @NotNull final String member
    ) {
        if (isBoolean(element)) {
            return element.getAsBoolean();
        }

        throw mismatch(member, "a boolean", element);
    }

    @Contract(pure = true)
    public @NotNull List<String> asStringList(
            @NotNull final JsonElement element,
            @NotNull final String member
    ) {
        val array = asArray(element, member);
        val list = new ArrayList<String>(array.size());

        for (var index = 0; index < array.size(); index++) {
            list.add(asString(array.get(index), member + "[" + index + "]"));
        }

        return list;
    }

    @Contract(pure = true)
    public @NotNull JsonElement require(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        val element = object.get(member);

        if (element == null) {
            throw new JsonSyntaxException("Missing " + member);
        }

        return element;
    }

    @Contract(pure = true)
    public @NotNull JsonObject getObject(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return asObject(require(object, member), member);
    }

    @Contract(pure = true)
    public @NotNull JsonArray getArray(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return asArray(require(object, member), member);
    }

    @Contract(pure = true)
    public @NotNull JsonPrimitive getPrimitive(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return asPrimitive(require(object, member), member);
    }

    @Contract(pure = true)
    public @NotNull String getString(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return asString(require(object, member), member);
    }

    @Contract(value = "_, _, !null -> !null", pure = true)
    public @Nullable String getString(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @Nullable final String fallback
    ) {
        return object.has(member) ? asString(object.get(member), member) : fallback;
    }

    @Contract(pure = true)
    public @NotNull Number getNumber(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return asNumber(require(object, member), member);
    }

    @Contract(pure = true)
    public @NotNull List<String> getStringList(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return asStringList(require(object, member), member);
    }

    @Contract(pure = true)
    public boolean getBoolean(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return getPrimitive(object, member).getAsBoolean();
    }

    @Contract(pure = true)
    public boolean getBoolean(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final boolean fallback
    ) {
        return object.has(member) ? getBoolean(object, member) : fallback;
    }

    @Contract(pure = true)
    public int getInt(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return getPrimitive(object, member).getAsInt();
    }

    @Contract(pure = true)
    public int getInt(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final int fallback
    ) {
        return object.has(member) ? getInt(object, member) : fallback;
    }

    @Contract(pure = true)
    public long getLong(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return getPrimitive(object, member).getAsLong();
    }

    @Contract(pure = true)
    public long getLong(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final long fallback
    ) {
        return object.has(member) ? getLong(object, member) : fallback;
    }

    @Contract(pure = true)
    public float getFloat(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return getPrimitive(object, member).getAsFloat();
    }

    @Contract(pure = true)
    public float getFloat(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final float fallback
    ) {
        return object.has(member) ? getFloat(object, member) : fallback;
    }

    @Contract(pure = true)
    public double getDouble(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return getPrimitive(object, member).getAsDouble();
    }

    @Contract(pure = true)
    public double getDouble(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final double fallback
    ) {
        return object.has(member) ? getDouble(object, member) : fallback;
    }

    @Contract(pure = true)
    public @Nullable JsonObject optObject(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return object.get(member) instanceof JsonObject value
                ? value
                : null;
    }

    @Contract(pure = true)
    public @Nullable JsonArray optArray(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return object.get(member) instanceof JsonArray value
                ? value
                : null;
    }

    @Contract(pure = true)
    public @Nullable List<String> optStringList(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        val array = optArray(object, member);
        if (array == null) {
            return null;
        }

        val list = new ArrayList<String>(array.size());
        for (val element : array) {
            if (element instanceof JsonPrimitive primitive && primitive.isString()) {
                list.add(primitive.getAsString());
            }
        }

        return list;
    }

    @Contract(pure = true)
    public @Nullable Map<String, String> optStringMap(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        val source = optObject(object, member);
        if (source == null) {
            return null;
        }

        val map = new LinkedHashMap<String, String>();
        for (val entry : source.entrySet()) {
            if (entry.getValue() instanceof JsonPrimitive primitive && primitive.isString()) {
                map.put(entry.getKey(), primitive.getAsString());
            }
        }

        return map;
    }

    @Contract(pure = true)
    public @Nullable JsonPrimitive optPrimitive(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        return object.get(member) instanceof JsonPrimitive value
                ? value
                : null;
    }

    @Contract(pure = true)
    public @Nullable String optString(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        val primitive = optPrimitive(object, member);
        return primitive != null && primitive.isString()
                ? primitive.getAsString()
                : null;
    }

    @Contract(value = "_, _, !null -> !null", pure = true)
    public @Nullable String optString(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @Nullable final String fallback
    ) {
        val value = optString(object, member);
        return value != null ? value : fallback;
    }

    @Contract(pure = true)
    public @Nullable Number optNumber(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        val primitive = optPrimitive(object, member);
        return primitive != null && primitive.isNumber()
                ? primitive.getAsNumber()
                : null;
    }

    @Contract(pure = true)
    public @Nullable Boolean optBoolean(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        val primitive = optPrimitive(object, member);
        return primitive != null && primitive.isBoolean()
                ? primitive.getAsBoolean()
                : null;
    }

    @Contract(pure = true)
    public boolean optBoolean(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final boolean fallback
    ) {
        val value = optBoolean(object, member);
        return value != null ? value : fallback;
    }

    @Contract(pure = true)
    public int optInt(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final int fallback
    ) {
        val value = optNumber(object, member);
        return value != null ? value.intValue() : fallback;
    }

    @Contract(pure = true)
    public long optLong(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final long fallback
    ) {
        val value = optNumber(object, member);
        return value != null ? value.longValue() : fallback;
    }

    @Contract(pure = true)
    public float optFloat(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final float fallback
    ) {
        val value = optNumber(object, member);
        return value != null ? value.floatValue() : fallback;
    }

    @Contract(pure = true)
    public double optDouble(
            @NotNull final JsonObject object,
            @NotNull final String member,
            final double fallback
    ) {
        val value = optNumber(object, member);
        return value != null ? value.doubleValue() : fallback;
    }

    @Contract(value = "_, _, _, !null -> !null", pure = true)
    public <T extends Identifiable> @Nullable T optById(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @NotNull final T @NotNull [] values,
            @Nullable final T fallback
    ) {
        val value = Identifiable.findById(values, optString(object, member));
        return value != null ? value : fallback;
    }

    @Contract(value = "_, _, _, !null -> !null", pure = true)
    public <E extends Enum<E>> @Nullable E optEnum(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @NotNull final Class<E> type,
            @Nullable final E fallback
    ) {
        val name = optString(object, member);
        if (name == null) {
            return fallback;
        }

        for (val constant : type.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(name)) {
                return constant;
            }
        }

        return fallback;
    }

    @Contract(pure = true)
    public boolean hasNonNull(
            @NotNull final JsonObject object,
            @NotNull final String member
    ) {
        val element = object.get(member);
        return element != null && !element.isJsonNull();
    }

    @Contract(pure = true)
    public boolean isString(
            @NotNull final JsonElement element
    ) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }

    @Contract(pure = true)
    public boolean isNumber(
            @NotNull final JsonElement element
    ) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
    }

    @Contract(pure = true)
    public boolean isBoolean(
            @NotNull final JsonElement element
    ) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean();
    }

    public void addIfNotNull(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @Nullable final JsonElement value
    ) {
        if (value != null && !value.isJsonNull()) {
            object.add(member, value);
        }
    }

    public void addIfNotNull(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @Nullable final String value
    ) {
        if (value != null) {
            object.addProperty(member, value);
        }
    }

    public void addIfNotNull(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @Nullable final Number value
    ) {
        if (value != null) {
            object.addProperty(member, value);
        }
    }

    public void addIfNotNull(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @Nullable final Boolean value
    ) {
        if (value != null) {
            object.addProperty(member, value);
        }
    }

    public void addOrNull(
            @NotNull final JsonObject object,
            @NotNull final String member,
            @Nullable final String value
    ) {
        object.add(member, value != null ? new JsonPrimitive(value) : JsonNull.INSTANCE);
    }

    @Contract("_ -> new")
    public @NotNull JsonArray toArray(
            @NotNull final Iterable<String> values
    ) {
        val array = new JsonArray();
        for (val value : values) {
            array.add(value);
        }

        return array;
    }

    @Contract("_, _ -> param1")
    public @NotNull JsonObject deepMerge(
            @NotNull final JsonObject target,
            @NotNull final JsonObject source
    ) {
        for (val entry : source.entrySet()) {
            val key = entry.getKey();
            val value = entry.getValue();

            if (value instanceof JsonObject sourceObject && target.get(key) instanceof JsonObject targetObject) {
                deepMerge(targetObject, sourceObject);
            } else {
                target.add(key, value.deepCopy());
            }
        }

        return target;
    }

    @Contract(pure = true)
    public @NotNull String describe(
            @NotNull final JsonElement element
    ) {
        if (element.isJsonNull()) {
            return "null";
        }

        if (element.isJsonArray()) {
            return "an array";
        }

        if (element.isJsonObject()) {
            return "an object";
        }

        val primitive = element.getAsJsonPrimitive();

        if (primitive.isNumber()) {
            return "a number";
        }

        if (primitive.isBoolean()) {
            return "a boolean";
        }

        return "a string";
    }

    private @NotNull JsonSyntaxException mismatch(
            @NotNull final String member,
            @NotNull final String expected,
            @NotNull final JsonElement element
    ) {
        return new JsonSyntaxException("Expected " + member + " to be " + expected + ", was " + describe(element));
    }

}
