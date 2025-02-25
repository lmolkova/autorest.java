// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex.generated;

import com.azure.core.util.BinaryData;
import fixtures.bodycomplex.models.Fish;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;

public final class FishTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        Fish model =
                BinaryData.fromString(
                                "{\"fishtype\":\"Fish\",\"species\":\"k\",\"length\":32.04869,\"siblings\":[{\"fishtype\":\"Fish\",\"species\":\"ccsnhsjc\",\"length\":26.373684,\"siblings\":[]},{\"fishtype\":\"Fish\",\"species\":\"kryhtnapczwlokj\",\"length\":68.2114,\"siblings\":[]},{\"fishtype\":\"Fish\",\"species\":\"vnipjox\",\"length\":40.86501,\"siblings\":[]}]}")
                        .toObject(Fish.class);
        Assertions.assertEquals("k", model.getSpecies());
        Assertions.assertEquals(32.04869f, model.getLength());
        Assertions.assertEquals("ccsnhsjc", model.getSiblings().get(0).getSpecies());
        Assertions.assertEquals(26.373684f, model.getSiblings().get(0).getLength());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        Fish model =
                new Fish(32.04869f)
                        .setSpecies("k")
                        .setSiblings(
                                Arrays.asList(
                                        new Fish(26.373684f).setSpecies("ccsnhsjc").setSiblings(Arrays.asList()),
                                        new Fish(68.2114f).setSpecies("kryhtnapczwlokj").setSiblings(Arrays.asList()),
                                        new Fish(40.86501f).setSpecies("vnipjox").setSiblings(Arrays.asList())));
        model = BinaryData.fromObject(model).toObject(Fish.class);
        Assertions.assertEquals("k", model.getSpecies());
        Assertions.assertEquals(32.04869f, model.getLength());
        Assertions.assertEquals("ccsnhsjc", model.getSiblings().get(0).getSpecies());
        Assertions.assertEquals(26.373684f, model.getSiblings().get(0).getLength());
    }
}
