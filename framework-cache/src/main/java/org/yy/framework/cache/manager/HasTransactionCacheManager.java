package org.yy.framework.cache.manager;
import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

/**
 * 带事务的缓存管理器
 * 
 * @author zhouliang
 *
 */
public class HasTransactionCacheManager extends
		AbstractTransactionSupportingCacheManager {

	private Collection<Cache> caches;

	@Override
	protected Collection<? extends Cache> loadCaches() {
		return this.caches;
	}

	public void setCaches(Collection<Cache> caches) {
		this.caches = caches;
	}

	public Cache getCache(String name) {
		return super.getCache(name);
	}

}