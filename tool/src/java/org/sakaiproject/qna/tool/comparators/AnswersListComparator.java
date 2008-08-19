/***********************************************************************************
 * AnswersListComparator.java
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

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
	
	/**
	 * List:
	 * 	- Answers added by lecturer (user with update rights) chronologically
	 *  - Answers approved chronologically
	 *  - Answers not approved chronologically
	 *  
	 *  @see Comparator#compare(Object, Object)
	 */
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
