package com.msymobile.www.commons.utils;

import java.text.SimpleDateFormat;

public class DateUtil {

	public static void main(String[] args) {
		
	}
	public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 通过日期格式返回相应格式转换对象
	 * @param pattern
	 * @return
	 */
	public static SimpleDateFormat getSimpleDateFormat(String pattern){
		return new SimpleDateFormat(pattern);
	}

}
