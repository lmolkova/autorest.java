// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.cadl.optional.models;

import com.azure.core.annotation.Immutable;
import com.azure.core.util.CoreUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/** The AllPropertiesOptional model. */
@Immutable
public final class AllPropertiesOptional {
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
    @JsonProperty(value = "booleanRequired")
    private Boolean booleanRequired;

    /*
     * The booleanRequiredNullable property.
     */
    @JsonProperty(value = "booleanRequiredNullable")
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
    @JsonProperty(value = "stringRequired")
    private String stringRequired;

    /*
     * The stringRequiredNullable property.
     */
    @JsonProperty(value = "stringRequiredNullable")
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

    /*
     * The immutable property.
     */
    @JsonProperty(value = "immutable")
    private ImmutableModel immutable;

    /** Creates an instance of AllPropertiesOptional class. */
    private AllPropertiesOptional() {}

    /**
     * Get the booleanProperty property: The boolean property.
     *
     * @return the booleanProperty value.
     */
    public Boolean isBooleanProperty() {
        return this.booleanProperty;
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
     * Get the booleanRequired property: The booleanRequired property.
     *
     * @return the booleanRequired value.
     */
    public Boolean isBooleanRequired() {
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
     * Get the stringNullable property: The stringNullable property.
     *
     * @return the stringNullable value.
     */
    public String getStringNullable() {
        return this.stringNullable;
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
     * Get the intProperty property: The int property.
     *
     * @return the intProperty value.
     */
    public Integer getIntProperty() {
        return this.intProperty;
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
     * Get the floatProperty property: The float property.
     *
     * @return the floatProperty value.
     */
    public Double getFloatProperty() {
        return this.floatProperty;
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
     * Get the duration property: The duration property.
     *
     * @return the duration value.
     */
    public Duration getDuration() {
        return this.duration;
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
     * Get the stringList property: The stringList property.
     *
     * @return the stringList value.
     */
    public List<String> getStringList() {
        return this.stringList;
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
     * Get the immutable property: The immutable property.
     *
     * @return the immutable value.
     */
    public ImmutableModel getImmutable() {
        return this.immutable;
    }
}
