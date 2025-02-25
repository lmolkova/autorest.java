// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.cadl.polymorphism.models;

import com.azure.core.annotation.Immutable;
import com.azure.core.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The DerivedTypeData model. */
@Immutable
public final class DerivedTypeData extends BaseType {
    /*
     * The data property.
     */
    @JsonProperty(value = "data", required = true)
    private byte[] data;

    /**
     * Creates an instance of DerivedTypeData class.
     *
     * @param name the name value to set.
     * @param data the data value to set.
     */
    @JsonCreator
    public DerivedTypeData(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "data", required = true) byte[] data) {
        super(name);
        this.data = data;
    }

    /**
     * Get the data property: The data property.
     *
     * @return the data value.
     */
    public byte[] getData() {
        return CoreUtils.clone(this.data);
    }
}
