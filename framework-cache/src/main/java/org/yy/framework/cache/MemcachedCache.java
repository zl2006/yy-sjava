package org.yy.framework.cache;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * 功能说明：自定义spring的cache的实现，参考cache包实现 
 */
public class MemcachedCache extends AbstractCache implements Cache {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MemcachedCache.class);
    
    /**
     * memcached客户端
     */
    private MemcachedClient client;
    
    @Override
    public Object getNativeCache() {
        return this.client;
    }
    
    @Override
    public ValueWrapper get(Object key) {
        Object object = null;
        try {
            object = this.client.get(handleKey(objectToString(key)));
        }
        catch (TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (MemcachedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        return (object != null ? new SimpleValueWrapper(object) : null);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Class<T> type) {
        try {
            Object object = this.client.get(handleKey(objectToString(key)));
            return (T)object;
        }
        catch (TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (MemcachedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public void put(Object key, Object value) {
        if (value == null) {
            return;
        }
        
        try {
            this.client.set(handleKey(objectToString(key)), getExp(), value);
        }
        catch (TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (MemcachedException e) {
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
        }
        catch (TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (MemcachedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    @Override
    public void clear() {
        try {
            this.client.flushAll();
        }
        catch (TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        catch (MemcachedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public MemcachedClient getClient() {
        return client;
    }
    
    public void setClient(MemcachedClient client) {
        this.client = client;
    }
    
}