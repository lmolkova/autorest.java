// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.customization;

import com.azure.autorest.customization.implementation.Utils;
import com.azure.autorest.customization.implementation.ls.EclipseLanguageClient;
import com.azure.autorest.customization.implementation.ls.models.CodeAction;
import com.azure.autorest.customization.implementation.ls.models.JavaCodeActionKind;
import com.azure.autorest.customization.implementation.ls.models.SymbolInformation;
import com.azure.autorest.customization.implementation.ls.models.SymbolKind;
import com.azure.autorest.customization.implementation.ls.models.TextEdit;
import com.azure.autorest.customization.implementation.ls.models.WorkspaceEdit;
import com.azure.autorest.customization.implementation.ls.models.WorkspaceEditCommand;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Customization for an AutoRest generated instance property.
 * <p>
 * For constant property customizations use {@link ConstantCustomization}.
 */
public final class PropertyCustomization extends CodeCustomization {
    private static final Pattern METHOD_PARAMS_CAPTURE = Pattern.compile("\\(.*\\)");

    private final String packageName;
    private final String className;
    private final String propertyName;

    PropertyCustomization(Editor editor, EclipseLanguageClient languageClient, String packageName, String className,
        SymbolInformation symbol, String propertyName) {
        super(editor, languageClient, symbol);
        this.packageName = packageName;
        this.className = className;
        this.propertyName = propertyName;
    }

    /**
     * Gets the name of the class that contains this property.
     *
     * @return The name of the class that contains this property.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the name of this property.
     *
     * @return The name of this property.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Rename a property in the class. This is a refactor operation. All references of the property will be renamed and
     * the getter and setter method(s) for this property will be renamed accordingly as well.
     *
     * @param newName the new name for the property
     * @return the current class customization for chaining
     */
    public PropertyCustomization rename(String newName) {
        List<SymbolInformation> symbols = languageClient.listDocumentSymbols(fileUri)
            .stream().filter(si -> si.getName().toLowerCase().contains(propertyName.toLowerCase()))
            .collect(Collectors.toList());
        String propertyPascalName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        String newPascalName = newName.substring(0, 1).toUpperCase() + newName.substring(1);
        for (SymbolInformation symbol : symbols) {
            if (symbol.getKind() == SymbolKind.FIELD) {
                WorkspaceEdit edit = languageClient.renameSymbol(fileUri, symbol.getLocation().getRange().getStart(), newName);
                Utils.applyWorkspaceEdit(edit, editor, languageClient);
            } else if (symbol.getKind() == SymbolKind.METHOD) {
                String methodName = symbol.getName().replace(propertyPascalName, newPascalName)
                    .replace(propertyName, newName);
                methodName = METHOD_PARAMS_CAPTURE.matcher(methodName).replaceFirst("");
                WorkspaceEdit edit = languageClient.renameSymbol(fileUri, symbol.getLocation().getRange().getStart(), methodName);
                Utils.applyWorkspaceEdit(edit, editor, languageClient);
            }
        }
        return refreshCustomization(newName);
    }

    /**
     * Add an annotation to a property in the class.
     *
     * @param annotation the annotation to add. The leading @ can be omitted.
     * @return the current property customization for chaining
     */
    public PropertyCustomization addAnnotation(String annotation) {
        return Utils.addAnnotation(annotation, this, () -> refreshCustomization(propertyName));
    }

    /**
     * Remove an annotation from the property.
     *
     * @param annotation the annotation to remove from the property. The leading @ can be omitted.
     * @return the current property customization for chaining
     */
    public PropertyCustomization removeAnnotation(String annotation) {
        return Utils.removeAnnotation(this, compilationUnit -> compilationUnit.getClassByName(className).get()
            .getFieldByName(propertyName).get()
            .getAnnotationByName(Utils.cleanAnnotationName(annotation)), () -> refreshCustomization(propertyName));
    }

    /**
     * Generates a getter and a setter method(s) for a property in the class. This is a refactor operation. If a getter
     * or a setter is already available on the class, the current getter or setter will be kept.
     *
     * @return the current class customization for chaining
     */
    public PropertyCustomization generateGetterAndSetter() {
        Optional<CodeAction> generateAccessors = languageClient.listCodeActions(fileUri, symbol.getLocation().getRange())
            .stream().filter(ca -> ca.getKind().equals(JavaCodeActionKind.SOURCE_GENERATE_ACCESSORS.toString()))
            .findFirst();
        if (generateAccessors.isPresent()) {
            WorkspaceEditCommand command;
            if (generateAccessors.get().getCommand() instanceof WorkspaceEditCommand) {
                command = (WorkspaceEditCommand) generateAccessors.get().getCommand();
                for (WorkspaceEdit workspaceEdit : command.getArguments()) {
                    Utils.applyWorkspaceEdit(workspaceEdit, editor, languageClient);
                }
                List<TextEdit> formats = languageClient.format(fileUri);
                Utils.applyTextEdits(fileUri, formats, editor, languageClient);

                String setterMethod = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                new PackageCustomization(editor, languageClient, packageName)
                    .getClass(className)
                    .getMethod(setterMethod).setReturnType(className, "this");
            }
        }

        return this;
    }

    /**
     * Replace the modifier for this property.
     * <p>
     * For compound modifiers such as {@code public final} use bitwise OR ({@code |}) of multiple Modifiers, {@code
     * Modifier.PUBLIC | Modifier.FINAL}.
     * <p>
     * Pass {@code 0} for {@code modifiers} to indicate that the property has no modifiers.
     *
     * @param modifiers The {@link Modifier Modifiers} for the property.
     * @return The updated PropertyCustomization object.
     * @throws IllegalArgumentException If the {@code modifier} is less than {@code 0} or any {@link Modifier} included
     * in the bitwise OR isn't a valid property {@link Modifier}.
     */
    public PropertyCustomization setModifier(int modifiers) {
        String target = " *(?:(?:public|protected|private|static|final|transient|volatile) ?)*(.* )";
        languageClient.listDocumentSymbols(symbol.getLocation().getUri())
            .stream().filter(si -> si.getName().equals(propertyName) && si.getKind() == SymbolKind.FIELD)
            .findFirst()
            .ifPresent(symbolInformation -> Utils.replaceModifier(symbolInformation, editor, languageClient,
                target + propertyName, "$1" + propertyName, Modifier.fieldModifiers(), modifiers));

        return refreshCustomization(propertyName);
    }

    private PropertyCustomization refreshCustomization(String propertyName) {
        return new PackageCustomization(editor, languageClient, packageName)
            .getClass(className)
            .getProperty(propertyName);
    }
}
