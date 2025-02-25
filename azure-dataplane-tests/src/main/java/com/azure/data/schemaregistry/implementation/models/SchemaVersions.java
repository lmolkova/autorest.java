// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.data.schemaregistry.implementation.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Array received from the registry containing the list of versions for specific schema. */
@Fluent
public final class SchemaVersions {
    /*
     * Array of schema groups.
     */
    @JsonProperty(value = "schemaVersions")
    private List<Integer> schemaVersions;

    /** Creates an instance of SchemaVersions class. */
    public SchemaVersions() {}

    /**
     * Get the schemaVersions property: Array of schema groups.
     *
     * @return the schemaVersions value.
     */
    public List<Integer> getSchemaVersions() {
        return this.schemaVersions;
    }

    /**
     * Set the schemaVersions property: Array of schema groups.
     *
     * @param schemaVersions the schemaVersions value to set.
     * @return the SchemaVersions object itself.
     */
    public SchemaVersions setSchemaVersions(List<Integer> schemaVersions) {
        this.schemaVersions = schemaVersions;
        return this;
    }
}
