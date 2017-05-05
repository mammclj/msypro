package com.msymobile.www.commons.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import com.sun.istack.internal.logging.Logger;

public class PhoneUtil {
	private static Logger logger = Logger.getLogger(PhoneUtil.class);
	private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

	private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

	private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();

	/**
	 * 根据国家代码和手机号 判断手机号是否有效
	 * 
	 * @param phoneNumber
	 * @param countryCode
	 * @return
	 */
	public static boolean checkPhoneNumber(String phoneNumber, String countryCode) {

		int ccode = Integer.parseInt(countryCode);
		long phone = Long.parseLong(phoneNumber);

		PhoneNumber pn = new PhoneNumber();
		pn.setCountryCode(ccode);
		pn.setNationalNumber(phone);

		return phoneNumberUtil.isValidNumber(pn);

	}

	/**
	 * 根据国家代码和手机号 判断手机运营商
	 * 
	 * @param phoneNumber
	 * @param countryCode
	 * @return
	 */
	public static String getCarrier(String phoneNumber, String countryCode) {

		int ccode = Integer.parseInt(countryCode);
		long phone = Long.parseLong(phoneNumber);

		PhoneNumber pn = new PhoneNumber();
		pn.setCountryCode(ccode);
		pn.setNationalNumber(phone);
		// 返回结果只有英文，自己转成成中文
		String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
		String carrierZh = "";
		carrierZh += geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
		switch (carrierEn) {
		case "China Mobile":
			carrierZh += "移动";
			break;
		case "China Unicom":
			carrierZh += "联通";
			break;
		case "China Telecom":
			carrierZh += "电信";
			break;
		default:
			break;
		}
		return carrierZh;
	}

	/**
	 * 
	 * @Description: 根据国家代码和手机号 手机归属地 @date 2015-7-13 上午11:33:18 @param
	 * phoneNumber @param countryCode @return 参数 @throws
	 */
	public static String getGeo(String phoneNumber, String countryCode) {

		int ccode = Integer.parseInt(countryCode);
		long phone = Long.parseLong(phoneNumber);

		PhoneNumber pn = new PhoneNumber();
		pn.setCountryCode(ccode);
		pn.setNationalNumber(phone);

		return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);

	}

	public static void readTxt(String filePath) {
		int i = 0;
		int j = 0;
		long startTime = System.currentTimeMillis();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				logger.info("运行开始。。。。。。。。。。。。");
				String outStr = "";
				OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(new File("D:\\QQRelations\\QQDoc\\869840420\\FileRecv\\phone_data_new.txt")), encoding);
				
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if(lineTxt.contains("*")|| lineTxt.contains("null") || !ValidationUtil.isMobileNO(lineTxt)){
						j++;
						continue;
					}
					outStr = lineTxt + " 归属地为：" + getGeo(lineTxt, "86")+"\r\n";
					ow.write(outStr);
					logger.info(outStr);
					i++;
				}
				ow.close();
				read.close();
			} else {
				logger.info("找不到指定的文件");
			}
		} catch (Exception e) {
			logger.info("读取文件内容出错");
			e.printStackTrace();
		}finally{
			long allTime = System.currentTimeMillis()-startTime;
			logger.info("运行结束。。。。。。。。。。。。");
			logger.info("共处理有效手机号【"+i+"】个。。。。。。。。。。");
			logger.info("共处理无效手机号【"+j+"】个。。。。。。。。。。");
			logger.info("完成所需时间为:"+allTime+"毫秒,约为"+allTime/1000/60+"分钟");
		}
	}

	public static void main(String[] args) {
		String filePath = "D:\\QQRelations\\QQDoc\\869840420\\FileRecv\\phone_data.txt";
		readTxt(filePath);
//		String phoneNum = "18612815377";
//		String phoneNum = "17756923145";
		// phoneNum = "18898756604";//广东省深圳市
		// phoneNum = "18609978223";//新疆阿克苏地区
		// phoneNum = "18895082685";//宁夏银川市
//		String nationalMobileNum = "86";
//		logger.info(phoneNum + " 归属地为：" + getGeo(phoneNum, nationalMobileNum));
	}
}
