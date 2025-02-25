// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex;

import com.azure.core.annotation.Generated;
import com.azure.core.annotation.ReturnType;
import com.azure.core.annotation.ServiceClient;
import com.azure.core.annotation.ServiceMethod;
import com.azure.core.http.rest.Response;
import fixtures.bodycomplex.implementation.PolymorphicrecursivesImpl;
import fixtures.bodycomplex.implementation.models.ErrorException;
import fixtures.bodycomplex.implementation.models.Fish;
import reactor.core.publisher.Mono;

/** Initializes a new instance of the asynchronous AutoRestComplexTestService type. */
@ServiceClient(builder = AutoRestComplexTestServiceBuilder.class, isAsync = true)
public final class PolymorphicrecursiveAsyncClient {
    @Generated private final PolymorphicrecursivesImpl serviceClient;

    /**
     * Initializes an instance of PolymorphicrecursiveAsyncClient class.
     *
     * @param serviceClient the service client implementation.
     */
    @Generated
    PolymorphicrecursiveAsyncClient(PolymorphicrecursivesImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Get complex types that are polymorphic and have recursive references.
     *
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return complex types that are polymorphic and have recursive references along with {@link Response} on
     *     successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Fish>> getValidWithResponse() {
        return this.serviceClient.getValidWithResponseAsync();
    }

    /**
     * Get complex types that are polymorphic and have recursive references.
     *
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return complex types that are polymorphic and have recursive references on successful completion of {@link
     *     Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Fish> getValid() {
        return this.serviceClient.getValidAsync();
    }

    /**
     * Put complex types that are polymorphic and have recursive references.
     *
     * @param complexBody Please put a salmon that looks like this: { "fishtype": "salmon", "species": "king", "length":
     *     1, "age": 1, "location": "alaska", "iswild": true, "siblings": [ { "fishtype": "shark", "species":
     *     "predator", "length": 20, "age": 6, "siblings": [ { "fishtype": "salmon", "species": "coho", "length": 2,
     *     "age": 2, "location": "atlantic", "iswild": true, "siblings": [ { "fishtype": "shark", "species": "predator",
     *     "length": 20, "age": 6 }, { "fishtype": "sawshark", "species": "dangerous", "length": 10, "age": 105 } ] }, {
     *     "fishtype": "sawshark", "species": "dangerous", "length": 10, "age": 105 } ] }, { "fishtype": "sawshark",
     *     "species": "dangerous", "length": 10, "age": 105 } ] }.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the {@link Response} on successful completion of {@link Mono}.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Response<Void>> putValidWithResponse(Fish complexBody) {
        return this.serviceClient.putValidWithResponseAsync(complexBody);
    }

    /**
     * Put complex types that are polymorphic and have recursive references.
     *
     * @param complexBody Please put a salmon that looks like this: { "fishtype": "salmon", "species": "king", "length":
     *     1, "age": 1, "location": "alaska", "iswild": true, "siblings": [ { "fishtype": "shark", "species":
     *     "predator", "length": 20, "age": 6, "siblings": [ { "fishtype": "salmon", "species": "coho", "length": 2,
     *     "age": 2, "location": "atlantic", "iswild": true, "siblings": [ { "fishtype": "shark", "species": "predator",
     *     "length": 20, "age": 6 }, { "fishtype": "sawshark", "species": "dangerous", "length": 10, "age": 105 } ] }, {
     *     "fishtype": "sawshark", "species": "dangerous", "length": 10, "age": 105 } ] }, { "fishtype": "sawshark",
     *     "species": "dangerous", "length": 10, "age": 105 } ] }.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return A {@link Mono} that completes when a successful response is received.
     */
    @Generated
    @ServiceMethod(returns = ReturnType.SINGLE)
    public Mono<Void> putValid(Fish complexBody) {
        return this.serviceClient.putValidAsync(complexBody);
    }
}
