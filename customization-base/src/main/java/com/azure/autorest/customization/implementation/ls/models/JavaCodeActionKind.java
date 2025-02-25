// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.customization.implementation.ls.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum JavaCodeActionKind {
    /**
     * Base kind for "generate" source actions
     */
    SOURCE_GENERATE(CodeActionKind.SOURCE + ".generate"),

    /**
     * Generate accessors kind
     */
    SOURCE_GENERATE_ACCESSORS(SOURCE_GENERATE + ".accessors"),

    /**
     * Generate hashCode/equals kind
     */
    SOURCE_GENERATE_HASHCODE_EQUALS(SOURCE_GENERATE + ".hashCodeEquals"),

    /**
     * Generate toString kind
     */
    SOURCE_GENERATE_TO_STRING(SOURCE_GENERATE + ".toString"),

    /**
     * Generate constructors kind
     */
    SOURCE_GENERATE_CONSTRUCTORS(SOURCE_GENERATE + ".constructors"),

    /**
     * Generate delegate methods
     */
    SOURCE_GENERATE_DELEGATE_METHODS(SOURCE_GENERATE + ".delegateMethods"),

    /**
     * Override/Implement methods kind
     */
    SOURCE_OVERRIDE_METHODS(CodeActionKind.SOURCE + ".overrideMethods"),

    /**
     * Extract to method kind
     */
    REFACTOR_EXTRACT_METHOD(CodeActionKind.REFACTOR_EXTRACT + ".function"), // using `.function` instead of `.method` to match existing keybinding),

    /**
     * Extract to constant kind
     */
    REFACTOR_EXTRACT_CONSTANT(CodeActionKind.REFACTOR_EXTRACT + ".constant"),

    /**
     * Extract to variable kind
     */
    REFACTOR_EXTRACT_VARIABLE(CodeActionKind.REFACTOR_EXTRACT + ".variable"),

    /**
     * Extract to field kind
     */
    REFACTOR_EXTRACT_FIELD(CodeActionKind.REFACTOR_EXTRACT + ".field"),

    /**
     * Move kind
     */
    REFACTOR_MOVE(CodeActionKind.REFACTOR + ".move"),

    /**
     * Assign statement to new local variable
     */
    REFACTOR_ASSIGN_VARIABLE(CodeActionKind.REFACTOR + ".assign.variable"),

    /**
     * Assign statement to new field
     */
    REFACTOR_ASSIGN_FIELD(CodeActionKind.REFACTOR + ".assign.field"),

    /**
     * Base kind for "quickassist" code actions
     */
    QUICK_ASSIST("quickassist");

    private static final Map<String, JavaCodeActionKind> STRING_TO_KIND_MAP;

    static {
        STRING_TO_KIND_MAP = new HashMap<>();

        for (JavaCodeActionKind kind : JavaCodeActionKind.values()) {
            STRING_TO_KIND_MAP.putIfAbsent(kind.value, kind);
        }
    }

    private final String value;

    JavaCodeActionKind(String value) {
        this.value = value;
    }

    @JsonCreator
    public static JavaCodeActionKind fromString(String value) {
        return STRING_TO_KIND_MAP.get(value);
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
