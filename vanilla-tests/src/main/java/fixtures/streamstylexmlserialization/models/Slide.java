// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package fixtures.streamstylexmlserialization.models;

import com.azure.core.annotation.Fluent;
import com.azure.xml.XmlReader;
import com.azure.xml.XmlSerializable;
import com.azure.xml.XmlToken;
import com.azure.xml.XmlWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

/** A slide in a slideshow. */
@Fluent
public final class Slide implements XmlSerializable<Slide> {
    /*
     * The type property.
     */
    private String type;

    /*
     * The title property.
     */
    private String title;

    /*
     * The items property.
     */
    private List<String> items = new ArrayList<>();

    /** Creates an instance of Slide class. */
    public Slide() {}

    /**
     * Get the type property: The type property.
     *
     * @return the type value.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set the type property: The type property.
     *
     * @param type the type value to set.
     * @return the Slide object itself.
     */
    public Slide setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get the title property: The title property.
     *
     * @return the title value.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Set the title property: The title property.
     *
     * @param title the title value to set.
     * @return the Slide object itself.
     */
    public Slide setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get the items property: The items property.
     *
     * @return the items value.
     */
    public List<String> getItems() {
        return this.items;
    }

    /**
     * Set the items property: The items property.
     *
     * @param items the items value to set.
     * @return the Slide object itself.
     */
    public Slide setItems(List<String> items) {
        this.items = items;
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
        xmlWriter.writeStartElement("slide");
        xmlWriter.writeStringAttribute("type", this.type);
        xmlWriter.writeStringElement("title", this.title);
        if (this.items != null) {
            for (String element : this.items) {
                xmlWriter.writeStringElement("items", element);
            }
        }
        return xmlWriter.writeEndElement();
    }

    /**
     * Reads an instance of Slide from the XmlReader.
     *
     * @param xmlReader The XmlReader being read.
     * @return An instance of Slide if the XmlReader was pointing to an instance of it, or null if it was pointing to
     *     XML null.
     */
    public static Slide fromXml(XmlReader xmlReader) throws XMLStreamException {
        return xmlReader.readObject(
                "slide",
                reader -> {
                    String type = reader.getStringAttribute(null, "type");
                    String title = null;
                    List<String> items = null;
                    while (reader.nextElement() != XmlToken.END_ELEMENT) {
                        QName fieldName = reader.getElementName();

                        if ("title".equals(fieldName.getLocalPart())) {
                            title = reader.getStringElement();
                        } else if ("items".equals(fieldName.getLocalPart())) {
                            if (items == null) {
                                items = new LinkedList<>();
                            }
                            items.add(reader.getStringElement());
                        } else {
                            reader.skipElement();
                        }
                    }
                    Slide deserializedValue = new Slide();
                    deserializedValue.type = type;
                    deserializedValue.title = title;
                    deserializedValue.items = items;

                    return deserializedValue;
                });
    }
}
