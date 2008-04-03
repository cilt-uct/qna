package org.sakaiproject.qna.logic.impl;

import org.sakaiproject.event.api.Event;
import org.sakaiproject.qna.logic.ExternalEventLogic;
import org.sakaiproject.qna.logic.ExternalLogic;

public class ExternalEventLogicImpl implements ExternalEventLogic {

    private org.sakaiproject.event.api.EventTrackingService eventTrackingService;
    private ExternalLogic externalLogic;
    
    public void setEventTrackingService(org.sakaiproject.event.api.EventTrackingService eventTrackingService) {
    	this.eventTrackingService = eventTrackingService;
    }
    
    public void setExternalLogic(ExternalLogic externalLogic) {
    	this.externalLogic = externalLogic;
    }
    
    
	public void postEvent(String message, String objectId) {
		if (objectId != null && !"".equals(objectId)) {
			String referenceObject = externalLogic.getCurrentLocationId() + "/" + objectId;
		
			Event event = eventTrackingService.newEvent(message, referenceObject, true);
			eventTrackingService.post(event);
		}
	}
}
