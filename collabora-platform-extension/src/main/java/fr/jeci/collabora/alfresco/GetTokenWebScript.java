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

import org.alfresco.service.cmr.repository.NodeRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.webscripts.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetTokenWebScript extends DeclarativeWebScript {
	private static final Logger logger = LoggerFactory.getLogger(GetTokenWebScript.class);

	private static final String WOPI_SRC_URL = "wopi_src_url";
	private static final String ACCESS_TOKEN_TTL = "access_token_ttl";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String PARAM_ACTION = "action";
	private static final String PARAM_NODE_REF = "nodeRef";

	protected CollaboraOnlineService collaboraOnlineService;

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		final Map<String, Object> model = new HashMap<>();
		final String nodeRefStr = req.getParameter(PARAM_NODE_REF);
		if (nodeRefStr == null) {
			throw new WebScriptException("No 'nodeRef' parameter supplied");
		}

		final NodeRef nodeRef = new NodeRef(nodeRefStr);
		final String action = req.getParameter(PARAM_ACTION);
		if (action == null) {
			throw new WebScriptException("No 'action' parameter supplied");
		}

		final WOPIAccessTokenInfo tokenInfo = this.collaboraOnlineService.createAccessToken(nodeRef);
		model.put(ACCESS_TOKEN, tokenInfo.getAccessToken());
		model.put(ACCESS_TOKEN_TTL, tokenInfo.getExpiresAt().toDate().getTime());

		try {
			String wopiSrcUrl = this.collaboraOnlineService.getWopiSrcURL(nodeRef, action);
			logger.debug("Get Token {} for wopiSrcUrl {}", action, wopiSrcUrl);
			model.put(WOPI_SRC_URL, wopiSrcUrl);
		} catch (IOException e) {
			status.setCode(Status.STATUS_INTERNAL_SERVER_ERROR, "Failed to get wopiSrcURL");
		}
		return model;
	}

	public void setCollaboraOnlineService(CollaboraOnlineService collaboraOnlineService) {
		this.collaboraOnlineService = collaboraOnlineService;
	}

}