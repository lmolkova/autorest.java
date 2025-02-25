// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex.generated;

import com.azure.core.util.BinaryData;
import fixtures.bodycomplex.models.ReadonlyObj;
import org.junit.jupiter.api.Assertions;

public final class ReadonlyObjTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        ReadonlyObj model =
                BinaryData.fromString("{\"id\":\"lluwfzitonpeq\",\"size\":1432370634}").toObject(ReadonlyObj.class);
        Assertions.assertEquals(1432370634, model.getSize());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        ReadonlyObj model = new ReadonlyObj().setSize(1432370634);
        model = BinaryData.fromObject(model).toObject(ReadonlyObj.class);
        Assertions.assertEquals(1432370634, model.getSize());
    }
}
