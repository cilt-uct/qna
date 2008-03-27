package org.sakaiproject.qna.tool.params;


public class SortPagerViewParams extends PagerViewParams {

	public String sort_by;
	public String sort_dir;
	public String viewtype; // View type must correspond with ListViewType option
	
	
	public SortPagerViewParams() {}

	public SortPagerViewParams(String viewId) {
		super(viewId);
	}
	
    public SortPagerViewParams(String viewId, String sort_by, String sort_dir, String viewtype) {
    		super(viewId);
	        this.sort_by = sort_by;
	        this.sort_dir = sort_dir;
	        this.viewtype = viewtype;
    }
    
    public SortPagerViewParams(String viewId, String sort_by, String sort_dir, String viewtype, int currentStart, int currentCount) {
		super(viewId, currentStart, currentCount);
        this.sort_by = sort_by;
        this.sort_dir = sort_dir;
        this.viewtype = viewtype;
    }
    
    
	public String getParseSpec() {
		// include a comma delimited list of the public properties in this class
		return super.getParseSpec() + ",sort_by,sort_dir,viewtype,currentStart,currentCount";
	}
}