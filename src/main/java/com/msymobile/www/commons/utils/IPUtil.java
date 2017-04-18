package com.msymobile.www.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.msymobile.www.commons.utils.DateUtil;

public class IPUtil {

	public static void main(String[] args) {
		String ip = "101.254.183.41";
		getAddressByIP(ip);
		String paramIp = ip = ip.substring(0, ip.lastIndexOf(".")) + ".0";
		logger.info("只要获取ip的前三段就可以确定所属地信息了，此参数ip为--------> paramIp:" + paramIp);
		getAddressByIP(paramIp);
	}

	private static Logger logger = Logger.getLogger(IPUtil.class);
	/**
	 * 解析并获取用户的真实ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	/**
	 * 调用淘宝ip地址库远程接口，获取指定ip的地域信息
	 * 
	 * @param strIP
	 *            IP地址
	 * @return json字符串，如：
	 * {
		    "code": 0,
		    "data": {
		        "ip": "210.75.225.254",
		        "country": "中国",
		        "area": "华北",
		        "region": "北京市",
		        "city": "北京市",
		        "county": "",
		        "isp": "电信",
		        "country_id": "86",
		        "area_id": "100000",
		        "region_id": "110000",
		        "city_id": "110000",
		        "county_id": "-1",
		        "isp_id": "100017"
		    }
		}
	 */
	public static String getAddressByIP(String ip) {
		Long startTime = System.currentTimeMillis();
		HttpURLConnection connection = null;
		StringBuffer result = new StringBuffer();
		try {
			URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			// connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			// connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			logger.info("通过ip获取的结果信息：---------------------------> " + result);
			logger.info(DateUtil.getSimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS).format(new Date()) + "通过ip [" + ip + "] 获取用户地域信息耗时："
					+ (System.currentTimeMillis() - startTime) + " 毫秒");
		}
		return result.toString();
	}
	
	public static int getIPValue(String ip){
		int value = 1;
		for(String c:ip.split(".")){
			value +=Integer.parseInt(c)*255;
		}
		return value;
	}
}
