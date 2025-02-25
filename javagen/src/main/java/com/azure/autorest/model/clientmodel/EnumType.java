// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.model.clientmodel;

import com.azure.autorest.util.CodeNamer;

import java.util.List;
import java.util.Set;

/**
 * The details of an enumerated type that is used by a service.
 */
public class EnumType implements IType {
    /**
     * The name of the new Enum.
     */
    private final String name;
    private final String description;
    /**
     * The package that this enumeration belongs to.
     */
    private final String packageName;
    /**
     * Whether this will be an ExpandableStringEnum type.
     */
    private final boolean expandable;
    /**
     * The values of the Enum.
     */
    private final List<ClientEnumValue> values;

    private final IType elementType;

    private final ImplementationDetails implementationDetails;

    /**
     * Create a new Enum with the provided properties.
     * @param name The name of the new Enum.
     * @param description The description of the Enum.
     * @param expandable Whether this will be an ExpandableStringEnum type.
     * @param values The values of the Enum.
     */
    private EnumType(String packageKeyword, String name, String description,
                     boolean expandable, List<ClientEnumValue> values,
                     IType elementType,
                     ImplementationDetails implementationDetails) {
        this.name = name;
        this.packageName = packageKeyword;
        this.description = description;
        this.expandable = expandable;
        this.values = values;
        this.elementType = elementType;
        this.implementationDetails = implementationDetails;
    }

    public final String getName() {
        return name;
    }

    public final String getPackage() {
        return packageName;
    }

    public String getDescription() {
        return description;
    }

    public final boolean getExpandable() {
        return expandable;
    }

    public final List<ClientEnumValue> getValues() {
        return values;
    }

    public final IType getElementType() {
        return elementType;
    }

    public final void addImportsTo(Set<String> imports, boolean includeImplementationImports) {
        imports.add(String.format("%1$s.%2$s", getPackage(), getName()));

        // EnumTypes may result in Collectors being used, if Collectors isn't used the unused import will be removed.
        imports.add("java.util.stream.Collectors");
    }

    public final boolean isNullable() {
        return true;
    }

    public final IType asNullable() {
        return this;
    }

    public final boolean contains(IType type) {
        return this == type;
    }

    public final String defaultValueExpression(String sourceExpression) {
        if (sourceExpression == null) {
            return null;
        }
        if (this.getExpandable()) {
            for (ClientEnumValue enumValue : this.getValues()) {
                if (sourceExpression.equals(enumValue.getValue())) {
                    return String.format("%1$s.%2$s", getName(), enumValue.getName());
                }
            }
            return String.format("%1$s.from%2$s(%3$s)", getName(),
                CodeNamer.toPascalCase(this.getElementType().toString()),
                this.getElementType().defaultValueExpression(sourceExpression));
        } else {
            for (ClientEnumValue enumValue : this.getValues()) {
                if (sourceExpression.equals(enumValue.getValue())) {
                    return String.format("%1$s.%2$s", getName(), enumValue.getName());
                }
            }
            return null;
        }
    }

    /**
     * Gets the method name used to convert JSON to the enum type.
     *
     * @return The method name used to convert JSON to the enum type.
     */
    public final String getFromJsonMethodName() {
        return "from" + CodeNamer.toPascalCase(elementType.getClientType().toString());
    }

    /**
     * Gets the method name used to convert the enum type to JSON.
     *
     * @return The method name used to convert the enum type to JSON.
     */
    public final String getToJsonMethodName() {
        return "to" + CodeNamer.toPascalCase(elementType.getClientType().toString());
    }

    @Override
    public String defaultValueExpression() {
        return "null";
    }

    public final IType getClientType() {
        return this;
    }

    public final String convertToClientType(String expression) {
        return expression;
    }

    public final String convertFromClientType(String expression) {
        return expression;
    }

    public final String validate(String expression) {
        return null;
    }

    public ImplementationDetails getImplementationDetails() {
        return implementationDetails;
    }

    @Override
    public String jsonDeserializationMethod(String jsonReaderName) {
        return String.format("%s.%s(%s.getString())", name, getFromJsonMethodName(), jsonReaderName);
    }

    @Override
    public String jsonSerializationMethodCall(String jsonWriterName, String fieldName, String valueGetter) {
        return fieldName == null
            ? String.format("%s.writeString(Objects.toString(%s, null))", jsonWriterName, valueGetter)
            : String.format("%s.writeStringField(\"%s\", Objects.toString(%s, null))",
                jsonWriterName, fieldName, valueGetter);
    }

    @Override
    public String xmlDeserializationMethod(String attributeName, String attributeNamespace) {
        String createCall = name + "::" + getFromJsonMethodName();
        if (attributeName == null) {
            return String.format("getNullableElement(%s)", createCall);
        } else {
            return (attributeNamespace == null)
                ? String.format("getNullableAttribute(null, \"%s\", %s)", attributeName, createCall)
                : String.format("getNullableAttribute(\"%s\", \"%s\", %s)", attributeNamespace, attributeName, createCall);
        }
    }

    @Override
    public String xmlSerializationMethodCall(String xmlWriterName, String attributeOrElementName, String namespaceUri,
        String valueGetter, boolean isAttribute, boolean nameIsVariable) {
        String value = "Objects.toString(" + valueGetter + ", null)";
        return ClassType.xmlSerializationCallHelper(xmlWriterName, "writeString", attributeOrElementName, namespaceUri,
            value, isAttribute, nameIsVariable);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static class Builder {
        private String name;
        private String description;
        private String packageName;
        private boolean expandable;
        private List<ClientEnumValue> values;
        private IType elementType = ClassType.String;

        private ImplementationDetails implementationDetails;

        /**
         * Sets the name of the Enum.
         * @param name the name of the Enum
         * @return the Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the package name of the Enum.
         * @param packageName the package name of the Enum
         * @return the Builder
         */
        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        /**
         * Sets the description of the Enum.
         * @param description the description of the Enum
         * @return the Builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets whether the Enum is expandable.
         * @param expandable whether the Enum is expandable
         * @return the Builder
         */
        public Builder expandable(boolean expandable) {
            this.expandable = expandable;
            return this;
        }

        /**
         * Sets the values of the Enum.
         * @param values the values of the Enum
         * @return the Builder
         */
        public Builder values(List<ClientEnumValue> values) {
            this.values = values;
            return this;
        }

        /**
         * Sets the type of elements of the Enum.
         * @param elementType the type of elements of the Enum
         * @return the Builder
         */
        public Builder elementType(IType elementType) {
            if (elementType != null) {
                this.elementType = elementType;
            }
            return this;
        }

        /**
         * Sets the implementation details for the model.
         * @param implementationDetails the implementation details.
         * @return the Builder itself
         */
        public Builder implementationDetails(ImplementationDetails implementationDetails) {
            this.implementationDetails = implementationDetails;
            return this;
        }

        /**
         * @return an immutable EnumType instance with the configurations on this builder.
         */
        public EnumType build() {
            return new EnumType(
                    packageName,
                    name,
                    description,
                    expandable,
                    values,
                    elementType,
                    implementationDetails
            );
        }
    }
}
