
package org.sakaiproject.qna.tool.params;

import org.sakaiproject.qna.tool.producers.renderers.PagerRenderer;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class PagerViewParams extends SimpleViewParameters {

	public int current_start = 0;
	public int current_count = PagerRenderer.DEFAULT_START_COUNT;
	
	public PagerViewParams() {}
	
	public PagerViewParams(String viewId) {
		super(viewId);
	}

    public PagerViewParams(String viewId, int currentStart, int currentCount){
    		super(viewId);
	        this.current_start = currentStart;
	        this.current_count = currentCount;
    }
    
	public String getParseSpec() {
		// include a comma delimited list of the public properties in this class
		return super.getParseSpec() + ",current_start,current_count";
	}
}