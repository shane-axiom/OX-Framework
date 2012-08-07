package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class describes a set of mandatory parameters, which is necessary to call doDescribeSensor([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class DescribeSensorParamterBuilder_v100 {

	public static final String OUTPUT_FORMAT_SENSORML = "text/xml;subtype=\"sensorML/1.0.0\""; // defined output format for sensorml
	public static final String OUTPUT_FORMAT_TML = "text/xml;subtype=\"TML/1.0\""; // defined output format for tml
	
	private Map<String, String> parameters = new HashMap<String, String>(); // set of mandatory parameters
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorId
	 * @param outputFormat
	 */
	public DescribeSensorParamterBuilder_v100(String sensorId, String outputFormat) throws IllegalArgumentException {
		if (sensorId == null || outputFormat == null)
			throw new IllegalArgumentException("The parameters \"sensorId\" and \"outputFormat\" are mandatory. They cannot be left empty!");
		parameters.put(SOSWrapper.DESCRIBE_SENSOR_SENSOR_ID_PARAMETER, sensorId);
		parameters.put(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}
