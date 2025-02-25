// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.models.inheritance.models;

import com.azure.core.annotation.Immutable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/** This is base model for polymorphic multiple levels inheritance with a discriminator. */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "kind",
        defaultImpl = Fish.class)
@JsonTypeName("Fish")
@JsonSubTypes({
    @JsonSubTypes.Type(name = "shark", value = Shark.class),
    @JsonSubTypes.Type(name = "salmon", value = Salmon.class)
})
@Immutable
public class Fish {
    /*
     * The age property.
     */
    @JsonProperty(value = "age", required = true)
    private int age;

    /**
     * Creates an instance of Fish class.
     *
     * @param age the age value to set.
     */
    @JsonCreator
    public Fish(@JsonProperty(value = "age", required = true) int age) {
        this.age = age;
    }

    /**
     * Get the age property: The age property.
     *
     * @return the age value.
     */
    public int getAge() {
        return this.age;
    }
}
