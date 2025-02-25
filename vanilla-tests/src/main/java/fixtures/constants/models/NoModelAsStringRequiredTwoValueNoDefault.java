// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.constants.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The NoModelAsStringRequiredTwoValueNoDefault model. */
@Fluent
public final class NoModelAsStringRequiredTwoValueNoDefault {
    /*
     * The parameter property.
     */
    @JsonProperty(value = "parameter", required = true)
    private NoModelAsStringRequiredTwoValueNoDefaultEnum parameter;

    /** Creates an instance of NoModelAsStringRequiredTwoValueNoDefault class. */
    public NoModelAsStringRequiredTwoValueNoDefault() {}

    /**
     * Get the parameter property: The parameter property.
     *
     * @return the parameter value.
     */
    public NoModelAsStringRequiredTwoValueNoDefaultEnum getParameter() {
        return this.parameter;
    }

    /**
     * Set the parameter property: The parameter property.
     *
     * @param parameter the parameter value to set.
     * @return the NoModelAsStringRequiredTwoValueNoDefault object itself.
     */
    public NoModelAsStringRequiredTwoValueNoDefault setParameter(
            NoModelAsStringRequiredTwoValueNoDefaultEnum parameter) {
        this.parameter = parameter;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (getParameter() == null) {
            throw new IllegalArgumentException(
                    "Missing required property parameter in model NoModelAsStringRequiredTwoValueNoDefault");
        }
    }
}
