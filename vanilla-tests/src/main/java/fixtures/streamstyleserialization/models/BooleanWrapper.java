// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.streamstyleserialization.models;

import com.azure.core.annotation.Fluent;
import com.azure.json.JsonReader;
import com.azure.json.JsonSerializable;
import com.azure.json.JsonToken;
import com.azure.json.JsonWriter;
import java.io.IOException;

/** The BooleanWrapper model. */
@Fluent
public final class BooleanWrapper implements JsonSerializable<BooleanWrapper> {
    /*
     * The field_true property.
     */
    private Boolean fieldTrue;

    /*
     * The field_false property.
     */
    private Boolean fieldFalse;

    /** Creates an instance of BooleanWrapper class. */
    public BooleanWrapper() {}

    /**
     * Get the fieldTrue property: The field_true property.
     *
     * @return the fieldTrue value.
     */
    public Boolean isFieldTrue() {
        return this.fieldTrue;
    }

    /**
     * Set the fieldTrue property: The field_true property.
     *
     * @param fieldTrue the fieldTrue value to set.
     * @return the BooleanWrapper object itself.
     */
    public BooleanWrapper setFieldTrue(Boolean fieldTrue) {
        this.fieldTrue = fieldTrue;
        return this;
    }

    /**
     * Get the fieldFalse property: The field_false property.
     *
     * @return the fieldFalse value.
     */
    public Boolean isFieldFalse() {
        return this.fieldFalse;
    }

    /**
     * Set the fieldFalse property: The field_false property.
     *
     * @param fieldFalse the fieldFalse value to set.
     * @return the BooleanWrapper object itself.
     */
    public BooleanWrapper setFieldFalse(Boolean fieldFalse) {
        this.fieldFalse = fieldFalse;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {}

    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeBooleanField("field_true", this.fieldTrue);
        jsonWriter.writeBooleanField("field_false", this.fieldFalse);
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of BooleanWrapper from the JsonReader.
     *
     * @param jsonReader The JsonReader being read.
     * @return An instance of BooleanWrapper if the JsonReader was pointing to an instance of it, or null if it was
     *     pointing to JSON null.
     * @throws IOException If an error occurs while reading the BooleanWrapper.
     */
    public static BooleanWrapper fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(
                reader -> {
                    Boolean fieldTrue = null;
                    Boolean fieldFalse = null;
                    while (reader.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = reader.getFieldName();
                        reader.nextToken();

                        if ("field_true".equals(fieldName)) {
                            fieldTrue = reader.getNullable(JsonReader::getBoolean);
                        } else if ("field_false".equals(fieldName)) {
                            fieldFalse = reader.getNullable(JsonReader::getBoolean);
                        } else {
                            reader.skipChildren();
                        }
                    }
                    BooleanWrapper deserializedValue = new BooleanWrapper();
                    deserializedValue.fieldTrue = fieldTrue;
                    deserializedValue.fieldFalse = fieldFalse;

                    return deserializedValue;
                });
    }
}
