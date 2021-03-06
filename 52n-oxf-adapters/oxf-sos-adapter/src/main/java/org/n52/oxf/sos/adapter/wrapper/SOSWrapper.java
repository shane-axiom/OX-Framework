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
package org.n52.oxf.sos.adapter.wrapper;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.sos.adapter.SOSAdapter.*;

import java.util.Map;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.wrapper.builder.GetFeatureOfInterestParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationByIdParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationParameterBuilder_v100;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.n52.oxf.swes.request.DescribeSensorParameters;

/**
 * SOSWrapper wraps all SOS operations implemented in SOSAdapter class.
 * 
 * @author Eric
 * @see SOSAdapter
 */
public class SOSWrapper {
    
    // XXX SOSWrapper is not capable of intercepting custom IRequestBuilder implementations yet!

	// name of the service
	private static final String SERVICE_TYPE = "SOS";
	
	// GetCapabilities specific description of the service
	private final ServiceDescriptor serviceDescriptor; 
	
	// Binding to use (SOS 2.0 specific)
	private final Binding binding;
	
    protected SOSWrapper(final ServiceDescriptor serviceDescriptor, final Binding binding) {
		this.serviceDescriptor = serviceDescriptor;
		this.binding = binding;
	}
	
	/**
     * Performs a DescribeSensor request.
     * 
     * @param parameters parameter assembler
     * @return Request result
     * @throws OXFException
     * @throws ExceptionReport
     */
    public OperationResult doDescribeSensor(final DescribeSensorParameters parameters) throws OXFException, ExceptionReport {
        final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
        if (checkOperationAvailability(DESCRIBE_SENSOR)) {
            final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(DESCRIBE_SENSOR);    
            final ParameterContainer parameterContainer = createParameterContainerForDoDescribeSensor(parameters);
            return adapter.doOperation(operation, parameterContainer);
        } else {
            throw new OXFException("Operation: \"" + DESCRIBE_SENSOR + "\" not supported by the SOS!");
        }
    }
	
    private ParameterContainer createParameterContainerWithCommonServiceParameters() throws OXFException {
        final ParameterContainer parameterContainer = new ParameterContainer();
		parameterContainer.addParameterShell(SERVICE, SERVICE_TYPE);
		parameterContainer.addParameterShell(VERSION, serviceDescriptor.getVersion());
		if (binding != null) {
			parameterContainer.addParameterShell(BINDING, binding.name());
		}
        return parameterContainer;
    }
    
	private ParameterContainer createParameterContainerForDoDescribeSensor(final DescribeSensorParameters parameters) throws OXFException, ExceptionReport {
		final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		final String procedure = parameters.getSingleValue(DESCRIBE_SENSOR_PROCEDURE_PARAMETER);
        parameterContainer.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, procedure);
		final String outputFormat = parameters.getSingleValue(DESCRIBE_SENSOR_OUTPUT_FORMAT);
        parameterContainer.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
		return parameterContainer;
	}
	
	/**
	 * Requests observation(s).
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetObservation(final GetObservationParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(GET_OBSERVATION)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(GET_OBSERVATION);
			final ParameterContainer parameterContainer = createParameterContainerForGetOservation(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + GET_OBSERVATION + "\" not supported by the SOS!");
		}
	}
	
	private ParameterContainer createParameterContainerForGetOservation(final Map <String, Object> parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		// mandatory parameters from builder
		parameterContainer.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, (String) parameters.get(GET_OBSERVATION_OFFERING_PARAMETER));
		parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
		parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
		// optional parameters from builder
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER) != null) {
			parameterContainer.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER, (String) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
			parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
			parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, (String) parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_RESULT_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_MODEL_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER));
		}
		return parameterContainer;
	}
	
	/**
     * Requests the registration of a sensor.
     * 
     * @param parameters parameter assembler
     * @return Request result
     * @throws OXFException
     * @throws ExceptionReport
     */
    public OperationResult doRegisterSensor(final RegisterSensorParameters parameters) throws OXFException, ExceptionReport {
        // wrapped SOSAdapter instance
        final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
        if (checkOperationAvailability(REGISTER_SENSOR)) {
            final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(REGISTER_SENSOR);
            final ParameterContainer parameterContainer = createParameterContainerForRegisterSensor(parameters);
            return adapter.doOperation(operation, parameterContainer);
        } else {
            throw new OXFException("Operation: \"" + REGISTER_SENSOR + "\" not supported by the SOS!");
        }
    }
    
    /**
	 * @throws OXFException 
     * @throws ExceptionReport 
     * @see {@link #doRegisterSensor(RegisterSensorParameters)}
	 */
	public OperationResult doInsertSensor(final InsertSensorParameters insertSensorParameters) throws OXFException, ExceptionReport
	{
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		if (checkOperationAvailability(INSERT_SENSOR)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(INSERT_SENSOR);
			final ParameterContainer parameterContainer = getParameterContainer(insertSensorParameters);
			return adapter.doOperation(operation, parameterContainer);
		}
		else {
			throw new OXFException("Operation: '" + INSERT_SENSOR + "' not supported by the SOS instance!");
		}
	}
	
	private ParameterContainer getParameterContainer(final InsertSensorParameters insertSensorParameters) throws OXFException
	{
		final ParameterContainer paramContainer = createParameterContainerWithCommonServiceParameters();
		paramContainer.addParameterShell(
				REGISTER_SENSOR_ML_DOC_PARAMETER,
				insertSensorParameters.getSingleValue(InsertSensorParameters.PROCEDURE_DESCRIPTION));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER,
				insertSensorParameters.getSingleValue(InsertSensorParameters.PROCEDURE_DESCRIPTION_FORMAT));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVABLE_PROPERTIES)
				.toArray(new String[insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVABLE_PROPERTIES).size()]));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_OBSERVATION_TYPE,
				insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVATION_TYPES)
				.toArray(new String[insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVATION_TYPES).size()]));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_FEATURE_TYPE_PARAMETER,
				insertSensorParameters.getAllValues(InsertSensorParameters.FEATURE_OF_INTEREST_TYPES)
				.toArray(new String[insertSensorParameters.getAllValues(InsertSensorParameters.FEATURE_OF_INTEREST_TYPES).size()]));
		return paramContainer;
	}

	/**
	 * 
	 * @param sensorId
	 * @throws ExceptionReport 
	 */
	public OperationResult doDeleteSensor(final String sensorId) throws OXFException, ExceptionReport
	{
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		if (checkOperationAvailability(DELETE_SENSOR)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(DELETE_SENSOR);
			final ParameterContainer pc = new ParameterContainer();
			pc.addParameterShell(ISOSRequestBuilder.SERVICE, "SOS");
			pc.addParameterShell(ISOSRequestBuilder.VERSION, serviceDescriptor.getVersion());
			pc.addParameterShell(ISOSRequestBuilder.DELETE_SENSOR_PROCEDURE, sensorId);
			return adapter.doOperation(operation, pc);
		}else {
			throw new OXFException("Operation: '" + DELETE_SENSOR + "' not supported by the SOS instance!");
		}
	}
	
	private boolean checkOperationAvailability(final String operationName)
	{
		return serviceDescriptor.getOperationsMetadata().getOperationByName(operationName) != null;
	}

	private ParameterContainer createParameterContainerForRegisterSensor(final RegisterSensorParameters parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		parameterContainer.addParameterShell(
				REGISTER_SENSOR_ML_DOC_PARAMETER,
				parameters.getSingleValue(REGISTER_SENSOR_ML_DOC_PARAMETER));
		parameterContainer.addParameterShell(
				REGISTER_SENSOR_OBSERVATION_TEMPLATE,
				parameters.getSingleValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE));
        if (parameters.contains(REGISTER_SENSOR_DEFAULT_RESULT_VALUE)) {
            final String defaultResult = parameters.getSingleValue(REGISTER_SENSOR_DEFAULT_RESULT_VALUE);
			parameterContainer.addParameterShell(REGISTER_SENSOR_DEFAULT_RESULT_VALUE, defaultResult);
		}
		return parameterContainer;
	}

	/**
	 * Requests the insertion of an observation.
	 * 
	 * @param parameters parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doInsertObservation(final InsertObservationParameters parameters) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(INSERT_OBSERVATION)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(INSERT_OBSERVATION);
			final ParameterContainer parameterContainer = createParameterContainerForInsertObservation(parameters);
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + INSERT_OBSERVATION + "\" not supported by the SOS!");
		}
	}

	private ParameterContainer createParameterContainerForInsertObservation(final InsertObservationParameters parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		if (parameters instanceof org.n52.oxf.sos.request.v100.InsertObservationParameters) {
			addSos100Values((org.n52.oxf.sos.request.v100.InsertObservationParameters) parameters, parameterContainer);
		}
		else if (parameters instanceof org.n52.oxf.sos.request.v200.InsertObservationParameters) {
			addSos200Values((org.n52.oxf.sos.request.v200.InsertObservationParameters) parameters, parameterContainer);
		}
		else {
			throw new OXFException(String.format("Subtype of %s: %s not supported by this implemenation.",InsertObservationParameters.class.getName(),parameters.getClass().getName()));
		}
		return parameterContainer;
	}

	private void addSos200Values(final org.n52.oxf.sos.request.v200.InsertObservationParameters parameters,
			final ParameterContainer parameterContainer) throws OXFException
	{
		parameterContainer.addParameterShell(INSERT_OBSERVATION_TYPE, parameters.getSingleValue(INSERT_OBSERVATION_TYPE));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_PROCEDURE_PARAMETER));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
		if(parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_NAME, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_NAME));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_DESC, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_DESC));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_POSITION));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION_SRS, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_POSITION_SRS));
		} 
		else {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_FOI_ID_PARAMETER));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_POSITION_SRS) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_POSITION_SRS, parameters.getSingleValue(INSERT_OBSERVATION_POSITION_SRS));
		}
		parameterContainer.addParameterShell(INSERT_OBSERVATION_RESULT_TIME, parameters.getSingleValue(INSERT_OBSERVATION_RESULT_TIME));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_PHENOMENON_TIME, parameters.getSingleValue(INSERT_OBSERVATION_PHENOMENON_TIME));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER, parameters.getAllValues(INSERT_OBSERVATION_OFFERINGS_PARAMETER).toArray(new String[parameters.getAllValues(INSERT_OBSERVATION_OFFERINGS_PARAMETER).size()]));
		if (parameters.getSingleValue(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, parameters.getSingleValue(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, parameters.getSingleValue(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_VALUE_PARAMETER));
		}
	}

	private void addSos100Values(final org.n52.oxf.sos.request.v100.InsertObservationParameters parameters,
			final ParameterContainer parameterContainer) throws OXFException
	{
		// mandatory parameters
		parameterContainer.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_PROCEDURE_PARAMETER));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_TYPE, parameters.getSingleValue(INSERT_OBSERVATION_TYPE));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_SAMPLING_TIME, parameters.getSingleValue(INSERT_OBSERVATION_SAMPLING_TIME));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
		parameterContainer.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_FOI_ID_PARAMETER));
		// optional parameters
		if (parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_NAME) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_NAME, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_NAME));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_DESC) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_DESC, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_DESC));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_POSITION) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION, parameters.getSingleValue(INSERT_OBSERVATION_NEW_FOI_POSITION));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_POSITION_SRS) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_POSITION_SRS, parameters.getSingleValue(INSERT_OBSERVATION_POSITION_SRS));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, parameters.getSingleValue(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, parameters.getSingleValue(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE));
		}
		if (parameters.getSingleValue(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, parameters.getSingleValue(INSERT_OBSERVATION_VALUE_PARAMETER));
		}
	}

	/**
	 * Requests an observation by its id.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetObservationById(final GetObservationByIdParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(GET_OBSERVATION_BY_ID)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(GET_OBSERVATION_BY_ID);
			final ParameterContainer parameterContainer = createParameterContainerForGetObservationById(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + GET_OBSERVATION_BY_ID + "\" not supported by the SOS!");
		}
	}

	private ParameterContainer createParameterContainerForGetObservationById(final Map<String, String> parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		// mandatory parameters from builder
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER));
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER));
		// optional parameters from builder
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER) != null) {
			parameterContainer.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER, parameters.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER));
		}
		return parameterContainer;
	}
	
	/**
	 * Requests a feature of interest.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetFeatureOfInterest(final GetFeatureOfInterestParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(GET_FEATURE_OF_INTEREST)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(GET_FEATURE_OF_INTEREST);
			final ParameterContainer parameterContainer = createParameterContainerForGetFeatureOfInterest(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + GET_OBSERVATION + "\" not supported by the SOS!");
		}
	}
	
	private ParameterContainer createParameterContainerForGetFeatureOfInterest(final Map<String, String> parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		// mandatory parameters from builder
		if (parameters.get(GET_FOI_ID_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_FOI_ID_PARAMETER, parameters.get(GET_FOI_ID_PARAMETER));
		} else {
			parameterContainer.addParameterShell(GET_FOI_LOCATION_PARAMETER, parameters.get(GET_FOI_LOCATION_PARAMETER));
		}
		// optional parameters from builder
		if (parameters.get(GET_FOI_EVENT_TIME_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_FOI_EVENT_TIME_PARAMETER, parameters.get(GET_FOI_EVENT_TIME_PARAMETER));
		}
		return parameterContainer;
	}

    /**
     * Return the GetCapabilities specific description of the service.
     * 
     * @return service description  
     */
    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

}
