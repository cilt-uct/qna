package org.sakaiproject.qna.logic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.entitybroker.EntityBroker;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

public class ExternalLogicImpl implements ExternalLogic {
	
	private static Log log = LogFactory.getLog(ExternalLogicImpl.class);
	
    private FunctionManager functionManager;
    public void setFunctionManager(FunctionManager functionManager) {
        this.functionManager = functionManager;
    }
    
    private SessionManager sessionManager;
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

	private ToolManager toolManager;
	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}
    
	private SecurityService securityService;
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	private SiteService siteService;
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
    
    private UserDirectoryService userDirectoryService;
    public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
        this.userDirectoryService = userDirectoryService;
    }

    private EntityBroker entityBroker;
    public void setEntityBroker(EntityBroker entityBroker) {
        this.entityBroker = entityBroker;
    }
    
    public void init() {
    	log.debug("init");
        // register Sakai permissions for this tool
        functionManager.registerFunction(QNA_READ);
        functionManager.registerFunction(QNA_NEW_QUESTION);
        functionManager.registerFunction(QNA_NEW_ANSWER);
        functionManager.registerFunction(QNA_NEW_CATEGORY);
        functionManager.registerFunction(QNA_UPDATE);
    }
    
	public String getCurrentLocationId() {
		try {
			
			if (toolManager.getCurrentPlacement() == null)
			{
				return NO_LOCATION;
			}

			Site s = siteService.getSite(toolManager.getCurrentPlacement().getContext());
			return s.getReference();
		} catch (IdUnusedException e) {
			return NO_LOCATION;
		}
	}

	public String getLocationTitle(String locationId) {
        try {
			Site s = siteService.getSite(toolManager.getCurrentPlacement().getContext());
			return s.getTitle();
        } catch (Exception e) {
            // invalid site reference
            log.debug("Invalid site reference:" + locationId);
            return "----------";
        }
	}
    
	public String getCurrentUserId() {
		return sessionManager.getCurrentSessionUserId();
	}

	public String getUserDisplayName(String userId) {
		try {
			return userDirectoryService.getUser(userId).getDisplayName();
		} catch (UserNotDefinedException e) {
			log.warn("Cannot get user displayname for id: " + userId);
			return "--------";
		}
	}
	
	public boolean isUserAdmin(String userId) {
		return securityService.isSuperUser(userId);
	}

	public boolean isUserAllowedInLocation(String userId, String permission, String locationId) {
		if ( securityService.unlock(userId, permission, locationId) ) {
			return true;
		}
		return false;
	}

}
