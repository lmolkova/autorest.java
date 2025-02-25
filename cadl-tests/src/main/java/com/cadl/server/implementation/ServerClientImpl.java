// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.cadl.server.implementation;

import com.azure.core.annotation.ExpectedResponses;
import com.azure.core.annotation.Get;
import com.azure.core.annotation.HeaderParam;
import com.azure.core.annotation.Host;
import com.azure.core.annotation.HostParam;
import com.azure.core.annotation.PathParam;
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
import com.azure.core.util.Context;
import com.azure.core.util.FluxUtil;
import com.azure.core.util.serializer.JacksonAdapter;
import com.azure.core.util.serializer.SerializerAdapter;
import reactor.core.publisher.Mono;

/** Initializes a new instance of the ServerClient type. */
public final class ServerClientImpl {
    /** The proxy service used to perform REST calls. */
    private final ServerClientService service;

    /** second-level domain, use httpbin. */
    private final String domain;

    /**
     * Gets second-level domain, use httpbin.
     *
     * @return the domain value.
     */
    public String getDomain() {
        return this.domain;
    }

    /** top-level domain, use org. */
    private final String tld;

    /**
     * Gets top-level domain, use org.
     *
     * @return the tld value.
     */
    public String getTld() {
        return this.tld;
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
     * Initializes an instance of ServerClient client.
     *
     * @param domain second-level domain, use httpbin.
     * @param tld top-level domain, use org.
     */
    public ServerClientImpl(String domain, String tld) {
        this(
                new HttpPipelineBuilder()
                        .policies(new UserAgentPolicy(), new RetryPolicy(), new CookiePolicy())
                        .build(),
                JacksonAdapter.createDefaultSerializerAdapter(),
                domain,
                tld);
    }

    /**
     * Initializes an instance of ServerClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param domain second-level domain, use httpbin.
     * @param tld top-level domain, use org.
     */
    public ServerClientImpl(HttpPipeline httpPipeline, String domain, String tld) {
        this(httpPipeline, JacksonAdapter.createDefaultSerializerAdapter(), domain, tld);
    }

    /**
     * Initializes an instance of ServerClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param serializerAdapter The serializer to serialize an object into a string.
     * @param domain second-level domain, use httpbin.
     * @param tld top-level domain, use org.
     */
    public ServerClientImpl(HttpPipeline httpPipeline, SerializerAdapter serializerAdapter, String domain, String tld) {
        this.httpPipeline = httpPipeline;
        this.serializerAdapter = serializerAdapter;
        this.domain = domain;
        this.tld = tld;
        this.service = RestProxy.create(ServerClientService.class, this.httpPipeline, this.getSerializerAdapter());
    }

    /**
     * The interface defining all the services for ServerClient to be used by the proxy service to perform REST calls.
     */
    @Host("https://{domain}.{tld}")
    @ServiceInterface(name = "ServerClient")
    public interface ServerClientService {
        @Get("/status/{code}")
        @ExpectedResponses({200, 204})
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
        Mono<Response<Void>> status(
                @HostParam("domain") String domain,
                @HostParam("tld") String tld,
                @PathParam("code") int code,
                @HeaderParam("accept") String accept,
                RequestOptions requestOptions,
                Context context);
    }

    /**
     * The status operation.
     *
     * @param code The code parameter.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> statusWithResponseAsync(int code, RequestOptions requestOptions) {
        final String accept = "application/json";
        return FluxUtil.withContext(
                context -> service.status(this.getDomain(), this.getTld(), code, accept, requestOptions, context));
    }

    /**
     * The status operation.
     *
     * @param code The code parameter.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response}.
     */
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Response<Void> statusWithResponse(int code, RequestOptions requestOptions) {
        return statusWithResponseAsync(code, requestOptions).block();
    }
}
