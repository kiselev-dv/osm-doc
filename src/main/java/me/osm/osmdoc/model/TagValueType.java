//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.27 at 09:16:56 PM CEST 
//


package me.osm.osmdoc.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for null.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType>
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="node"/>
 *     &lt;enumeration value="area"/>
 *     &lt;enumeration value="way"/>
 *     &lt;enumeration value="relation"/>
 *     &lt;enumeration value="relation-member"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum TagValueType {

    @XmlEnumValue("node")
    NODE("node"),
    @XmlEnumValue("area")
    AREA("area"),
    @XmlEnumValue("way")
    WAY("way"),
    @XmlEnumValue("relation")
    RELATION("relation"),
    @XmlEnumValue("relation-member")
    RELATION_MEMBER("relation-member");
    private final String value;

    TagValueType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TagValueType fromValue(String v) {
        for (TagValueType c: TagValueType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
