package org.tbwork.roo.client.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
 
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
	
	public static String getDateString(){
		return sdf.format(new Date());
	}
	
}
