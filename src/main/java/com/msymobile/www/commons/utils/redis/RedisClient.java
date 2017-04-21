package com.msymobile.www.commons.utils.redis;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisClient {
	private static Logger logger = Logger.getLogger(RedisClient.class);

	private static JedisPool pool = null;
	private static String IP_ADDRESS = null;
	private static int PORT; // 端口
	private static String AUTH; // 密码(原始默认是没有密码)
	private static int MAX_ACTIVE; // 最大连接数
	private static int MAX_IDLE; // 设置最大空闲数
	private static int MAX_WAIT; // 最大连接时间
	private static int TIMEOUT; // 超时时间
	private static boolean BORROW; // 在borrow一个事例时是否提前进行validate操作

	@PostConstruct
	public void initRedisCOnfig() {
		try {
			logger.info("------------- redis pool init start------------- ");

			Properties props = new Properties();
			props.load(RedisClient.class.getClassLoader().getResourceAsStream("redis.properties"));
			IP_ADDRESS = props.getProperty("redis.ip");
			PORT = Integer.parseInt(props.getProperty("redis.port"));
			AUTH = props.getProperty("redis.auth");
			MAX_IDLE = Integer.parseInt(props.getProperty("redis.maxIdle"));
			MAX_ACTIVE = Integer.parseInt(props.getProperty("redis.maxActive"));
			MAX_WAIT = Integer.parseInt(props.getProperty("redis.maxWait"));
			TIMEOUT = Integer.parseInt(props.getProperty("redis.timeOut"));
			BORROW = Boolean.parseBoolean(props.getProperty("redis.testOnBorrow"));
			// 创建jedis池配置实例
			JedisPoolConfig config = new JedisPoolConfig();

			// 设置池配置项值
			config.setTestWhileIdle(false);
			config.setMaxTotal(Integer.valueOf(MAX_ACTIVE));
			config.setMaxIdle(Integer.valueOf(MAX_IDLE));
			config.setMaxWaitMillis(Long.valueOf(MAX_WAIT));
			config.setTestOnBorrow(Boolean.valueOf(BORROW));
			config.setTestOnReturn(true);

			pool = new JedisPool(config, IP_ADDRESS, PORT, TIMEOUT);

			boolean connected = isConnected();
			if (!connected) {
				logger.error("redis 初始化出错 缓存服务器连接不上！ ");
				throw new Exception("IP:" + IP_ADDRESS + ", redis服务器不可以连接~~~，请检查配置 与redis 服务器");
			}

			logger.info("------------- redis pool init end------------- ");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Error("IP:" + IP_ADDRESS + ",设置redis服务器出错", e);
		}
	}

	public boolean isConnected() {
		return getRedis().isConnected();
	}

	public void destory() {
		pool.destroy();
	}

	public Jedis getRedis() {
		Jedis jedis = pool.getResource();
		jedis.select(0);
		return jedis;
	}

	public Jedis getRedis(int index) {
		Jedis jedis = pool.getResource();
		jedis.select(index);
		return jedis;
	}

	public void returnRedis(Jedis jedis) {
		pool.returnResource(jedis);
	}

	public void returnBrokeRedis(Jedis jedis) {
		pool.returnBrokenResource(jedis);
	}
}
