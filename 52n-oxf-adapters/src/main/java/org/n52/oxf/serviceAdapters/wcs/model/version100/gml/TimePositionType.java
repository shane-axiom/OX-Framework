/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b11-EA 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.06.25 at 02:38:11 CEST 
//


package org.n52.oxf.serviceAdapters.wcs.model.version100.gml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "TimePositionType", namespace = "http://www.opengis.net/gml")
public class TimePositionType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "calendarEraName", namespace = "")
    protected String calendarEraName;
    @XmlAttribute(name = "frame", namespace = "")
    protected String frame;
    @XmlAttribute(name = "indeterminatePosition", namespace = "")
    protected TimeIndeterminateValueType indeterminatePosition;

    /**
     * Here we have collapsed the hierarchy of subtypes for temporal position in 19108
     * by defining a union of simple types for indicating temporal position relative to a
     * specific reference system.
     * Date and time may be indicated with varying degrees of precision:
     * year, year-month, date, or dateTime (all ISO 8601 format). Note
     * that the dateTime type does not allow right-truncation (i.e. omitting
     * seconds). An ordinal era may be referenced via URI, and a decimal value
     * can be used to indicate the distance from the scale origin (e.g. UNIX time,
     * GPS calendar).
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getValue() {
        return value;
    }

    /**
     * Here we have collapsed the hierarchy of subtypes for temporal position in 19108
     * by defining a union of simple types for indicating temporal position relative to a
     * specific reference system.
     * Date and time may be indicated with varying degrees of precision:
     * year, year-month, date, or dateTime (all ISO 8601 format). Note
     * that the dateTime type does not allow right-truncation (i.e. omitting
     * seconds). An ordinal era may be referenced via URI, and a decimal value
     * can be used to indicate the distance from the scale origin (e.g. UNIX time,
     * GPS calendar).
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the calendarEraName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getCalendarEraName() {
        return calendarEraName;
    }

    /**
     * Sets the value of the calendarEraName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setCalendarEraName(String value) {
        this.calendarEraName = value;
    }

    /**
     * Gets the value of the frame property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getFrame() {
        if (frame == null) {
            return "#ISO-8601";
        } else {
            return frame;
        }
    }

    /**
     * Sets the value of the frame property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setFrame(String value) {
        this.frame = value;
    }

    /**
     * Gets the value of the indeterminatePosition property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.serviceAdapters.wcs.model.version100.gml.TimeIndeterminateValueType}
     */
    public TimeIndeterminateValueType getIndeterminatePosition() {
        return indeterminatePosition;
    }

    /**
     * Sets the value of the indeterminatePosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.serviceAdapters.wcs.model.version100.gml.TimeIndeterminateValueType}
     */
    public void setIndeterminatePosition(TimeIndeterminateValueType value) {
        this.indeterminatePosition = value;
    }

}
