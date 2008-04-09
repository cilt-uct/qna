package org.sakaiproject.qna.tool.producers.renderers;

import org.sakaiproject.qna.tool.constants.SortByConstants;
import org.sakaiproject.qna.tool.params.SortPagerViewParams;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIJointContainer;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.viewstate.ViewParameters;

public class SortHeaderRenderer {
	
	public static final String BULLET_UP_IMG_SRC = "/library/image/sakai/sortascending.gif";
    public static final String BULLET_DOWN_IMG_SRC = "/library/image/sakai/sortdescending.gif";
    
    public void makeSortingLink (UIContainer tofill, String divID, ViewParameters viewparams, String sort_by, String link_text){
    	SortPagerViewParams params = (SortPagerViewParams) viewparams;
    	UIJointContainer joint = new UIJointContainer(tofill, divID, "sortHeader:", ""+1);

    	
    	//Link Text
    	UIMessage.make(joint, "text", link_text);
    	if (params.sortBy.equals(sort_by)){
    		UILink.make(joint, "arrow", (params.sortDir.equals(SortByConstants.SORT_DIR_ASC) ? BULLET_UP_IMG_SRC : BULLET_DOWN_IMG_SRC));
    	}

    	//Add Link and modify params
    	String newSortDir = (params.sortBy.equals(sort_by) ? (params.sortDir.equals(SortByConstants.SORT_DIR_ASC) 
    			? SortByConstants.SORT_DIR_DESC 
    			: SortByConstants.SORT_DIR_ASC) : SortByConstants.SORT_DIR_ASC);
    	
    	ViewParameters new_params = viewparams.copyBase();
    	((SortPagerViewParams)new_params).sortBy = sort_by;
    	((SortPagerViewParams)new_params).sortDir = newSortDir;

    	UIInternalLink.make(joint, "link", new_params);
    }

}