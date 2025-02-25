// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.specialwords.implementation;

import com.azure.core.http.HttpPipeline;
import com.azure.core.http.HttpPipelineBuilder;
import com.azure.core.http.policy.CookiePolicy;
import com.azure.core.http.policy.RetryPolicy;
import com.azure.core.http.policy.UserAgentPolicy;
import com.azure.core.util.serializer.JacksonAdapter;
import com.azure.core.util.serializer.SerializerAdapter;

/** Initializes a new instance of the SpecialWordsClient type. */
public final class SpecialWordsClientImpl {
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

    /** The OperationsImpl object to access its operations. */
    private final OperationsImpl operations;

    /**
     * Gets the OperationsImpl object to access its operations.
     *
     * @return the OperationsImpl object.
     */
    public OperationsImpl getOperations() {
        return this.operations;
    }

    /** The ParametersImpl object to access its operations. */
    private final ParametersImpl parameters;

    /**
     * Gets the ParametersImpl object to access its operations.
     *
     * @return the ParametersImpl object.
     */
    public ParametersImpl getParameters() {
        return this.parameters;
    }

    /** The ModelsImpl object to access its operations. */
    private final ModelsImpl models;

    /**
     * Gets the ModelsImpl object to access its operations.
     *
     * @return the ModelsImpl object.
     */
    public ModelsImpl getModels() {
        return this.models;
    }

    /** Initializes an instance of SpecialWordsClient client. */
    public SpecialWordsClientImpl() {
        this(
                new HttpPipelineBuilder()
                        .policies(new UserAgentPolicy(), new RetryPolicy(), new CookiePolicy())
                        .build(),
                JacksonAdapter.createDefaultSerializerAdapter());
    }

    /**
     * Initializes an instance of SpecialWordsClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     */
    public SpecialWordsClientImpl(HttpPipeline httpPipeline) {
        this(httpPipeline, JacksonAdapter.createDefaultSerializerAdapter());
    }

    /**
     * Initializes an instance of SpecialWordsClient client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param serializerAdapter The serializer to serialize an object into a string.
     */
    public SpecialWordsClientImpl(HttpPipeline httpPipeline, SerializerAdapter serializerAdapter) {
        this.httpPipeline = httpPipeline;
        this.serializerAdapter = serializerAdapter;
        this.operations = new OperationsImpl(this);
        this.parameters = new ParametersImpl(this);
        this.models = new ModelsImpl(this);
    }
}
