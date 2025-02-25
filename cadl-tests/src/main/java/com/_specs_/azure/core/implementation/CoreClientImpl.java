// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com._specs_.azure.core.implementation;

import com._specs_.azure.core.CoreServiceVersion;
import com.azure.core.annotation.BodyParam;
import com.azure.core.annotation.ExpectedResponses;
import com.azure.core.annotation.HeaderParam;
import com.azure.core.annotation.Host;
import com.azure.core.annotation.Patch;
import com.azure.core.annotation.PathParam;
import com.azure.core.annotation.QueryParam;
import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ServiceInterface;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.annotation.UnexpectedResponseExceptionType;
import com.azure.core.exception.ClientAuthenticationException;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.exception.ResourceModifiedException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.core.http.HttpPipeline;
import com.azure.core.http.HttpPipelineBuilder;
import com.azure.core.http.policy.CookiePolicy;
import com.azure.core.http.policy.RetryPolicy;
import com.azure.core.http.policy.UserAgentPolicy;
import com.azure.core.http.rest.RequestOptions;
import com.azure.core.http.rest.Response;
import com.azure.core.http.rest.RestProxy;
import com.azure.core.util.BinaryData;
import com.azure.core.util.Context;
import com.azure.core.util.FluxUtil;
import com.azure.core.util.serializer.JacksonAdapter;
import com.azure.core.util.serializer.SerializerAdapter;
import reactor.core.publisher.Mono;

/** Initializes a new instance of the CoreClient type. */
public final class CoreClientImpl {
    /** The proxy service used to perform REST calls. */
    private final CoreClientService service;

    /** Service version. */
    private final CoreServiceVersion serviceVersion;

    /**
     * Gets Service version.
     *
     * @return the serviceVersion value.
     */
    public CoreServiceVersion getServiceVersion() {
        return this.serviceVersion;
    }

    /** The HTTP pipeline to send requests through. */
    private final HttpPipeline httpPipeline;

    /**
     * Gets The HTTP pipeline to send requests through.
     *
     * @return the httpPipeline value.
     */
    public HttpPipeline getHttpPipeline() {
        return this.httpPipeline;
    }

    /** The serializer to serialize an object into a string. */
    private final SerializerAdapter serializerAdapter;

    /**
     * Gets The serializer to serialize an object into a string.
     *
     * @return the serializerAdapter value.
     */
    public SerializerAdapter getSerializerAdapter() {
        return this.serializerAdapter;
    }

    /**
     * Initializes an instance of CoreClient client.
     *
     * @param serviceVersion Service version.
     */
    public CoreClientImpl(CoreServiceVersion serviceVersion) {
        this(
                new HttpPipelineBuilder()
                        .policies(new UserAgentPolicy(), new RetryPolicy(), new CookiePolicy())
                        .build(),
                JacksonAdapter.createDefaultSerializerAdapter(),
                serviceVersion);
    }

    /**
     * Initializes an instance of CoreClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param serviceVersion Service version.
     */
    public CoreClientImpl(HttpPipeline httpPipeline, CoreServiceVersion serviceVersion) {
        this(httpPipeline, JacksonAdapter.createDefaultSerializerAdapter(), serviceVersion);
    }

    /**
     * Initializes an instance of CoreClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param serializerAdapter The serializer to serialize an object into a string.
     * @param serviceVersion Service version.
     */
    public CoreClientImpl(
            HttpPipeline httpPipeline, SerializerAdapter serializerAdapter, CoreServiceVersion serviceVersion) {
        this.httpPipeline = httpPipeline;
        this.serializerAdapter = serializerAdapter;
        this.serviceVersion = serviceVersion;
        this.service = RestProxy.create(CoreClientService.class, this.httpPipeline, this.getSerializerAdapter());
    }

    /** The interface defining all the services for CoreClient to be used by the proxy service to perform REST calls. */
    @Host("http://localhost:3000")
    @ServiceInterface(name = "CoreClient")
    public interface CoreClientService {
        @Patch("/azure/core/users/{id}")
        @ExpectedResponses({200, 201})
        @UnexpectedResponseExceptionType(
                value = ClientAuthenticationException.class,
                code = {401})
        @UnexpectedResponseExceptionType(
                value = ResourceNotFoundException.class,
                code = {404})
        @UnexpectedResponseExceptionType(
                value = ResourceModifiedException.class,
                code = {409})
        @UnexpectedResponseExceptionType(HttpResponseException.class)
        Mono<Response<BinaryData>> createOrUpdate(
                @QueryParam("api-version") String apiVersion,
                @PathParam("id") int id,
                @HeaderParam("Content-Type") String contentType,
                @HeaderParam("accept") String accept,
                @BodyParam("application/merge-patch+json") BinaryData resource,
                RequestOptions requestOptions,
                Context context);
    }

    /**
     * Adds a user or updates a user's fields.
     *
     * <p>Creates or updates a User.
     *
     * <p><strong>Request Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     id: int (Required)
     *     name: String (Required)
     * }
     * }</pre>
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     id: int (Required)
     *     name: String (Required)
     * }
     * }</pre>
     *
     * @param id The user's id.
     * @param resource The resource instance.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return details about a user along with {@link Response} on successful completion of {@link Mono}.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> createOrUpdateWithResponseAsync(
            int id, BinaryData resource, RequestOptions requestOptions) {
        final String contentType = "application/merge-patch+json";
        final String accept = "application/json";
        return FluxUtil.withContext(
                context ->
                        service.createOrUpdate(
                                this.getServiceVersion().getVersion(),
                                id,
                                contentType,
                                accept,
                                resource,
                                requestOptions,
                                context));
    }

    /**
     * Adds a user or updates a user's fields.
     *
     * <p>Creates or updates a User.
     *
     * <p><strong>Request Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     id: int (Required)
     *     name: String (Required)
     * }
     * }</pre>
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     id: int (Required)
     *     name: String (Required)
     * }
     * }</pre>
     *
     * @param id The user's id.
     * @param resource The resource instance.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return details about a user along with {@link Response}.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<BinaryData> createOrUpdateWithResponse(int id, BinaryData resource, RequestOptions requestOptions) {
        return createOrUpdateWithResponseAsync(id, resource, requestOptions).block();
    }
}
