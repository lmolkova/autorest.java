// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.constants.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Defines values for NoModelAsStringNoRequiredTwoValueDefaultOpEnum. */
public enum NoModelAsStringNoRequiredTwoValueDefaultOpEnum {
    /** Enum value value1. */
    VALUE1("value1"),

    /** Enum value value2. */
    VALUE2("value2");

    /** The actual serialized value for a NoModelAsStringNoRequiredTwoValueDefaultOpEnum instance. */
    private final String value;

    NoModelAsStringNoRequiredTwoValueDefaultOpEnum(String value) {
        this.value = value;
    }

    /**
     * Parses a serialized value to a NoModelAsStringNoRequiredTwoValueDefaultOpEnum instance.
     *
     * @param value the serialized value to parse.
     * @return the parsed NoModelAsStringNoRequiredTwoValueDefaultOpEnum object, or null if unable to parse.
     */
    @JsonCreator
    public static NoModelAsStringNoRequiredTwoValueDefaultOpEnum fromString(String value) {
        if (value == null) {
            return null;
        }
        NoModelAsStringNoRequiredTwoValueDefaultOpEnum[] items =
                NoModelAsStringNoRequiredTwoValueDefaultOpEnum.values();
        for (NoModelAsStringNoRequiredTwoValueDefaultOpEnum item : items) {
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
