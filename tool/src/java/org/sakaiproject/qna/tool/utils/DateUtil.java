package org.sakaiproject.qna.tool.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String getSimpleDate(Date date) {
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
}
