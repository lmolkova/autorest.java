// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.mapper;

import com.azure.autorest.extension.base.model.codemodel.CodeModel;
import com.azure.autorest.extension.base.model.codemodel.ConstantSchema;
import com.azure.autorest.extension.base.model.codemodel.Operation;
import com.azure.autorest.extension.base.model.codemodel.OperationGroup;
import com.azure.autorest.extension.base.model.codemodel.Parameter;
import com.azure.autorest.extension.base.model.codemodel.Scheme;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientMethod;
import com.azure.autorest.model.clientmodel.ClientMethodParameter;
import com.azure.autorest.model.clientmodel.Constructor;
import com.azure.autorest.model.clientmodel.IType;
import com.azure.autorest.model.clientmodel.MethodGroupClient;
import com.azure.autorest.model.clientmodel.ParameterSynthesizedOrigin;
import com.azure.autorest.model.clientmodel.Proxy;
import com.azure.autorest.model.clientmodel.ProxyMethod;
import com.azure.autorest.model.clientmodel.SecurityInfo;
import com.azure.autorest.model.clientmodel.ServiceClient;
import com.azure.autorest.model.clientmodel.ServiceClientProperty;
import com.azure.autorest.model.javamodel.JavaVisibility;
import com.azure.autorest.util.ClientModelUtil;
import com.azure.autorest.util.CodeNamer;
import com.azure.autorest.util.MethodUtil;
import com.azure.core.util.CoreUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServiceClientMapper implements IMapper<CodeModel, ServiceClient> {
    private static final ServiceClientMapper INSTANCE = new ServiceClientMapper();

    private static final Pattern TRAILING_FORWARD_SLASH = Pattern.compile("/+$");
    private static final Pattern URL_PATH = Pattern.compile("(?<!/)[/][^/]+");

    protected ServiceClientMapper() {
    }

    public static ServiceClientMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ServiceClient map(CodeModel codeModel) {
        ServiceClient.Builder builder = createClientBuilder();
        builder.builderDisabled(JavaSettings.getInstance().clientBuilderDisabled());

        String serviceClientInterfaceName = ClientModelUtil.getClientInterfaceName(codeModel);
        String serviceClientClassName = ClientModelUtil.getClientImplementClassName(serviceClientInterfaceName);
        String packageName = ClientModelUtil.getServiceClientPackageName(serviceClientClassName);
        builder.interfaceName(serviceClientInterfaceName)
                .className(serviceClientClassName)
                .packageName(packageName);

        if (!CoreUtils.isNullOrEmpty(codeModel.getOperationGroups())) {
            builder.baseUrl(getBaseUrl(codeModel));
        }

        List<Operation> codeModelRestAPIMethods = codeModel.getOperationGroups().stream()
                .filter(og -> CoreUtils.isNullOrEmpty(og.getLanguage().getJava().getName()))
                .flatMap(og -> og.getOperations().stream())
                .collect(Collectors.toList());

        Proxy proxy = null;
        if (!codeModelRestAPIMethods.isEmpty()) {
            proxy = processClientOperations(builder, codeModelRestAPIMethods, serviceClientInterfaceName);
        } else {
            builder.clientMethods(Collections.emptyList());
        }

        List<MethodGroupClient> serviceClientMethodGroupClients = new ArrayList<>();
        List<OperationGroup> codeModelMethodGroups = codeModel.getOperationGroups().stream()
                .filter(og -> og.getLanguage().getJava().getName() != null &&
                        !og.getLanguage().getJava().getName().isEmpty())
                .collect(Collectors.toList());
        for (OperationGroup codeModelMethodGroup : codeModelMethodGroups) {
            serviceClientMethodGroupClients.add(Mappers.getMethodGroupMapper().map(codeModelMethodGroup));
        }
        builder.methodGroupClients(serviceClientMethodGroupClients);

        if (proxy == null && !serviceClientMethodGroupClients.isEmpty()) {
            proxy = serviceClientMethodGroupClients.iterator().next().getProxy();
        }

        processParametersAndConstructors(builder, codeModel, ClientModelUtil.getServiceVersionClassName(serviceClientInterfaceName), proxy);

        return builder.build();
    }

    protected Proxy.Builder getProxyBuilder() {
        return new Proxy.Builder();
    }

    protected ClientMethodParameter createSerializerAdapterParameter() {
        return new ClientMethodParameter.Builder()
                .description("The serializer to serialize an object into a string")
                .finalParameter(false)
                .wireType(ClassType.SerializerAdapter)
                .name("serializerAdapter")
                .required(true)
                .constant(false)
                .fromClient(true)
                .defaultValue(null)
                .annotations(JavaSettings.getInstance().isNonNullAnnotations()
                        ? Collections.singletonList(ClassType.NonNull)
                        : new ArrayList<>())
                .build();
    }

    protected IType getHttpPipelineClassType() {
        return ClassType.HttpPipeline;
    }

    protected void addSerializerAdapterProperty(List<ServiceClientProperty> serviceClientProperties, com.azure.autorest.extension.base.plugin.JavaSettings settings) {
        serviceClientProperties.add(new ServiceClientProperty("The serializer to serialize an object into a string.",
                ClassType.SerializerAdapter, "serializerAdapter", true, null,
                settings.isFluent() ? JavaVisibility.PackagePrivate : JavaVisibility.Public));
    }

    protected void addHttpPipelineProperty(List<ServiceClientProperty> serviceClientProperties) {
        serviceClientProperties.add(new ServiceClientProperty("The HTTP pipeline to send requests through.",
                ClassType.HttpPipeline, "httpPipeline", true, null));
    }

    protected ServiceClient.Builder createClientBuilder() {
        return new ServiceClient.Builder();
    }

    protected static String getBaseUrl(CodeModel codeModel) {
        // assume all operations share the same base url
        return codeModel.getOperationGroups().get(0).getOperations().get(0).getRequests().get(0)
                .getProtocol().getHttp().getUri();
    }

    protected static String getBaseUrl(Operation operation) {
        // assume all operations share the same base url
        return operation.getRequests().get(0)
                .getProtocol().getHttp().getUri();
    }

    protected Proxy processClientOperations(ServiceClient.Builder builder, List<Operation> operations, String baseName) {
        JavaSettings settings = JavaSettings.getInstance();

        // TODO: Assume all operations share the same base url
        Proxy.Builder proxyBuilder = getProxyBuilder()
                .name(baseName + "Service")
                .clientTypeName(baseName)
                .baseURL(getBaseUrl(operations.iterator().next()));
        List<ProxyMethod> restAPIMethods = new ArrayList<>();
        for (Operation operation : operations) {
            if (settings.isDataPlaneClient()) {
                MethodUtil.tryMergeBinaryRequestsAndUpdateOperation(operation.getRequests(), operation);
            }
            restAPIMethods.addAll(Mappers.getProxyMethodMapper().map(operation).values().stream().flatMap(Collection::stream).collect(Collectors.toList()));
        }
        proxyBuilder.methods(restAPIMethods);
        Proxy proxy = proxyBuilder.build();
        builder.proxy(proxy);
        List<ClientMethod> clientMethods = operations.stream()
                .flatMap(m -> Mappers.getClientMethodMapper().map(m).stream())
                .collect(Collectors.toList());
        if (settings.isGenerateSendRequestMethod()) {
            clientMethods.add(ClientMethod.getAsyncSendRequestClientMethod(false));
            if (settings.getSyncMethods() != JavaSettings.SyncMethodsGeneration.NONE) {
                clientMethods.add(ClientMethod.getSyncSendRequestClientMethod(false));
            }
        }
        builder.clientMethods(clientMethods);

        return proxy;
    }

    protected void processParametersAndConstructors(ServiceClient.Builder builder, CodeModel codeModel, String serviceVersionClassName, Proxy proxy) {
        JavaSettings settings = JavaSettings.getInstance();

        List<ServiceClientProperty> serviceClientProperties = new ArrayList<>();
        List<Parameter> clientParameters = Stream.concat(codeModel.getGlobalParameters().stream(),
                        codeModel.getOperationGroups().stream()
                                .flatMap(og -> og.getOperations().stream())
                                .flatMap(o -> o.getRequests().stream())
                                .flatMap(r -> r.getParameters().stream()))
                .filter(p -> p.getImplementation() == Parameter.ImplementationLocation.CLIENT)
                .distinct()
                .collect(Collectors.toList());
        for (Parameter p : clientParameters) {
            String serviceClientPropertyDescription =
                    p.getDescription() != null ? p.getDescription() : p.getLanguage().getJava().getDescription();

            String serviceClientPropertyName = CodeNamer.getPropertyName(p.getLanguage().getJava().getName());

            IType serviceClientPropertyClientType = Mappers.getSchemaMapper().map(p.getSchema());
            if (p.isNullable() && serviceClientPropertyClientType != null) {
                serviceClientPropertyClientType = serviceClientPropertyClientType.asNullable();
            }

            boolean serviceClientPropertyIsReadOnly = p.getSchema() instanceof ConstantSchema;
            if (!settings.isFluent()) {
                serviceClientPropertyIsReadOnly = false;
            }
            String serviceClientPropertyDefaultValueExpression = serviceClientPropertyClientType.defaultValueExpression(ClientModelUtil.getClientDefaultValueOrConstantValue(p));
            boolean serviceClientPropertyRequired = p.isRequired();
            String serializedName = p.getLanguage().getDefault().getSerializedName();

            if (settings.isDataPlaneClient() && ParameterSynthesizedOrigin.fromValue(p.getOrigin()) == ParameterSynthesizedOrigin.API_VERSION) {
                serviceClientPropertyDescription = "Service version";
                serviceClientPropertyClientType = new ClassType.Builder()
                        .name(serviceVersionClassName)
                        .packageName(settings.getPackage())
                        .build();
                serviceClientPropertyName = "serviceVersion";
                serviceClientPropertyIsReadOnly = false;
                serviceClientPropertyDefaultValueExpression = serviceVersionClassName + ".getLatest()";
                serviceClientPropertyRequired = false;
            }

            if (serviceClientPropertyClientType != ClassType.TokenCredential) {
                ServiceClientProperty serviceClientProperty =
                        new ServiceClientProperty.Builder()
                                .description(serviceClientPropertyDescription)
                                .type(serviceClientPropertyClientType)
                                .name(serviceClientPropertyName)
                                .readOnly(serviceClientPropertyIsReadOnly)
                                .defaultValueExpression(serviceClientPropertyDefaultValueExpression)
                                .required(serviceClientPropertyRequired)
                                .requestParameterName(serializedName)
                                .build();
                if (!serviceClientProperties.contains(serviceClientProperty)) {
                    // Ignore duplicate client property.
                    serviceClientProperties.add(serviceClientProperty);
                }
            }
        }
        addHttpPipelineProperty(serviceClientProperties);
        addSerializerAdapterProperty(serviceClientProperties, settings);
        if (settings.isFluent()) {
            serviceClientProperties.add(new ServiceClientProperty.Builder()
                    .description("The default poll interval for long-running operation.")
                    .type(ClassType.Duration)
                    .name("defaultPollInterval")
                    .readOnly(true)
                    .build());
        }

        builder.properties(serviceClientProperties);

        ClientMethodParameter tokenCredentialParameter = new ClientMethodParameter.Builder()
                .description("the credentials for Azure")
                .finalParameter(false)
                .wireType(ClassType.TokenCredential)
                .name("credential")
                .required(true)
                .constant(false)
                .fromClient(true)
                .defaultValue(null)
                .annotations(JavaSettings.getInstance().isNonNullAnnotations()
                        ? Collections.singletonList(ClassType.NonNull)
                        : new ArrayList<>())
                .build();

        ClientMethodParameter httpPipelineParameter = new ClientMethodParameter.Builder()
                .description("The HTTP pipeline to send requests through")
                .finalParameter(false)
                .wireType(getHttpPipelineClassType())
                .name("httpPipeline")
                .required(true)
                .constant(false)
                .fromClient(true)
                .defaultValue(null)
                .annotations(JavaSettings.getInstance().isNonNullAnnotations()
                        ? Collections.singletonList(ClassType.NonNull)
                        : new ArrayList<>())
                .build();

        ClientMethodParameter serializerAdapterParameter = createSerializerAdapterParameter();

        // map security information in code model to ServiceClient.SecurityInfo
        SecurityInfo securityInfo = new SecurityInfo();
        if (codeModel.getSecurity() != null &&
                codeModel.getSecurity().getSchemes() != null &&
                codeModel.getSecurity().isAuthenticationRequired()) {
            final String userImpersonationScope = "user_impersonation";

            SecurityInfo securityInfoInCodeModel = new SecurityInfo();
            codeModel.getSecurity().getSchemes().forEach(securityScheme -> {
                // hack, ignore "user_impersonation", as these non-AADToken appears in modelerfour 4.23.0+
                if (securityScheme.getType() == Scheme.SecuritySchemeType.OAUTH2
                        && securityScheme.getScopes().size() == 1
                        && userImpersonationScope.equals(securityScheme.getScopes().iterator().next())) {
                    return;
                }

                securityInfoInCodeModel.getSecurityTypes().add(securityScheme.getType());
                if (securityScheme.getType().equals(Scheme.SecuritySchemeType.OAUTH2)) {
                    Set<String> credentialScopes = securityScheme.getScopes().stream()
                            .filter(s -> !userImpersonationScope.equals(s)) // hack, filter out "user_impersonation"
                            .map(scope -> {
                                if (!scope.startsWith("\"")) {
                                    return "\"" + scope + "\"";
                                } else {
                                    return scope;
                                }
                            }).collect(Collectors.toSet());
                    securityInfoInCodeModel.setScopes(credentialScopes);
                }
                if (securityScheme.getType().equals(Scheme.SecuritySchemeType.KEY)) {
                    securityInfoInCodeModel.setHeaderName(securityScheme.getName());
                }
            });
            securityInfo = securityInfoInCodeModel;
        }

        // overwrite securityInfo using JavaSettings
        if (settings.getCredentialTypes() != null && !settings.getCredentialTypes().isEmpty() &&
                !settings.getCredentialTypes().contains(JavaSettings.CredentialType.NONE)) {
            SecurityInfo securityInfoInJavaSettings = new SecurityInfo();
            if (settings.getCredentialTypes().contains(JavaSettings.CredentialType.TOKEN_CREDENTIAL)) {
                securityInfoInJavaSettings.getSecurityTypes().add(Scheme.SecuritySchemeType.OAUTH2);
                securityInfoInJavaSettings.setScopes(settings.getCredentialScopes());
            }
            if (settings.getCredentialTypes().contains(JavaSettings.CredentialType.AZURE_KEY_CREDENTIAL)) {
                securityInfoInJavaSettings.getSecurityTypes().add(Scheme.SecuritySchemeType.KEY);
                securityInfoInJavaSettings.setHeaderName(settings.getKeyCredentialHeaderName());
            }
            securityInfo = securityInfoInJavaSettings;
        }
        builder.securityInfo(securityInfo);

        if (securityInfo.getSecurityTypes().contains(Scheme.SecuritySchemeType.OAUTH2)) {
            Set<String> scopes = securityInfo.getScopes();
            String scopeParams;
            if (scopes != null && !scopes.isEmpty()) {
                scopeParams = "DEFAULT_SCOPES";
            } else {
                // Remove trailing / and all relative paths
                String host = TRAILING_FORWARD_SLASH.matcher(proxy.getBaseURL()).replaceAll("");
                host = URL_PATH.matcher(host).replaceAll("");
                List<String> parameters = new ArrayList<>();
                int start = host.indexOf("{");
                while (start >= 0) {
                    int end = host.indexOf("}", start);
                    String serializedName = host.substring(start + 1, end);
                    Optional<Parameter> hostParam = clientParameters.stream().filter(p -> serializedName.equals(p.getLanguage().getJava().getSerializedName())).findFirst();
                    if (hostParam.isPresent()) {
                        parameters.add(hostParam.get().getLanguage().getJava().getName());
                        host = host.substring(0, start) + "%s" + host.substring(end + 1);
                    }
                    start = host.indexOf("{", start + 1);
                }
                if (parameters.isEmpty()) {
                    scopeParams = String.format("\"%s/.default\"", host);
                } else {
                    scopeParams = String.format("String.format(\"%s/.default\", %s)", host, String.join(", ", parameters));
                }
            }
            builder.defaultCredentialScopes(scopeParams);
        }

        List<Constructor> serviceClientConstructors = new ArrayList<>();

        if (settings.isFluent()) {
            ClientMethodParameter azureEnvironmentParameter = new ClientMethodParameter.Builder()
                    .description("The Azure environment")
                    .finalParameter(false)
                    .wireType(ClassType.AzureEnvironment)
                    .name("environment")
                    .required(true)
                    .constant(false)
                    .fromClient(true)
                    .defaultValue("AzureEnvironment.AZURE")
                    .annotations(JavaSettings.getInstance().isNonNullAnnotations()
                            ? Collections.singletonList(ClassType.NonNull)
                            : new ArrayList<>())
                    .build();

            ClientMethodParameter defaultPollIntervalParameter = new ClientMethodParameter.Builder()
                    .description("The default poll interval for long-running operation")
                    .finalParameter(false)
                    .wireType(ClassType.Duration)
                    .name("defaultPollInterval")
                    .required(true)
                    .constant(false)
                    .fromClient(true)
                    .defaultValue("Duration.ofSeconds(30)")
                    .annotations(JavaSettings.getInstance().isNonNullAnnotations()
                            ? Collections.singletonList(ClassType.NonNull)
                            : new ArrayList<>())
                    .build();

            serviceClientConstructors.add(new Constructor(Arrays.asList(httpPipelineParameter, serializerAdapterParameter, defaultPollIntervalParameter, azureEnvironmentParameter)));
            builder.tokenCredentialParameter(tokenCredentialParameter)
                    .httpPipelineParameter(httpPipelineParameter)
                    .serializerAdapterParameter(serializerAdapterParameter)
                    .defaultPollIntervalParameter(defaultPollIntervalParameter)
                    .azureEnvironmentParameter(azureEnvironmentParameter)
                    .constructors(serviceClientConstructors);
        } else {
            serviceClientConstructors.add(new Constructor(new ArrayList<>()));
            serviceClientConstructors.add(new Constructor(Collections.singletonList(httpPipelineParameter)));
            serviceClientConstructors.add(new Constructor(Arrays.asList(httpPipelineParameter, serializerAdapterParameter)));
            builder.tokenCredentialParameter(tokenCredentialParameter)
                    .httpPipelineParameter(httpPipelineParameter)
                    .serializerAdapterParameter(serializerAdapterParameter)
                    .constructors(serviceClientConstructors);
        }
    }
}
