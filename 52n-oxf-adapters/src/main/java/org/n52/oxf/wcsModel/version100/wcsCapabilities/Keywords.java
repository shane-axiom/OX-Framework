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


package org.n52.oxf.wcsModel.version100.wcsCapabilities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.n52.oxf.serviceAdapters.wcs.model.version100.gml.CodeType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "keyword",
    "type"
})
@XmlRootElement(name = "keywords", namespace = "http://www.opengis.net/wcs")
public class Keywords {

    @XmlElement(name = "keyword", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected List<String> keyword;
    @XmlElement(name = "type", namespace = "http://www.opengis.net/wcs", type = CodeType.class)
    protected CodeType type;

    protected List<String> _getKeyword() {
        if (keyword == null) {
            keyword = new ArrayList<String>();
        }
        return keyword;
    }

    /**
     * Gets the value of the keyword property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyword property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyword().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    public List<String> getKeyword() {
        return this._getKeyword();
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link org.n52.oxf.serviceAdapters.wcs.model.version100.gml.CodeType}
     */
    public CodeType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.n52.oxf.serviceAdapters.wcs.model.version100.gml.CodeType}
     */
    public void setType(CodeType value) {
        this.type = value;
    }

}
