// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.cadl.longrunning.models;

import com.azure.core.util.ExpandableStringEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collection;

/** Defines values for OperationState. */
public final class OperationState extends ExpandableStringEnum<OperationState> {
    /** Static value InProgress for OperationState. */
    public static final OperationState IN_PROGRESS = fromString("InProgress");

    /** Static value Succeeded for OperationState. */
    public static final OperationState SUCCEEDED = fromString("Succeeded");

    /** Static value Failed for OperationState. */
    public static final OperationState FAILED = fromString("Failed");

    /** Static value Canceled for OperationState. */
    public static final OperationState CANCELED = fromString("Canceled");

    /**
     * Creates a new instance of OperationState value.
     *
     * @deprecated Use the {@link #fromString(String)} factory method.
     */
    @Deprecated
    public OperationState() {}

    /**
     * Creates or finds a OperationState from its string representation.
     *
     * @param name a name to look for.
     * @return the corresponding OperationState.
     */
    @JsonCreator
    public static OperationState fromString(String name) {
        return fromString(name, OperationState.class);
    }

    /**
     * Gets known OperationState values.
     *
     * @return known OperationState values.
     */
    public static Collection<OperationState> values() {
        return values(OperationState.class);
    }
}
