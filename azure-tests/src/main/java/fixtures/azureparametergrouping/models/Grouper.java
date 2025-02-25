// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.azureparametergrouping.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Parameter group. */
@Fluent
public final class Grouper {
    /*
     * A grouped parameter that is a constant.
     */
    @JsonProperty(value = "groupedConstant")
    private String groupedConstant = "foo";

    /*
     * Optional parameter part of a parameter grouping.
     */
    @JsonProperty(value = "groupedParameter")
    private String groupedParameter;

    /** Creates an instance of Grouper class. */
    public Grouper() {
        groupedConstant = "foo";
    }

    /**
     * Get the groupedConstant property: A grouped parameter that is a constant.
     *
     * @return the groupedConstant value.
     */
    public String getGroupedConstant() {
        return this.groupedConstant;
    }

    /**
     * Set the groupedConstant property: A grouped parameter that is a constant.
     *
     * @param groupedConstant the groupedConstant value to set.
     * @return the Grouper object itself.
     */
    public Grouper setGroupedConstant(String groupedConstant) {
        this.groupedConstant = groupedConstant;
        return this;
    }

    /**
     * Get the groupedParameter property: Optional parameter part of a parameter grouping.
     *
     * @return the groupedParameter value.
     */
    public String getGroupedParameter() {
        return this.groupedParameter;
    }

    /**
     * Set the groupedParameter property: Optional parameter part of a parameter grouping.
     *
     * @param groupedParameter the groupedParameter value to set.
     * @return the Grouper object itself.
     */
    public Grouper setGroupedParameter(String groupedParameter) {
        this.groupedParameter = groupedParameter;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {}
}
