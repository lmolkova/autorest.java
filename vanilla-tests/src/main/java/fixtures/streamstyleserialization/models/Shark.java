// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.streamstyleserialization.models;

import com.azure.core.annotation.Fluent;
import com.azure.core.util.logging.ClientLogger;
import com.azure.json.JsonReader;
import com.azure.json.JsonToken;
import com.azure.json.JsonWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** The Shark model. */
@Fluent
public class Shark extends Fish {
    /*
     * The fishtype property.
     */
    private static final String FISHTYPE = "shark";

    /*
     * The age property.
     */
    private Integer age;

    /*
     * The birthday property.
     */
    private final OffsetDateTime birthday;

    /**
     * Creates an instance of Shark class.
     *
     * @param length the length value to set.
     * @param birthday the birthday value to set.
     */
    public Shark(float length, OffsetDateTime birthday) {
        super(length);
        this.birthday = birthday;
    }

    /**
     * Get the age property: The age property.
     *
     * @return the age value.
     */
    public Integer getAge() {
        return this.age;
    }

    /**
     * Set the age property: The age property.
     *
     * @param age the age value to set.
     * @return the Shark object itself.
     */
    public Shark setAge(Integer age) {
        this.age = age;
        return this;
    }

    /**
     * Get the birthday property: The birthday property.
     *
     * @return the birthday value.
     */
    public OffsetDateTime getBirthday() {
        return this.birthday;
    }

    /** {@inheritDoc} */
    @Override
    public Shark setSpecies(String species) {
        super.setSpecies(species);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public Shark setSiblings(List<Fish> siblings) {
        super.setSiblings(siblings);
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    @Override
    public void validate() {
        super.validate();
        if (getBirthday() == null) {
            throw LOGGER.logExceptionAsError(
                    new IllegalArgumentException("Missing required property birthday in model Shark"));
        }
    }

    private static final ClientLogger LOGGER = new ClientLogger(Shark.class);

    @Override
    public JsonWriter toJson(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeStartObject();
        jsonWriter.writeStringField("fishtype", FISHTYPE);
        jsonWriter.writeFloatField("length", getLength());
        jsonWriter.writeStringField("species", getSpecies());
        jsonWriter.writeArrayField("siblings", getSiblings(), (writer, element) -> writer.writeJson(element));
        jsonWriter.writeStringField("birthday", Objects.toString(this.birthday, null));
        jsonWriter.writeNumberField("age", this.age);
        return jsonWriter.writeEndObject();
    }

    /**
     * Reads an instance of Shark from the JsonReader.
     *
     * @param jsonReader The JsonReader being read.
     * @return An instance of Shark if the JsonReader was pointing to an instance of it, or null if it was pointing to
     *     JSON null.
     * @throws IllegalStateException If the deserialized JSON object was missing any required properties or the
     *     polymorphic discriminator.
     * @throws IOException If an error occurs while reading the Shark.
     */
    public static Shark fromJson(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(
                reader -> {
                    String discriminatorValue = null;
                    JsonReader readerToUse = null;

                    // Read the first field name and determine if it's the discriminator field.
                    reader.nextToken();
                    if ("fishtype".equals(reader.getFieldName())) {
                        reader.nextToken();
                        discriminatorValue = reader.getString();
                        readerToUse = reader;
                    } else {
                        // If it isn't the discriminator field buffer the JSON to make it replayable and find the
                        // discriminator field value.
                        JsonReader replayReader = reader.bufferObject();
                        replayReader.nextToken(); // Prepare for reading
                        while (replayReader.nextToken() != JsonToken.END_OBJECT) {
                            String fieldName = replayReader.getFieldName();
                            replayReader.nextToken();
                            if ("fishtype".equals(fieldName)) {
                                discriminatorValue = replayReader.getString();
                                break;
                            } else {
                                replayReader.skipChildren();
                            }
                        }

                        if (discriminatorValue != null) {
                            readerToUse = replayReader.reset();
                        }
                    }
                    // Use the discriminator value to determine which subtype should be deserialized.
                    if (discriminatorValue == null || "shark".equals(discriminatorValue)) {
                        return fromJsonKnownDiscriminator(readerToUse);
                    } else if ("sawshark".equals(discriminatorValue)) {
                        return Sawshark.fromJson(readerToUse);
                    } else if ("goblin".equals(discriminatorValue)) {
                        return Goblinshark.fromJson(readerToUse);
                    } else if ("cookiecuttershark".equals(discriminatorValue)) {
                        return Cookiecuttershark.fromJson(readerToUse);
                    } else {
                        throw new IllegalStateException(
                                "Discriminator field 'fishtype' didn't match one of the expected values 'shark', 'sawshark', 'goblin', or 'cookiecuttershark'. It was: '"
                                        + discriminatorValue
                                        + "'.");
                    }
                });
    }

    static Shark fromJsonKnownDiscriminator(JsonReader jsonReader) throws IOException {
        return jsonReader.readObject(
                reader -> {
                    boolean lengthFound = false;
                    float length = 0.0f;
                    String species = null;
                    List<Fish> siblings = null;
                    boolean birthdayFound = false;
                    OffsetDateTime birthday = null;
                    Integer age = null;
                    while (reader.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = reader.getFieldName();
                        reader.nextToken();

                        if ("fishtype".equals(fieldName)) {
                            String fishtype = reader.getString();
                            if (!FISHTYPE.equals(fishtype)) {
                                throw new IllegalStateException(
                                        "'fishtype' was expected to be non-null and equal to '"
                                                + FISHTYPE
                                                + "'. The found 'fishtype' was '"
                                                + fishtype
                                                + "'.");
                            }
                        } else if ("length".equals(fieldName)) {
                            length = reader.getFloat();
                            lengthFound = true;
                        } else if ("species".equals(fieldName)) {
                            species = reader.getString();
                        } else if ("siblings".equals(fieldName)) {
                            siblings = reader.readArray(reader1 -> Fish.fromJson(reader1));
                        } else if ("birthday".equals(fieldName)) {
                            birthday =
                                    reader.getNullable(
                                            nonNullReader -> OffsetDateTime.parse(nonNullReader.getString()));
                            birthdayFound = true;
                        } else if ("age".equals(fieldName)) {
                            age = reader.getNullable(JsonReader::getInt);
                        } else {
                            reader.skipChildren();
                        }
                    }
                    if (lengthFound && birthdayFound) {
                        Shark deserializedValue = new Shark(length, birthday);
                        deserializedValue.setSpecies(species);
                        deserializedValue.setSiblings(siblings);
                        deserializedValue.age = age;

                        return deserializedValue;
                    }
                    List<String> missingProperties = new ArrayList<>();
                    if (!lengthFound) {
                        missingProperties.add("length");
                    }
                    if (!birthdayFound) {
                        missingProperties.add("birthday");
                    }

                    throw new IllegalStateException(
                            "Missing required property/properties: " + String.join(", ", missingProperties));
                });
    }
}
