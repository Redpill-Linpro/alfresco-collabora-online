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
        // Check for missing or empty parameter
        if (nodeRefParam == null || nodeRefParam.trim().isEmpty()) {
            result.put("locked", false);
            return result;
        }

        NodeRef nodeRef;
        try {
            nodeRef = new NodeRef(nodeRefParam);
        } catch (Exception e) {
            // In case of an invalid nodeRef string, log the error and return a safe default.
            result.put("locked", false);
            return result;
        }

        try {
            boolean isLocked = nodeService.hasAspect(nodeRef, CollaboraOnlineModel.ASPECT_COLLABORA_LOCK);
            result.put("locked", Boolean.toString(isLocked));
        } catch (Exception e) {
            // If checking the aspect fails, log the error and default to unlocked.
            result.put("locked", false);
        }

        return result;
    }
}
