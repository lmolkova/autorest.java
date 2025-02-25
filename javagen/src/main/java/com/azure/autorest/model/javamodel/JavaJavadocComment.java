// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.model.javamodel;

import com.azure.autorest.util.CodeNamer;

public class JavaJavadocComment {
    private JavaFileContents contents;
    private boolean expectsLineSeparator;

    public JavaJavadocComment(JavaFileContents contents) {
        this.contents = contents;
    }

    private static String trim(String value) {
        return value == null || value.isEmpty() ? value : value.trim();
    }

    private static String ensurePeriod(String value) {
        return value == null || value.isEmpty() || value.endsWith(".") ? value : value + '.';
    }

    private static String processText(String value) {
        return CodeNamer.escapeComment(CodeNamer.escapeXmlComment(ensurePeriod(trim(value))));
    }

    private void addExpectedLineSeparator() {
        if (expectsLineSeparator) {
            expectsLineSeparator = false;
            contents.line();
        }
    }

    public final void description(String description) {
        String processedText = processText(description);
        line(processedText);
    }

    public final void line(String text) {
        if (text != null && !text.isEmpty()) {
            contents.line(text);
            expectsLineSeparator = true;
        }
    }

    public final void param(String parameterName, String parameterDescription) {
        addExpectedLineSeparator();
        contents.line(String.format("@param %1$s %2$s", parameterName, processText(parameterDescription)));
    }

    public final void methodReturns(String returnValueDescription) {
        if (returnValueDescription != null && !returnValueDescription.isEmpty()) {
            addExpectedLineSeparator();
            contents.line(String.format("@return %1$s", processText(returnValueDescription)));
        }
    }

    public final void methodThrows(String exceptionTypeName, String description) {
        addExpectedLineSeparator();
        contents.line(String.format("@throws %1$s %2$s", exceptionTypeName, processText(description)));
    }

    public final void inheritDoc() {
        addExpectedLineSeparator();
        contents.line("{@inheritDoc}");
    }

    public final void deprecated(String description) {
        addExpectedLineSeparator();
        contents.line(String.format("@deprecated %1$s", description));
    }
}
