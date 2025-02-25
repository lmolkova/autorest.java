// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.autorest.customization.implementation.ls.models;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class WorkspaceEdit {
    Map<URI, List<TextEdit>> changes;

    public Map<URI, List<TextEdit>> getChanges() {
        return changes;
    }

    public void setChanges(Map<URI, List<TextEdit>> changes) {
        this.changes = changes;
    }
}
