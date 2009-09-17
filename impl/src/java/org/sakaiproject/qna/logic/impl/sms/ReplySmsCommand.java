/***********************************************************************************
 * ReplySmsCommand.java
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
package org.sakaiproject.qna.logic.impl.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.OptionsLogic;
import org.sakaiproject.qna.logic.QnaBundleLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.sms.logic.incoming.SmsCommand;

/**
 * Reply to a existing question posted in a specific Sakai site. Usage: REPLY
 * <site> <question nr> <answer text>
 * 
 * @author wilhelm@psybergate.co.za
 * 
 */
public class ReplySmsCommand implements SmsCommand {

	private static Log log = LogFactory.getLog(ReplySmsCommand.class);
	private static final String REPLY = "REPLY";
	private static final String REPLY_ALIAS = "R";

	private QuestionLogic questionLogic;
	private AnswerLogic answerLogic;
	private OptionsLogic optionsLogic;
	private QnaBundleLogic qnaBundleLogic;

	public void setQuestionLogic(QuestionLogic questionLogic) {
		this.questionLogic = questionLogic;
	}

	public void setAnswerLogic(AnswerLogic answerLogic) {
		this.answerLogic = answerLogic;
	}

	public void setOptionsLogic(OptionsLogic optionsLogic) {
		this.optionsLogic = optionsLogic;
	}

	public void setQnaBundleLogic(QnaBundleLogic qnaBundleLogic) {
		this.qnaBundleLogic = qnaBundleLogic;
	}

	public String execute(String siteId, String userId, String mobileNr,
			String... body) {
		log.debug(getCommandKey() + " command called with parameters: ("
				+ siteId + ", " + userId + ", )");

		if (body[0] == null || "".equals(body[0].trim())) {
			return qnaBundleLogic.getString("qna.sms.no-question-id");
		} else {
			String questionId = body[0].trim();

			if (body[1] == null || "".equals(body[1].trim())) {
				return qnaBundleLogic.getString("qna.sms.no-answer-text");
			}

			String answerText = body[1].trim();

			QnaQuestion question = null;
			try {
				question = questionLogic.getQuestionById(Long.valueOf(questionId), userId,
						true);
			} catch (SecurityException se) {
				return qnaBundleLogic.getFormattedMessage(
						"qna.sms.read-denied", new Object[] { userId, siteId });
			}

			if (question == null) {
				return qnaBundleLogic.getString("qna.sms.invalid-question-id");
			} else {
				QnaAnswer answer = new QnaAnswer();
				answer.setQuestion(question);
				answer.setAnswerText(answerText);
				answer.setPrivateReply(false);
				answer.setOwnerMobileNr(mobileNr);
				answer.setAnonymous(false);

				String siteRef = "/site/" + siteId;

				if (userId != null) {
					try {
						answerLogic.saveAnswer(answer, siteRef, userId);
					} catch (SecurityException se) {
						return qnaBundleLogic.getFormattedMessage(
								"qna.sms.save-answer-denied", new Object[] {
										siteId, userId });
					}

					return qnaBundleLogic.getFormattedMessage(
							"qna.sms.reply-posted", new Object[] {
									question.getId(), answer.getId() });
				} else {
					if (optionsLogic.getOptionsForLocation(siteRef)
							.getAllowUnknownMobile()) {
						answerLogic.saveAnswer(answer, siteRef, null);
						return qnaBundleLogic.getFormattedMessage(
								"qna.sms.reply-posted", new Object[] {
										question.getId(), answer.getId() });
					} else {
						return qnaBundleLogic
								.getString("qna.sms.anonymous-not-allowed");
					}
				}

			}

		}
	}

	public String[] getAliases() {
		return new String[] { REPLY_ALIAS };
	}

	public String getCommandKey() {
		return REPLY;
	}

	public String getHelpMessage() {
		return qnaBundleLogic.getString("qna.sms.reply-help");
	}

	public int getBodyParameterCount() {
		return 2;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		return true;
	}
}
