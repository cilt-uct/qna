/***********************************************************************************
 * DeleteMultiplesHelper.java
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

package org.sakaiproject.qna.tool.otp;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class DeleteMultiplesHelper extends SimpleViewParameters {
	public String[] questionids;
	public String[] categoryids;

	public String[] getQuestionids() {
		return questionids;
	}
	public void setQuestionids(String[] questionids) {
		this.questionids = questionids;
	}

	public String[] getCategoryids() {
		return categoryids;
	}
	public void setCategoryids(String[] categoryids) {
		this.categoryids = categoryids;
	}
}