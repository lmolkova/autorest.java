// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.customization;

import com.azure.autorest.customization.implementation.Utils;
import com.azure.autorest.customization.implementation.ls.EclipseLanguageClient;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * The base class for customization. Extend this class to plug into AutoRest generation.
 */
public abstract class Customization {
    /**
     * Start the customization process. This is called by the post processor in AutoRest.
     *
     * @param files the list of files generated in the previous steps in AutoRest
     * @param logger the logger
     * @return the list of files after customization
     */
    public final Map<String, String> run(Map<String, String> files, Logger logger) {
        return run(null, files, logger);
    }

    final Map<String, String> run(String pathToLanguageServerPlugin, Map<String, String> files, Logger logger) {
        Path tempDirWithPrefix;

        // Populate editor
        Editor editor;
        try {
            tempDirWithPrefix = Files.createTempDirectory("temp");
            editor = new Editor(files, tempDirWithPrefix);
            InputStream pomStream = Customization.class.getResourceAsStream("/pom.xml");
            byte[] buffer = new byte[pomStream.available()];
            pomStream.read(buffer);
            editor.addFile("pom.xml", new String(buffer, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Start language client
        try (EclipseLanguageClient languageClient = new EclipseLanguageClient(pathToLanguageServerPlugin,
            tempDirWithPrefix.toString())) {
            languageClient.initialize();
            customize(new LibraryCustomization(editor, languageClient), logger);
            editor.removeFile("pom.xml");
            return editor.getContents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Utils.deleteDirectory(tempDirWithPrefix.toFile());
        }
    }

    /**
     * Override this method to customize the client library.
     *
     * @param libraryCustomization the top level customization object
     * @param logger the logger
     */
    public abstract void customize(LibraryCustomization libraryCustomization, Logger logger);
}
