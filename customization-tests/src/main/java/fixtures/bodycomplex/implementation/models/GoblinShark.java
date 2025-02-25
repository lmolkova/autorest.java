// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex.implementation.models;

import com.azure.core.annotation.Fluent;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import java.util.List;

/** The Goblinshark model. */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "fishtype")
@JsonTypeName("goblin")
@Fluent
public final class GoblinShark extends Shark {
    /*
     * The jawsize property.
     */
    @JsonProperty(value = "jawsize")
    private Integer jawsize;

    /*
     * Colors possible
     */
    @JsonProperty(value = "color")
    private GoblinSharkColor color;

    /** Creates an instance of Goblinshark class. */
    public GoblinShark() {}

    /**
     * Get the jawsize property: The jawsize property.
     *
     * @return the jawsize value.
     */
    public Integer getJawsize() {
        return this.jawsize;
    }

    /**
     * Set the jawsize property: The jawsize property.
     *
     * @param jawsize the jawsize value to set.
     * @return the Goblinshark object itself.
     */
    public GoblinShark setJawsize(Integer jawsize) {
        this.jawsize = jawsize;
        return this;
    }

    /**
     * Get the color property: Colors possible.
     *
     * @return the color value.
     */
    public GoblinSharkColor getColor() {
        return this.color;
    }

    /**
     * Set the color property: Colors possible.
     *
     * @param color the color value to set.
     * @return the Goblinshark object itself.
     */
    public GoblinShark setColor(GoblinSharkColor color) {
        this.color = color;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public GoblinShark setAge(Integer age) {
        super.setAge(age);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public GoblinShark setBirthday(OffsetDateTime birthday) {
        super.setBirthday(birthday);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public GoblinShark setSpecies(String species) {
        super.setSpecies(species);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public GoblinShark setLength(float length) {
        super.setLength(length);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public GoblinShark setSiblings(List<Fish> siblings) {
        super.setSiblings(siblings);
        return this;
    }
}
