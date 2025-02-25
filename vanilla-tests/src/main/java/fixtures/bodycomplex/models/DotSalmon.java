// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex.models;

import com.azure.core.annotation.Immutable;
import com.azure.core.annotation.JsonFlatten;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/** The DotSalmon model. */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "fish\\.type")
@JsonTypeName("DotSalmon")
@JsonFlatten
@Immutable
public class DotSalmon extends DotFish {
    /*
     * The location property.
     */
    @JsonProperty(value = "location")
    private String location;

    /*
     * The iswild property.
     */
    @JsonProperty(value = "iswild")
    private Boolean iswild;

    /** Creates an instance of DotSalmon class. */
    private DotSalmon() {}

    /**
     * Get the location property: The location property.
     *
     * @return the location value.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Get the iswild property: The iswild property.
     *
     * @return the iswild value.
     */
    public Boolean iswild() {
        return this.iswild;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    @Override
    public void validate() {
        super.validate();
    }
}
