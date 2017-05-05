package com.msymobile.www.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ValidationUtil {
	private static Logger logger = Logger.getLogger(ValidationUtil.class);

	/**
	 * 验证手机号码 
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186
	 * 电信：133、153、180、189、（1349卫通）
	 * 
	 * @param mobile 移动电话号码
	 */
	public static boolean isMobileNO(String mobile) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
		Matcher m = p.matcher(mobile);
		logger.info("验证电话号码【"+mobile+"】结果为:"+m.matches());
		return m.matches();
	}

	public static void main(String[] args) {
		isMobileNO("14799999064");
		
	}

}
