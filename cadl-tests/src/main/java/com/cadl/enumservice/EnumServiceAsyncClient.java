// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.cadl.enumservice;

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
import com.azure.core.util.serializer.CollectionFormat;
import com.azure.core.util.serializer.JacksonAdapter;
import com.cadl.enumservice.implementation.EnumServiceClientImpl;
import com.cadl.enumservice.models.Color;
import com.cadl.enumservice.models.ColorModel;
import com.cadl.enumservice.models.Operation;
import com.cadl.enumservice.models.OperationStateValues;
import com.cadl.enumservice.models.Priority;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

/** Initializes a new instance of the asynchronous EnumServiceClient type. */
@ServiceClient(builder = EnumServiceClientBuilder.class, isAsync = true)
public final class EnumServiceAsyncClient {
    @Generated private final EnumServiceClientImpl serviceClient;

    /**
     * Initializes an instance of EnumServiceAsyncClient class.
     *
     * @param serviceClient the service client implementation.
     */
    @Generated
    EnumServiceAsyncClient(EnumServiceClientImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * The getColor operation.
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * String(Red/Blue/Green)
     * }</pre>
     *
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<String>> getColorWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getColorWithResponseAsync(requestOptions);
    }

    /**
     * The getColorModel operation.
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * String(Red/Blue/Green)
     * }</pre>
     *
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<String>> getColorModelWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getColorModelWithResponseAsync(requestOptions);
    }

    /**
     * The setColorModel operation.
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     name: String(Read/Write) (Required)
     *     best: boolean (Required)
     *     age: long (Required)
     *     priority: String(100/0) (Required)
     *     color: String(Red/Blue/Green) (Required)
     * }
     * }</pre>
     *
     * @param color The color parameter. Allowed values: "Red", "Blue", "Green".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> setColorModelWithResponse(String color, RequestOptions requestOptions) {
        return this.serviceClient.setColorModelWithResponseAsync(color, requestOptions);
    }

    /**
     * The setPriority operation.
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     name: String(Read/Write) (Required)
     *     best: boolean (Required)
     *     age: long (Required)
     *     priority: String(100/0) (Required)
     *     color: String(Red/Blue/Green) (Required)
     * }
     * }</pre>
     *
     * @param priority The priority parameter. Allowed values: 100, 0.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> setPriorityWithResponse(String priority, RequestOptions requestOptions) {
        return this.serviceClient.setPriorityWithResponseAsync(priority, requestOptions);
    }

    /**
     * The getRunningOperation operation.
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     name: String(Read/Write) (Required)
     *     best: boolean (Required)
     *     age: long (Required)
     *     priority: String(100/0) (Required)
     *     color: String(Red/Blue/Green) (Required)
     * }
     * }</pre>
     *
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> getRunningOperationWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.getRunningOperationWithResponseAsync(requestOptions);
    }

    /**
     * The getOperation operation.
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * {
     *     name: String(Read/Write) (Required)
     *     best: boolean (Required)
     *     age: long (Required)
     *     priority: String(100/0) (Required)
     *     color: String(Red/Blue/Green) (Required)
     * }
     * }</pre>
     *
     * @param state The state parameter. Allowed values: "Running", "Completed", "Failed".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> getOperationWithResponse(String state, RequestOptions requestOptions) {
        return this.serviceClient.getOperationWithResponseAsync(state, requestOptions);
    }

    /**
     * The setStringEnumArray operation.
     *
     * <p><strong>Query Parameters</strong>
     *
     * <table border="1">
     *     <caption>Query Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>colorArrayOpt</td><td>List&lt;String&gt;</td><td>No</td><td>Array of ColorModel. In the form of "," separated string.</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addQueryParam}
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * String
     * }</pre>
     *
     * @param colorArray Array of ColorModel.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> setStringEnumArrayWithResponse(
            List<String> colorArray, RequestOptions requestOptions) {
        return this.serviceClient.setStringEnumArrayWithResponseAsync(colorArray, requestOptions);
    }

    /**
     * The setIntEnumArray operation.
     *
     * <p><strong>Query Parameters</strong>
     *
     * <table border="1">
     *     <caption>Query Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>priorityArrayOpt</td><td>List&lt;String&gt;</td><td>No</td><td>Array of Priority. In the form of "," separated string.</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addQueryParam}
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * String
     * }</pre>
     *
     * @param priorityArray Array of Priority.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> setIntEnumArrayWithResponse(
            List<String> priorityArray, RequestOptions requestOptions) {
        return this.serviceClient.setIntEnumArrayWithResponseAsync(priorityArray, requestOptions);
    }

    /**
     * The setStringArray operation.
     *
     * <p><strong>Query Parameters</strong>
     *
     * <table border="1">
     *     <caption>Query Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>stringArrayOpt</td><td>List&lt;String&gt;</td><td>No</td><td>Array of Response. In the form of "," separated string.</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addQueryParam}
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * String
     * }</pre>
     *
     * @param stringArray Array of Response.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> setStringArrayWithResponse(
            List<String> stringArray, RequestOptions requestOptions) {
        return this.serviceClient.setStringArrayWithResponseAsync(stringArray, requestOptions);
    }

    /**
     * The setIntArray operation.
     *
     * <p><strong>Query Parameters</strong>
     *
     * <table border="1">
     *     <caption>Query Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>intArrayOpt</td><td>List&lt;Integer&gt;</td><td>No</td><td>Array of IntArray. In the form of "," separated string.</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addQueryParam}
     *
     * <p><strong>Response Body Schema</strong>
     *
     * <pre>{@code
     * String
     * }</pre>
     *
     * @param intArray Array of IntArray.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the response body along with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<BinaryData>> setIntArrayWithResponse(List<Integer> intArray, RequestOptions requestOptions) {
        return this.serviceClient.setIntArrayWithResponseAsync(intArray, requestOptions);
    }

    /**
     * The getColor operation.
     *
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Color> getColor() {
        // Generated convenience method for getColorWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getColorWithResponse(requestOptions).flatMap(FluxUtil::toMono).map(Color::fromString);
    }

    /**
     * The getColorModel operation.
     *
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<ColorModel> getColorModel() {
        // Generated convenience method for getColorModelWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getColorModelWithResponse(requestOptions).flatMap(FluxUtil::toMono).map(ColorModel::fromString);
    }

    /**
     * The setColorModel operation.
     *
     * @param color The color parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Operation> setColorModel(ColorModel color) {
        // Generated convenience method for setColorModelWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return setColorModelWithResponse(color.toString(), requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(Operation.class));
    }

    /**
     * The setPriority operation.
     *
     * @param priority The priority parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Operation> setPriority(Priority priority) {
        // Generated convenience method for setPriorityWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return setPriorityWithResponse(String.valueOf(priority.toLong()), requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(Operation.class));
    }

    /**
     * The getRunningOperation operation.
     *
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Operation> getRunningOperation() {
        // Generated convenience method for getRunningOperationWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getRunningOperationWithResponse(requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(Operation.class));
    }

    /**
     * The getOperation operation.
     *
     * @param state The state parameter.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Operation> getOperation(OperationStateValues state) {
        // Generated convenience method for getOperationWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return getOperationWithResponse(state.toString(), requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(Operation.class));
    }

    /**
     * The setStringEnumArray operation.
     *
     * @param colorArray Array of ColorModel.
     * @param colorArrayOpt Array of ColorModel.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setStringEnumArray(List<ColorModel> colorArray, List<ColorModel> colorArrayOpt) {
        // Generated convenience method for setStringEnumArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        if (colorArrayOpt != null) {
            requestOptions.addQueryParam(
                    "colorArrayOpt",
                    JacksonAdapter.createDefaultSerializerAdapter()
                            .serializeIterable(colorArrayOpt, CollectionFormat.CSV));
        }
        return setStringEnumArrayWithResponse(
                        colorArray.stream()
                                .map(paramItemValue -> Objects.toString(paramItemValue, ""))
                                .collect(Collectors.toList()),
                        requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }

    /**
     * The setStringEnumArray operation.
     *
     * @param colorArray Array of ColorModel.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setStringEnumArray(List<ColorModel> colorArray) {
        // Generated convenience method for setStringEnumArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return setStringEnumArrayWithResponse(
                        colorArray.stream()
                                .map(paramItemValue -> Objects.toString(paramItemValue, ""))
                                .collect(Collectors.toList()),
                        requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }

    /**
     * The setIntEnumArray operation.
     *
     * @param priorityArray Array of Priority.
     * @param priorityArrayOpt Array of Priority.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setIntEnumArray(List<Priority> priorityArray, List<Priority> priorityArrayOpt) {
        // Generated convenience method for setIntEnumArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        if (priorityArrayOpt != null) {
            requestOptions.addQueryParam(
                    "priorityArrayOpt",
                    JacksonAdapter.createDefaultSerializerAdapter()
                            .serializeIterable(priorityArrayOpt, CollectionFormat.CSV));
        }
        return setIntEnumArrayWithResponse(
                        priorityArray.stream()
                                .map(
                                        paramItemValue ->
                                                paramItemValue == null ? "" : String.valueOf(paramItemValue.toLong()))
                                .collect(Collectors.toList()),
                        requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }

    /**
     * The setIntEnumArray operation.
     *
     * @param priorityArray Array of Priority.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setIntEnumArray(List<Priority> priorityArray) {
        // Generated convenience method for setIntEnumArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return setIntEnumArrayWithResponse(
                        priorityArray.stream()
                                .map(
                                        paramItemValue ->
                                                paramItemValue == null ? "" : String.valueOf(paramItemValue.toLong()))
                                .collect(Collectors.toList()),
                        requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }

    /**
     * The setStringArray operation.
     *
     * @param stringArray Array of Response.
     * @param stringArrayOpt Array of Response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setStringArray(List<String> stringArray, List<String> stringArrayOpt) {
        // Generated convenience method for setStringArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        if (stringArrayOpt != null) {
            requestOptions.addQueryParam(
                    "stringArrayOpt",
                    stringArrayOpt.stream()
                            .map(paramItemValue -> Objects.toString(paramItemValue, ""))
                            .collect(Collectors.joining(",")));
        }
        return setStringArrayWithResponse(stringArray, requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }

    /**
     * The setStringArray operation.
     *
     * @param stringArray Array of Response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setStringArray(List<String> stringArray) {
        // Generated convenience method for setStringArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return setStringArrayWithResponse(stringArray, requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }

    /**
     * The setIntArray operation.
     *
     * @param intArray Array of IntArray.
     * @param intArrayOpt Array of IntArray.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setIntArray(List<Integer> intArray, List<Integer> intArrayOpt) {
        // Generated convenience method for setIntArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        if (intArrayOpt != null) {
            requestOptions.addQueryParam(
                    "intArrayOpt",
                    JacksonAdapter.createDefaultSerializerAdapter()
                            .serializeIterable(intArrayOpt, CollectionFormat.CSV));
        }
        return setIntArrayWithResponse(intArray, requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }

    /**
     * The setIntArray operation.
     *
     * @param intArray Array of IntArray.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws com.azure.core.exception.HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response body on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<String> setIntArray(List<Integer> intArray) {
        // Generated convenience method for setIntArrayWithResponse
        RequestOptions requestOptions = new RequestOptions();
        return setIntArrayWithResponse(intArray, requestOptions)
                .flatMap(FluxUtil::toMono)
                .map(protocolMethodData -> protocolMethodData.toObject(String.class));
    }
}
