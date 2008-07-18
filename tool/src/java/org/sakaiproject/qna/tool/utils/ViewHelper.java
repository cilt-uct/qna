package org.sakaiproject.qna.tool.utils;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.model.constants.QnaConstants;
import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

public class ViewHelper {
    
	public static final String VIEW_TYPE_ATTR = "view-type-attr";  
	public static final String SORT_BY_ATTR =  "sort-by-attr";
	public static final String SORT_DIR_ATTR= "sort-dir-attr";
	
	private OptionsLogic optionsLogic;
    private SessionManager sessionManager;
	private ExternalLogic externalLogic;
	private PermissionLogic permissionLogic;
    
    public void setSessionManager(SessionManager sessionManager) {
    	  this.sessionManager = sessionManager;
      }
    
    public void setOptionsLogic(OptionsLogic optionsLogic) {
    	this.optionsLogic = optionsLogic;
    }
    
    public void setExternalLogic(ExternalLogic externalLogic) {
    	this.externalLogic = externalLogic;
    }

    public void setPermissionLogic(PermissionLogic permissionLogic) {
    	this.permissionLogic = permissionLogic;
    }
    
    public String getViewType() {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
    	if (toolSession.getAttribute(VIEW_TYPE_ATTR) != null) {
    		return (String)toolSession.getAttribute(VIEW_TYPE_ATTR);
    	} else {
    		String defaultView = optionsLogic.getOptionsForLocation(externalLogic.getCurrentLocationId()).getDefaultStudentView();
    		String returnView = ViewTypeConstants.CATEGORIES;
    		
    		if (defaultView.equals(QnaConstants.CATEGORY_VIEW)) {
				returnView = ViewTypeConstants.CATEGORIES;
			} else if (defaultView.equals(QnaConstants.MOST_POPULAR_VIEW)){
				if (permissionLogic.canUpdate(externalLogic.getCurrentLocationId(), externalLogic.getCurrentUserId())) {
					returnView = ViewTypeConstants.ALL_DETAILS; // Admin users see all details
				} else {
					returnView = ViewTypeConstants.STANDARD;
				}
			}
    		toolSession.setAttribute(VIEW_TYPE_ATTR, returnView);
    		return returnView;
    	}
    }
    
    public String getSortBy() {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		if (toolSession.getAttribute(SORT_BY_ATTR) != null) {
			return (String)toolSession.getAttribute(SORT_BY_ATTR);
		} else {
    		String defaultView = optionsLogic.getOptionsForLocation(externalLogic.getCurrentLocationId()).getDefaultStudentView();
    		if (defaultView.equals(QnaConstants.CATEGORY_VIEW)) {
    			return null;
    		} else {
    			toolSession.setAttribute(SORT_BY_ATTR, SortByConstants.VIEWS);
    			return SortByConstants.VIEWS;
    		}
		}
    }
    
    public String getSortDir() {
    	ToolSession toolSession = sessionManager.getCurrentToolSession();
		
    	if (getViewType().equals(ViewTypeConstants.CATEGORIES)) {
    		return SortByConstants.SORT_DIR_ASC;
    	}
    	
    	if (toolSession.getAttribute(SORT_DIR_ATTR) != null) {
			return (String)toolSession.getAttribute(SORT_DIR_ATTR);
		} else {
			toolSession.setAttribute(SORT_DIR_ATTR, SortByConstants.SORT_DIR_ASC);
			return  SortByConstants.SORT_DIR_ASC;
		}
    }
    
    public void setupSession(String viewType, String sortBy, String sortDir) {
		ToolSession toolSession = sessionManager.getCurrentToolSession();
		if (viewType != null) {
			toolSession.setAttribute(VIEW_TYPE_ATTR, viewType);
		}

		if (sortBy != null) {
			toolSession.setAttribute(SORT_BY_ATTR, sortBy);
		}

		if (sortDir != null) {
			toolSession.setAttribute(SORT_DIR_ATTR, sortDir);
		}
    }
}
