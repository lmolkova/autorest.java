// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.models.property.types.models;

import com.azure.core.annotation.Immutable;
import com.azure.core.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Model with a bytes property. */
@Immutable
public final class BytesProperty {
    /*
     * Property
     */
    @JsonProperty(value = "property", required = true)
    private byte[] property;

    /**
     * Creates an instance of BytesProperty class.
     *
     * @param property the property value to set.
     */
    @JsonCreator
    public BytesProperty(@JsonProperty(value = "property", required = true) byte[] property) {
        this.property = property;
    }

    /**
     * Get the property property: Property.
     *
     * @return the property value.
     */
    public byte[] getProperty() {
        return CoreUtils.clone(this.property);
    }
}
