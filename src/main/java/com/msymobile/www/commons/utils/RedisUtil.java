package com.msymobile.www.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description:redis工具类
 * @date 2017年03月28日 下午13:25:06
 */
@SuppressWarnings("unused")
public class RedisUtil {
	private static String IP; // ip
	private static int PORT; // 端口
	private static String AUTH; // 密码(原始默认是没有密码)
	private static int MAX_ACTIVE; // 最大连接数
	private static int MAX_IDLE; // 设置最大空闲数
	private static int MAX_WAIT; // 最大连接时间
	private static int TIMEOUT; // 超时时间
	private static boolean BORROW; // 在borrow一个事例时是否提前进行validate操作
	private static JedisPool pool = null;
	private static Logger logger = Logger.getLogger(RedisUtil.class);
	/**
	 * 初始化线程池
	 */
	static {
		InputStream in = RedisUtil.class.getClassLoader().getResourceAsStream("/redis.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			IP = pro.getProperty("redis.ip");
			PORT = Integer.parseInt(pro.getProperty("redis.port"));
			AUTH = pro.getProperty("redis.auth");
			MAX_IDLE = Integer.parseInt(pro.getProperty("redis.maxIdle"));
			MAX_ACTIVE = Integer.parseInt(pro.getProperty("redis.maxActive"));
			MAX_WAIT = Integer.parseInt(pro.getProperty("redis.maxWait"));
			TIMEOUT = Integer.parseInt(pro.getProperty("redis.timeOut"));
			BORROW = Boolean.parseBoolean(pro.getProperty("redis.testOnBorrow"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT);
		config.setTestOnBorrow(BORROW);
		pool = new JedisPool(config, IP, PORT, TIMEOUT);
	}

	/**
	 * 获取连接
	 */
	public static synchronized Jedis getJedis() {
		try {
			if (pool != null) {
				return pool.getResource();
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.info("连接池连接异常");
			return null;
		}

	}

	/**
	 * @Description:设置失效时间
	 * @param  key
	 * @param seconds
	 * @return boolean 返回类型
	 */
	public static void disableTime(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.expire(key, seconds);

		} catch (Exception e) {
			logger.debug("设置失效失败.");
		} finally {
			getColse(jedis);
		}
	}

	/**
	 * @Description:插入对象
	 * @param key
	 * @param obj
	 * @return boolean 返回类型
	 */
	public static boolean addObject(String key, Object obj) {

		Jedis jedis = null;
		String value = JSONObject.toJSONString(obj);
		try {
			jedis = getJedis();
			String code = jedis.set(key, value);
			if (code.equals("ok")) {
				return true;
			}
		} catch (Exception e) {
			logger.debug("插入数据有异常.");
			return false;
		} finally {
			getColse(jedis);
		}
		return false;
	}

	/**
	 * @Description:存储key~value
	 * @param  key
	 * @param value
	 * @return void 返回类型
	 */

	public static boolean addValue(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			String code = jedis.set(key, value);
			if (code.equals("ok")) {
				return true;
			}
		} catch (Exception e) {
			logger.debug("插入数据有异常.");
			return false;
		} finally {
			getColse(jedis);
		}
		return false;
	}

	/**
	 * @Description:删除key
	 * @param key
	 * @return boolean 返回类型
	 */
	public static boolean delKey(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Long code = jedis.del(key);
			if (code > 1) {
				return true;
			}
		} catch (Exception e) {
			logger.debug("删除key异常.");
			return false;
		} finally {
			getColse(jedis);
		}
		return false;
	}

	/**
	 * @Description: 关闭连接
	 * @param jedis
	 * @return void 返回类型
	 */

	public static void getColse(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public static void main(String[] args) {
		System.out.println(RedisUtil.getJedis().select(5));
	}
}