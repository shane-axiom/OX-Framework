/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.xmlbeans.parser;

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlValidationError;
import org.n52.oxf.xml.XMLConstants;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class SFSpatialSamplingFeatureCase extends AbstractLaxValidationCase {
	
	private static SFSpatialSamplingFeatureCase instance;

	@Override
	public boolean shouldPass(final XmlValidationError xve)
	{
		final QName offending = xve.getOffendingQName();
		final List<?> expected = xve.getExpectedQNames();
		final QName field = xve.getFieldQName();
		if (offending != null && 
				offending.equals(XMLConstants.QN_SF_2_0_SPATIAL_SAMPLING_FEATURE) &&
				field.equals(XMLConstants.QN_OM_2_0_FEATURE_OF_INTEREST) &&
				expected.contains(XMLConstants.QN_GML_3_2_ABSTRACT_FEATURE))
		{
			return validateSubstitutionGroup(xve);
		}
		return false;
	}

	private boolean validateSubstitutionGroup(final XmlValidationError xve)
	{
		try {
			final SFSpatialSamplingFeatureType featureDocument = SFSpatialSamplingFeatureType.Factory.parse(xve.getObjectLocation().xmlText());
			final Collection<XmlError> revalidation = XMLBeansParser.validate(featureDocument);
			return revalidation.size()==0?true:false;
		} catch (final XmlException e) {}
		return false;
	}

	public static SFSpatialSamplingFeatureCase getInstance()
	{
		if (instance == null) {
			instance = new SFSpatialSamplingFeatureCase();
		}
		return instance;
	}

	private SFSpatialSamplingFeatureCase() {}
}
