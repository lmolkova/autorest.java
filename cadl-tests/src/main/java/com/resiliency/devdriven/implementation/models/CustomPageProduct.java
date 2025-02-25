// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.resiliency.devdriven.implementation.models;

import com.azure.core.annotation.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resiliency.devdriven.models.Product;
import java.util.List;

/** Paged collection of Product items. */
@Immutable
public final class CustomPageProduct {
    /*
     * The Product items on this page
     */
    @JsonProperty(value = "value", required = true)
    private List<Product> value;

    /*
     * The link to the next page of items
     */
    @JsonProperty(value = "nextLink")
    private String nextLink;

    /**
     * Creates an instance of CustomPageProduct class.
     *
     * @param value the value value to set.
     */
    @JsonCreator
    private CustomPageProduct(@JsonProperty(value = "value", required = true) List<Product> value) {
        this.value = value;
    }

    /**
     * Get the value property: The Product items on this page.
     *
     * @return the value value.
     */
    public List<Product> getValue() {
        return this.value;
    }

    /**
     * Get the nextLink property: The link to the next page of items.
     *
     * @return the nextLink value.
     */
    public String getNextLink() {
        return this.nextLink;
    }
}
