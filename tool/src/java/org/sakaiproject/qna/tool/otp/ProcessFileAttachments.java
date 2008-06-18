package org.sakaiproject.qna.tool.otp;

import java.util.List;

import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.qna.model.QnaAttachment;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolSession;

public class ProcessFileAttachments {
	public SessionManager sessionManager;
	
	public QuestionLocator questionLocator;
	
	public String process() {
		
		QnaQuestion question = (QnaQuestion)questionLocator.locateBean(QuestionLocator.NEW_1);
		
	    ToolSession session = sessionManager.getCurrentToolSession();
	    if (session.getAttribute(FilePickerHelper.FILE_PICKER_CANCEL) == null &&
	        session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS) != null) 
	    {
	    	List refs = (List)session.getAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
	        for (int i = 0; i < refs.size(); i++) {
	        	 Reference ref = (Reference) refs.get(i);
	        	 if (question != null) {
		        	 QnaAttachment attachment = new QnaAttachment();
		        	 attachment.setAttachmentId(ref.getId());
		        	 question.addAttachment(attachment);
	        	 }
	        }
	    }
	    session.removeAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS);
	    session.removeAttribute(FilePickerHelper.FILE_PICKER_CANCEL);

		return "processed";
	}
}
