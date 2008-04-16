package org.sakaiproject.qna.tool.comparators;

import java.util.Comparator;

import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.PermissionLogic;
import org.sakaiproject.qna.model.QnaAnswer;

/**
 *	Used to compare Answers for question list 
 *
 */
public class AnswersListComparator implements Comparator<QnaAnswer> {

	private PermissionLogic permissionLogic;
	private ExternalLogic externalLogic;
		
	public AnswersListComparator(PermissionLogic permissionLogic, ExternalLogic externalLogic) {
		this.permissionLogic = permissionLogic;
		this.externalLogic = externalLogic;
	}
	
	public int compare(QnaAnswer a1, QnaAnswer a2) {
		if (a1.isApproved() && a2.isApproved()) {
			String currentLocation = externalLogic.getCurrentLocationId();
			if (permissionLogic.canUpdate(currentLocation, a1.getOwnerId()) && permissionLogic.canUpdate(currentLocation, a2.getOwnerId())) {
				return a1.getDateCreated().compareTo(a2.getDateCreated());
			} else if (permissionLogic.canUpdate(currentLocation, a1.getOwnerId()) && !permissionLogic.canUpdate(currentLocation, a2.getOwnerId())) {
				return -1;
			} else if (!permissionLogic.canUpdate(currentLocation, a1.getOwnerId()) && permissionLogic.canUpdate(currentLocation, a2.getOwnerId())) {
				return 1;
			} else {
				return a1.getDateCreated().compareTo(a2.getDateCreated());
			}
		} else if (a1.isApproved() && !a2.isApproved()) {
			return -1;
		} else if (!a1.isApproved() && a2.isApproved()) {
			return 1;
		} else {
			return a1.getDateCreated().compareTo(a2.getDateCreated());
		}
	}

}
