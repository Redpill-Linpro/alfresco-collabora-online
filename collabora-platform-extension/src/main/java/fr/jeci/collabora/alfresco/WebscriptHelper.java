/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package fr.jeci.collabora.alfresco;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class WebscriptHelper {
	private WebscriptHelper() {
		// No Constructor
	}

	/**
	 * Get Mandatory parameters from Map
	 */
	public static String getParam(Map<String, String> templateArgs, String header) throws WebScriptException {
		String value = templateArgs.get(header);
		assertParam(header, value);
		return value;
	}

	/**
	 * Assert param is not null or empty
	 *
	 * @param header Need only for log
	 * @param param  value tested
	 */
	public static void assertParam(String header, String param) throws WebScriptException {
		if (StringUtils.isBlank(param)) {
			throw new WebScriptException(Status.STATUS_BAD_REQUEST, "No '" + header + "' parameter supplied");
		}
	}

	/**
	 * Get parameter as Integer (Not Mandatory)
	 *
	 * @return integer or null
	 */
	public static Integer integerValue(Map<String, String> templateArgs, String header) throws WebScriptException {
		return integerValue(templateArgs.get(header), header);
	}

	/**
	 * Get parameter as Integer (Not Mandatory)
	 */
	public static Integer integerValue(WebScriptRequest req, String header) throws WebScriptException {
		return integerValue(req.getParameter(header), header);
	}

	/**
	 * Get parameter as Integer (Not Mandatory)
	 *
	 * @param header Need only for log
	 */
	public static Integer integerValue(String strVal, String header) throws WebScriptException {
		if (strVal == null) {
			return null;
		}
		try {
			return Integer.parseInt(strVal);
		} catch (NumberFormatException e) {
			throw new WebScriptException(Status.STATUS_BAD_REQUEST,
					"Parameter '" + header + "' is not a number = " + strVal);
		}
	}

}
