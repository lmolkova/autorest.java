// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.models.property.types;

import com.models.property.types.models.CollectionsModelProperty;
import com.models.property.types.models.InnerModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class CollectionsModelClientTest {

    CollectionsModelClient client = new TypesClientBuilder().buildCollectionsModelClient();

    @Test
    void get() {
        CollectionsModelProperty collectionsModelProperty = client.get();
        List<InnerModel> properties = collectionsModelProperty.getProperty();
        Assertions.assertEquals("hello", properties.get(0).getProperty());
        Assertions.assertEquals("world", properties.get(1).getProperty());
    }

    @Test
    void put() {
        InnerModel innerModel1 = new InnerModel("hello");
        InnerModel innerModel2 = new InnerModel("world");
        List<InnerModel> properties = Arrays.asList(innerModel1, innerModel2);
        CollectionsModelProperty collectionsModelProperty = new CollectionsModelProperty(properties);
        client.put(collectionsModelProperty);
    }
}