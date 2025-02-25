// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.header;

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
import fixtures.header.implementation.HeadersImpl;
import java.time.Duration;
import java.time.OffsetDateTime;
import reactor.core.publisher.Mono;

/** Initializes a new instance of the asynchronous AutoRestSwaggerBatHeaderServiceClient type. */
@ServiceClient(builder = AutoRestSwaggerBatHeaderServiceClientBuilder.class, isAsync = true)
public final class AutoRestSwaggerBatHeaderServiceAsyncClient {
    @Generated private final HeadersImpl serviceClient;

    /**
     * Initializes an instance of AutoRestSwaggerBatHeaderServiceAsyncClient class.
     *
     * @param serviceClient the service client implementation.
     */
    @Generated
    AutoRestSwaggerBatHeaderServiceAsyncClient(HeadersImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Send a post request with header value "User-Agent": "overwrite".
     *
     * @param userAgent Send a post request with header value "User-Agent": "overwrite".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramExistingKeyWithResponse(String userAgent, RequestOptions requestOptions) {
        return this.serviceClient.paramExistingKeyWithResponseAsync(userAgent, requestOptions);
    }

    /**
     * Get a response with header value "User-Agent": "overwrite".
     *
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header value "User-Agent": "overwrite" along with {@link Response} on successful
     *     completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseExistingKeyWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.responseExistingKeyWithResponseAsync(requestOptions);
    }

    /**
     * Send a post request with header value "Content-Type": "text/html".
     *
     * @param contentType Send a post request with header value "Content-Type": "text/html".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramProtectedKeyWithResponse(String contentType, RequestOptions requestOptions) {
        return this.serviceClient.paramProtectedKeyWithResponseAsync(contentType, requestOptions);
    }

    /**
     * Get a response with header value "Content-Type": "text/html".
     *
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header value "Content-Type": "text/html" along with {@link Response} on successful
     *     completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseProtectedKeyWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.responseProtectedKeyWithResponseAsync(requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "positive", "value": 1 or "scenario": "negative", "value": -2.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param value Send a post request with header values 1 or -2.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramIntegerWithResponse(String scenario, int value, RequestOptions requestOptions) {
        return this.serviceClient.paramIntegerWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header value "value": 1 or -2.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header value "value": 1 or -2 along with {@link Response} on successful completion of
     *     {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseIntegerWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseIntegerWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "positive", "value": 105 or "scenario": "negative", "value":
     * -2.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param value Send a post request with header values 105 or -2.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramLongWithResponse(String scenario, long value, RequestOptions requestOptions) {
        return this.serviceClient.paramLongWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header value "value": 105 or -2.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header value "value": 105 or -2 along with {@link Response} on successful completion of
     *     {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseLongWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseLongWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "positive", "value": 0.07 or "scenario": "negative", "value":
     * -3.0.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param value Send a post request with header values 0.07 or -3.0.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramFloatWithResponse(String scenario, float value, RequestOptions requestOptions) {
        return this.serviceClient.paramFloatWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header value "value": 0.07 or -3.0.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header value "value": 0.07 or -3.0 along with {@link Response} on successful completion
     *     of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseFloatWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseFloatWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "positive", "value": 7e120 or "scenario": "negative", "value":
     * -3.0.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param value Send a post request with header values 7e120 or -3.0.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramDoubleWithResponse(String scenario, double value, RequestOptions requestOptions) {
        return this.serviceClient.paramDoubleWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header value "value": 7e120 or -3.0.
     *
     * @param scenario Send a post request with header values "scenario": "positive" or "negative".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header value "value": 7e120 or -3.0 along with {@link Response} on successful completion
     *     of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseDoubleWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseDoubleWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "true", "value": true or "scenario": "false", "value": false.
     *
     * @param scenario Send a post request with header values "scenario": "true" or "false".
     * @param value Send a post request with header values true or false.
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramBoolWithResponse(String scenario, boolean value, RequestOptions requestOptions) {
        return this.serviceClient.paramBoolWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header value "value": true or false.
     *
     * @param scenario Send a post request with header values "scenario": "true" or "false".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header value "value": true or false along with {@link Response} on successful completion
     *     of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseBoolWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseBoolWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "valid", "value": "The quick brown fox jumps over the lazy
     * dog" or "scenario": "null", "value": null or "scenario": "empty", "value": "".
     *
     * <p><strong>Header Parameters</strong>
     *
     * <table border="1">
     *     <caption>Header Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>value</td><td>String</td><td>No</td><td>Send a post request with header values "The quick brown fox jumps over the lazy dog" or null or ""</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addHeader}
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "null" or "empty".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramStringWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.paramStringWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Get a response with header values "The quick brown fox jumps over the lazy dog" or null or "".
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "null" or "empty".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header values "The quick brown fox jumps over the lazy dog" or null or "" along with
     *     {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseStringWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseStringWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "valid", "value": "2010-01-01" or "scenario": "min", "value":
     * "0001-01-01".
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "min".
     * @param value Send a post request with header values "2010-01-01" or "0001-01-01".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramDateWithResponse(String scenario, String value, RequestOptions requestOptions) {
        return this.serviceClient.paramDateWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header values "2010-01-01" or "0001-01-01".
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "min".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header values "2010-01-01" or "0001-01-01" along with {@link Response} on successful
     *     completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseDateWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseDateWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "valid", "value": "2010-01-01T12:34:56Z" or "scenario": "min",
     * "value": "0001-01-01T00:00:00Z".
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "min".
     * @param value Send a post request with header values "2010-01-01T12:34:56Z" or "0001-01-01T00:00:00Z".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramDatetimeWithResponse(
            String scenario, OffsetDateTime value, RequestOptions requestOptions) {
        return this.serviceClient.paramDatetimeWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header values "2010-01-01T12:34:56Z" or "0001-01-01T00:00:00Z".
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "min".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header values "2010-01-01T12:34:56Z" or "0001-01-01T00:00:00Z" along with {@link
     *     Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseDatetimeWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseDatetimeWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "valid", "value": "Wed, 01 Jan 2010 12:34:56 GMT" or
     * "scenario": "min", "value": "Mon, 01 Jan 0001 00:00:00 GMT".
     *
     * <p><strong>Header Parameters</strong>
     *
     * <table border="1">
     *     <caption>Header Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>value</td><td>OffsetDateTime</td><td>No</td><td>Send a post request with header values "Wed, 01 Jan 2010 12:34:56 GMT" or "Mon, 01 Jan 0001 00:00:00 GMT"</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addHeader}
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "min".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramDatetimeRfc1123WithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.paramDatetimeRfc1123WithResponseAsync(scenario, requestOptions);
    }

    /**
     * Get a response with header values "Wed, 01 Jan 2010 12:34:56 GMT" or "Mon, 01 Jan 0001 00:00:00 GMT".
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "min".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header values "Wed, 01 Jan 2010 12:34:56 GMT" or "Mon, 01 Jan 0001 00:00:00 GMT" along
     *     with {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseDatetimeRfc1123WithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseDatetimeRfc1123WithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "valid", "value": "P123DT22H14M12.011S".
     *
     * @param scenario Send a post request with header values "scenario": "valid".
     * @param value Send a post request with header values "P123DT22H14M12.011S".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramDurationWithResponse(
            String scenario, Duration value, RequestOptions requestOptions) {
        return this.serviceClient.paramDurationWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header values "P123DT22H14M12.011S".
     *
     * @param scenario Send a post request with header values "scenario": "valid".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header values "P123DT22H14M12.011S" along with {@link Response} on successful completion
     *     of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseDurationWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseDurationWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "valid", "value": "啊齄丂狛狜隣郎隣兀﨩".
     *
     * @param scenario Send a post request with header values "scenario": "valid".
     * @param value Send a post request with header values "啊齄丂狛狜隣郎隣兀﨩".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramByteWithResponse(String scenario, byte[] value, RequestOptions requestOptions) {
        return this.serviceClient.paramByteWithResponseAsync(scenario, value, requestOptions);
    }

    /**
     * Get a response with header values "啊齄丂狛狜隣郎隣兀﨩".
     *
     * @param scenario Send a post request with header values "scenario": "valid".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header values "啊齄丂狛狜隣郎隣兀﨩" along with {@link Response} on successful completion of {@link
     *     Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseByteWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseByteWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send a post request with header values "scenario": "valid", "value": "GREY" or "scenario": "null", "value": null.
     *
     * <p><strong>Header Parameters</strong>
     *
     * <table border="1">
     *     <caption>Header Parameters</caption>
     *     <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>
     *     <tr><td>value</td><td>String</td><td>No</td><td>Send a post request with header values 'GREY' . Allowed values: "White", "black", "GREY".</td></tr>
     * </table>
     *
     * You can add these to a request with {@link RequestOptions#addHeader}
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "null" or "empty".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> paramEnumWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.paramEnumWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Get a response with header values "GREY" or null.
     *
     * @param scenario Send a post request with header values "scenario": "valid" or "null" or "empty".
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return a response with header values "GREY" or null along with {@link Response} on successful completion of
     *     {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> responseEnumWithResponse(String scenario, RequestOptions requestOptions) {
        return this.serviceClient.responseEnumWithResponseAsync(scenario, requestOptions);
    }

    /**
     * Send x-ms-client-request-id = 9C4D50EE-2D56-4CD3-8152-34347DC9F2B0 in the header of the request.
     *
     * @param requestOptions The options to configure the HTTP request before HTTP client sends it.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws ClientAuthenticationException thrown if the request is rejected by server on status code 401.
     * @throws ResourceNotFoundException thrown if the request is rejected by server on status code 404.
     * @throws ResourceModifiedException thrown if the request is rejected by server on status code 409.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> customRequestIdWithResponse(RequestOptions requestOptions) {
        return this.serviceClient.customRequestIdWithResponseAsync(requestOptions);
    }
}
