// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.streamstylexmlserialization.models;

import com.azure.core.annotation.Fluent;
import com.azure.xml.XmlReader;
import com.azure.xml.XmlSerializable;
import com.azure.xml.XmlWriter;
import javax.xml.stream.XMLStreamException;

/** Contans property. */
@Fluent
public final class ObjectWithXMsTextProperty implements XmlSerializable<ObjectWithXMsTextProperty> {
    /*
     * Returned value should be 'english'
     */
    private String language;

    /*
     * Returned value should be 'I am text'
     */
    private String content;

    /** Creates an instance of ObjectWithXMsTextProperty class. */
    public ObjectWithXMsTextProperty() {}

    /**
     * Get the language property: Returned value should be 'english'.
     *
     * @return the language value.
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Set the language property: Returned value should be 'english'.
     *
     * @param language the language value to set.
     * @return the ObjectWithXMsTextProperty object itself.
     */
    public ObjectWithXMsTextProperty setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Get the content property: Returned value should be 'I am text'.
     *
     * @return the content value.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Set the content property: Returned value should be 'I am text'.
     *
     * @param content the content value to set.
     * @return the ObjectWithXMsTextProperty object itself.
     */
    public ObjectWithXMsTextProperty setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Validates the instance.
     *
     * @throws IllegalArgumentException thrown if the instance is not valid.
     */
    public void validate() {}

    @Override
    public XmlWriter toXml(XmlWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("Data");
        xmlWriter.writeStringAttribute("language", this.language);
        xmlWriter.writeString(this.content);
        return xmlWriter.writeEndElement();
    }

    /**
     * Reads an instance of ObjectWithXMsTextProperty from the XmlReader.
     *
     * @param xmlReader The XmlReader being read.
     * @return An instance of ObjectWithXMsTextProperty if the XmlReader was pointing to an instance of it, or null if
     *     it was pointing to XML null.
     */
    public static ObjectWithXMsTextProperty fromXml(XmlReader xmlReader) throws XMLStreamException {
        return xmlReader.readObject(
                "Data",
                reader -> {
                    String language = reader.getStringAttribute(null, "language");
                    String content = reader.getStringElement();
                    ObjectWithXMsTextProperty deserializedValue = new ObjectWithXMsTextProperty();
                    deserializedValue.language = language;
                    deserializedValue.content = content;

                    return deserializedValue;
                });
    }
}
