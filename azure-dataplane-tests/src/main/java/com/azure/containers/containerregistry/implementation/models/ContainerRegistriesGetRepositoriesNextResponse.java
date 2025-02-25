// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.containers.containerregistry.implementation.models;

import com.azure.core.http.HttpHeaders;
import com.azure.core.http.HttpRequest;
import com.azure.core.http.rest.ResponseBase;

/** Contains all response data for the getRepositoriesNext operation. */
public final class ContainerRegistriesGetRepositoriesNextResponse
        extends ResponseBase<ContainerRegistriesGetRepositoriesNextHeaders, Repositories> {
    /**
     * Creates an instance of ContainerRegistriesGetRepositoriesNextResponse.
     *
     * @param request the request which resulted in this ContainerRegistriesGetRepositoriesNextResponse.
     * @param statusCode the status code of the HTTP response.
     * @param rawHeaders the raw headers of the HTTP response.
     * @param value the deserialized value of the HTTP response.
     * @param headers the deserialized headers of the HTTP response.
     */
    public ContainerRegistriesGetRepositoriesNextResponse(
            HttpRequest request,
            int statusCode,
            HttpHeaders rawHeaders,
            Repositories value,
            ContainerRegistriesGetRepositoriesNextHeaders headers) {
        super(request, statusCode, rawHeaders, value, headers);
    }

    /**
     * Gets the deserialized response body.
     *
     * @return the deserialized response body.
     */
    @Override
    public Repositories getValue() {
        return super.getValue();
    }
}
