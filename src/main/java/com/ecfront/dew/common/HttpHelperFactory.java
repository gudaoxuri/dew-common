package com.ecfront.dew.common;


public class HttpHelperFactory {

    static HttpHelper choose() {
        return choose(200, 20);
    }

    /**
     * @param maxTotal    整个连接池最大连接数
     * @param maxPerRoute 每个路由（域）的默认最大连接
     */
    static HttpHelper choose(int maxTotal, int maxPerRoute) {
        if (dependencyHelper.hasDependency("org.apache.http.impl.client.CloseableHttpClient")) {
            return new ApacheHttpHelper(maxTotal, maxPerRoute);
        }
        return null;
    }

}
