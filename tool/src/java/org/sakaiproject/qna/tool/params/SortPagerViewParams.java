package org.sakaiproject.qna.tool.params;


public class SortPagerViewParams extends PagerViewParams {

	public String sortBy;
	public String sortDir;
	public String viewtype; // View type must correspond with ListViewType option
	
	
	public SortPagerViewParams() {}

	public SortPagerViewParams(String viewId) {
		super(viewId);
	}
	
    public SortPagerViewParams(String viewId, String sort_by, String sort_dir, String viewtype) {
    		super(viewId);
	        this.sortBy = sort_by;
	        this.sortDir = sort_dir;
	        this.viewtype = viewtype;
    }
    
    public SortPagerViewParams(String viewId, String sort_by, String sort_dir, String viewtype, int currentStart, int currentCount) {
		super(viewId, currentStart, currentCount);
        this.sortBy = sort_by;
        this.sortDir = sort_dir;
        this.viewtype = viewtype;
    }
    
    
	public String getParseSpec() {
		// include a comma delimited list of the public properties in this class
		return super.getParseSpec() + ",sortBy,sortDir,viewtype,currentStart,currentCount";
	}
}