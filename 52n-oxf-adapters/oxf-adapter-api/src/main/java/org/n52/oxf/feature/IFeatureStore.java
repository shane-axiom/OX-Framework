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

package org.n52.oxf.feature;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public interface IFeatureStore {

    /**
     * This method unmarshals the specified dataToUnmarshal to the feature model of the framework.
     * 
     * @param dataToUnmarshal
     *        an OperationResult containing an InputStream consisting of feature data.
     * @return an OXFFeatureCollection containing all the OXFFeatures are contained in the OperationResult.
     * 
     * @throws OXFException
     * 
     * @deprecated Use {@link IFeatureStore#unmarshalFeatures()}
     */
    @Deprecated
    public OXFFeatureCollection unmarshalFeatures(OperationResult dataToUnmarshal) throws OXFException;
    
    /**
     * This method unmarshals the features in this store to the feature model of the framework.
     * 
     * @return An OXFFeatureCollection containing all the OXFFeatures are contained {@link IFeatureStore} instance.
     * 
     * @throws OXFException
     */
    public OXFFeatureCollection unmarshalFeatures() throws OXFException;
}