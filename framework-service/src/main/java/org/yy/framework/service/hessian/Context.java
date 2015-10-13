package org.yy.framework.service.hessian;

/**
 * Hessian访问的上下文，客服端url链接前半部分配置
 * 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
public enum Context {
    API_V2("api.v2.remote.url");
    
    private String remoteUrlConfigKey;
    
    private Context(String remoteUrlConfigKey) {
        this.remoteUrlConfigKey = remoteUrlConfigKey;
    }
    
    public String getRemoteUrl() {
        return System.getProperty(remoteUrlConfigKey, "http://127.0.0.1");
    }
}