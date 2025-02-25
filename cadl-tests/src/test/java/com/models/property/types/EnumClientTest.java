// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.models.property.types;

import com.models.property.types.models.EnumProperty;
import com.models.property.types.models.FixedInnerEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnumClientTest {

    EnumClient client = new TypesClientBuilder().buildEnumClient();

    @Test
    void get() {
        EnumProperty enumProperty = client.get();
        FixedInnerEnum innerEnum = enumProperty.getProperty();
        Assertions.assertEquals("ValueOne", innerEnum.toString());
    }

    @Test
    void put() {
        FixedInnerEnum innerEnum = FixedInnerEnum.VALUE_ONE;
        EnumProperty enumProperty = new EnumProperty(innerEnum);
        client.put(enumProperty);
    }
}
