package org.yy.framework.cache;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;


/**
 * 功能说明：自定义spring的cache的实现，参考cache包实现
 */
public class HashMapCache implements Cache {

	/**
	 * 缓存的别名
	 */
	private String name;
	/**
	 * taobao缓存客户端
	 */
	private Map<String, Object> client = new HashMap<String, Object>();
	/**
	 * 缓存过期时间，默认是1小时 自定义的属性
	 */
	@SuppressWarnings("unused")
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
		object = this.client.get(handleKey(objectToString(key)));
		return (object != null ? new SimpleValueWrapper(object) : null);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> type) {
		Object object = this.client.get(handleKey(objectToString(key)));
		return (T) object;
	}

	@Override
	public void put(Object key, Object value) {
		if (value == null) {
			// this.evict(key);
			return;
		}
		if (!(value instanceof Serializable)) {
			throw new RuntimeException("缓存对象未序列化");
		}

		this.client.put(handleKey(objectToString(key)), (Serializable) value);
		// this.client.set(handleKey(objectToString(key)), exp, value);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		this.put(key, value);
		return this.get(key);
	}

	@Override
	public void evict(Object key) {
		this.client.remove(handleKey(objectToString(key)));
	}

	@Override
	public void clear() {
		// 缓存不支持
		// this.client.flushAll();
		client.clear();
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getClient() {
		return client;
	}

	public void setClient( Map<String, Object> client) {
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