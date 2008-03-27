

package org.sakaiproject.qna.tool.params;


public class QuestionListParams extends SortPagerViewParams {

	public Long questionIdToDuplicate;
	
	public QuestionListParams() {}

	public QuestionListParams(String viewId) {
		super(viewId);
	}
	
    public QuestionListParams(String viewId, String sort_by, String sort_dir, String viewtype) {
    		super(viewId, sort_by, sort_dir,viewtype);
    }
    
    public QuestionListParams(String viewId, String sort_by, String sort_dir, String viewtype, int currentStart, int currentCount) {
		super(viewId, sort_by, sort_dir,viewtype, currentStart, currentCount);
    }
    
    public QuestionListParams(String viewId, String sort_by, String sort_dir,String viewtype, int currentStart, int currentCount, Long questionIdToDuplicate) {
    	super(viewId, sort_by, sort_dir,viewtype, currentStart, currentCount);
        this.questionIdToDuplicate = questionIdToDuplicate;
    }
    
	public String getParseSpec() {
		// include a comma delimited list of the public properties in this class
		return super.getParseSpec() + ",questionIdToDuplicate";
	}
}