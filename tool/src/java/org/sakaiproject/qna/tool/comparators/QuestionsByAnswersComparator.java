/***********************************************************************************
 * QuestionsByAnswersComparator.java
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

import org.sakaiproject.qna.model.QnaQuestion;

public class QuestionsByAnswersComparator implements Comparator<QnaQuestion> {

	public int compare(QnaQuestion o1, QnaQuestion o2) {
		if (o1.getAnswers().size() > o2.getAnswers().size()) {
			return -1;
		} else if (o1.getAnswers().size() < o2.getAnswers().size()) {
			return 1;
		} else {
			return 0;
		}
	}

}
