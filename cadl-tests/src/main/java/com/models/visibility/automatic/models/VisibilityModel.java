// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.models.visibility.automatic.models;

import com.azure.core.annotation.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Output model with visibility properties. */
@Immutable
public final class VisibilityModel {
    /*
     * Required string, illustrating a readonly property.
     */
    @JsonProperty(value = "readProp", required = true, access = JsonProperty.Access.WRITE_ONLY)
    private String readProp;

    /*
     * Required int32, illustrating a query property.
     */
    @JsonProperty(value = "queryProp", required = true)
    private int queryProp;

    /*
     * Required string[], illustrating a create property.
     */
    @JsonProperty(value = "createProp", required = true)
    private List<String> createProp;

    /*
     * Required int32[], illustrating a update property.
     */
    @JsonProperty(value = "updateProp", required = true)
    private List<Integer> updateProp;

    /*
     * Required bool, illustrating a delete property.
     */
    @JsonProperty(value = "deleteProp", required = true)
    private boolean deleteProp;

    /**
     * Creates an instance of VisibilityModel class.
     *
     * @param queryProp the queryProp value to set.
     * @param createProp the createProp value to set.
     * @param updateProp the updateProp value to set.
     * @param deleteProp the deleteProp value to set.
     */
    @JsonCreator
    public VisibilityModel(
            @JsonProperty(value = "queryProp", required = true) int queryProp,
            @JsonProperty(value = "createProp", required = true) List<String> createProp,
            @JsonProperty(value = "updateProp", required = true) List<Integer> updateProp,
            @JsonProperty(value = "deleteProp", required = true) boolean deleteProp) {
        this.queryProp = queryProp;
        this.createProp = createProp;
        this.updateProp = updateProp;
        this.deleteProp = deleteProp;
    }

    /**
     * Get the readProp property: Required string, illustrating a readonly property.
     *
     * @return the readProp value.
     */
    public String getReadProp() {
        return this.readProp;
    }

    /**
     * Get the queryProp property: Required int32, illustrating a query property.
     *
     * @return the queryProp value.
     */
    public int getQueryProp() {
        return this.queryProp;
    }

    /**
     * Get the createProp property: Required string[], illustrating a create property.
     *
     * @return the createProp value.
     */
    public List<String> getCreateProp() {
        return this.createProp;
    }

    /**
     * Get the updateProp property: Required int32[], illustrating a update property.
     *
     * @return the updateProp value.
     */
    public List<Integer> getUpdateProp() {
        return this.updateProp;
    }

    /**
     * Get the deleteProp property: Required bool, illustrating a delete property.
     *
     * @return the deleteProp value.
     */
    public boolean isDeleteProp() {
        return this.deleteProp;
    }
}
