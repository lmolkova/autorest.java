// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.template;


import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientResponse;
import com.azure.autorest.model.clientmodel.GenericType;
import com.azure.autorest.model.clientmodel.IType;
import com.azure.autorest.model.javamodel.JavaFile;

import java.util.HashSet;
import java.util.Set;

/**
 * Writes a ClientResponse to a JavaFile.
 */
public class ResponseTemplate implements IJavaTemplate<ClientResponse, JavaFile> {
    private static final ResponseTemplate INSTANCE = new ResponseTemplate();

    protected ResponseTemplate() {
    }

    public static ResponseTemplate getInstance() {
        return INSTANCE;
    }

    public final void write(ClientResponse response, JavaFile javaFile) {
        JavaSettings settings = JavaSettings.getInstance();

        Set<String> imports = new HashSet<>();
        addRequestAndHeaderImports(imports);
        IType restResponseType = getRestResponseType(response);
        restResponseType.addImportsTo(imports, true);

        boolean isStreamResponse = response.getBodyType().equals(GenericType.FluxByteBuffer);

        // Stream responses implement Closeable to offer a way for the Flux<ByteBuffer> response to be drained
        // if it isn't consumed.
        if (isStreamResponse) {
            imports.add("java.io.Closeable");
        }

        javaFile.declareImport(imports);

        String classSignature;
        if (isStreamResponse) {
            classSignature = String.format("%1$s extends %2$s implements Closeable", response.getName(),
                restResponseType);
        } else if (settings.isGenericResponseTypes()) {
            classSignature = restResponseType.toString();
        } else {
            classSignature = String.format("%1$s extends %2$s", response.getName(), restResponseType);
        }

        javaFile.javadocComment(javadoc -> javadoc.description(response.getDescription()));

        javaFile.publicFinalClass(classSignature, classBlock -> {
            classBlock.javadocComment(javadoc -> {
                javadoc.description(String.format("Creates an instance of %1$s.", response.getName()));
                javadoc.param("request", String.format("the request which resulted in this %1$s.", response.getName()));
                javadoc.param("statusCode", "the status code of the HTTP response");
                javadoc.param("rawHeaders", "the raw headers of the HTTP response");
                javadoc.param("value", isStreamResponse ? "the content stream" : "the deserialized value of the HTTP response");
                javadoc.param("headers", "the deserialized headers of the HTTP response");
            });

            classBlock.publicConstructor(String.format("%1$s(HttpRequest request, int statusCode, HttpHeaders rawHeaders, %2$s value, %3$s headers)",
                response.getName(), response.getBodyType().asNullable(), response.getHeadersType()),
                ctorBlock -> ctorBlock.line("super(request, statusCode, rawHeaders, value, headers);"));

            if (!response.getBodyType().asNullable().equals(ClassType.Void)) {
                if (response.getBodyType().equals(GenericType.FluxByteBuffer)) {
                    classBlock.javadocComment(javadoc -> {
                        javadoc.description("Gets the response content stream.");
                        javadoc.methodReturns("the response content stream");
                    });
                } else {
                    classBlock.javadocComment(javadoc -> {
                        javadoc.description("Gets the deserialized response body.");
                        javadoc.methodReturns("the deserialized response body");
                    });
                }

                classBlock.annotation("Override");
                classBlock.publicMethod(String.format("%1$s getValue()", response.getBodyType().asNullable()),
                    methodBlock -> methodBlock.methodReturn("super.getValue()"));
            }

            if (isStreamResponse) {
                classBlock.javadocComment(javadoc -> javadoc.description("Disposes of the connection associated with this stream response."));
                classBlock.annotation("Override");
                classBlock.publicMethod("void close()", methodBlock -> methodBlock.line("value().subscribe(bb -> { }, t -> { }).dispose();"));
            }
        });
    }

    protected IType getRestResponseType(ClientResponse response) {
        return GenericType.RestResponse(response.getHeadersType(), response.getBodyType());
    }

    protected void addRequestAndHeaderImports(java.util.Set<String> imports) {
        imports.add("com.azure.core.http.HttpRequest");
        imports.add("com.azure.core.http.HttpHeaders");
    }
}
