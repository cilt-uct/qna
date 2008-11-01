package org.sakaiproject.qna.logic.impl.entity;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.entitybroker.EntityBroker;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.event.api.Event;
import org.sakaiproject.qna.logic.AnswerLogic;
import org.sakaiproject.qna.logic.QuestionLogic;
import org.sakaiproject.qna.model.QnaAnswer;
import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.search.api.EntityContentProducer;
import org.sakaiproject.search.api.SearchIndexBuilder;
import org.sakaiproject.search.api.SearchService;
import org.sakaiproject.search.model.SearchBuilderItem;
import org.sakaiproject.util.FormattedText;

/**
 * | event                | ref                                            |
+----------------------+------------------------------------------------+
| qna.answer.created   | /qna-answer/091ee1301d5769e3011d57700f390006   |
| qna.category.created | /qna-category/091ee1301d5769e3011d576fa8b00004 |
| qna.options.created  | /qna-options/091ee1301d5769e3011d576fa81f0003  |
| qna.question.created | /qna-question/091ee1301d5769e3011d576fc19d0005 |
| qna.question.updated | /qna-question/091ee1301d5769e3011d576fc19d0005 |
 * @author dhorwitz
 *
 */
public class AnswerEntityContentProducer implements EntityContentProducer {

	private static Log log = LogFactory.getLog(AnswerEntityContentProducer.class);
	
	// runtime dependency
	private List addEvents = null;

	// runtime dependency
	private List removeEvents = null;
	
	
	public void setAddEvents(List addEvents) {
		this.addEvents = addEvents;
	}


	public void setRemoveEvents(List removeEvents) {
		this.removeEvents = removeEvents;
	}

	/**
	 * Injected Services and settings
	 */
	private AnswerLogic answerLogic;
	private EntityBroker entityBroker;
	private ServerConfigurationService serverConfigurationService;
	private SearchService searchService;
	private SearchIndexBuilder searchIndexBuilder;
	private String toolName;
	
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}


	public void setAnswerLogic(AnswerLogic questionLogic) {
		this.answerLogic = questionLogic;
	}


	public void setEntityBroker(EntityBroker entityBroker) {
		this.entityBroker = entityBroker;
	}


	public void setServerConfigurationService(
			ServerConfigurationService serverConfigurationService) {
		this.serverConfigurationService = serverConfigurationService;
	}


	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}


	public void setSearchIndexBuilder(SearchIndexBuilder searchIndexBuilder) {
		this.searchIndexBuilder = searchIndexBuilder;
	}

	
	
	
	/***
	 * Init
	 */
	public void init()
	{

		if ( "true".equals(serverConfigurationService.getString(
				"search.enable", "false")))
		{
			for (Iterator i = addEvents.iterator(); i.hasNext();)
			{
				searchService.registerFunction((String) i.next());
			}
			
			for (Iterator i = removeEvents.iterator(); i.hasNext();)
			{
				searchService.registerFunction((String) i.next());
			}
			
			searchIndexBuilder.registerEntityContentProducer(this);
		}
	}
	
	
	/**
	 *  ContentProducer Methods
	 */
	
	public boolean canRead(String reference) {
		// TODO Auto-generated method stub
		return true;
	}

	public String getContainer(String ref) {
		return getSiteId(ref);
	}

	/**
	 * 
	 */
	public String getContent(String reference) {
		log.debug("getting qna answer content " + reference);
		String id = getId(reference);
		QnaAnswer a = answerLogic.getAnswerById(id);
		String ret = null;
		ret = FormattedText.convertFormattedTextToPlaintext(a.getAnswerText());
		return ret;
	}

	public Reader getContentReader(String reference) {
		return new StringReader(getContent(reference));
	}

	public Map getCustomProperties(String ref) {
		return null;
	}

	public String getCustomRDF(String ref) {
		return null;
	}

	public String getId(String ref) {
		return EntityReference.getIdFromRef(ref);
	}

	/**
	 * deprecated method
	 */
	public List getSiteContent(String context) {
		return null;
	}

	public Iterator getSiteContentIterator(String context) {
		log.debug("getting qna answers for " + context);
		
		List<QnaAnswer> answers = answerLogic.getAllAnswers("/site/"+ context);
		log.debug("found " + answers.size() + " questions");
		List<String> refs = new ArrayList<String>();
		for (int i = 0; i < answers.size(); i++) {
			QnaAnswer a = (QnaAnswer)answers.get(i);
			String ref = "/" + toolName + "/" + a.getId();
			refs.add(ref);
		}
		return refs.iterator();
	}

	public String getSiteId(String reference) {
		String id = getId(reference);
		QnaAnswer a = answerLogic.getAnswerById(id);
		String siteId = EntityReference.getIdFromRefByKey(a.getQuestion().getLocation(),"site");
		log.debug("returnint " + siteId);
		return siteId;
	}

	public String getSubType(String ref) {
		return toolName;
	}

	public String getTitle(String reference) {
		return getContent(reference);
	}

	public String getTool() {
		return toolName;
	}

	public String getType(String ref) {
		return toolName;
	}

	public String getUrl(String reference) {
		log.debug("getUrl(" + reference +")");
		String msgId = EntityReference.getIdFromRefByKey(reference, toolName);
		String url = entityBroker.getEntityURL("/"+ toolName +"/" + msgId);
		log.debug("Returning url: " + url);
		return url;
	}

	public boolean isContentFromReader(String reference) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isForIndex(String reference) {
		String id = getId(reference);
		QnaAnswer a = answerLogic.getAnswerById(id);
		if (a != null)
			return true;
		
		return false;
	}

	public boolean matches(String reference) {
		String prefix = EntityReference.getPrefix(reference);
		log.debug("checkin if " + prefix + " matches");
		if (toolName.equals(prefix))
			return true;
		
		return false;
	}

	public boolean matches(Event event) {
		return matches(event.getResource());
	}

	
	public Integer getAction(Event event) {
		String evt = event.getEvent();
		if (evt == null) return SearchBuilderItem.ACTION_UNKNOWN;
		for (Iterator i = addEvents.iterator(); i.hasNext();)
		{
			String match = (String) i.next();
			if (evt.equals(match))
			{
				return SearchBuilderItem.ACTION_ADD;
			}
		}
		for (Iterator i = removeEvents.iterator(); i.hasNext();)
		{
			String match = (String) i.next();
			if (evt.equals(match))
			{
				return SearchBuilderItem.ACTION_DELETE;
			}
		}
		return SearchBuilderItem.ACTION_UNKNOWN;
	}

	/** deprecated in 2.5
	 * 
	 * @return
	 */
	public List getAllContent() {
		return null;
	}
}
