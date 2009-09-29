/**
 * Copyright (c) 2007-2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.qna.logic.test;

import static org.sakaiproject.qna.logic.test.TestDataPreload.LOCATION1_ID;
import static org.sakaiproject.qna.logic.test.TestDataPreload.USER_UPDATE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.qna.dao.QnaDao;
import org.sakaiproject.qna.logic.impl.CategoryLogicImpl;
import org.sakaiproject.qna.logic.impl.OptionsLogicImpl;
import org.sakaiproject.qna.logic.impl.PermissionLogicImpl;
import org.sakaiproject.qna.logic.impl.QuestionLogicImpl;
import org.sakaiproject.qna.logic.impl.sms.QuestionSmsCommand;
import org.sakaiproject.qna.logic.test.stubs.DeveloperHelperServiceStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalEventLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ExternalLogicStub;
import org.sakaiproject.qna.logic.test.stubs.NotificationLogicStub;
import org.sakaiproject.qna.logic.test.stubs.QnaBundleLogicStub;
import org.sakaiproject.qna.logic.test.stubs.ServerConfigurationServiceStub;
import org.sakaiproject.qna.model.QnaOptions;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.sms.logic.incoming.ParsedMessage;
import org.sakaiproject.sms.logic.incoming.ShortMessageCommand;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public class QuestionSmsCommandTest extends
		AbstractTransactionalSpringContextTests {

	QuestionSmsCommand questionSmsCommand;
	OptionsLogicImpl optionsLogic;
	QuestionLogicImpl questionLogic;
	PermissionLogicImpl permissionLogic;
	CategoryLogicImpl categoryLogic;
	
	private final ExternalLogicStub externalLogicStub = new ExternalLogicStub();
	private final ExternalEventLogicStub externalEventLogicStub = new ExternalEventLogicStub();
	private final QnaBundleLogicStub bundleLogicStub = new QnaBundleLogicStub();
	private final ServerConfigurationServiceStub serverConfigurationServiceStub = new ServerConfigurationServiceStub();	
	private final DeveloperHelperServiceStub developerHelperServiceStub = new DeveloperHelperServiceStub();
	private final NotificationLogicStub notificationLogicStub = new NotificationLogicStub();
	
	@SuppressWarnings("unused")
	private final TestDataPreload tdp = new TestDataPreload();

	private static Log log = LogFactory.getLog(QuestionSmsCommandTest.class);
	private static String CMD = "QUESTION";
	private static String SITE = "ref-1111111";
	
	@Override
	protected String[] getConfigLocations() {
		// point to the needed spring config files, must be on the classpath
		// (add component/src/webapp/WEB-INF to the build path in Eclipse),
		// they also need to be referenced in the project.xml file
		return new String[] { "hibernate-test.xml", "spring-hibernate.xml" };
	}

	// run this before each test starts
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
	}
	
	// run this before each test starts and as part of the transaction
	@Override
	protected void onSetUpInTransaction() {
		// load the spring created dao class bean from the Spring Application
		// Context
		QnaDao dao = (QnaDao) applicationContext.getBean("org.sakaiproject.qna.dao.impl.QnaDaoTarget");
		if (dao == null) {
			log.error("onSetUpInTransaction: DAO could not be retrieved from spring context");
		}
			
		permissionLogic = new PermissionLogicImpl();
		permissionLogic.setExternalLogic(externalLogicStub);
		
		// create and setup options
		optionsLogic = new OptionsLogicImpl();
		optionsLogic.setDao(dao);
		optionsLogic.setPermissionLogic(permissionLogic);
		optionsLogic.setExternalLogic(externalLogicStub);
		optionsLogic.setExternalEventLogic(externalEventLogicStub);
		optionsLogic.setServerConfigurationService(serverConfigurationServiceStub);
		
		// create and setup category logic
		categoryLogic = new CategoryLogicImpl();
		categoryLogic.setDao(dao);
		categoryLogic.setExternalEventLogic(externalEventLogicStub);
		categoryLogic.setExternalLogic(externalLogicStub);
		categoryLogic.setPermissionLogic(permissionLogic);
		categoryLogic.setQnaBundleLogic(bundleLogicStub);
		
		// create and setup the question logic
		questionLogic = new QuestionLogicImpl();
		questionLogic.setDao(dao);
		questionLogic.setPermissionLogic(permissionLogic);
		questionLogic.setOptionsLogic(optionsLogic);
		questionLogic.setExternalLogic(externalLogicStub);
		questionLogic.setExternalEventLogic(externalEventLogicStub);
		questionLogic.setDeveloperHelperService(developerHelperServiceStub);
		questionLogic.setNotificationLogic(notificationLogicStub);
		questionLogic.setCategoryLogic(categoryLogic);
		
		questionSmsCommand = new QuestionSmsCommand();
		questionSmsCommand.setOptionsLogic(optionsLogic);
		questionSmsCommand.setQnaBundleLogic(bundleLogicStub);
		questionSmsCommand.setQuestionLogic(questionLogic);
		questionSmsCommand.setCategoryLogic(categoryLogic);
		questionSmsCommand.setExternalLogic(externalLogicStub);

	}
	
	/**
	 * Test empty or null body
	 */
	public void testEmptyBody() {
		assertEquals("qna.sms.no-question-text", questionSmsCommand.execute(
				new ParsedMessage(USER_UPDATE, CMD, SITE, "", 1), ShortMessageCommand.MESSAGE_TYPE_SMS, "1234"));
		String nullString = null;
		assertEquals("qna.sms.no-question-text", questionSmsCommand.execute(
				new ParsedMessage(USER_UPDATE, CMD, SITE, nullString, 0), ShortMessageCommand.MESSAGE_TYPE_SMS,"1234"));
		assertEquals(0, questionLogic.getAllQuestions(LOCATION1_ID).size()); 
	}
	
	/**
	 * Test saving of question
	 */
	public void testSaveQuestion() {
		assertEquals("qna.sms.question-posted.no-replies", questionSmsCommand.execute(
				new ParsedMessage(USER_UPDATE, CMD, SITE, "new question", 1), ShortMessageCommand.MESSAGE_TYPE_SMS,"1234" ));
		String id = bundleLogicStub.getLastParameters()[1].toString();
		QnaQuestion question = questionLogic.getQuestionById(Long.valueOf(id));
		assertEquals("new question", question.getQuestionText());
		assertEquals(USER_UPDATE, question.getOwnerId());
		assertEquals("1234", question.getOwnerMobileNr());
		assertEquals(1, questionLogic.getAllQuestions(LOCATION1_ID).size()); 
	}
	
	public void testSaveQuestionNullUserId() {
		externalLogicStub.currentUserId = USER_UPDATE;
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		options.setAllowUnknownMobile(true);
		options.setSmsNotification(false);
		optionsLogic.saveOptions(options, LOCATION1_ID);
		assertTrue(options.getAllowUnknownMobile());
		assertEquals("qna.sms.question-posted.no-replies", questionSmsCommand.execute(
				new ParsedMessage(null, CMD, SITE, "new question", 1), ShortMessageCommand.MESSAGE_TYPE_SMS,"1234" ));
		String id = bundleLogicStub.getLastParameters()[1].toString();
		QnaQuestion question = questionLogic.getQuestionById(Long.valueOf(id));
		assertEquals("new question", question.getQuestionText());
		assertEquals(null, question.getOwnerId());
		assertEquals("1234", question.getOwnerMobileNr());
		assertEquals(1, questionLogic.getAllQuestions(LOCATION1_ID).size()); 
	}
	
	public void testSaveQuestionNullUserIdAnonMobileNotAllowed() {
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		assertFalse(options.getAllowUnknownMobile());
		assertEquals("qna.sms.anonymous-not-allowed", questionSmsCommand.execute(
				new ParsedMessage(null, CMD, SITE, "new question", 1), ShortMessageCommand.MESSAGE_TYPE_SMS,"1234" ));
		assertEquals(0, questionLogic.getAllQuestions(LOCATION1_ID).size()); 
	}
	
	public void testSaveQuestionNullUserIdNotModerated() {
		externalLogicStub.currentUserId = USER_UPDATE;
		QnaOptions options = optionsLogic.getOptionsForLocation(LOCATION1_ID);
		options.setAllowUnknownMobile(true);
		options.setModerated(false);
		options.setSmsNotification(false);
		optionsLogic.saveOptions(options, LOCATION1_ID);
		assertTrue(options.getAllowUnknownMobile());
		assertEquals("qna.sms.question-posted.no-replies", questionSmsCommand.execute(
				new ParsedMessage(null, CMD, SITE, "new question", 1), ShortMessageCommand.MESSAGE_TYPE_SMS,"1234" ));
		String id = bundleLogicStub.getLastParameters()[1].toString();
		QnaQuestion question = questionLogic.getQuestionById(Long.valueOf(id));
		assertEquals("new question", question.getQuestionText());
		assertEquals(null, question.getOwnerId());
		assertEquals("1234", question.getOwnerMobileNr());
		assertEquals(1, questionLogic.getAllQuestions(LOCATION1_ID).size()); 
		assertNotNull(question.getCategory());
	}
	
}
