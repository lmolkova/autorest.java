// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.models.property.types;

import com.models.property.types.models.DatetimeProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

class DatetimeOperationClientTest {

    DatetimeOperationClient client = new TypesClientBuilder().buildDatetimeOperationClient();

    @Test
    void get() {
        DatetimeProperty datetimeProperty = client.get();
        Assertions.assertEquals("2022-08-26T18:38Z", datetimeProperty.getProperty().toString());
    }

    @Test
    void put() {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2022-08-26T18:38Z");
        DatetimeProperty datetimeProperty = new DatetimeProperty(offsetDateTime);
        client.put(datetimeProperty);
    }
}