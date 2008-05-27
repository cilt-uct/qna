/***********************************************************************************
 * AnswerLocator.java
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

import java.util.HashMap;
import java.util.Map;

import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.tool.utils.TextUtil;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class AnswerLocator implements EntityBeanLocator {
	
    public static final String NEW_PREFIX = "new ";
    public static String NEW_1 = NEW_PREFIX + "1";
	
	private ExternalLogic externalLogic;
	private AnswerLogic answerLogic;
	
	private TargettedMessageList messages;
	
	private Map<String, QnaAnswer> delivered = new HashMap<String,QnaAnswer>();
    
	public boolean remove(String beanname) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void set(String beanname, Object toset) {
		throw new UnsupportedOperationException("Not implemented");
		
	}

	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}
	
	public Object locateBean(String name) {
		QnaAnswer togo = delivered.get(name);
		if (togo == null) {
			 if (name.startsWith(NEW_PREFIX)) {
				 togo = answerLogic.createDefaultAnswer(externalLogic.getCurrentLocationId());
			 } else {
				 togo = answerLogic.getAnswerById(name);
			 }
			 delivered.put(name, togo);
		}
		return togo;
	}

    public String saveAll() {
        for (QnaAnswer answer : delivered.values()) {
        	answerLogic.saveAnswer(answer, externalLogic.getCurrentLocationId());
        	
        	if (answer.isPrivateReply()) {
		        messages.addMessage(new TargettedMessage("qna.reply-privately.save-success",
		                new Object[] { TextUtil.stripTags(answer.getAnswerText()) }, 
		                TargettedMessage.SEVERITY_INFO));
        		
        	} else {
		        messages.addMessage(new TargettedMessage("qna.add-answer.save-success",
		                new Object[] { TextUtil.stripTags(answer.getAnswerText()) }, 
		                TargettedMessage.SEVERITY_INFO));
        	}
        }
        return "saved";
    }
	
    public String approve() {
    	for (QnaAnswer answer : delivered.values()) {	
    		answerLogic.approveAnswer(answer.getId(), externalLogic.getCurrentLocationId());
    		messages.addMessage(new TargettedMessage("qna.view-question.answer-approved",null,TargettedMessage.SEVERITY_INFO));
    	}
    	return "marked-correct";
    }
    
    public String withdrawApproval() {
    	for (QnaAnswer answer : delivered.values()) {
    		answerLogic.withdrawApprovalAnswer(answer.getId(), externalLogic.getCurrentLocationId());
    		messages.addMessage(new TargettedMessage("qna.view-question.answer-approval-withdrawn",null,TargettedMessage.SEVERITY_INFO));
    	}
    	return "approval-withdrawn";
    }
    
    public String delete() {
    	for (QnaAnswer answer : delivered.values()) {
    		answerLogic.removeAnswer(answer.getId(), externalLogic.getCurrentLocationId());
    		messages.addMessage(new TargettedMessage("qna.delete-answer.delete-success",new String[] {TextUtil.stripTags(answer.getAnswerText())}, TargettedMessage.SEVERITY_INFO));
    	}
    	return "deleted";
    }
    
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	public void setAnswerLogic(AnswerLogic answerLogic) {
		this.answerLogic = answerLogic;
	}
	
	public Map getDeliveredBeans() {
		return delivered;
	}
}
