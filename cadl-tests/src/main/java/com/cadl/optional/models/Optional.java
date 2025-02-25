// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.cadl.optional.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/** The Optional model. */
@Fluent
public final class Optional {
    /*
     * The boolean property.
     */
    @JsonProperty(value = "boolean")
    private Boolean booleanProperty;

    /*
     * The booleanNullable property.
     */
    @JsonProperty(value = "booleanNullable")
    private Boolean booleanNullable;

    /*
     * The booleanRequired property.
     */
    @JsonProperty(value = "booleanRequired", required = true)
    private boolean booleanRequired;

    /*
     * The booleanRequiredNullable property.
     */
    @JsonProperty(value = "booleanRequiredNullable", required = true)
    private Boolean booleanRequiredNullable;

    /*
     * The string property.
     */
    @JsonProperty(value = "string")
    private String string;

    /*
     * The stringNullable property.
     */
    @JsonProperty(value = "stringNullable")
    private String stringNullable;

    /*
     * The stringRequired property.
     */
    @JsonProperty(value = "stringRequired", required = true)
    private String stringRequired;

    /*
     * The stringRequiredNullable property.
     */
    @JsonProperty(value = "stringRequiredNullable", required = true)
    private String stringRequiredNullable;

    /*
     * The bytes property.
     */
    @JsonProperty(value = "bytes")
    private byte[] bytes;

    /*
     * The int property.
     */
    @JsonProperty(value = "int")
    private Integer intProperty;

    /*
     * The long property.
     */
    @JsonProperty(value = "long")
    private Long longProperty;

    /*
     * The float property.
     */
    @JsonProperty(value = "float")
    private Double floatProperty;

    /*
     * The double property.
     */
    @JsonProperty(value = "double")
    private Double doubleProperty;

    /*
     * The duration property.
     */
    @JsonProperty(value = "duration")
    private Duration duration;

    /*
     * The dateTime property.
     */
    @JsonProperty(value = "dateTime")
    private OffsetDateTime dateTime;

    /*
     * The stringList property.
     */
    @JsonProperty(value = "stringList")
    private List<String> stringList;

    /*
     * The bytesDict property.
     */
    @JsonProperty(value = "bytesDict")
    private Map<String, byte[]> bytesDict;

    /**
     * Creates an instance of Optional class.
     *
     * @param booleanRequired the booleanRequired value to set.
     * @param booleanRequiredNullable the booleanRequiredNullable value to set.
     * @param stringRequired the stringRequired value to set.
     * @param stringRequiredNullable the stringRequiredNullable value to set.
     */
    @JsonCreator
    public Optional(
            @JsonProperty(value = "booleanRequired", required = true) boolean booleanRequired,
            @JsonProperty(value = "booleanRequiredNullable", required = true) Boolean booleanRequiredNullable,
            @JsonProperty(value = "stringRequired", required = true) String stringRequired,
            @JsonProperty(value = "stringRequiredNullable", required = true) String stringRequiredNullable) {
        this.booleanRequired = booleanRequired;
        this.booleanRequiredNullable = booleanRequiredNullable;
        this.stringRequired = stringRequired;
        this.stringRequiredNullable = stringRequiredNullable;
    }

    /**
     * Get the booleanProperty property: The boolean property.
     *
     * @return the booleanProperty value.
     */
    public Boolean isBooleanProperty() {
        return this.booleanProperty;
    }

    /**
     * Set the booleanProperty property: The boolean property.
     *
     * @param booleanProperty the booleanProperty value to set.
     * @return the Optional object itself.
     */
    public Optional setBooleanProperty(Boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
        return this;
    }

    /**
     * Get the booleanNullable property: The booleanNullable property.
     *
     * @return the booleanNullable value.
     */
    public Boolean isBooleanNullable() {
        return this.booleanNullable;
    }

    /**
     * Set the booleanNullable property: The booleanNullable property.
     *
     * @param booleanNullable the booleanNullable value to set.
     * @return the Optional object itself.
     */
    public Optional setBooleanNullable(Boolean booleanNullable) {
        this.booleanNullable = booleanNullable;
        return this;
    }

    /**
     * Get the booleanRequired property: The booleanRequired property.
     *
     * @return the booleanRequired value.
     */
    public boolean isBooleanRequired() {
        return this.booleanRequired;
    }

    /**
     * Get the booleanRequiredNullable property: The booleanRequiredNullable property.
     *
     * @return the booleanRequiredNullable value.
     */
    public Boolean isBooleanRequiredNullable() {
        return this.booleanRequiredNullable;
    }

    /**
     * Get the string property: The string property.
     *
     * @return the string value.
     */
    public String getString() {
        return this.string;
    }

    /**
     * Set the string property: The string property.
     *
     * @param string the string value to set.
     * @return the Optional object itself.
     */
    public Optional setString(String string) {
        this.string = string;
        return this;
    }

    /**
     * Get the stringNullable property: The stringNullable property.
     *
     * @return the stringNullable value.
     */
    public String getStringNullable() {
        return this.stringNullable;
    }

    /**
     * Set the stringNullable property: The stringNullable property.
     *
     * @param stringNullable the stringNullable value to set.
     * @return the Optional object itself.
     */
    public Optional setStringNullable(String stringNullable) {
        this.stringNullable = stringNullable;
        return this;
    }

    /**
     * Get the stringRequired property: The stringRequired property.
     *
     * @return the stringRequired value.
     */
    public String getStringRequired() {
        return this.stringRequired;
    }

    /**
     * Get the stringRequiredNullable property: The stringRequiredNullable property.
     *
     * @return the stringRequiredNullable value.
     */
    public String getStringRequiredNullable() {
        return this.stringRequiredNullable;
    }

    /**
     * Get the bytes property: The bytes property.
     *
     * @return the bytes value.
     */
    public byte[] getBytes() {
        return CoreUtils.clone(this.bytes);
    }

    /**
     * Set the bytes property: The bytes property.
     *
     * @param bytes the bytes value to set.
     * @return the Optional object itself.
     */
    public Optional setBytes(byte[] bytes) {
        this.bytes = CoreUtils.clone(bytes);
        return this;
    }

    /**
     * Get the intProperty property: The int property.
     *
     * @return the intProperty value.
     */
    public Integer getIntProperty() {
        return this.intProperty;
    }

    /**
     * Set the intProperty property: The int property.
     *
     * @param intProperty the intProperty value to set.
     * @return the Optional object itself.
     */
    public Optional setIntProperty(Integer intProperty) {
        this.intProperty = intProperty;
        return this;
    }

    /**
     * Get the longProperty property: The long property.
     *
     * @return the longProperty value.
     */
    public Long getLongProperty() {
        return this.longProperty;
    }

    /**
     * Set the longProperty property: The long property.
     *
     * @param longProperty the longProperty value to set.
     * @return the Optional object itself.
     */
    public Optional setLongProperty(Long longProperty) {
        this.longProperty = longProperty;
        return this;
    }

    /**
     * Get the floatProperty property: The float property.
     *
     * @return the floatProperty value.
     */
    public Double getFloatProperty() {
        return this.floatProperty;
    }

    /**
     * Set the floatProperty property: The float property.
     *
     * @param floatProperty the floatProperty value to set.
     * @return the Optional object itself.
     */
    public Optional setFloatProperty(Double floatProperty) {
        this.floatProperty = floatProperty;
        return this;
    }

    /**
     * Get the doubleProperty property: The double property.
     *
     * @return the doubleProperty value.
     */
    public Double getDoubleProperty() {
        return this.doubleProperty;
    }

    /**
     * Set the doubleProperty property: The double property.
     *
     * @param doubleProperty the doubleProperty value to set.
     * @return the Optional object itself.
     */
    public Optional setDoubleProperty(Double doubleProperty) {
        this.doubleProperty = doubleProperty;
        return this;
    }

    /**
     * Get the duration property: The duration property.
     *
     * @return the duration value.
     */
    public Duration getDuration() {
        return this.duration;
    }

    /**
     * Set the duration property: The duration property.
     *
     * @param duration the duration value to set.
     * @return the Optional object itself.
     */
    public Optional setDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Get the dateTime property: The dateTime property.
     *
     * @return the dateTime value.
     */
    public OffsetDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * Set the dateTime property: The dateTime property.
     *
     * @param dateTime the dateTime value to set.
     * @return the Optional object itself.
     */
    public Optional setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    /**
     * Get the stringList property: The stringList property.
     *
     * @return the stringList value.
     */
    public List<String> getStringList() {
        return this.stringList;
    }

    /**
     * Set the stringList property: The stringList property.
     *
     * @param stringList the stringList value to set.
     * @return the Optional object itself.
     */
    public Optional setStringList(List<String> stringList) {
        this.stringList = stringList;
        return this;
    }

    /**
     * Get the bytesDict property: The bytesDict property.
     *
     * @return the bytesDict value.
     */
    public Map<String, byte[]> getBytesDict() {
        return this.bytesDict;
    }

    /**
     * Set the bytesDict property: The bytesDict property.
     *
     * @param bytesDict the bytesDict value to set.
     * @return the Optional object itself.
     */
    public Optional setBytesDict(Map<String, byte[]> bytesDict) {
        this.bytesDict = bytesDict;
        return this;
    }
}
