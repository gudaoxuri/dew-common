/*
 * Copyright 2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecfront.dew.common;


/**
 * The type Http helper factory.
 *
 * @author gudaoxuri
 */
public class HttpHelperFactory {

    public enum BACKEND {
        APACHE, JDK, AUTO
    }


    /**
     * Choose http helper.
     *
     * @return the http helper
     */
    protected static HttpHelper choose() {
        return choose(200, 20, -1, -1, false, true, BACKEND.AUTO);
    }

    protected static HttpHelper choose(BACKEND backend) {
        return choose(200, 20, -1, -1, false, true, backend);
    }

    protected static HttpHelper choose(int timeoutMS, boolean autoRedirect, BACKEND backend) {
        return choose(200, 20, -1, -1, false, true, backend);
    }


    /**
     * Choose http helper.
     *
     * @param maxTotal                整个连接池最大连接数
     * @param maxPerRoute             每个路由（域）的默认最大连接
     * @param defaultConnectTimeoutMS 默认连接超时时间
     * @param defaultSocketTimeoutMS  默认读取超时时间
     * @param autoRedirect            302状态下是否自动跳转
     * @param retryAble               是否重试
     * @param backend                 指定HTTP Client的实现
     * @return the http helper
     */
    static HttpHelper choose(int maxTotal, int maxPerRoute, int defaultConnectTimeoutMS, int defaultSocketTimeoutMS,
                             boolean autoRedirect, boolean retryAble, HttpHelperFactory.BACKEND backend) {
        switch (backend) {
            case JDK:
                return new JDKHttpHelper(defaultConnectTimeoutMS, autoRedirect);
            case APACHE:
                return new ApacheHttpHelper(maxTotal, maxPerRoute, defaultConnectTimeoutMS, defaultSocketTimeoutMS, autoRedirect, retryAble);
            case AUTO:
            default:
                if (DependencyHelper.hasDependency("org.apache.http.impl.client.CloseableHttpClient")) {
                    return new ApacheHttpHelper(maxTotal, maxPerRoute, defaultConnectTimeoutMS, defaultSocketTimeoutMS, autoRedirect, retryAble);
                }
                return new JDKHttpHelper(defaultConnectTimeoutMS, autoRedirect);
        }
    }
}
