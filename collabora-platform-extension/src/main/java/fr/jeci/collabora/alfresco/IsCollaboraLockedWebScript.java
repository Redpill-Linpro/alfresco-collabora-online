package fr.jeci.collabora.alfresco;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class IsCollaboraLockedWebScript extends DeclarativeWebScript implements InitializingBean {
    private static final Log logger = LogFactory.getLog(IsCollaboraLockedWebScript.class);

    private NodeService nodeService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(nodeService, "nodeService");
    }
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> result = new HashMap<>();
        String nodeRefParam = req.getParameter("nodeRef");
        logger.error("nodeRefParam="+nodeRefParam);
        // Check for missing or empty parameter
        if (nodeRefParam == null || nodeRefParam.trim().isEmpty()) {
            logger.error("test1");
            result.put("locked", false);
            return result;
        }

        NodeRef nodeRef;
        try {
            logger.error("test2");
            nodeRef = new NodeRef(nodeRefParam);
        } catch (Exception e) {
            // In case of an invalid nodeRef string, log the error and return a safe default.
            logger.error("Invalid nodeRef parameter: " + nodeRefParam, e);
            result.put("locked", false);
            return result;
        }

        try {
            // TODO REMOVE THIS NEW ASPECT THAT WE DONT NEED
            logger.error("test3");
            boolean isLocked = nodeService.hasAspect(nodeRef, CollaboraOnlineModel.ASPECT_COLLABORA_ONLINE);
            logger.error("Locked: " + isLocked);
            result.put("locked", Boolean.toString(isLocked));
        } catch (Exception e) {
            // If checking the aspect fails, log the error and default to unlocked.
            logger.error("Error checking aspect for nodeRef: " + nodeRef, e);
            result.put("locked", false);
        }

        return result;
    }
}
