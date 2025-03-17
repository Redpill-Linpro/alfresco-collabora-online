package fr.jeci.collabora.alfresco;

import org.alfresco.repo.cache.SimpleCache;
import org.alfresco.service.cmr.repository.NodeRef;
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

    private SimpleCache<String, Boolean> collaboraMarkerCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(collaboraMarkerCache, "collaboraMarkerCache must be set");
    }

    public void setCollaboraMarkerCache(SimpleCache<String, Boolean> collaboraMarkerCache) {
        this.collaboraMarkerCache = collaboraMarkerCache;
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
            Boolean isLocked = collaboraMarkerCache.get(nodeRef.getId());
            result.put("locked", Boolean.toString(isLocked != null ? isLocked : Boolean.FALSE));
        } catch (Exception e) {
            // If checking the aspect fails, log the error and default to unlocked.
            result.put("locked", false);
        }

        return result;
    }
}
