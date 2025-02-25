// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.bodycomplex.generated;

import com.azure.core.util.BinaryData;
import fixtures.bodycomplex.models.Dog;
import org.junit.jupiter.api.Assertions;

public final class DogTests {
    @org.junit.jupiter.api.Test
    public void testDeserialize() throws Exception {
        Dog model =
                BinaryData.fromString("{\"food\":\"cnpqxuhivyqniwby\",\"id\":143848779,\"name\":\"vd\"}")
                        .toObject(Dog.class);
        Assertions.assertEquals(143848779, model.getId());
        Assertions.assertEquals("vd", model.getName());
        Assertions.assertEquals("cnpqxuhivyqniwby", model.getFood());
    }

    @org.junit.jupiter.api.Test
    public void testSerialize() throws Exception {
        Dog model = new Dog().setId(143848779).setName("vd").setFood("cnpqxuhivyqniwby");
        model = BinaryData.fromObject(model).toObject(Dog.class);
        Assertions.assertEquals(143848779, model.getId());
        Assertions.assertEquals("vd", model.getName());
        Assertions.assertEquals("cnpqxuhivyqniwby", model.getFood());
    }
}
