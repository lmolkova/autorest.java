// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.model.clientmodel;

import java.util.Set;
import java.util.function.Function;


/**
 * The details of an array type that is used by a client.
 */
public class ArrayType implements IType {
    /**
     * The {@code byte[]} type.
     */
    public static final ArrayType ByteArray = new ArrayType(PrimitiveType.Byte,
        defaultValueExpression -> defaultValueExpression == null
            ? "new byte[0]" // TODO (alzimmer): Should this be new byte[0] or null?
            : String.format("\"%1$s\".getBytes()", defaultValueExpression));

    private final String toStringValue;
    private final IType elementType;
    private final Function<String, String> defaultValueExpressionConverter;

    private ArrayType(IType elementType, Function<String, String> defaultValueExpressionConverter) {
        this.toStringValue = elementType + "[]";
        this.elementType = elementType;
        this.defaultValueExpressionConverter = defaultValueExpressionConverter;
    }

    /**
     * Gets the element type of the array.
     *
     * @return The element type of the array.
     */
    public final IType getElementType() {
        return elementType;
    }

    @Override
    public String toString() {
        return toStringValue;
    }

    @Override
    public final IType asNullable() {
        return this;
    }

    @Override
    public final boolean contains(IType type) {
        return this == type || getElementType().contains(type);
    }

    @Override
    public final void addImportsTo(Set<String> imports, boolean includeImplementationImports) {
        getElementType().addImportsTo(imports, includeImplementationImports);
    }

    @Override
    public final String defaultValueExpression(String sourceExpression) {
        return defaultValueExpressionConverter.apply(sourceExpression);
    }

    @Override
    public String defaultValueExpression() {
        return defaultValueExpression(null);
    }

    @Override
    public final IType getClientType() {
        // The only supported array type is byte[]
        return this;
    }

    @Override
    public final String convertToClientType(String expression) {
        // The only supported array type is byte[]
        return expression;
    }

    @Override
    public final String convertFromClientType(String expression) {
        // The only supported array type is byte[]
        return expression;
    }

    @Override
    public String validate(String expression) {
        return null;
    }

    @Override
    public String jsonDeserializationMethod(String jsonReaderName) {
        return jsonReaderName + ".getBinary()";
    }

    @Override
    public String jsonSerializationMethodCall(String jsonWriterName, String fieldName, String valueGetter) {
        return fieldName == null
            ? String.format("%s.writeBinary(%s)", jsonWriterName, valueGetter)
            : String.format("%s.writeBinaryField(\"%s\", %s)", jsonWriterName, fieldName, valueGetter);
    }

    @Override
    public String xmlDeserializationMethod(String attributeName, String attributeNamespace) {
        if (attributeName == null) {
            return "getBinaryElement()";
        } else {
            return (attributeNamespace == null)
                ? "getBinaryAttribute(null, \"" + attributeName + "\")"
                : "getBinaryAttribute(\"" + attributeNamespace + "\", \"" + attributeName + "\")";
        }
    }

    @Override
    public String xmlSerializationMethodCall(String xmlWriterName, String attributeOrElementName, String namespaceUri,
        String valueGetter, boolean isAttribute, boolean nameIsVariable) {
        return ClassType.xmlSerializationCallHelper(xmlWriterName, "writeBinary", attributeOrElementName, namespaceUri,
            valueGetter, isAttribute, nameIsVariable);
    }
}
