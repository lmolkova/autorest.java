// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.template;

import com.azure.autorest.extension.base.model.codemodel.RequestParameterLocation;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientEnumValue;
import com.azure.autorest.model.clientmodel.ClientMethod;
import com.azure.autorest.model.clientmodel.ClientMethodParameter;
import com.azure.autorest.model.clientmodel.ClientMethodType;
import com.azure.autorest.model.clientmodel.ClientModel;
import com.azure.autorest.model.clientmodel.ClientModelProperty;
import com.azure.autorest.model.clientmodel.EnumType;
import com.azure.autorest.model.clientmodel.GenericType;
import com.azure.autorest.model.clientmodel.IType;
import com.azure.autorest.model.clientmodel.ListType;
import com.azure.autorest.model.clientmodel.MapType;
import com.azure.autorest.model.clientmodel.ParameterSynthesizedOrigin;
import com.azure.autorest.model.clientmodel.PrimitiveType;
import com.azure.autorest.model.clientmodel.ProxyMethod;
import com.azure.autorest.model.clientmodel.ProxyMethodParameter;
import com.azure.autorest.model.javamodel.JavaJavadocComment;
import com.azure.autorest.model.javamodel.JavaType;
import com.azure.autorest.util.ClientModelUtil;
import com.azure.autorest.util.CodeNamer;
import com.azure.core.util.CoreUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ClientMethodTemplateBase implements IJavaTemplate<ClientMethod, JavaType> {

    protected static void generateProtocolMethodJavadoc(ClientMethod clientMethod, JavaJavadocComment commentBlock) {
        commentBlock.description(clientMethod.getDescription());

        if (clientMethod.getProxyMethod() != null) {
            List<ProxyMethodParameter> queryParameters = clientMethod.getProxyMethod().getAllParameters().stream()
                    .filter(p -> RequestParameterLocation.QUERY.equals(p.getRequestParameterLocation()))
                    // ignore if synthesized by modelerfour, i.e. api-version
                    .filter(p -> p.getOrigin() == ParameterSynthesizedOrigin.NONE)
                    .collect(Collectors.toList());
            if (!queryParameters.isEmpty() && hasParametersToPrintInJavadoc(queryParameters)) {
                optionalParametersJavadoc("Query Parameters", queryParameters, commentBlock);
                commentBlock.line("You can add these to a request with {@link RequestOptions#addQueryParam}");
            }

            List<ProxyMethodParameter> headerParameters = clientMethod.getProxyMethod().getAllParameters().stream()
                    .filter(p -> RequestParameterLocation.HEADER.equals(p.getRequestParameterLocation()))
                    // ignore if synthesized by modelerfour and is constant
                    // we would want user to provide a correct "content-type" if it is not a constant
                    .filter(p -> p.getOrigin() == ParameterSynthesizedOrigin.NONE || !p.isConstant())
                    .collect(Collectors.toList());
            if (!headerParameters.isEmpty() && hasParametersToPrintInJavadoc(headerParameters)) {
                optionalParametersJavadoc("Header Parameters", headerParameters, commentBlock);
                commentBlock.line("You can add these to a request with {@link RequestOptions#addHeader}");
            }

            // Request body
            Set<IType> typesInJavadoc = new HashSet<>();

            boolean isBodyParamRequired = clientMethod.getProxyMethod().getAllParameters()
                    .stream().filter(p -> RequestParameterLocation.BODY.equals(p.getRequestParameterLocation()))
                            .map(ProxyMethodParameter::isRequired).findFirst().orElse(false);

            clientMethod.getProxyMethod().getAllParameters()
                    .stream().filter(p -> RequestParameterLocation.BODY.equals(p.getRequestParameterLocation()))
                    .map(ProxyMethodParameter::getRawType)
                    .findFirst()
                    .ifPresent(type -> requestBodySchemaJavadoc(type, commentBlock, typesInJavadoc, isBodyParamRequired));

            // Response body
            IType responseBodyType;
            if (JavaSettings.getInstance().isDataPlaneClient()) {
                // special handling for paging method
                if (clientMethod.getType() == ClientMethodType.PagingSync || clientMethod.getType() == ClientMethodType.PagingAsync || clientMethod.getType() == ClientMethodType.PagingAsyncSinglePage || clientMethod.getType() == ClientMethodType.PagingSyncSinglePage) {
                    String itemName = clientMethod.getMethodPageDetails().getItemName();
                    // rawResponseType has properties: 'value' and 'nextLink'
                    IType rawResponseType = clientMethod.getProxyMethod().getRawResponseBodyType();
                    if (!(rawResponseType instanceof ClassType)) {
                        throw new IllegalStateException(String.format("clientMethod.getProxyMethod().getRawResponseBodyType() should be ClassType for paging method. rawResponseType = %s", rawResponseType.toString()));
                    }
                    ClientModel model = ClientModelUtil.getClientModel(((ClassType) rawResponseType).getName());
                    List<ClientModelProperty> properties = new ArrayList<>();
                    traverseProperties(model, properties);
                    responseBodyType = properties.stream()
                            .filter(property -> property.getName().equals(itemName))
                            .map(clientModelProperty -> clientModelProperty.getClientType())
                            .map(valueListType -> {
                                // value type is List<T>, we need to get the typeArguments
                                if (!(valueListType instanceof ListType)) {
                                    throw new IllegalStateException(String.format("value type must be list for paging method. rawResponseType = %s", rawResponseType.toString()));
                                }
                                IType[] listTypeArgs = ((ListType) valueListType).getTypeArguments();
                                if (listTypeArgs.length == 0) {
                                    throw new IllegalStateException(String.format("list type arguments' length should not be 0 for paging method. rawResponseType = %s", rawResponseType.toString()));
                                }
                                return listTypeArgs[0];
                            })
                            .findFirst().orElse(null);
                    if (responseBodyType == null) {
                        throw new IllegalStateException(String.format("%s not found in properties of rawResponseType. rawResponseType = ", itemName, rawResponseType.toString()));
                    }
                } else {
                    responseBodyType = clientMethod.getProxyMethod().getRawResponseBodyType();
                }
            } else {
                responseBodyType = clientMethod.getProxyMethod().getResponseBodyType();
            }
            if (responseBodyType != null && !responseBodyType.equals(PrimitiveType.Void)) {
                responseBodySchemaJavadoc(responseBodyType, commentBlock, typesInJavadoc);
            }
        }

        clientMethod.getParameters().forEach(p -> commentBlock.param(p.getName(), methodParameterDescriptionOrDefault(p)));
        if (clientMethod.getProxyMethod() != null) {
            generateJavadocExceptions(clientMethod, commentBlock, false);
        }
        commentBlock.methodReturns(clientMethod.getReturnValue().getDescription());


        // add external documentation
        if (clientMethod.getMethodDocumentation() != null) {
            commentBlock.line("@see <a href=" + clientMethod.getMethodDocumentation().getUrl() + ">" + clientMethod.getMethodDocumentation().getDescription() + "</a>");
        }
    }

    protected static void generateJavadocExceptions(ClientMethod clientMethod, JavaJavadocComment commentBlock, boolean useFullClassName) {
        ProxyMethod restAPIMethod = clientMethod.getProxyMethod();
        if (restAPIMethod != null && restAPIMethod.getUnexpectedResponseExceptionType() != null) {
            commentBlock.methodThrows(useFullClassName
                            ? restAPIMethod.getUnexpectedResponseExceptionType().getFullName()
                            : restAPIMethod.getUnexpectedResponseExceptionType().getName(),
                    "thrown if the request is rejected by server");
        }
        if (restAPIMethod != null && restAPIMethod.getUnexpectedResponseExceptionTypes() != null) {
            for (Map.Entry<ClassType, List<Integer>> exception : restAPIMethod.getUnexpectedResponseExceptionTypes().entrySet()) {
                commentBlock.methodThrows(exception.getKey().toString(),
                        String.format("thrown if the request is rejected by server on status code %s",
                                exception.getValue().stream().map(String::valueOf).collect(Collectors.joining(", "))));
            }
        }
    }

    private static void optionalParametersJavadoc(String title, List<ProxyMethodParameter> parameters, JavaJavadocComment commentBlock) {
        commentBlock.line(String.format("<p><strong>%s</strong></p>", title));
        commentBlock.line("<table border=\"1\">");
        commentBlock.line(String.format("    <caption>%s</caption>", title));
        commentBlock.line("    <tr><th>Name</th><th>Type</th><th>Required</th><th>Description</th></tr>");
        for (ProxyMethodParameter parameter : parameters) {
            boolean parameterIsConstantOrFromClient = parameter.isConstant() || parameter.isFromClient();
            if (!parameter.isRequired() && !parameterIsConstantOrFromClient) {
                commentBlock.line(String.format(
                        "    <tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        parameter.getRequestParameterName(),
                        CodeNamer.escapeXmlComment(parameter.getClientType().toString()),
                        parameter.isRequired() ? "Yes" : "No",
                        parameterDescriptionOrDefault(parameter)));
            }

        }
        commentBlock.line("</table>");
    }

    private static boolean hasParametersToPrintInJavadoc(List<ProxyMethodParameter> parameters) {
        return parameters.stream().anyMatch(parameter -> {
            boolean parameterIsConstantOrFromClient = parameter.isConstant() || parameter.isFromClient();
            boolean parameterIsRequired = parameter.isRequired();
            return !parameterIsRequired && !parameterIsConstantOrFromClient;
        });
    }

    private static void requestBodySchemaJavadoc(IType requestBodyType, JavaJavadocComment commentBlock, Set<IType> typesInJavadoc, boolean isBodyParamRequired) {
        typesInJavadoc.clear();

        if (requestBodyType == null) {
            return;
        }
        commentBlock.line("<p><strong>Request Body Schema</strong></p>");
        commentBlock.line("<pre>{@code");
        bodySchemaJavadoc(requestBodyType, commentBlock, "", null, typesInJavadoc, isBodyParamRequired, true);
        commentBlock.line("}</pre>");
    }

    private static void responseBodySchemaJavadoc(IType responseBodyType, JavaJavadocComment commentBlock, Set<IType> typesInJavadoc) {
        typesInJavadoc.clear();

        if (responseBodyType == null) {
            return;
        }
        commentBlock.line("<p><strong>Response Body Schema</strong></p>");
        commentBlock.line("<pre>{@code");
        bodySchemaJavadoc(responseBodyType, commentBlock, "", null, typesInJavadoc, true, true);
        commentBlock.line("}</pre>");
    }

    private static void bodySchemaJavadoc(IType type, JavaJavadocComment commentBlock, String indent, String name, Set<IType> typesInJavadoc, boolean isRequired, boolean isRootSchema) {
        String nextIndent = indent + "    ";
        if (ClientModelUtil.isClientModel(type) && !typesInJavadoc.contains(type)) {
            typesInJavadoc.add(type);
            ClientModel model = ClientModelUtil.getClientModel(((ClassType) type).getName());
            if (name != null) {
                commentBlock.line(indent + name + appendOptionalOrRequiredAttribute(isRequired, isRootSchema) + ": {");
            } else {
                commentBlock.line(indent + appendOptionalOrRequiredAttribute(isRequired, isRootSchema) + "{");
            }
            List<ClientModelProperty> properties = new ArrayList<>();
            traverseProperties(model, properties);
            for (ClientModelProperty property : properties) {
                bodySchemaJavadoc(property.getWireType(), commentBlock, nextIndent, property.getSerializedName(), typesInJavadoc, property.isRequired(), false);
            }
            commentBlock.line(indent + "}");
        } else if (typesInJavadoc.contains(type)) {
            if (name != null) {
                commentBlock.line(indent + name + appendOptionalOrRequiredAttribute(isRequired, isRootSchema) + ": (recursive schema, see " + name + " above)");
            } else {
                commentBlock.line(indent + "(recursive schema, see above)");
            }
        } else if (type instanceof ListType) {
            if (name != null) {
                commentBlock.line(indent + name + appendOptionalOrRequiredAttribute(isRequired, isRootSchema) + ": [");
            } else {
                commentBlock.line(indent + appendOptionalOrRequiredAttribute(isRequired, isRootSchema) + "[");
            }
            bodySchemaJavadoc(((ListType) type).getElementType(), commentBlock, nextIndent, null, typesInJavadoc, isRequired, false);
            commentBlock.line(indent + "]");
        } else if (type instanceof EnumType) {
            String values = ((EnumType) type).getValues().stream()
                    .map(ClientEnumValue::getValue)
                    .collect(Collectors.joining("/"));
            if (name != null) {
                commentBlock.line(indent + name + ": String(" + values + ")" + appendOptionalOrRequiredAttribute(isRequired, isRootSchema));
            } else {
                commentBlock.line(indent + "String(" + values + ")" + appendOptionalOrRequiredAttribute(isRequired, isRootSchema));
            }
        } else if (type instanceof MapType) {
            if (name != null) {
                commentBlock.line(indent + name + appendOptionalOrRequiredAttribute(isRequired, isRootSchema) + ": {");
            } else {
                commentBlock.line(indent + appendOptionalOrRequiredAttribute(isRequired, isRootSchema) + "{");
            }
            bodySchemaJavadoc(((MapType) type).getValueType(), commentBlock, nextIndent, "String", typesInJavadoc, isRequired, false);
            commentBlock.line(indent + "}");
        } else {
            String javadoc = convertToBodySchemaJavadoc(type);
            if (name != null) {
                commentBlock.line(indent + name + ": " + javadoc + appendOptionalOrRequiredAttribute(isRequired, isRootSchema));
            } else {
                commentBlock.line(indent + javadoc + appendOptionalOrRequiredAttribute(isRequired, isRootSchema));
            }
        }
    }

    /*
     * Converts raw type into type to display in javadoc as body schema type.
     * 1. converts Flux<ByteBuffer> to BinaryData (applies to request body schema, since DPG response type can't be Flux<ByteBuffer>)
     */
    private static String convertToBodySchemaJavadoc(IType type) {
        if (GenericType.FluxByteBuffer.equals(type)) {
            return ClassType.BinaryData.toString();
        }
        return type.toString();
    }

    private static void traverseProperties(ClientModel model, List<ClientModelProperty> properties) {
        if (model.getParentModelName() != null) {
            traverseProperties(ClientModelUtil.getClientModel(model.getParentModelName()), properties);
        }
        properties.addAll(model.getProperties());
    }

    private static String parameterDescriptionOrDefault(ProxyMethodParameter parameter) {
        String paramJavadoc = parameter.getDescription();
        if (CoreUtils.isNullOrEmpty(paramJavadoc)) {
            paramJavadoc = String.format("The %1$s parameter", parameter.getName());
        }
        String description = CodeNamer.escapeXmlComment(paramJavadoc);
        // query with array, add additional description
        if (parameter.getRequestParameterLocation() == RequestParameterLocation.QUERY && parameter.getCollectionFormat() != null) {
            description = (CoreUtils.isNullOrEmpty(description) || description.endsWith(".")) ? description : (description + ".");
            if (parameter.getExplode()) {
                // collectionFormat: multi
                description += " Call {@link RequestOptions#addQueryParam} to add string to array.";
            } else {
                // collectionFormat: csv, ssv, tsv, pipes
                description += String.format(" In the form of \"%s\" separated string.", parameter.getCollectionFormat().getDelimiter());
            }
        }
        return description;
    }

    private static String methodParameterDescriptionOrDefault(ClientMethodParameter p) {
        String doc = p.getDescription();
        if (CoreUtils.isNullOrEmpty(doc)) {
            doc = String.format("The %1$s parameter", p.getName());
        }
        return doc;
    }

    private static String appendOptionalOrRequiredAttribute(boolean isRequired, boolean isRootSchema) {
        return isRootSchema ? "" : isRequired ? " (Required)" : " (Optional)";
    }
}
