/***********************************************************************************
 * AnswerEntityProviderImpl.java
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

package org.sakaiproject.qna.logic.impl.entity;

import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.entity.AnswerEntityProvider;
import org.sakaiproject.qna.model.QnaAnswer;

public class AnswerEntityProviderImpl implements AnswerEntityProvider, CoreEntityProvider, AutoRegisterEntityProvider {

	private AnswerLogic answerLogic;
	
	public void setAnswerLogic(AnswerLogic answerLogic) {
		this.answerLogic = answerLogic;
	}
	
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}

	public boolean entityExists(String id) {
		QnaAnswer answer = answerLogic.getAnswerById(id);
		if (answer == null) {
			return false;
		}
		
		return true;
	}
}
