// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.requiredfieldsascotrargstransformation.models;

import com.azure.core.annotation.Immutable;
import com.azure.core.util.Base64Url;
import com.azure.core.util.DateTimeRfc1123;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;

/** The TransformationAsRequiredFields model. */
@Immutable
public class TransformationAsRequiredFields {
    /*
     * The rfc1123NonRequired property.
     */
    @JsonProperty(value = "rfc1123NonRequired")
    private DateTimeRfc1123 rfc1123NonRequired;

    /*
     * The rfc1123Required property.
     */
    @JsonProperty(value = "rfc1123Required", required = true)
    private DateTimeRfc1123 rfc1123Required;

    /*
     * The nameRequired property.
     */
    @JsonProperty(value = "nameRequired", required = true)
    private String nameRequired;

    /*
     * The urlBase64EncodedRequired property.
     */
    @JsonProperty(value = "urlBase64EncodedRequired", required = true)
    private Base64Url urlBase64EncodedRequired;

    /*
     * The unixTimeLongRequired property.
     */
    @JsonProperty(value = "unixTimeLongRequired", required = true)
    private long unixTimeLongRequired;

    /*
     * The unixTimeDateTimeRequired property.
     */
    @JsonProperty(value = "unixTimeDateTimeRequired", required = true)
    private OffsetDateTime unixTimeDateTimeRequired;

    /**
     * Creates an instance of TransformationAsRequiredFields class.
     *
     * @param rfc1123Required the rfc1123Required value to set.
     * @param nameRequired the nameRequired value to set.
     * @param urlBase64EncodedRequired the urlBase64EncodedRequired value to set.
     * @param unixTimeLongRequired the unixTimeLongRequired value to set.
     * @param unixTimeDateTimeRequired the unixTimeDateTimeRequired value to set.
     */
    @JsonCreator
    protected TransformationAsRequiredFields(
            @JsonProperty(value = "rfc1123Required", required = true) OffsetDateTime rfc1123Required,
            @JsonProperty(value = "nameRequired", required = true) String nameRequired,
            @JsonProperty(value = "urlBase64EncodedRequired", required = true) byte[] urlBase64EncodedRequired,
            @JsonProperty(value = "unixTimeLongRequired", required = true) OffsetDateTime unixTimeLongRequired,
            @JsonProperty(value = "unixTimeDateTimeRequired", required = true)
                    OffsetDateTime unixTimeDateTimeRequired) {
        this.rfc1123Required = new DateTimeRfc1123(rfc1123Required);
        this.nameRequired = nameRequired;
        this.urlBase64EncodedRequired = Base64Url.encode(urlBase64EncodedRequired);
        this.unixTimeLongRequired = unixTimeLongRequired.toEpochSecond();
        this.unixTimeDateTimeRequired = unixTimeDateTimeRequired;
    }

    /**
     * Get the rfc1123NonRequired property: The rfc1123NonRequired property.
     *
     * @return the rfc1123NonRequired value.
     */
    public OffsetDateTime getRfc1123NonRequired() {
        if (this.rfc1123NonRequired == null) {
            return null;
        }
        return this.rfc1123NonRequired.getDateTime();
    }

    /**
     * Get the rfc1123Required property: The rfc1123Required property.
     *
     * @return the rfc1123Required value.
     */
    public OffsetDateTime getRfc1123Required() {
        if (this.rfc1123Required == null) {
            return null;
        }
        return this.rfc1123Required.getDateTime();
    }

    /**
     * Get the nameRequired property: The nameRequired property.
     *
     * @return the nameRequired value.
     */
    public String getNameRequired() {
        return this.nameRequired;
    }

    /**
     * Get the urlBase64EncodedRequired property: The urlBase64EncodedRequired property.
     *
     * @return the urlBase64EncodedRequired value.
     */
    public byte[] getUrlBase64EncodedRequired() {
        if (this.urlBase64EncodedRequired == null) {
            return new byte[0];
        }
        return this.urlBase64EncodedRequired.decodedBytes();
    }

    /**
     * Get the unixTimeLongRequired property: The unixTimeLongRequired property.
     *
     * @return the unixTimeLongRequired value.
     */
    public OffsetDateTime getUnixTimeLongRequired() {
        return OffsetDateTime.from(Instant.ofEpochSecond(this.unixTimeLongRequired));
    }

    /**
     * Get the unixTimeDateTimeRequired property: The unixTimeDateTimeRequired property.
     *
     * @return the unixTimeDateTimeRequired value.
     */
    public OffsetDateTime getUnixTimeDateTimeRequired() {
        return this.unixTimeDateTimeRequired;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {
        if (getRfc1123Required() == null) {
            throw new IllegalArgumentException(
                    "Missing required property rfc1123Required in model TransformationAsRequiredFields");
        }
        if (getNameRequired() == null) {
            throw new IllegalArgumentException(
                    "Missing required property nameRequired in model TransformationAsRequiredFields");
        }
        if (getUrlBase64EncodedRequired() == null) {
            throw new IllegalArgumentException(
                    "Missing required property urlBase64EncodedRequired in model TransformationAsRequiredFields");
        }
        if (getUnixTimeLongRequired() == null) {
            throw new IllegalArgumentException(
                    "Missing required property unixTimeLongRequired in model TransformationAsRequiredFields");
        }
        if (getUnixTimeDateTimeRequired() == null) {
            throw new IllegalArgumentException(
                    "Missing required property unixTimeDateTimeRequired in model TransformationAsRequiredFields");
        }
    }
}
