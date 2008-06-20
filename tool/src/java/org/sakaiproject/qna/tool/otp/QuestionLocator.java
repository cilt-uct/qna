/***********************************************************************************
 * QuestionLocator.java
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
import java.util.List;
import java.util.Map;

import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.qna.logic.AttachmentLogic;
import org.sakaiproject.qna.logic.ExternalLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.logic.exceptions.AttachmentException;
import org.sakaiproject.qna.model.QnaAttachment;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.utils.TextUtil;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

import uk.org.ponder.beanutil.entity.EntityBeanLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;

public class QuestionLocator implements EntityBeanLocator  {
    public static final String NEW_PREFIX = "new ";
    public static String NEW_1 = NEW_PREFIX + "1";

    private QuestionLogic questionLogic;
    private ExternalLogic externalLogic;
    private AttachmentLogic attachmentLogic;

	private Map<String, QnaQuestion> delivered = new HashMap<String,QnaQuestion>();

	private TargettedMessageList messages;

	private SessionManager sessionManager;
	
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	public void setMessages(TargettedMessageList messages) {
		this.messages = messages;
	}

	public boolean remove(String beanname) {
		try {
			QnaQuestion question = questionLogic.getQuestionById(beanname);
			String questionText = question.getQuestionText();

			questionLogic.removeQuestion(beanname, externalLogic.getCurrentLocationId());

			messages.addMessage(
				new TargettedMessage("qna.delete-question.delete-successful",
				new Object[] { TextUtil.stripTags(questionText) },
				TargettedMessage.SEVERITY_INFO)
			);
			return true;
		} catch (AttachmentException ae) {
			messages.addMessage(new TargettedMessage("qna.delete-question.attachment-error", null, TargettedMessage.SEVERITY_ERROR));
			return false;
		}
	}

	public void set(String beanname, Object toset) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Object locateBean(String name) {
		QnaQuestion togo = delivered.get(name);
		if (togo == null) {
			 if (name.startsWith(NEW_PREFIX)) {
				 togo = new QnaQuestion();
			 } else {
				 togo = questionLogic.getQuestionById(name);
			 }
			 delivered.put(name, togo);
		}
		return togo;
	}

	public String saveAll() {
		for (QnaQuestion question : delivered.values()) {
			questionLogic.saveQuestion(question, externalLogic.getCurrentLocationId());
			messages.addMessage(
				new TargettedMessage("qna.ask-question.save-success",
				new Object[] { TextUtil.stripTags(question.getQuestionText()) },
				TargettedMessage.SEVERITY_INFO)
			);
		}
		return "saved";
	}

	public String delete() {
		for (QnaQuestion question : delivered.values()) {
			try {
				questionLogic.removeQuestion(question.getId(), externalLogic.getCurrentLocationId());
			} catch (AttachmentException ae) {
				messages.addMessage(new TargettedMessage("qna.delete-question.attachment-error",null,TargettedMessage.SEVERITY_ERROR));
			} finally {
				messages.addMessage(
					new TargettedMessage("qna.delete-question.delete-successful",
					new Object[] {TextUtil.stripTags(question.getQuestionText())},
					TargettedMessage.SEVERITY_INFO));
			}
		}
		return "delete";
	}
	
	public String cancel() {
		// Clears file attachments from tool session
		ToolSession session = sessionManager.getCurrentToolSession();
		
		if (session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) 
		{
			List refs = (List)session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
			for (int i = 0; i < refs.size(); i++) {
				Reference ref = (Reference) refs.get(i);
				try {
					attachmentLogic.deleteAttachment(ref.getId());
				} catch (AttachmentException ae) {
					messages.addMessage(new TargettedMessage("qna.delete-question.attachment-error",null,TargettedMessage.SEVERITY_ERROR));
				}
			}
		}
		
	    session.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
	    session.removeAttribute(FilePickerHelper.FILE_PICKER_CANCEL);
		
		return "cancel";
	}

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}

	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void setAttachmentLogic(AttachmentLogic attachmentLogic) {
		this.attachmentLogic = attachmentLogic;
	}

	public Map<String, QnaQuestion> getDeliveredBeans() {
		return delivered;
	}
}
