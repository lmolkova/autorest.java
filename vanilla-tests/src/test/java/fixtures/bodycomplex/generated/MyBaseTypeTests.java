// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex.generated;

import com.azure.core.util.BinaryData;
import fixtures.bodycomplex.models.MyBaseType;
import org.junit.jupiter.api.Assertions;

public final class MyBaseTypeTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        MyBaseType model =
                BinaryData.fromString(
                                "{\"kind\":\"MyBaseType\",\"propB1\":\"jlxofpdvhpfxxyp\",\"helper\":{\"propBH1\":\"i\"}}")
                        .toObject(MyBaseType.class);
        Assertions.assertEquals("jlxofpdvhpfxxyp", model.getPropB1());
    }
}
