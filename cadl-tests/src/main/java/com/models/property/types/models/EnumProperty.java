// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.models.property.types.models;

import com.azure.core.annotation.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Model with enum properties. */
@Immutable
public final class EnumProperty {
    /*
     * Property
     */
    @JsonProperty(value = "property", required = true)
    private FixedInnerEnum property;

    /**
     * Creates an instance of EnumProperty class.
     *
     * @param property the property value to set.
     */
    @JsonCreator
    public EnumProperty(@JsonProperty(value = "property", required = true) FixedInnerEnum property) {
        this.property = property;
    }

    /**
     * Get the property property: Property.
     *
     * @return the property value.
     */
    public FixedInnerEnum getProperty() {
        return this.property;
    }
}
