// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.template.example;

import com.azure.autorest.Javagen;
import com.azure.autorest.extension.base.plugin.JavaSettings;
import com.azure.autorest.extension.base.plugin.PluginLogger;
import com.azure.autorest.model.clientmodel.ClassType;
import com.azure.autorest.model.clientmodel.ClientModel;
import com.azure.autorest.model.clientmodel.ClientModelProperty;
import com.azure.autorest.model.clientmodel.IType;
import com.azure.autorest.model.clientmodel.ModelProperty;
import com.azure.autorest.model.clientmodel.PrimitiveType;
import com.azure.autorest.model.clientmodel.examplemodel.ClientModelNode;
import com.azure.autorest.model.clientmodel.examplemodel.ExampleNode;
import com.azure.autorest.model.clientmodel.examplemodel.ExampleHelperFeature;
import com.azure.autorest.model.clientmodel.examplemodel.ListNode;
import com.azure.autorest.model.clientmodel.examplemodel.LiteralNode;
import com.azure.autorest.model.clientmodel.examplemodel.MapNode;
import com.azure.autorest.model.clientmodel.examplemodel.ObjectNode;
import com.azure.autorest.model.javamodel.JavaBlock;
import com.azure.autorest.model.javamodel.JavaClass;
import com.azure.autorest.model.javamodel.JavaModifier;
import com.azure.autorest.model.javamodel.JavaVisibility;
import com.azure.autorest.util.ClientModelUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelExampleWriter {

    private static final Logger LOGGER = new PluginLogger(Javagen.getPluginInstance(), ModelExampleWriter.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Set<String> imports = new HashSet<>();

    private final Consumer<JavaBlock> assertionWriter;
    private final ExampleNodeModelInitializationVisitor modelInitializationVisitor =
            new ExampleNodeModelInitializationVisitor();
    private final String modelInitializationCode;

    public ModelExampleWriter(ExampleNode exampleNode, String modelVariableName) {
        this.imports.add("org.junit.jupiter.api.Assertions");

        ExampleNodeAssertionVisitor assertionVisitor = new ExampleNodeAssertionVisitor();
        assertionVisitor.accept(exampleNode, modelVariableName);
        imports.addAll(assertionVisitor.imports);

        this.assertionWriter = methodBlock -> {
            assertionVisitor.assertions.forEach(methodBlock::line);
        };

        modelInitializationCode = modelInitializationVisitor.accept(exampleNode);
        imports.addAll(modelInitializationVisitor.getImports());
    }

    public Set<String> getImports() {
        return imports;
    }

    public Set<ExampleHelperFeature> getHelperFeatures() {
        return modelInitializationVisitor.getHelperFeatures();
    }

    public void writeAssertion(JavaBlock methodBlock) {
        assertionWriter.accept(methodBlock);
    }

    public String getModelInitializationCode() {
        return modelInitializationCode;
    }

    public static void writeMapOfMethod(JavaClass classBlock) {
        classBlock.annotation("SuppressWarnings(\"unchecked\")");
        classBlock.method(JavaVisibility.Private, Collections.singletonList(JavaModifier.Static), "<T> Map<String, T> mapOf(Object... inputs)", methodBlock -> {
            methodBlock.line("Map<String, T> map = new HashMap<>();");
            methodBlock.line("for (int i = 0; i < inputs.length; i += 2) {");
            methodBlock.indent(() -> {
                methodBlock.line("String key = (String) inputs[i];");
                methodBlock.line("T value = (T) inputs[i + 1];");
                methodBlock.line("map.put(key, value);");
            });
            methodBlock.line("}");
            methodBlock.line("return map;");
        });
    }

    public static class ExampleNodeAssertionVisitor {

        private final Set<String> imports = new HashSet<>();

        private final List<String> assertions = new ArrayList<>();

        private void addEqualsAssertion(String expected, String code) {
            assertions.add(String.format("Assertions.assertEquals(%1$s, %2$s);", expected, code));
        }

        public void accept(ExampleNode node, String getterCode) {
            if (node instanceof LiteralNode) {
                node.getClientType().addImportsTo(imports, false);

                addEqualsAssertion(
                        node.getClientType().defaultValueExpression(((LiteralNode) node).getLiteralsValue()),
                        getterCode);
            } else if (node instanceof ObjectNode) {
                // additionalProperties
            } else if (node instanceof ListNode) {
                if (!node.getChildNodes().isEmpty()) {
                    node = node.getChildNodes().get(0);
                    getterCode += ".get(0)";
                    accept(node, getterCode);
                }
            } else if (node instanceof MapNode) {
                if (!node.getChildNodes().isEmpty()) {
                    String key = ((MapNode) node).getKeys().get(0);
                    node = node.getChildNodes().get(0);
                    getterCode += String.format(".get(%s)", ClassType.String.defaultValueExpression(key));
                    accept(node, getterCode);
                }
            } else if (node instanceof ClientModelNode) {
                ClientModelNode clientModelNode = ((ClientModelNode) node);

                ClientModel model = clientModelNode.getClientModel();

                imports.add(model.getFullName());

                for (ExampleNode childNode : node.getChildNodes()) {
                    ModelProperty modelProperty = clientModelNode.getClientModelProperties().get(childNode);
                    String childGetterCode = getterCode + String.format(".%s()", modelProperty.getGetterName());
                    accept(childNode, childGetterCode);
                }
            }
        }

        public Set<String> getImports() {
            return imports;
        }

        public List<String> getAssertions() {
            return assertions;
        }
    }

    public static class ExampleNodeModelInitializationVisitor {

        protected final Set<String> imports = new HashSet<>();
        protected final Set<ExampleHelperFeature> helperFeatures = new HashSet<>();

        /**
         * Extension to write code for deserialize JSON String to Object.
         * @param jsonStr the JSON String.
         */
        protected String codeDeserializeJsonString(String jsonStr) {
            imports.add(com.azure.core.util.serializer.JacksonAdapter.class.getName());
            imports.add(com.azure.core.util.serializer.SerializerEncoding.class.getName());

            return String.format("JacksonAdapter.createDefaultSerializerAdapter().deserialize(%s, Object.class, SerializerEncoding.JSON)",
                    ClassType.String.defaultValueExpression(jsonStr));
        }

        public Set<String> getImports() {
            if (helperFeatures.contains(ExampleHelperFeature.ThrowsIOException)) {
                imports.add(java.io.IOException.class.getName());
            }
            return imports;
        }

        public Set<ExampleHelperFeature> getHelperFeatures() {
            return helperFeatures;
        }

        public String accept(ExampleNode node) {
            if (node instanceof LiteralNode) {
                if (node.getClientType() != ClassType.Context) {
                    node.getClientType().addImportsTo(imports, false);
                }

                if (node.getClientType() == ClassType.URL) {
                    helperFeatures.add(ExampleHelperFeature.ThrowsIOException); // MalformedURLException from URL ctor
                }

                return node.getClientType().defaultValueExpression(((LiteralNode) node).getLiteralsValue());
            } else if (node instanceof ObjectNode) {
                IType simpleType = null;
                if (node.getObjectValue() instanceof Integer) {
                    simpleType = PrimitiveType.Int;
                } else if (node.getObjectValue() instanceof Long) {
                    simpleType = PrimitiveType.Long;
                } else if (node.getObjectValue() instanceof Float) {
                    simpleType = PrimitiveType.Float;
                } else if (node.getObjectValue() instanceof Double) {
                    simpleType = PrimitiveType.Double;
                } else if (node.getObjectValue() instanceof Boolean) {
                    simpleType = PrimitiveType.Boolean;
                } else if (node.getObjectValue() instanceof String) {
                    simpleType = ClassType.String;
                }

                if (simpleType != null) {
                    return simpleType.defaultValueExpression(node.getObjectValue().toString());
                } else {
                    helperFeatures.add(ExampleHelperFeature.ThrowsIOException);

                    try {
                        String jsonStr = OBJECT_MAPPER.writeValueAsString(node.getObjectValue());

                        return codeDeserializeJsonString(jsonStr);
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Failed to write JSON {}", node.getObjectValue());
                        throw new IllegalStateException(e);
                    }
                }
            } else if (node instanceof ListNode) {
                imports.add(java.util.Arrays.class.getName());

                StringBuilder builder = new StringBuilder();
                // Arrays.asList(...)
                builder.append("Arrays.asList(")
                        .append(node.getChildNodes().stream().map(this::accept).collect(Collectors.joining(", ")))
                        .append(")");

                return builder.toString();
            } else if (node instanceof MapNode) {
                imports.add(java.util.Map.class.getName());
                imports.add(java.util.HashMap.class.getName());

                helperFeatures.add(ExampleHelperFeature.MapOfMethod);

                List<String> keys = ((MapNode) node).getKeys();

                StringBuilder builder = new StringBuilder();
                // mapOf(...)
                // similar to Map.of in Java 9
                builder.append("mapOf(");
                for (int i = 0; i < keys.size(); ++i) {
                    if (i != 0) {
                        builder.append(", ");
                    }
                    String key = keys.get(i);
                    ExampleNode elementNode = node.getChildNodes().get(i);
                    builder.append(ClassType.String.defaultValueExpression(key))
                            .append(", ")
                            .append(this.accept(elementNode));
                }
                builder.append(")");

                return builder.toString();
            } else if (node instanceof ClientModelNode) {
                ClientModelNode clientModelNode = ((ClientModelNode) node);

                ClientModel model = clientModelNode.getClientModel();

                imports.add(model.getFullName());

                StringBuilder builder = new StringBuilder();
                if (JavaSettings.getInstance().isRequiredFieldsAsConstructorArgs()) {
                    List<ClientModelProperty> requiredParentProperties = ClientModelUtil.getRequiredWritableParentProperties(model);
                    List<ClientModelProperty> requiredProperties = model.getProperties().stream()
                            .filter(ClientModelProperty::isRequired)
                            .filter(property -> !property.isConstant())
                            .collect(Collectors.toList());

                    List<ModelProperty> properties = Stream.concat(
                            requiredParentProperties.stream(), requiredProperties.stream())
                            .map(ModelProperty::ofClientModelProperty)
                            .collect(Collectors.toList());
                    Map<ModelProperty, Integer> ctorPosition = new HashMap<>();
                    for (int i = 0; i < properties.size(); ++i) {
                        ctorPosition.put(properties.get(i), i);
                    }

                    List<String> initAtCtors = new ArrayList<>(Collections.nCopies(properties.size(), ""));
                    List<String> initAtSetters = new ArrayList<>();
                    for (ExampleNode childNode : node.getChildNodes()) {
                        ModelProperty modelProperty = clientModelNode.getClientModelProperties().get(childNode);
                        if (ctorPosition.containsKey(modelProperty)) {
                            initAtCtors.set(ctorPosition.get(modelProperty), this.accept(childNode));
                        } else {
                            // .setProperty(...)
                            initAtSetters.add(String.format(".%1$s(%2$s)", modelProperty.getSetterName(), this.accept(childNode)));
                        }
                    }
                    // model constructor
                    builder.append("new ").append(model.getName())
                            .append("(").append(String.join(", ", initAtCtors)).append(")");
                    // setters
                    initAtSetters.forEach(builder::append);
                } else {
                    // model with setters
                    builder.append("new ").append(model.getName()).append("()");
                    for (ExampleNode childNode : node.getChildNodes()) {
                        ModelProperty modelProperty = clientModelNode.getClientModelProperties().get(childNode);
                        // .setProperty(...)
                        builder.append(".").append(modelProperty.getSetterName())
                                .append("(").append(this.accept(childNode)).append(")");
                    }
                }
                return builder.toString();
            }
            return null;
        }
    }

}
