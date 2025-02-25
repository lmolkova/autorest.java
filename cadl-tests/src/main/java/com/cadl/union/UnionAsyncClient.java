// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.cadl.union;

import com.azure.core.annotation.Generated;
import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ServiceClient;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.exception.ClientAuthenticationException;
import com.azure.core.exception.HttpResponseException;
import com.azure.core.exception.ResourceModifiedException;
import com.azure.core.exception.ResourceNotFoundException;
import com.azure.core.http.rest.RequestOptions;
import com.azure.core.http.rest.Response;
import com.azure.core.util.BinaryData;
import com.azure.core.util.FluxUtil;
import com.cadl.union.implementation.UnionClientImpl;
import com.cadl.union.models.InputModelBase;
import com.cadl.union.models.SendLongOptions;
import com.cadl.union.models.User;
import java.util.HashMap;
import java.util.Map;
import reactor.core.publisher.Mono;

/** Initializes a new instance of the asynchronous UnionClient type. */
@ServiceClient(builder = UnionClientBuilder.class, isAsync = true)
public final class UnionAsyncClient {
    @Generated private final UnionClientImpl serviceClient;

    /**
     * Initializes an instance of UnionAsyncClient class.
     *
     * @param serviceClient the service client implementation.
     */
    @Generated
    UnionAsyncClient(UnionClientImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * The send operation.
     *
     * <p><strong>Request Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     user (Optional): {
     *         user: String (Required)
     *     }
     *     input: InputModelBase (Required)
     * }
     * }</pre>
     *
     * @param id The id parameter.
     * @param request The request parameter.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> sendWithResponse(String id, BinaryData request, RequestOptions requestOptions) {
        return this.serviceClient.sendWithResponseAsync(id, request, requestOptions);
    }

    /**
     * The sendLong operation.
     *
     * <p><strong>Query Parameters</strong>
     *
     * <table border="1">
     *     <caption>Query Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>filter</td><td>String</td><td>No</td><td>The filter parameter</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addQueryParam}
     *
     * <p><strong>Request Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     user (Optional): {
     *         user: String (Required)
     *     }
     *     input: InputModelBase (Required)
     * }
     * }</pre>
     *
     * @param id The id parameter.
     * @param request The request parameter.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> sendLongWithResponse(String id, BinaryData request, RequestOptions requestOptions) {
        return this.serviceClient.sendLongWithResponseAsync(id, request, requestOptions);
    }

    /**
     * The send operation.
     *
     * @param id The id parameter.
     * @param input The input parameter.
     * @param user The user parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return A {@link Mono} that completes when a successful response is received.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Void> send(String id, InputModelBase input, User user) {
        // Generated convenience method for sendWithResponse
        RequestOptions requestOptions = new RequestOptions();
        Map<String, Object> requestObj = new HashMap<>();
        requestObj.put("user", user);
        requestObj.put("input", input);
        BinaryData request = BinaryData.fromObject(requestObj);
        return sendWithResponse(id, request, requestOptions).flatMap(FluxUtil::toMono);
    }

    /**
     * The send operation.
     *
     * @param id The id parameter.
     * @param input The input parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return A {@link Mono} that completes when a successful response is received.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Void> send(String id, InputModelBase input) {
        // Generated convenience method for sendWithResponse
        RequestOptions requestOptions = new RequestOptions();
        Map<String, Object> requestObj = new HashMap<>();
        requestObj.put("input", input);
        BinaryData request = BinaryData.fromObject(requestObj);
        return sendWithResponse(id, request, requestOptions).flatMap(FluxUtil::toMono);
    }

    /**
     * The sendLong operation.
     *
     * @param options Options for sendLong API.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return A {@link Mono} that completes when a successful response is received.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Void> sendLong(SendLongOptions options) {
        // Generated convenience method for sendLongWithResponse
        RequestOptions requestOptions = new RequestOptions();
        String id = options.getId();
        String filter = options.getFilter();
        Map<String, Object> requestObj = new HashMap<>();
        requestObj.put("user", options.getUser());
        requestObj.put("input", options.getInput());
        requestObj.put("dataInt", options.getDataInt());
        requestObj.put("dataUnion", options.getDataUnion());
        requestObj.put("dataLong", options.getDataLong());
        requestObj.put("data_float", options.getDataFloat());
        BinaryData request = BinaryData.fromObject(requestObj);
        if (filter != null) {
            requestOptions.addQueryParam("filter", filter);
        }
        return sendLongWithResponse(id, request, requestOptions).flatMap(FluxUtil::toMono);
    }
}
