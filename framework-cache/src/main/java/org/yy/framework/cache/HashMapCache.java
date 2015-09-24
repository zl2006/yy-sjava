package org.yy.framework.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * 功能说明：自定义spring的cache的实现，参考cache包实现
 */
public class HashMapCache extends AbstractCache implements Cache {
    
    /**
     *缓存客户端
     */
    private Map<String, Object> client = new HashMap<String, Object>();
    
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
        return (T)object;
    }
    
    @Override
    public void put(Object key, Object value) {
        if (value == null) {
            return;
        }
        if (!(value instanceof Serializable)) {
            throw new RuntimeException("缓存对象未序列化");
        }
        
        this.client.put(handleKey(objectToString(key)), (Serializable)value);
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
    
    public Map<String, Object> getClient() {
        return client;
    }
    
    public void setClient(Map<String, Object> client) {
        this.client = client;
    }
    
}