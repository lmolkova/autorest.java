// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.android.template;

import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.AsyncSyncClient;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ListType;
import com.azure.autorest.model.clientmodel.SecurityInfo;
import com.azure.autorest.model.clientmodel.ServiceClientProperty;
import com.azure.autorest.model.javamodel.JavaBlock;
import com.azure.autorest.model.javamodel.JavaContext;
import com.azure.autorest.template.ServiceClientBuilderTemplate;

import java.util.ArrayList;
import java.util.Set;

public class AndroidServiceClientBuilderTemplate extends ServiceClientBuilderTemplate {
    private static ServiceClientBuilderTemplate _instance = new AndroidServiceClientBuilderTemplate();

    protected AndroidServiceClientBuilderTemplate() {
    }

    public static ServiceClientBuilderTemplate getInstance() {
        return _instance;
    }

    @Override
    protected void writeSyncClientBuildMethod(AsyncSyncClient syncClient, AsyncSyncClient asyncClient, JavaBlock function,
                                              String buildMethodName, boolean wrapServiceClient) {
        writeSyncClientBuildMethodFromInnerClient(syncClient, function, buildMethodName, wrapServiceClient);
    }

    @Override
    protected String getSerializerMemberName() {
        return "JacksonSerder.createDefault()";
    }

    @Override
    protected ArrayList<ServiceClientProperty> addCommonClientProperties(JavaSettings settings, SecurityInfo securityInfo) {
        ArrayList<ServiceClientProperty> commonProperties = new ArrayList<ServiceClientProperty>();


        commonProperties.add(new ServiceClientProperty("The HTTP pipeline to send requests through",
                ClassType.AndroidHttpPipeline,
                "pipeline",
                false,
                "createHttpPipeline()"));

        commonProperties.add(new ServiceClientProperty("The HTTP client used to send the request.",
                ClassType.AndroidHttpClient,
                "httpClient",
                false,
                null));

        commonProperties.add(new ServiceClientProperty("The logging configuration for HTTP requests and "
                + "responses.",
                ClassType.AndroidHttpLogOptions,
                "httpLogOptions",
                false,
                null));
        commonProperties.add(new ServiceClientProperty("The retry policy that will attempt to retry failed "
                + "requests, if applicable.",
                ClassType.AndroidRetryPolicy,
                "retryPolicy",
                false,
                null));

        commonProperties.add(new ServiceClientProperty("The list of Http pipeline policies to add.",
                new ListType(ClassType.AndroidHttpPipelinePolicy),
                "pipelinePolicies",
                true,
                null));

        return commonProperties;
    }

    @Override
    protected void addServiceClientBuilderAnnotationImport(Set<String> imports) {
        imports.add("com.azure.android.core.rest.annotation.ServiceClientBuilder");
    }

    @Override
    protected void addHttpPolicyImports(Set<String> imports) {
        imports.add("com.azure.android.core.http.policy.HttpLoggingPolicy");
    }

    @Override
    protected void addImportForCoreUtils(Set<String> imports) {
    }

    @Override
    protected void addSerializerImport(Set<String> imports, JavaSettings settings) {
        imports.add("com.azure.android.core.serde.jackson.JacksonSerder");
    }

    @Override
    protected void addCreateHttpPipelineMethod(com.azure.autorest.extension.base.plugin.JavaSettings settings,
                                               com.azure.autorest.model.javamodel.JavaClass classBlock,
                                               String defaultCredentialScopes, SecurityInfo securityInfo) {
        classBlock.privateMethod("HttpPipeline createHttpPipeline()", function -> {

            function.ifBlock("httpLogOptions == null", action -> {
                function.line("httpLogOptions = new HttpLogOptions();");
            });

            function.line("List<HttpPipelinePolicy> policies = new ArrayList<>();");

            function.line("String clientName = properties.get(SDK_NAME);");
            function.line("if (clientName == null) {");
            function.increaseIndent();
            function.line("clientName = \"UnknownName\";");
            function.decreaseIndent();
            function.line("}");
            function.line("String clientVersion = properties.get(SDK_VERSION);");
            function.line("if (clientVersion == null) {");
            function.increaseIndent();
            function.line("clientVersion = \"UnknownVersion\";");
            function.decreaseIndent();
            function.line("}");

            function.line("policies.add(new UserAgentPolicy(null, clientName, clientVersion));");
            function.line("policies.add(retryPolicy == null ? RetryPolicy.withExponentialBackoff() : retryPolicy);");
            function.line("policies.add(new CookiePolicy());");
            function.line("policies.addAll(this.pipelinePolicies);");
            function.line("policies.add(new HttpLoggingPolicy(httpLogOptions));");

            function.line("HttpPipeline httpPipeline = new HttpPipelineBuilder().policies(policies.toArray(new "
                    + "HttpPipelinePolicy[0])).httpClient(httpClient).build();");
            function.methodReturn("httpPipeline");
        });
    }

    @Override
    protected void addGeneratedImport(Set<String> imports) {
    }

    @Override
    protected void addGeneratedAnnotation(JavaContext classBlock) {
    }
}
