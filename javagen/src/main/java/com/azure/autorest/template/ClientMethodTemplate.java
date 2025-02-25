// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.template;

import com.azure.autorest.extension.base.model.codemodel.RequestParameterLocation;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.ArrayType;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientMethod;
import com.azure.autorest.model.clientmodel.ClientMethodParameter;
import com.azure.autorest.model.clientmodel.ClientMethodType;
import com.azure.autorest.model.clientmodel.EnumType;
import com.azure.autorest.model.clientmodel.GenericType;
import com.azure.autorest.model.clientmodel.IType;
import com.azure.autorest.model.clientmodel.IterableType;
import com.azure.autorest.model.clientmodel.ListType;
import com.azure.autorest.model.clientmodel.MethodTransformationDetail;
import com.azure.autorest.model.clientmodel.ParameterMapping;
import com.azure.autorest.model.clientmodel.PrimitiveType;
import com.azure.autorest.model.clientmodel.ProxyMethod;
import com.azure.autorest.model.clientmodel.ProxyMethodParameter;
import com.azure.autorest.model.javamodel.JavaBlock;
import com.azure.autorest.model.javamodel.JavaClass;
import com.azure.autorest.model.javamodel.JavaIfBlock;
import com.azure.autorest.model.javamodel.JavaInterface;
import com.azure.autorest.model.javamodel.JavaJavadocComment;
import com.azure.autorest.model.javamodel.JavaType;
import com.azure.autorest.model.javamodel.JavaVisibility;
import com.azure.autorest.util.CodeNamer;
import com.azure.autorest.util.MethodNamer;
import com.azure.autorest.util.MethodUtil;
import com.azure.autorest.util.TemplateUtil;
import com.azure.core.util.CoreUtils;
import com.azure.core.util.serializer.CollectionFormat;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Writes a ClientMethod to a JavaType block.
 */
public class ClientMethodTemplate extends ClientMethodTemplateBase {
    private static final ClientMethodTemplate INSTANCE = new ClientMethodTemplate();

    protected ClientMethodTemplate() {
    }

    public static ClientMethodTemplate getInstance() {
        return INSTANCE;
    }

    /**
     * Adds validations to the client method.
     *
     * @param function The client method code block.
     * @param expressionsToCheck Expressions to validate as non-null.
     * @param validateExpressions Expressions to validate with a custom validation (key is the expression, value is the
     * validation).
     * @param settings AutoRest generation settings, used to determine if validations should be added.
     */
    protected static void addValidations(JavaBlock function, List<String> expressionsToCheck,
        Map<String, String> validateExpressions, JavaSettings settings) {
        if (!settings.isClientSideValidations()) {
            return;
        }

        // Iteration of validateExpressions uses expressionsToCheck effectively as a set lookup, may as well turn the
        // expressionsToCheck to a set.
        Set<String> expressionsToCheckSet = new LinkedHashSet<>(expressionsToCheck);

        for (String expressionToCheck : expressionsToCheckSet) {
            // TODO (alzimmer): Need to discuss if this can be changed to the more appropriate NullPointerException.
            String exceptionExpression = String.format("new IllegalArgumentException(\"Parameter %s is required and cannot be null.\")", expressionToCheck);

            // TODO (alzimmer): Determine if the assumption being made here are always true.
            // 1. Assumes that the expression is nullable.
            // 2. Assumes that the client method returns a reactive response.
            // 3. Assumes that the reactive response is a Mono.
            JavaIfBlock nullCheck = function.ifBlock(expressionToCheck + " == null", ifBlock -> {
                if (JavaSettings.getInstance().isSyncStackEnabled()) {
                    if (settings.isUseClientLogger()) {
                        ifBlock.line("throw LOGGER.logExceptionAsError(%s);", exceptionExpression);
                    } else {
                        ifBlock.line("throw %s;", exceptionExpression);
                    }
                } else {
                    ifBlock.methodReturn(String.format("Mono.error(%s)", exceptionExpression));
                }
            });

            String potentialValidateExpression = validateExpressions.get(expressionToCheck);
            if (potentialValidateExpression != null) {
                nullCheck.elseBlock(elseBlock -> elseBlock.line(potentialValidateExpression + ";"));
            }
        }

        for (Map.Entry<String, String> validateExpression : validateExpressions.entrySet()) {
            if (!expressionsToCheckSet.contains(validateExpression.getKey())) {
                function.ifBlock(validateExpression.getKey() + " != null",
                    ifBlock -> ifBlock.line(validateExpression.getValue() + ";"));
            }
        }
    }

    /**
     * Adds optional variable instantiations into the client method.
     *
     * @param function The client method code block.
     * @param clientMethod The client method.
     */
    protected static void addOptionalVariables(JavaBlock function, ClientMethod clientMethod) {
        if (!clientMethod.getOnlyRequiredParameters()) {
            return;
        }

        for (ClientMethodParameter parameter : clientMethod.getMethodParameters()) {
            // Parameter is required and will be part of the method signature.
            if (parameter.isRequired()) {
                continue;
            }

            IType parameterClientType = parameter.getClientType();
            String defaultValue = parameterClientType.defaultValueExpression(parameter.getDefaultValue());
            function.line("final %s %s = %s;", parameterClientType, parameter.getName(),
                defaultValue == null ? "null" : defaultValue);
        }
    }

    /**
     * Adds optional variable instantiations and constant variables into the client method.
     *
     * @param function The client method code block.
     * @param clientMethod The client method.
     * @param proxyMethodAndConstantParameters Proxy method constant parameters.
     * @param settings AutoRest generation settings.
     */
    protected static void addOptionalAndConstantVariables(JavaBlock function, ClientMethod clientMethod,
        List<ProxyMethodParameter> proxyMethodAndConstantParameters, JavaSettings settings) {
        addOptionalAndConstantVariables(function, clientMethod, proxyMethodAndConstantParameters, settings, true, true,
            true);
    }

    /**
     * Add optional and constant variables.
     *
     * @param function The client method code block.
     * @param clientMethod The client method.
     * @param proxyMethodAndConstantParameters Proxy method constant parameters.
     * @param settings AutoRest generation settings.
     * @param addOptional Whether optional variable instantiations are added, initialized to default or null.
     * @param addConstant Whether constant variables are added, initialized to default.
     * @param ignoreParameterNeedConvert When adding optional/constant variable, ignore those which need conversion from
     * client type to wire type. Let "ConvertClientTypesToWireTypes" handle them.
     */
    protected static void addOptionalAndConstantVariables(JavaBlock function, ClientMethod clientMethod,
        List<ProxyMethodParameter> proxyMethodAndConstantParameters, JavaSettings settings, boolean addOptional,
        boolean addConstant, boolean ignoreParameterNeedConvert) {
        for (ProxyMethodParameter parameter : proxyMethodAndConstantParameters) {
            IType parameterWireType = parameter.getWireType();
            if (parameter.isNullable()) {
                parameterWireType = parameterWireType.asNullable();
            }
            IType parameterClientType = parameter.getClientType();

            // TODO (alzimmer): There are a few similar transforms like this but they all have slight nuances on output.
            // This always turns ArrayType and ListType into String, the case further down this file may not.
            if (parameterWireType != ClassType.Base64Url
                && parameter.getRequestParameterLocation() != RequestParameterLocation.BODY
                //&& parameter.getRequestParameterLocation() != RequestParameterLocation.FormData
                && (parameterClientType instanceof ArrayType || parameterClientType instanceof ListType)) {
                parameterWireType = ClassType.String;
            }

            // If the parameter isn't required and the client method only uses required parameters optional
            // parameters are omitted and will need to instantiated in the method.
            boolean optionalOmitted = clientMethod.getOnlyRequiredParameters() && !parameter.isRequired();

            // Optional variables and constants are always null if their wire type and client type differ and applying
            // conversions between the types is ignored.
            boolean alwaysNull = ignoreParameterNeedConvert && parameterWireType != parameterClientType
                && optionalOmitted;

            // Constants should be included if the parameter is a constant and it's either required or optional
            // constants aren't generated as enums.
            boolean includeConstant = parameter.isConstant() &&
                (!settings.isOptionalConstantAsEnum() || parameter.isRequired());

            // Client methods only add local variable instantiations when the parameter isn't passed by the caller,
            // isn't always null, is an optional parameter that was omitted or is a constant that is either required
            // or AutoRest isn't generating with optional constant as enums.
            if (!parameter.isFromClient()
                && !alwaysNull
                && ((addOptional && optionalOmitted) || (addConstant && includeConstant))) {
                String defaultValue = parameterClientType.defaultValueExpression(parameter.getDefaultValue());
                function.line("final %s %s = %s;", parameterClientType, parameter.getParameterReference(),
                    defaultValue == null ? "null" : defaultValue);
            }
        }
    }

    /**
     * Applies parameter transformations to the client method parameters.
     *
     * @param function The client method code block.
     * @param clientMethod The client method.
     * @param settings AutoRest generation settings.
     */
    protected static void applyParameterTransformations(JavaBlock function, ClientMethod clientMethod,
        JavaSettings settings) {
        for (MethodTransformationDetail transformation : clientMethod.getMethodTransformationDetails()) {
            if (transformation.getParameterMappings().isEmpty()) {
                // the case that this flattened parameter is not original parameter from any other parameters
                ClientMethodParameter outParameter = transformation.getOutParameter();
                if (outParameter.isRequired() && outParameter.getClientType() instanceof ClassType) {
                    function.line("%1$s %2$s = new %1$s();", outParameter.getClientType(), outParameter.getName());
                } else {
                    function.line("%1$s %2$s = null;", outParameter.getClientType(), outParameter.getName());
                }

                // TODO (alzimmer): Should this break here? What if there are subsequent method transformation details?
                break;
            }

            String nullCheck = transformation.getParameterMappings().stream()
                .filter(m -> !m.getInputParameter().isRequired())
                .map(m -> {
                    ClientMethodParameter parameter = m.getInputParameter();

                    String parameterName;
                    if (!parameter.isFromClient()) {
                        parameterName = parameter.getName();
                    } else {
                        parameterName = m.getInputParameterProperty().getName();
                    }

                    return parameterName + " != null";
                }).collect(Collectors.joining(" || "));

            boolean conditionalAssignment = !nullCheck.isEmpty()
                && !transformation.getOutParameter().isRequired()
                && !clientMethod.getOnlyRequiredParameters();

            // Use a mutable internal variable, leave the original name for effectively final variable
            String outParameterName = conditionalAssignment
                ? transformation.getOutParameter().getName() + "Internal"
                : transformation.getOutParameter().getName();
            if (conditionalAssignment) {
                function.line("%s %s = null;",
                    transformation.getOutParameter().getClientType(),
                    outParameterName);
                function.line("if (%s) {", nullCheck);
                function.increaseIndent();
            }

            IType transformationOutputParameterModelType = transformation.getOutParameter().getClientType();
            boolean generatedCompositeType = false;
            if (transformationOutputParameterModelType instanceof ClassType) {
                generatedCompositeType = ((ClassType) transformationOutputParameterModelType).getPackage().startsWith(settings.getPackage());
            }
            if (generatedCompositeType && transformation.getParameterMappings().stream().anyMatch(m -> m.getOutputParameterPropertyName() != null && !m.getOutputParameterPropertyName().isEmpty())) {
                String transformationOutputParameterModelCompositeTypeName = transformationOutputParameterModelType.toString();

                function.line("%s%s = new %s();",
                    !conditionalAssignment ? transformation.getOutParameter().getClientType() + " " : "",
                    outParameterName,
                    transformationOutputParameterModelCompositeTypeName);
            }

            for (ParameterMapping mapping : transformation.getParameterMappings()) {
                String inputPath;
                if (mapping.getInputParameterProperty() != null) {
                    inputPath = String.format("%s.%s()", mapping.getInputParameter().getName(),
                        CodeNamer.getModelNamer().modelPropertyGetterName(mapping.getInputParameterProperty()));
                } else {
                    inputPath = mapping.getInputParameter().getName();
                }

                if (clientMethod.getOnlyRequiredParameters() && !mapping.getInputParameter().isRequired()) {
                    inputPath = "null";
                }

                String getMapping;
                if (mapping.getOutputParameterPropertyName() != null) {
                    getMapping = String.format(".%s(%s)", CodeNamer.getModelNamer().modelPropertySetterName(mapping.getOutputParameterPropertyName()), inputPath);
                } else {
                    getMapping = String.format(" = %s", inputPath);
                }

                function.line("%s%s%s;",
                    !conditionalAssignment && !generatedCompositeType ? transformation.getOutParameter().getClientType() + " " : "",
                    outParameterName,
                    getMapping);
            }

            if (conditionalAssignment) {
                function.decreaseIndent();
                function.line("}");

                String name = transformation.getOutParameter().getName();
                if (clientMethod.getParameters().stream().anyMatch(param -> param.getName().equals(transformation.getOutParameter().getName()))) {
                    name = name + "Local";
                }
                function.line(String.format("%s %s = %s;",
                    transformation.getOutParameter().getClientType(),
                    name,
                    outParameterName));
            }
        }
    }

    /**
     * Converts the type represented to the client into the type that is sent over the wire to the service.
     *
     * @param function The client method code block.
     * @param clientMethod The client method.
     * @param autoRestMethodRetrofitParameters Rest API method parameters.
     * @param methodClientReference Client method client reference (can be removed as it is metadata on ClientMethod).
     * @param settings AutoRest generation settings.
     */
    protected static void convertClientTypesToWireTypes(JavaBlock function, ClientMethod clientMethod,
        List<ProxyMethodParameter> autoRestMethodRetrofitParameters, String methodClientReference,
        JavaSettings settings) {
        for (ProxyMethodParameter parameter : autoRestMethodRetrofitParameters) {
            IType parameterWireType = parameter.getWireType();

            if (parameter.isNullable()) {
                parameterWireType = parameterWireType.asNullable();
            }

            IType parameterClientType = parameter.getClientType();

            // TODO (alzimmer): Reconcile the logic here with that earlier in the file.
            // This check parameter explosion but earlier in the file it doesn't.
            if (parameterWireType != ClassType.Base64Url
                && parameter.getRequestParameterLocation() != RequestParameterLocation.BODY
                //&& parameter.getRequestParameterLocation() != RequestParameterLocation.FormData &&
                && (parameterClientType instanceof ArrayType || parameterClientType instanceof ListType)) {
                parameterWireType = (parameter.getExplode()) ? new ListType(ClassType.String) : ClassType.String;
            }

            // If the wire type and client type are the same there is no conversion needed.
            if (parameterWireType == parameterClientType) {
                continue;
            }

            String parameterName = parameter.getParameterReference();
            String parameterWireName = parameter.getParameterReferenceConverted();

            boolean addedConversion = false;
            boolean alwaysNull = clientMethod.getOnlyRequiredParameters() && !parameter.isRequired();

            RequestParameterLocation parameterLocation = parameter.getRequestParameterLocation();
            if (parameterLocation != RequestParameterLocation.BODY &&
                //parameterLocation != RequestParameterLocation.FormData &&
                (parameterClientType instanceof ArrayType || parameterClientType instanceof IterableType)) {

                if (parameterClientType == ArrayType.ByteArray) {
                    String expression = "null";
                    if (!alwaysNull) {
                        expression = String.format("%s(%s)",
                            parameterWireType == ClassType.String ? "Base64Util.encodeToString" : "Base64Url.encode",
                            parameterName);
                    }

                    function.line("%s %s = %s;", parameterWireType, parameterWireName, expression);
                    addedConversion = true;
                } else if (parameterClientType instanceof IterableType) {
                    boolean alreadyNullChecked = clientMethod.getRequiredNullableParameterExpressions()
                        .contains(parameterName);
                    IType elementType = ((IterableType) parameterClientType).getElementType();
                    String expression;
                    if (alwaysNull) {
                        expression = "null";
                    } else if (!parameter.getExplode()) {
                        CollectionFormat collectionFormat = parameter.getCollectionFormat();
                        if (elementType instanceof EnumType) {
                            // EnumTypes should provide a toString implementation that represents the wire value.
                            // Circumvent the use of JacksonAdapter and handle this manually.

                            // If the parameter is null, the converted value is null.
                            // Otherwise, convert the parameter to a string, mapping each element to the toString
                            // value, finally joining with the collection format.
                            if (alreadyNullChecked) {
                                expression =
                                    parameterName + ".stream()\n" +
                                        "    .map(value -> Objects.toString(value, \"\"))\n" +
                                        "    .collect(Collectors.joining(\"" + collectionFormat.getDelimiter() + "\"))";
                            } else {
                                expression =
                                    "(" + parameterName + " == null) ? null : " + parameterName + ".stream()\n" +
                                        "    .map(value -> Objects.toString(value, \"\"))\n" +
                                        "    .collect(Collectors.joining(\"" + collectionFormat.getDelimiter() + "\"))";
                            }
                        } else {
                            if (elementType == ClassType.String) {
                                if (alreadyNullChecked) {
                                    expression = parameterName + ".stream()\n" +
                                        "    .map(value -> Objects.toString(value, \"\"))\n" +
                                        "    .collect(Collectors.joining(\"" + collectionFormat.getDelimiter() + "\"))";
                                } else {
                                    expression = "(" + parameterName + " == null) ? null : " + parameterName + ".stream()\n" +
                                        "    .map(value -> Objects.toString(value, \"\"))\n" +
                                        "    .collect(Collectors.joining(\"" + collectionFormat.getDelimiter() + "\"))";
                                }
                            } else {
                                // Always use serializeIterable as Iterable supports both Iterable and List.
                                expression = String.format(
                                    "JacksonAdapter.createDefaultSerializerAdapter().serializeIterable(%s, CollectionFormat.%s)",
                                    parameterName, collectionFormat.toString().toUpperCase(Locale.ROOT));
                            }
                        }
                    } else {
                        if (alreadyNullChecked) {
                            expression = parameterName + ".stream()\n"
                                + "    .map(item -> Objects.toString(item, \"\"))\n"
                                + "    .collect(Collectors.toList())";
                        } else {
                            expression = "(" + parameterName + " == null) ? new ArrayList<>()\n"
                                + ": " + parameterName + ".stream().map(item -> Objects.toString(item, \"\")).collect(Collectors.toList())";
                        }
                    }
                    function.line("%s %s = %s;", parameterWireType, parameterWireName, expression);
                    addedConversion = true;
                }
            }

            if (settings.isGenerateXmlSerialization()
                && parameterClientType instanceof ListType
                && (parameterLocation == RequestParameterLocation.BODY /*|| parameterLocation == RequestParameterLocation.FormData*/)) {
                function.line("%s %s = new %s(%s);", parameter.getWireType(), parameterWireName,
                    parameter.getWireType(), alwaysNull ? "null" : parameterName);
                addedConversion = true;
            }

            if (!addedConversion) {
                function.line(parameter.convertFromClientType(parameterName, parameterWireName,
                    clientMethod.getOnlyRequiredParameters() && !parameter.isRequired(),
                    parameter.isConstant() || alwaysNull));
            }
        }
    }

    private static boolean addSpecialHeadersToRequestOptions(JavaBlock function, ClientMethod clientMethod) {
        boolean requestOptionsLocal = false;
        if (MethodUtil.isMethodIncludeRepeatableRequestHeaders(clientMethod.getProxyMethod())) {
            requestOptionsLocal = true;
            function.line("RequestOptions requestOptionsLocal = requestOptions == null ? new RequestOptions() : requestOptions;");
            function.line(String.format("requestOptionsLocal.setHeader(\"%1$s\", UUID.randomUUID().toString());", MethodUtil.REPEATABILITY_REQUEST_ID_HEADER));
            function.line(String.format("requestOptionsLocal.setHeader(\"%1$s\", DateTimeRfc1123.toRfc1123String(OffsetDateTime.now()));", MethodUtil.REPEATABILITY_FIRST_SENT_HEADER));
        }
        return requestOptionsLocal;
    }

    protected static void addSpecialHeadersToLocalVariables(JavaBlock function, ClientMethod clientMethod) {
        if (MethodUtil.isMethodIncludeRepeatableRequestHeaders(clientMethod.getProxyMethod())) {
            function.line(String.format("String %1$s = UUID.randomUUID().toString();", MethodUtil.REPEATABILITY_REQUEST_ID_VARIABLE_NAME));
            function.line(String.format("String %1$s = DateTimeRfc1123.toRfc1123String(OffsetDateTime.now());", MethodUtil.REPEATABILITY_FIRST_SENT_VARIABLE_NAME));
        }
    }

    protected static void writeMethod(JavaType typeBlock, JavaVisibility visibility, String methodSignature, Consumer<JavaBlock> method) {
        if (visibility == JavaVisibility.Public) {
            typeBlock.publicMethod(methodSignature, method);
        } else if (typeBlock instanceof JavaClass) {
            JavaClass classBlock = (JavaClass) typeBlock;
            classBlock.method(visibility, null, methodSignature, method);
        }
    }

    public final void write(ClientMethod clientMethod, JavaType typeBlock) {
        final boolean writingInterface = typeBlock instanceof JavaInterface;
        if (clientMethod.getMethodVisibility() != JavaVisibility.Public && writingInterface) {
            return;
        }

        JavaSettings settings = JavaSettings.getInstance();

        ProxyMethod restAPIMethod = clientMethod.getProxyMethod();
        //IType restAPIMethodReturnBodyClientType = restAPIMethod.getReturnType().getClientType();

        //MethodPageDetails pageDetails = clientMethod.getMethodPageDetails();

        generateJavadoc(clientMethod, typeBlock, restAPIMethod, writingInterface);

        switch (clientMethod.getType()) {
            case PagingSync:
                if (settings.isSyncStackEnabled()) {
                    if (settings.isDataPlaneClient()) {
                        generateProtocolPagingPlainSync(clientMethod, typeBlock, restAPIMethod, settings);
                    } else {
                        generatePagingPlainSync(clientMethod, typeBlock, restAPIMethod, settings);
                    }
                } else {
                    if (settings.isDataPlaneClient()) {
                        generateProtocolPagingSync(clientMethod, typeBlock, restAPIMethod, settings);
                    } else {
                        generatePagingSync(clientMethod, typeBlock, restAPIMethod, settings);
                    }
                }
                break;
            case PagingAsync:
                if (settings.isDataPlaneClient()) {
                    generateProtocolPagingAsync(clientMethod, typeBlock, restAPIMethod, settings);
                } else {
                    generatePagingAsync(clientMethod, typeBlock, restAPIMethod, settings);
                }
                break;
            case PagingSyncSinglePage:
                if (settings.isDataPlaneClient()) {
                    generateProtocolPagingSinglePage(clientMethod, typeBlock, restAPIMethod.toSync(), settings);
                } else {
                    generatePagedSinglePage(clientMethod, typeBlock, restAPIMethod.toSync(), settings);
                }
                break;
            case PagingAsyncSinglePage:
                if (settings.isDataPlaneClient()) {
                    generateProtocolPagingAsyncSinglePage(clientMethod, typeBlock, restAPIMethod, settings);
                } else {
                    generatePagedAsyncSinglePage(clientMethod, typeBlock, restAPIMethod, settings);
                }
                break;

            case LongRunningAsync:
                generateLongRunningAsync(clientMethod, typeBlock, restAPIMethod, settings);
                break;

            case LongRunningSync:
                generateLongRunningSync(clientMethod, typeBlock, restAPIMethod, settings);
                break;

            case LongRunningBeginAsync:
                if (settings.isDataPlaneClient()) {
                    generateProtocolLongRunningBeginAsync(clientMethod, typeBlock);
                } else {
                    generateLongRunningBeginAsync(clientMethod, typeBlock, restAPIMethod, settings);
                }
                break;

            case LongRunningBeginSync:
                if (settings.isSyncStackEnabled()) {
                    if (settings.isDataPlaneClient()) {
                        generateProtocolLongRunningBeginSync(clientMethod, typeBlock);
                    } else {
                        generateLongRunningBeginSync(clientMethod, typeBlock, restAPIMethod, settings);
                    }
                } else {
                    generateLongRunningBeginSyncOverAsync(clientMethod, typeBlock, restAPIMethod, settings);
                }
                break;

            case Resumable:
                generateResumable(clientMethod, typeBlock, restAPIMethod, settings);
                break;

            case SimpleSync:
                if (settings.isSyncStackEnabled()) {
                    generateSimpleSyncMethod(clientMethod, typeBlock, restAPIMethod, settings);
                } else {
                    generateSimplePlainSyncMethod(clientMethod, typeBlock, restAPIMethod, settings);
                }
                break;
            case SimpleSyncRestResponse:
                if (settings.isSyncStackEnabled()) {
                    generatePlainSyncMethod(clientMethod, typeBlock, restAPIMethod, settings);
                } else {
                    generateSyncMethod(clientMethod, typeBlock, restAPIMethod, settings);
                }
                break;

            case SimpleAsyncRestResponse:
                generateSimpleAsyncRestResponse(clientMethod, typeBlock, restAPIMethod, settings);
                break;

            case SimpleAsync:
                generateSimpleAsync(clientMethod, typeBlock, restAPIMethod, settings);
                break;

            case SendRequestAsync:
                generateSendRequestAsync(clientMethod, typeBlock, settings);
                break;
            case SendRequestSync:
                generateSendRequestSync(clientMethod, typeBlock, settings);
                break;
        }
    }

    protected void generateProtocolPagingSync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        generatePagingSync(clientMethod, typeBlock, restAPIMethod, settings);
    }

    protected void generateProtocolPagingPlainSync(ClientMethod clientMethod, JavaType typeBlock,
        ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.COLLECTION)");
        if (clientMethod.getMethodPageDetails().nonNullNextLink()) {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
                addOptionalVariables(function, clientMethod);
                function.line("RequestOptions requestOptionsForNextPage = new RequestOptions();");
                function.line("requestOptionsForNextPage.setContext(requestOptions != null && requestOptions.getContext() != null ? requestOptions.getContext() : Context.NONE);");
                function.line("return new PagedIterable<>(");

                String nextMethodArgs = clientMethod.getMethodPageDetails().getNextMethod().getArgumentList().replace("requestOptions", "requestOptionsForNextPage");
                String firstPageArgs = clientMethod.getArgumentList();
                if (clientMethod.getParameters()
                        .stream()
                        .noneMatch(p -> p.getClientType() == ClassType.Context)) {
                    nextMethodArgs = nextMethodArgs.replace("context", "Context.NONE");

                }
                String effectiveNextMethodArgs = nextMethodArgs;
                function.indent(() -> {
                    function.line("() -> %s(%s),",
                            clientMethod.getProxyMethod().getPagingSinglePageMethodName(), firstPageArgs);
                    function.line("nextLink -> %s(%s));",
                            clientMethod.getMethodPageDetails().getNextMethod().getProxyMethod().getPagingSinglePageMethodName(),
                            effectiveNextMethodArgs);
                });
            });
        } else {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
                String firstPageArgs = clientMethod.getArgumentList();
                if (clientMethod.getParameters()
                        .stream()
                        .noneMatch(p -> p.getClientType() == ClassType.Context)) {
                }
                addOptionalVariables(function, clientMethod);
                function.line("return new PagedIterable<>(");
                function.indent(() -> {
                    function.line("() -> %s(%s));",
                            clientMethod.getProxyMethod().getPagingSinglePageMethodName(), firstPageArgs);
                });
            });
        }
    }

    protected void generateProtocolPagingAsync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        generatePagingAsync(clientMethod, typeBlock, restAPIMethod, settings);
    }

    protected void generateProtocolPagingAsyncSinglePage(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        generatePagedAsyncSinglePage(clientMethod, typeBlock, restAPIMethod, settings);
    }

    protected void generateProtocolPagingSinglePage(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        generatePagedSinglePage(clientMethod, typeBlock, restAPIMethod, settings);
    }

    private void generatePagedSinglePage(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");

        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            if (!settings.isSyncStackEnabled()) {
                function.methodReturn(String.format("%s(%s).block()", clientMethod.getProxyMethod().getPagingAsyncSinglePageMethodName(),
                    clientMethod.getArgumentList()));
                return;
            }

            addValidations(function, clientMethod.getRequiredNullableParameterExpressions(), clientMethod.getValidateExpressions(), settings);
            addOptionalAndConstantVariables(function, clientMethod, restAPIMethod.getParameters(), settings);
            applyParameterTransformations(function, clientMethod, settings);
            convertClientTypesToWireTypes(function, clientMethod, restAPIMethod.getParameters(), clientMethod.getClientReference(), settings);

            boolean requestOptionsLocal = false;
            if (settings.isDataPlaneClient()) {
                requestOptionsLocal = addSpecialHeadersToRequestOptions(function, clientMethod);
            } else {
                addSpecialHeadersToLocalVariables(function, clientMethod);
            }

            String serviceMethodCall = checkAndReplaceParamNameCollision(clientMethod, restAPIMethod, requestOptionsLocal, settings);
            function.line(String.format("%s res = %s;", restAPIMethod.getReturnType(), serviceMethodCall));
            function.line("return new PagedResponseBase<>(");
            function.line("res.getRequest(),");
            function.line("res.getStatusCode(),");
            function.line("res.getHeaders(),");
            if (settings.isDataPlaneClient()) {
                function.line("getValues(res.getValue(), \"%s\"),", clientMethod.getMethodPageDetails().getSerializedItemName());
            } else {
                function.line("res.getValue().%s(),", CodeNamer.getModelNamer().modelPropertyGetterName(clientMethod.getMethodPageDetails().getItemName()));
            }
            if (clientMethod.getMethodPageDetails().nonNullNextLink()) {
                if (settings.isDataPlaneClient()) {
                    function.line("getNextLink(res.getValue(), \"%s\"),", clientMethod.getMethodPageDetails().getSerializedNextLinkName());
                } else {
                    function.line(nextLinkLine(clientMethod));
                }
            } else {
                function.line("null,");
            }

            if (responseTypeHasDeserializedHeaders(clientMethod.getProxyMethod().getReturnType())) {
                function.line("res.getDeserializedHeaders());");
            } else {
                function.line("null);");
            }
        });
    }


    protected void generatePagingSync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.COLLECTION)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.methodReturn(String.format("new PagedIterable<>(%s(%s))", clientMethod.getProxyMethod().getSimpleAsyncMethodName(), clientMethod.getArgumentList()));
        });
    }

    protected void generatePagingPlainSync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.COLLECTION)");
        if (clientMethod.getMethodPageDetails().nonNullNextLink()) {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
                addOptionalVariables(function, clientMethod);
                if (settings.isDataPlaneClient()) {
                    function.line("RequestOptions requestOptionsForNextPage = new RequestOptions();");
                    function.line("requestOptionsForNextPage.setContext(requestOptions != null && requestOptions.getContext() != null ? requestOptions.getContext() : Context.NONE);");
                }
                function.line("return new PagedIterable<>(");

                String nextMethodArgs = clientMethod.getMethodPageDetails().getNextMethod().getArgumentList().replace("requestOptions", "requestOptionsForNextPage");
                String firstPageArgs = clientMethod.getArgumentList();
                if (clientMethod.getParameters()
                    .stream()
                    .noneMatch(p -> p.getClientType() == ClassType.Context)) {
                    nextMethodArgs = nextMethodArgs.replace("context", "Context.NONE");
                    if (!CoreUtils.isNullOrEmpty(firstPageArgs)) {
                        firstPageArgs = firstPageArgs + ", Context.NONE";
                    } else {
                        // If there are no first page arguments don't include a leading comma.
                        firstPageArgs = "Context.NONE";
                    }
                }
                String effectiveNextMethodArgs = nextMethodArgs;
                String effectiveFirstPageArgs = firstPageArgs;
                function.indent(() -> {
                    function.line("() -> %s(%s),",
                        clientMethod.getProxyMethod().getPagingSinglePageMethodName(),
                        effectiveFirstPageArgs);
                    function.line("nextLink -> %s(%s));",
                        clientMethod.getMethodPageDetails().getNextMethod().getProxyMethod().getPagingSinglePageMethodName(),
                        effectiveNextMethodArgs);
                });
            });
        } else {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {

                String firstPageArgs = clientMethod.getArgumentList();
                if (clientMethod.getParameters()
                    .stream()
                    .noneMatch(p -> p.getClientType() == ClassType.Context)) {
                    if (!CoreUtils.isNullOrEmpty(firstPageArgs)) {
                        firstPageArgs = firstPageArgs + ", Context.NONE";
                    } else {
                        // If there are no first page arguments don't include a leading comma.
                        firstPageArgs = "Context.NONE";
                    }
                }
                String effectiveFirstPageArgs = firstPageArgs;
                addOptionalVariables(function, clientMethod);
                function.line("return new PagedIterable<>(");
                function.indent(() -> {
                    function.line("() -> %s(%s));",
                        clientMethod.getProxyMethod().getPagingSinglePageMethodName(),
                        effectiveFirstPageArgs);
                });
            });
        }
    }

    protected void generatePagingAsync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.COLLECTION)");
        if (clientMethod.getMethodPageDetails().nonNullNextLink()) {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
                addOptionalVariables(function, clientMethod);
                if (settings.isDataPlaneClient()) {
                    function.line("RequestOptions requestOptionsForNextPage = new RequestOptions();");
                    function.line("requestOptionsForNextPage.setContext(requestOptions != null && requestOptions.getContext() != null ? requestOptions.getContext() : Context.NONE);");
                }
                function.line("return new PagedFlux<>(");
                function.indent(() -> {
                    function.line("() -> %s(%s),",
                        clientMethod.getProxyMethod().getPagingAsyncSinglePageMethodName(),
                        clientMethod.getArgumentList());
                    function.line("nextLink -> %s(%s));",
                        clientMethod.getMethodPageDetails().getNextMethod().getProxyMethod().getPagingAsyncSinglePageMethodName(),
                        clientMethod.getMethodPageDetails().getNextMethod().getArgumentList().replace("requestOptions", "requestOptionsForNextPage"));
                });
            });
        } else {
            writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
                addOptionalVariables(function, clientMethod);
                function.line("return new PagedFlux<>(");
                function.indent(() -> function.line("() -> %s(%s));",
                    clientMethod.getProxyMethod().getPagingAsyncSinglePageMethodName(),
                    clientMethod.getArgumentList()));
            });
        }
    }

    protected void generateResumable(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        typeBlock.publicMethod(clientMethod.getDeclaration(), function -> {
            ProxyMethodParameter parameter = restAPIMethod.getParameters().get(0);
            addValidations(function, clientMethod.getRequiredNullableParameterExpressions(), clientMethod.getValidateExpressions(), settings);
            function.methodReturn(String.format("service.%s(%s)", restAPIMethod.getName(), parameter.getName()));
        });
    }

    protected void generateSimpleAsync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), (function -> {
            addOptionalVariables(function, clientMethod);
            function.line("return %s(%s)", clientMethod.getProxyMethod().getSimpleAsyncRestResponseMethodName(), clientMethod.getArgumentList());
            function.indent(() -> {
                if (GenericType.Flux(ClassType.ByteBuffer).equals(clientMethod.getReturnValue().getType())) {
                    // Previously this used StreamResponse::getValue, but it isn't guaranteed that the return is
                    // StreamResponse, instead use Response::getValue as StreamResponse is just a fancier
                    // Response<Flux<ByteBuffer>>.
                    function.text(".flatMapMany(fluxByteBufferResponse -> fluxByteBufferResponse.getValue());");
                } else if (!GenericType.Mono(ClassType.Void).equals(clientMethod.getReturnValue().getType()) &&
                    !GenericType.Flux(ClassType.Void).equals(clientMethod.getReturnValue().getType())) {
                    function.text(".flatMap(res -> Mono.justOrEmpty(res.getValue()));");
                } else {
                    function.text(".flatMap(ignored -> Mono.empty());");
                }
            });
        }));
    }

    private void generateSimpleSyncMethod(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), (function -> {
            addOptionalVariables(function, clientMethod);

            String argumentList = clientMethod.getArgumentList();
            if (CoreUtils.isNullOrEmpty(argumentList)) {
                // If there are no arguments the argument is Context.NONE
                argumentList = "Context.NONE";
            } else if (clientMethod.getParameters().stream().noneMatch(p -> p.getClientType() == ClassType.Context)) {
                // If the arguments don't contain Context append Context.NONE
                argumentList += ", Context.NONE";
            }

            if (ClassType.StreamResponse.equals(clientMethod.getReturnValue().getType())) {
                function.text(".flatMapMany(StreamResponse::getValue);");
            }
            if (clientMethod.getReturnValue().getType().equals(PrimitiveType.Void)) {
                function.line("%s(%s);",
                    clientMethod.getProxyMethod().getSimpleRestResponseMethodName(),
                    argumentList);
            } else {
                function.line("return %s(%s).getValue();",
                    clientMethod.getProxyMethod().getSimpleRestResponseMethodName(),
                    argumentList);
            }
        }));
    }

    private void generateSimplePlainSyncMethod(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod,
        JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), (function -> {
            addOptionalVariables(function, clientMethod);

            String argumentList = clientMethod.getArgumentList();
            if (CoreUtils.isNullOrEmpty(argumentList)) {
                // If there are no arguments the argument is Context.NONE
                argumentList = "Context.NONE";
            } else if (clientMethod.getParameters().stream().noneMatch(p -> p.getClientType() == ClassType.Context)) {
                // If the arguments don't contain Context append Context.NONE
                argumentList += ", Context.NONE";
            }

            if (clientMethod.getReturnValue().getType().equals(PrimitiveType.Void)) {
                function.line("%s(%s);",
                    clientMethod.getProxyMethod().getSimpleRestResponseMethodName(),
                    argumentList);
            } else {
                function.line("return %s(%s).getValue();",
                    clientMethod.getProxyMethod().getSimpleRestResponseMethodName(),
                    argumentList);
            }
        }));
    }

    protected void generateSyncMethod(ClientMethod clientMethod, JavaType typeBlock,
        ProxyMethod restAPIMethod, JavaSettings settings) {
        String asyncMethodName = MethodNamer.getSimpleAsyncMethodName(clientMethod.getName());
        if (clientMethod.getType() == ClientMethodType.SimpleSyncRestResponse) {
            asyncMethodName = clientMethod.getProxyMethod().getSimpleAsyncRestResponseMethodName();
        }
        String effectiveAsyncMethodName = asyncMethodName;
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            if (clientMethod.getReturnValue().getType() == ClassType.InputStream) {
                function.line("Iterator<ByteBufferBackedInputStream> iterator = %s(%s).map(ByteBufferBackedInputStream::new).toStream().iterator();",
                    effectiveAsyncMethodName, clientMethod.getArgumentList());
                function.anonymousClass("Enumeration<InputStream>", "enumeration", javaBlock -> {
                    javaBlock.annotation("Override");
                    javaBlock.publicMethod("boolean hasMoreElements()", methodBlock -> methodBlock.methodReturn("iterator.hasNext()"));
                    javaBlock.annotation("Override");
                    javaBlock.publicMethod("InputStream nextElement()", methodBlock -> methodBlock.methodReturn("iterator.next()"));
                });
                function.methodReturn("new SequenceInputStream(enumeration)");
            } else if (clientMethod.getReturnValue().getType() != PrimitiveType.Void) {
                IType returnType = clientMethod.getReturnValue().getType();
                if (returnType instanceof PrimitiveType) {
                    function.line("%s value = %s(%s).block();", returnType.asNullable(),
                        effectiveAsyncMethodName, clientMethod.getArgumentList());
                    function.ifBlock("value != null", ifAction -> {
                        ifAction.methodReturn("value");
                    }).elseBlock(elseAction -> {
                        if (settings.isUseClientLogger()) {
                            elseAction.line("throw LOGGER.logExceptionAsError(new NullPointerException());");
                        } else {
                            elseAction.line("throw new NullPointerException();");
                        }
                    });
                } else if (returnType instanceof GenericType && !settings.isDataPlaneClient()) {
                    GenericType genericType = (GenericType) returnType;
                    if ("Response".equals(genericType.getName()) && genericType.getTypeArguments()[0].equals(ClassType.InputStream)) {
                        function.line("return %s(%s).map(response -> {", effectiveAsyncMethodName, clientMethod.getArgumentList());
                        function.indent(() -> {
                            function.line("Iterator<ByteBufferBackedInputStream> iterator = response.getValue().map(ByteBufferBackedInputStream::new).toStream().iterator();");
                            function.anonymousClass("Enumeration<InputStream>", "enumeration", javaBlock -> {
                                javaBlock.annotation("Override");
                                javaBlock.publicMethod("boolean hasMoreElements()", methodBlock -> methodBlock.methodReturn("iterator.hasNext()"));
                                javaBlock.annotation("Override");
                                javaBlock.publicMethod("InputStream nextElement()", methodBlock -> methodBlock.methodReturn("iterator.next()"));
                            });

                            function.methodReturn("new SimpleResponse<InputStream>(response.getRequest(), response.getStatusCode(), response.getHeaders(), new SequenceInputStream(enumeration))");
                        });

                        function.line("}).block();");
//                    } else if ("Response".equals(genericType.getName()) && genericType.getTypeArguments()[0].equals(ClassType.BinaryData)) {
//                        function.line("return %s(%s).flatMap(response -> new BinaryData(response.getValue())",
//                            effectiveAsyncMethodName, clientMethod.getArgumentList());
//                        function.line(".map(bd -> new SimpleResponse<>(response.getRequest(), response.getStatusCode(), response.getHeaders(), bd)))");
//                        function.line(".block();");
                    } else {
                        function.methodReturn(String.format("%s(%s).block()", effectiveAsyncMethodName, clientMethod.getArgumentList()));
                    }
                } else {
                    function.methodReturn(String.format("%s(%s).block()", effectiveAsyncMethodName, clientMethod.getArgumentList()));
                }
            } else {
                function.line("%s(%s).block();", effectiveAsyncMethodName, clientMethod.getArgumentList());
            }
        });
    }

    protected void generatePlainSyncMethod(ClientMethod clientMethod, JavaType typeBlock,
        ProxyMethod restAPIMethod, JavaSettings settings) {
        String effectiveProxyMethodName = clientMethod.getProxyMethod().getName();
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {

            addValidations(function, clientMethod.getRequiredNullableParameterExpressions(), clientMethod.getValidateExpressions(), settings);
            addOptionalAndConstantVariables(function, clientMethod, restAPIMethod.getParameters(), settings);
            applyParameterTransformations(function, clientMethod, settings);
            convertClientTypesToWireTypes(function, clientMethod, restAPIMethod.getParameters(), clientMethod.getClientReference(), settings);

            String serviceMethodCall = checkAndReplaceParamNameCollision(clientMethod, restAPIMethod.toSync(), false,
                settings);
            if (clientMethod.getReturnValue().getType() == ClassType.InputStream) {
                function.line("Iterator<ByteBufferBackedInputStream> iterator = %s(%s).map(ByteBufferBackedInputStream::new).toStream().iterator();",
                    effectiveProxyMethodName, clientMethod.getArgumentList());
                function.anonymousClass("Enumeration<InputStream>", "enumeration", javaBlock -> {
                    javaBlock.annotation("Override");
                    javaBlock.publicMethod("boolean hasMoreElements()", methodBlock -> methodBlock.methodReturn("iterator.hasNext()"));
                    javaBlock.annotation("Override");
                    javaBlock.publicMethod("InputStream nextElement()", methodBlock -> methodBlock.methodReturn("iterator.next()"));
                });
                function.methodReturn("new SequenceInputStream(enumeration)");
            } else if (clientMethod.getReturnValue().getType() != PrimitiveType.Void) {
                IType returnType = clientMethod.getReturnValue().getType();
                if (returnType instanceof PrimitiveType) {
                    function.line("%s value = %s(%s);", returnType.asNullable(),
                        effectiveProxyMethodName, clientMethod.getArgumentList());
                    function.ifBlock("value != null", ifAction -> {
                        ifAction.methodReturn("value");
                    }).elseBlock(elseAction -> {
                        if (settings.isUseClientLogger()) {
                            elseAction.line("throw LOGGER.logExceptionAsError(new NullPointerException());");
                        } else {
                            elseAction.line("throw new NullPointerException();");
                        }
                    });
                } else {
                    function.methodReturn(serviceMethodCall);
                }
            } else {
                function.line("%s(%s);", effectiveProxyMethodName, clientMethod.getArgumentList());
            }
        });
    }

    /**
     * Generate javadoc for client method.
     *
     * @param clientMethod client method
     * @param typeBlock code block
     * @param restAPIMethod proxy method
     * @param useFullClassName whether to use fully-qualified class name in javadoc
     */
    public static void generateJavadoc(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, boolean useFullClassName) {
        // interface need a fully-qualified exception class name, since exception is usually only included in ProxyMethod
        typeBlock.javadocComment(comment -> {
            if (JavaSettings.getInstance().isDataPlaneClient()) {
                generateProtocolMethodJavadoc(clientMethod, comment);
            } else {
                generateJavadoc(clientMethod, comment, restAPIMethod, useFullClassName);
            }
        });
    }

    /**
     * Generate javadoc for client method.
     *
     * @param clientMethod client method
     * @param commentBlock comment block
     * @param restAPIMethod proxy method
     * @param useFullClassName whether to use fully-qualified class name in javadoc
     */
    public static void generateJavadoc(ClientMethod clientMethod, JavaJavadocComment commentBlock, ProxyMethod restAPIMethod, boolean useFullClassName) {
        commentBlock.description(clientMethod.getDescription());
        List<ClientMethodParameter> methodParameters = clientMethod.getMethodInputParameters();
        for (ClientMethodParameter parameter : methodParameters) {
            commentBlock.param(parameter.getName(), parameterDescriptionOrDefault(parameter));
        }
        if (restAPIMethod != null && clientMethod.getParametersDeclaration() != null && !clientMethod.getParametersDeclaration().isEmpty()) {
            commentBlock.methodThrows("IllegalArgumentException", "thrown if parameters fail the validation");
        }
        generateJavadocExceptions(clientMethod, commentBlock, useFullClassName);
        commentBlock.methodThrows("RuntimeException", "all other wrapped checked exceptions if the request fails to be sent");
        commentBlock.methodReturns(clientMethod.getReturnValue().getDescription());
    }

    protected static String parameterDescriptionOrDefault(ClientMethodParameter parameter) {
        String paramJavadoc = parameter.getDescription();
        if (CoreUtils.isNullOrEmpty(paramJavadoc)) {
            paramJavadoc = String.format("The %1$s parameter", parameter.getName());
        }
        return paramJavadoc;
    }

    protected void generatePagedAsyncSinglePage(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");

        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addValidations(function, clientMethod.getRequiredNullableParameterExpressions(), clientMethod.getValidateExpressions(), settings);
            addOptionalAndConstantVariables(function, clientMethod, restAPIMethod.getParameters(), settings);
            applyParameterTransformations(function, clientMethod, settings);
            convertClientTypesToWireTypes(function, clientMethod, restAPIMethod.getParameters(), clientMethod.getClientReference(), settings);

            boolean requestOptionsLocal = false;
            if (settings.isDataPlaneClient()) {
                requestOptionsLocal = addSpecialHeadersToRequestOptions(function, clientMethod);
            } else {
                addSpecialHeadersToLocalVariables(function, clientMethod);
            }

            String serviceMethodCall = checkAndReplaceParamNameCollision(clientMethod, restAPIMethod, requestOptionsLocal, settings);
            if (contextInParameters(clientMethod)) {
                function.line(String.format("return %s", serviceMethodCall));
            } else {
                function.line(String.format("return FluxUtil.withContext(context -> %s)",
                    serviceMethodCall));
            }
            function.indent(() -> {
                function.line(".map(res -> new PagedResponseBase<>(");
                function.indent(() -> {
                    function.line("res.getRequest(),");
                    function.line("res.getStatusCode(),");
                    function.line("res.getHeaders(),");
                    if (settings.isDataPlaneClient()) {
                        function.line("getValues(res.getValue(), \"%s\"),", clientMethod.getMethodPageDetails().getSerializedItemName());
                    } else {
                        function.line("res.getValue().%s(),", CodeNamer.getModelNamer().modelPropertyGetterName(clientMethod.getMethodPageDetails().getItemName()));
                    }
                    if (clientMethod.getMethodPageDetails().nonNullNextLink()) {
                        if (settings.isDataPlaneClient()) {
                            function.line("getNextLink(res.getValue(), \"%s\"),", clientMethod.getMethodPageDetails().getSerializedNextLinkName());
                        } else {
                            function.line(nextLinkLine(clientMethod));
                        }
                    } else {
                        function.line("null,");
                    }

                    if (responseTypeHasDeserializedHeaders(clientMethod.getProxyMethod().getReturnType())) {
                        function.line("res.getDeserializedHeaders()));");
                    } else {
                        function.line("null));");
                    }
                });
            });
        });
    }

    protected static String nextLinkLine(ClientMethod clientMethod) {
        return nextLinkLine(clientMethod, "getValue()");
    }

    protected static String nextLinkLine(ClientMethod clientMethod, String valueExpression) {
        return String.format("res.%3$s.%1$s()%2$s,",
            CodeNamer.getModelNamer().modelPropertyGetterName(clientMethod.getMethodPageDetails().getNextLinkName()),
            // nextLink could be type URL
            (clientMethod.getMethodPageDetails().getNextLinkType() == ClassType.URL ? ".toString()" : ""),
            valueExpression);
    }

    private static boolean responseTypeHasDeserializedHeaders(IType type) {
        if (type instanceof GenericType && "Mono".equals(((GenericType) type).getName())) {
            type = ((GenericType) type).getTypeArguments()[0];
        }

        // TODO (alzimmer): ClassTypes should maintain reference to any super class or interface they extend/implement.
        // This code is based on the previous implementation that assume if the T type for Mono<T> is a class that
        // it has deserialized headers. This won't always be the case, but ClassType also isn't able to maintain
        // whether the class is an extension of ResponseBase.
        if (type instanceof ClassType) {
            return true;
        } else if (type instanceof GenericType && "ResponseBase".equals(((GenericType) type).getName())) {
            return true;
        } else {
            return false;
        }
    }

    private static String checkAndReplaceParamNameCollision(ClientMethod clientMethod, ProxyMethod restAPIMethod,
        boolean useLocalRequestOptions, JavaSettings settings) {
        // Asynchronous methods will use 'FluxUtils.withContext' to infer 'Context' from the Reactor's context.
        // Only replace 'context' with 'Context.NONE' for synchronous methods that don't have a 'Context' parameter.
        boolean isSync = clientMethod.getProxyMethod().isSync();
        StringBuilder builder = new StringBuilder("service.").append(restAPIMethod.getName()).append('(');
        Map<String, ClientMethodParameter> nameToParameter = clientMethod.getParameters().stream()
            .collect(Collectors.toMap(ClientMethodParameter::getName, Function.identity()));
        Set<String> parametersWithTransformations = clientMethod.getMethodTransformationDetails().stream()
            .map(transform -> transform.getOutParameter().getName())
            .collect(Collectors.toSet());

        boolean firstParameter = true;
        for (String proxyMethodArgument : clientMethod.getProxyMethodArguments(settings)) {
            String parameterName;
            if (useLocalRequestOptions && "requestOptions".equals(proxyMethodArgument)) {
                // Simple static mapping for RequestOptions when 'useLocalRequestOptions' is true.
                parameterName = "requestOptionsLocal";
            } else {
                ClientMethodParameter parameter = nameToParameter.get(proxyMethodArgument);
                if (parameter != null && parametersWithTransformations.contains(proxyMethodArgument)) {
                    // If this ClientMethod contains the ProxyMethod parameter and it has a transformation use the
                    // '*Local' transformed version in the service call.
                    parameterName = proxyMethodArgument + "Local";
                } else {
                    if (!isSync) {
                        // For asynchronous methods always use the argument name.
                        parameterName = proxyMethodArgument;
                    } else {
                        // For synchronous methods check if this parameter is the 'Context' parameter and map to
                        // 'Context.NONE' as synchronous methods have no way to infer 'Context'. Without doing this
                        // mapping generated code will reference a non-existent value which won't compile.
                        // TODO (alzimmer): If needed in the future use a more complex validation than String matching.
                        //  It could be possible for the interface method to have another parameter called 'context'
                        //  which isn't 'Context'. This can be done by looking for the 'ProxyMethodParameter' with the
                        //  matching name and checking if it's the 'Context' parameter.
                        parameterName = (parameter == null && "context".equals(proxyMethodArgument))
                            ? "Context.NONE"
                            : proxyMethodArgument;
                    }
                }
            }

            if (firstParameter) {
                builder.append(parameterName);
                firstParameter = false;
            } else {
                builder.append(", ").append(parameterName);
            }
        }

        return builder.append(')').toString();
    }

    protected void generateSimpleAsyncRestResponse(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addValidations(function, clientMethod.getRequiredNullableParameterExpressions(), clientMethod.getValidateExpressions(), settings);
            addOptionalAndConstantVariables(function, clientMethod, restAPIMethod.getParameters(), settings);
            applyParameterTransformations(function, clientMethod, settings);
            convertClientTypesToWireTypes(function, clientMethod, restAPIMethod.getParameters(), clientMethod.getClientReference(), settings);

            boolean requestOptionsLocal = false;
            if (settings.isDataPlaneClient()) {
                requestOptionsLocal = addSpecialHeadersToRequestOptions(function, clientMethod);
            } else {
                addSpecialHeadersToLocalVariables(function, clientMethod);
            }

            String serviceMethodCall = checkAndReplaceParamNameCollision(clientMethod, restAPIMethod, requestOptionsLocal, settings);
            if (contextInParameters(clientMethod)) {
                function.methodReturn(serviceMethodCall);
            } else {
                function.methodReturn(String.format("FluxUtil.withContext(context -> %s)", serviceMethodCall));
            }
        });
    }

    protected boolean contextInParameters(ClientMethod clientMethod) {
        return clientMethod.getParameters().stream().anyMatch(param -> getContextType().equals(param.getClientType()));
    }

    protected IType getContextType() {
        return ClassType.Context;
    }

    /**
     * Extension to write LRO async client method.
     *
     * @param clientMethod client method
     * @param typeBlock type block
     * @param restAPIMethod proxy method
     * @param settings java settings
     */
    protected void generateLongRunningAsync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {

    }

    /**
     * Extension to write LRO sync client method.
     *
     * @param clientMethod client method
     * @param typeBlock type block
     * @param restAPIMethod proxy method
     * @param settings java settings
     */
    protected void generateLongRunningSync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {

    }

    /**
     * Extension to write LRO begin async client method.
     *
     * @param clientMethod client method
     * @param typeBlock type block
     * @param restAPIMethod proxy method
     * @param settings java settings
     */
    protected void generateLongRunningBeginAsync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        String contextParam;
        if (clientMethod.getParameters().stream().anyMatch(p -> p.getClientType().equals(ClassType.Context))) {
            contextParam = "context";
        } else {
            contextParam = "Context.NONE";
        }
        String pollingStrategy = getPollingStrategy(clientMethod, contextParam);
        typeBlock.annotation("ServiceMethod(returns = ReturnType.LONG_RUNNING_OPERATION)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.line("return PollerFlux.create(Duration.ofSeconds(%s),", clientMethod.getMethodPollingDetails().getPollIntervalInSeconds());
            function.increaseIndent();
            function.line("() -> this.%s(%s),", clientMethod.getProxyMethod().getSimpleAsyncRestResponseMethodName(), clientMethod.getArgumentList());
            function.line(pollingStrategy + ",");
            function.line(TemplateUtil.getLongRunningOperationTypeReferenceExpression(clientMethod.getMethodPollingDetails()) + ");");
            function.decreaseIndent();
        });
    }

    /**
     * Extension to write LRO begin sync client method.
     *
     * @param clientMethod client method
     * @param typeBlock type block
     * @param restAPIMethod proxy method
     * @param settings java settings
     */
    protected void generateLongRunningBeginSyncOverAsync(ClientMethod clientMethod, JavaType typeBlock,
                                                 ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.LONG_RUNNING_OPERATION)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.methodReturn(String.format("this.%sAsync(%s).getSyncPoller()",
                    clientMethod.getName(), clientMethod.getArgumentList()));
        });
    }

    /**
     * Extension to write LRO begin sync client method.
     *
     * @param clientMethod client method
     * @param typeBlock type block
     * @param restAPIMethod proxy method
     * @param settings java settings
     */
    protected void generateLongRunningBeginSync(ClientMethod clientMethod, JavaType typeBlock, ProxyMethod restAPIMethod, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.LONG_RUNNING_OPERATION)");
        String contextParam;
        if (clientMethod.getParameters().stream().anyMatch(p -> p.getClientType().equals(ClassType.Context))) {
            contextParam = "context";
        } else {
            contextParam = "Context.NONE";
        }
        String pollingStrategy = getSyncPollingStrategy(clientMethod, contextParam);

        String argumentList = clientMethod.getArgumentList();
        if (CoreUtils.isNullOrEmpty(argumentList)) {
            // If there are no arguments the argument is Context.NONE
            argumentList = "Context.NONE";
        } else if (clientMethod.getParameters().stream().noneMatch(p -> p.getClientType() == ClassType.Context)) {
            // If the arguments don't contain Context append Context.NONE
            argumentList += ", Context.NONE";
        }

        String effectiveArgumentList = argumentList;
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.line("return SyncPoller.createPoller(Duration.ofSeconds(%s),",
                    clientMethod.getMethodPollingDetails().getPollIntervalInSeconds());
            function.increaseIndent();
            function.line("() -> this.%s(%s),", clientMethod.getProxyMethod().getSimpleRestResponseMethodName(), effectiveArgumentList);
            function.line(pollingStrategy + ",");
            function.line(TemplateUtil.getLongRunningOperationTypeReferenceExpression(clientMethod.getMethodPollingDetails()) + ");");
            function.decreaseIndent();
        });
    }

    private void generateProtocolLongRunningBeginSync(ClientMethod clientMethod, JavaType typeBlock) {
        String contextParam = "requestOptions != null && requestOptions.getContext() != null ? requestOptions.getContext() : Context.NONE";
        String pollingStrategy = getSyncPollingStrategy(clientMethod, contextParam);
        typeBlock.annotation("ServiceMethod(returns = ReturnType.LONG_RUNNING_OPERATION)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.line("return SyncPoller.createPoller(Duration.ofSeconds(%s),",
                    clientMethod.getMethodPollingDetails().getPollIntervalInSeconds());
            function.increaseIndent();
            function.line("() -> this.%s(%s),", clientMethod.getProxyMethod().getSimpleRestResponseMethodName(), clientMethod.getArgumentList());
            function.line(pollingStrategy + ",");
            function.line(TemplateUtil.getLongRunningOperationTypeReferenceExpression(clientMethod.getMethodPollingDetails()) + ");");
            function.decreaseIndent();
        });
    }

    /**
     * Generate long-running begin async method for protocol client
     *
     * @param clientMethod client method
     * @param typeBlock type block
     */
    protected void generateProtocolLongRunningBeginAsync(ClientMethod clientMethod, JavaType typeBlock) {
        String contextParam = "requestOptions != null && requestOptions.getContext() != null ? requestOptions.getContext() : Context.NONE";
        String pollingStrategy = getPollingStrategy(clientMethod, contextParam);
        typeBlock.annotation("ServiceMethod(returns = ReturnType.LONG_RUNNING_OPERATION)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            addOptionalVariables(function, clientMethod);
            function.line("return PollerFlux.create(Duration.ofSeconds(%s),", clientMethod.getMethodPollingDetails().getPollIntervalInSeconds());
            function.increaseIndent();
            function.line("() -> this.%s(%s),", clientMethod.getProxyMethod().getSimpleAsyncRestResponseMethodName(), clientMethod.getArgumentList());
            function.line(pollingStrategy + ",");
            function.line(TemplateUtil.getLongRunningOperationTypeReferenceExpression(clientMethod.getMethodPollingDetails()) + ");");
            function.decreaseIndent();
        });
    }



    private String getPollingStrategy(ClientMethod clientMethod, String contextParam) {
        String endpoint = "null";
        if (clientMethod.getProxyMethod() != null && clientMethod.getProxyMethod().getParameters() != null) {
            if (clientMethod.getProxyMethod().getParameters().stream()
                .anyMatch(p -> p.isFromClient() && p.getRequestParameterLocation() == RequestParameterLocation.URI && "endpoint".equals(p.getName()))) {
                // has EndpointTrait

                final String baseUrl = clientMethod.getProxyMethod().getBaseUrl();
                final String endpointReplacementExpr = clientMethod.getProxyMethod().getParameters().stream()
                        .filter(p -> p.isFromClient() && p.getRequestParameterLocation() == RequestParameterLocation.URI)
                        .filter(p -> baseUrl.contains(String.format("{%s}", p.getRequestParameterName())))
                        .map(p -> String.format(".replace(%1$s, %2$s)",
                                ClassType.String.defaultValueExpression(String.format("{%s}", p.getRequestParameterName())),
                                p.getParameterReference()
                        )).collect(Collectors.joining());
                if (!CoreUtils.isNullOrEmpty(endpointReplacementExpr)) {
                    endpoint = ClassType.String.defaultValueExpression(baseUrl) + endpointReplacementExpr;
                }
            }
        }
        return clientMethod.getMethodPollingDetails().getPollingStrategy()
            .replace("{httpPipeline}", clientMethod.getClientReference() + ".getHttpPipeline()")
            .replace("{endpoint}", endpoint)
            .replace("{context}", contextParam)
            .replace("{serializerAdapter}", clientMethod.getClientReference() + ".getSerializerAdapter()")
            .replace("{intermediate-type}", clientMethod.getMethodPollingDetails().getIntermediateType().toString())
            .replace("{final-type}", clientMethod.getMethodPollingDetails().getFinalType().toString());
    }

    private String getSyncPollingStrategy(ClientMethod clientMethod, String contextParam) {
        String endpoint = "null";
        if (clientMethod.getProxyMethod() != null && clientMethod.getProxyMethod().getParameters() != null) {
            if (clientMethod.getProxyMethod().getParameters().stream()
                    .anyMatch(p -> p.isFromClient() && p.getRequestParameterLocation() == RequestParameterLocation.URI && "endpoint".equals(p.getName()))) {
                // has EndpointTrait

                final String baseUrl = clientMethod.getProxyMethod().getBaseUrl();
                final String endpointReplacementExpr = clientMethod.getProxyMethod().getParameters().stream()
                        .filter(p -> p.isFromClient() && p.getRequestParameterLocation() == RequestParameterLocation.URI)
                        .filter(p -> baseUrl.contains(String.format("{%s}", p.getRequestParameterName())))
                        .map(p -> String.format(".replace(%1$s, %2$s)",
                                ClassType.String.defaultValueExpression(String.format("{%s}", p.getRequestParameterName())),
                                p.getParameterReference()
                        )).collect(Collectors.joining());
                if (!CoreUtils.isNullOrEmpty(endpointReplacementExpr)) {
                    endpoint = ClassType.String.defaultValueExpression(baseUrl) + endpointReplacementExpr;
                }
            }
        }
        return clientMethod.getMethodPollingDetails().getSyncPollingStrategy()
                .replace("{httpPipeline}", clientMethod.getClientReference() + ".getHttpPipeline()")
                .replace("{endpoint}", endpoint)
                .replace("{context}", contextParam)
                .replace("{serializerAdapter}", clientMethod.getClientReference() + ".getSerializerAdapter()")
                .replace("{intermediate-type}", clientMethod.getMethodPollingDetails().getIntermediateType().toString())
                .replace("{final-type}", clientMethod.getMethodPollingDetails().getFinalType().toString());
    }

    protected void generateSendRequestAsync(ClientMethod clientMethod, JavaType typeBlock, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            function.line("return FluxUtil.withContext(context -> %1$s.getHttpPipeline().send(%2$s, context)",
                clientMethod.getClientReference(), clientMethod.getArgumentList());
            function.indent(() -> {
                function.line(".flatMap(response -> BinaryData.fromFlux(response.getBody())");
                function.line(".map(body -> new SimpleResponse<>(response.getRequest(), response.getStatusCode(), response.getHeaders(), body))));");
            });
        });
    }

    protected void generateSendRequestSync(ClientMethod clientMethod, JavaType typeBlock, JavaSettings settings) {
        typeBlock.annotation("ServiceMethod(returns = ReturnType.SINGLE)");
        writeMethod(typeBlock, clientMethod.getMethodVisibility(), clientMethod.getDeclaration(), function -> {
            function.methodReturn("this.sendRequestAsync(httpRequest).contextWrite(c -> c.putAll(FluxUtil.toReactorContext(context).readOnly())).block()");
        });
    }
}
