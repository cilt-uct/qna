/***********************************************************************************
 * ComparatorUtil.java
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

package org.sakaiproject.qna.tool.utils;

import java.util.Comparator;

import org.sakaiproject.qna.model.QnaQuestion;
import org.sakaiproject.qna.tool.comparators.MostPopularComparator;
import org.sakaiproject.qna.tool.comparators.QuestionTextComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsByAnswersComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsByCategoryTextComparator;
import org.sakaiproject.qna.tool.comparators.QuestionsSortOrderComparator;
import org.sakaiproject.qna.tool.comparators.RecentChangesComparator;
import org.sakaiproject.qna.tool.comparators.RecentQuestionsComparator;
import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.constants.ViewTypeConstants;

public class ComparatorUtil {
	
	public static Comparator<QnaQuestion> getComparator(String viewType, String sortBy) {
		if (viewType.equals(ViewTypeConstants.CATEGORIES)) {
			return new QuestionsSortOrderComparator();
		} else {
			if (sortBy.equals(SortByConstants.ANSWERS)) {
				return new QuestionsByAnswersComparator();
			} else if (sortBy.equals(SortByConstants.CATEGORY)) {
				return new QuestionsByCategoryTextComparator();
			} else if (sortBy.equals(SortByConstants.CREATED)) {
				return new RecentQuestionsComparator();
			} else if (sortBy.equals(SortByConstants.MODIFIED)) {
				return new RecentChangesComparator();
			} else if (sortBy.equals(SortByConstants.QUESTIONS)) {
				return new QuestionTextComparator();
			} else if (sortBy.equals(SortByConstants.VIEWS)) {
				return new MostPopularComparator();
			}
		}
		return new QuestionTextComparator(); // default
	}
}
