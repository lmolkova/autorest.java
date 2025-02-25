// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex.generated;

import com.azure.core.util.BinaryData;
import fixtures.bodycomplex.models.Cookiecuttershark;
import fixtures.bodycomplex.models.Fish;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;

public final class CookiecuttersharkTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        Cookiecuttershark model =
                BinaryData.fromString(
                                "{\"fishtype\":\"cookiecuttershark\",\"age\":885062254,\"birthday\":\"2021-05-27T22:50:20Z\",\"species\":\"uticndvkaozwyif\",\"length\":25.075048,\"siblings\":[{\"fishtype\":\"Fish\",\"species\":\"urokft\",\"length\":81.88607,\"siblings\":[]},{\"fishtype\":\"Fish\",\"species\":\"iwpwcuk\",\"length\":48.25244,\"siblings\":[]}]}")
                        .toObject(Cookiecuttershark.class);
        Assertions.assertEquals("uticndvkaozwyif", model.getSpecies());
        Assertions.assertEquals(25.075048f, model.getLength());
        Assertions.assertEquals("urokft", model.getSiblings().get(0).getSpecies());
        Assertions.assertEquals(81.88607f, model.getSiblings().get(0).getLength());
        Assertions.assertEquals(885062254, model.getAge());
        Assertions.assertEquals(OffsetDateTime.parse("2021-05-27T22:50:20Z"), model.getBirthday());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        Cookiecuttershark model =
                new Cookiecuttershark(25.075048f, OffsetDateTime.parse("2021-05-27T22:50:20Z"))
                        .setSpecies("uticndvkaozwyif")
                        .setSiblings(
                                Arrays.asList(
                                        new Fish(81.88607f).setSpecies("urokft").setSiblings(Arrays.asList()),
                                        new Fish(48.25244f).setSpecies("iwpwcuk").setSiblings(Arrays.asList())))
                        .setAge(885062254);
        model = BinaryData.fromObject(model).toObject(Cookiecuttershark.class);
        Assertions.assertEquals("uticndvkaozwyif", model.getSpecies());
        Assertions.assertEquals(25.075048f, model.getLength());
        Assertions.assertEquals("urokft", model.getSiblings().get(0).getSpecies());
        Assertions.assertEquals(81.88607f, model.getSiblings().get(0).getLength());
        Assertions.assertEquals(885062254, model.getAge());
        Assertions.assertEquals(OffsetDateTime.parse("2021-05-27T22:50:20Z"), model.getBirthday());
    }
}
