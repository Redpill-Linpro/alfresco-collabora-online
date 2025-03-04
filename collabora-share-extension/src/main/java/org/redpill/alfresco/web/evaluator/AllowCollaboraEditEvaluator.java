package org.redpill.alfresco.web.evaluator;

import org.alfresco.web.evaluator.BaseEvaluator;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.extensions.surf.RequestContext;
import org.springframework.extensions.surf.ServletUtil;
import org.springframework.extensions.surf.exception.ConnectorServiceException;
import org.springframework.extensions.surf.support.ThreadLocalRequestContext;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.connector.Connector;
import org.springframework.extensions.webscripts.connector.Response;

import java.util.Map;

public class AllowCollaboraEditEvaluator extends BaseEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(AllowCollaboraEditEvaluator.class);
    private BaseEvaluator evaluator;

    public void setEvaluator(BaseEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public boolean evaluate(JSONObject jsonObject) {
        RequestContext context = ThreadLocalRequestContext.getRequestContext();
        Map<String, String> uriTokens = context.getUriTokens();
        String nodeRef = uriTokens.get("nodeRef");
        if (nodeRef == null) {
            nodeRef = context.getParameter("nodeRef");
        }
        if (nodeRef == null || nodeRef.trim().isEmpty()) {
            logger.error("No nodeRef parameter found in the request.");
            return false;
        }

        try {
            final Connector conn = context.getServiceRegistry()
                    .getConnectorService()
                    .getConnector("alfresco", context.getUserId(), ServletUtil.getSession());

            final Response response = conn.call("/lool/is-collabora-locked?nodeRef=" + nodeRef);
            if (response.getStatus().getCode() == Status.STATUS_OK) {
                // Parse the JSON response to determine if the node is locked
                JSONParser parser = new JSONParser();
                JSONObject jsonResponse = (JSONObject) parser.parse(response.getResponse());
                Object lockedObj = jsonResponse.get("locked");
                String locked = lockedObj == null ? "false" : lockedObj.toString();
                if(locked.equals("false") && evaluator.evaluate(jsonObject)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                logger.error("Unexpected response status: " + response.getStatus().getCode());
            }
        } catch (ConnectorServiceException cse) {
            logger.error("Connector service exception while checking collabora lock: ", cse);
        } catch (ParseException pe) {
            logger.error("Parse exception while parsing response from collabora lock check: ", pe);
        }
        return false;
    }
}

