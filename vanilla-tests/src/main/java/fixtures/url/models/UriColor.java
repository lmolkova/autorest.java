// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.url.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Defines values for UriColor. */
public enum UriColor {
    /** Enum value red color. */
    RED_COLOR("red color"),

    /** Enum value green color. */
    GREEN_COLOR("green color"),

    /** Enum value blue color. */
    BLUE_COLOR("blue color");

    /** The actual serialized value for a UriColor instance. */
    private final String value;

    UriColor(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a UriColor instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed UriColor object, or null if unable to parse.
     */
    @JsonCreator
    public static UriColor fromString(String value) {
        if (value == null) {
            return null;
        }
        UriColor[] items = UriColor.values();
        for (UriColor item : items) {
            if (item.toString().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
