package org.yy.framework.cache;

import java.util.concurrent.TimeoutException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * 功能说明：自定义spring的cache的实现，参考cache包实现 作者：liuxing(2015-04-12 13:57)
 */
public class MemcachedCache implements Cache {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedCache.class);

	/**
	 * 缓存的别名
	 */
	private String name;
	/**
	 * memcached客户端
	 */
	private MemcachedClient client;
	/**
	 * 缓存过期时间，默认是1小时 自定义的属性
	 */
	private int exp = 3600;
	/**
	 * 是否对key进行base64加密
	 */
	private boolean base64Key = false;
	/**
	 * 前缀名
	 */
	private String prefix = "";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return this.client;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object object = null;
		try {
			object = this.client.get(handleKey(objectToString(key)));
		} catch (TimeoutException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return (object != null ? new SimpleValueWrapper(object) : null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		try {
			Object object = this.client.get(handleKey(objectToString(key)));
			return (T) object;
		} catch (TimeoutException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			LOGGER.error(e.getMessage(), e);
		}

		return null;
	}

	@Override
	public void put(Object key, Object value) {
		if (value == null) {
			// this.evict(key);
			return;
		}

		try {
			this.client.set(handleKey(objectToString(key)), exp, value);
		} catch (TimeoutException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		this.put(key, value);
		return this.get(key);
	}

	@Override
	public void evict(Object key) {
		try {
			this.client.delete(handleKey(objectToString(key)));
		} catch (TimeoutException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public void clear() {
		try {
			this.client.flushAll();
		} catch (TimeoutException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (MemcachedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public MemcachedClient getClient() {
		return client;
	}

	public void setClient(MemcachedClient client) {
		this.client = client;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setBase64Key(boolean base64Key) {
		this.base64Key = base64Key;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 处理key
	 * 
	 * @param key
	 * @return
	 */
	private String handleKey(final String key) {
		if (base64Key) {
			return this.prefix + Base64.encodeBase64String(key.getBytes());
		}

		return this.prefix + key;
	}

	/**
	 * 转换key，去掉空格
	 * 
	 * @param object
	 * @return
	 */
	private String objectToString(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof String) {
			return ((String) object).trim();
		} else {
			return object.toString();
		}
	}

}