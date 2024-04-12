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

import com.ecfront.dew.common.exception.RTException;
import com.ecfront.dew.common.exception.RTGeneralSecurityException;
import com.ecfront.dew.common.exception.RTIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * HTTP操作.
 *
 * @author gudaoxuri
 */
public class HttpHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    private final HttpClient httpClient;
    private Function<PreRequestContext, PreRequestContext> preRequestFun;

    /**
     * 初始化.
     *
     * @param defaultTimeoutMS 默认超时时间
     * @param autoRedirect     302状态下是否自动跳转
     */
    HttpHelper(int defaultTimeoutMS, boolean autoRedirect) {
        final Properties props = System.getProperties();
        props.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        var allowAllHeaders = ManagementFactory.getRuntimeMXBean().getInputArguments().stream().anyMatch(arg ->
                arg.contains("--add-opens=java.net.http/jdk.internal.net.http.common"));
        if (allowAllHeaders) {
            try {
                // 解决JDK11对请求头限制的Bug.
                // 如果请求头有包含 "connection", "content-length", "date", "expect", "from", "host", "upgrade", "via", "warning" 字段，
                // 则需要调用此方法解除限制。
                // NOTE: 需要在运行参数中添加 --add-opens java.net.http/jdk.internal.net.http.common=ALL-UNNAMED
                // @see <a href="https://bugs.openjdk.java.net/browse/JDK-8213696">JDK-8213696</a>
                var jdkUtilsClazz = Class.forName("jdk.internal.net.http.common.Utils");
                var disallowedHeads = jdkUtilsClazz.getDeclaredField("DISALLOWED_HEADERS_SET");
                var modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(disallowedHeads, disallowedHeads.getModifiers() & ~Modifier.FINAL);
                $.bean.setValue(null, disallowedHeads, new HashSet<String>());
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignore) {
            }
        }
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            var httpClientBuild =
                    HttpClient.newBuilder()
                            .followRedirects(autoRedirect ? HttpClient.Redirect.ALWAYS : HttpClient.Redirect.NEVER)
                            .sslContext(ctx)
                            .sslParameters(new SSLParameters());
            if (defaultTimeoutMS != -1 && defaultTimeoutMS != 0) {
                httpClientBuild.connectTimeout(Duration.ofMillis(defaultTimeoutMS));
            }
            httpClient = httpClientBuild.build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RTGeneralSecurityException(e);
        }
    }

    /**
     * 设置请求前置拦截器.
     *
     * @param preRequestFun 拦截处理方法
     */
    public void setPreRequest(Function<PreRequestContext, PreRequestContext> preRequestFun) {
        this.preRequestFun = preRequestFun;
    }

    /**
     * Get请求.
     *
     * @param url 请求url
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String get(String url) throws RTIOException {
        return get(url, null, null, null, -1);
    }

    /**
     * Get请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String get(String url, Map<String, String> header) throws RTIOException {
        return get(url, header, null, null, -1);
    }

    /**
     * Get请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String get(String url, String contentType) throws RTIOException {
        return get(url, null, contentType, null, -1);
    }

    /**
     * Get请求.
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String get(String url, Map<String, String> header, String contentType, String charset, int timeoutMS) throws RTIOException {
        return request("GET", url, null, header, contentType, charset, timeoutMS).result;
    }

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url 请求url
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap getWrap(String url) throws RTIOException {
        return getWrap(url, null, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap getWrap(String url, Map<String, String> header) throws RTIOException {
        return getWrap(url, header, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap getWrap(String url, String contentType) throws RTIOException {
        return getWrap(url, null, contentType, null, -1);
    }

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap getWrap(String url, Map<String, String> header, String contentType, String charset, int timeoutMS) throws RTIOException {
        return request("GET", url, null, header, contentType, charset, timeoutMS);
    }

    /**
     * Post请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String post(String url, Object body) throws RTIOException {
        return post(url, body, null, null, null, -1);
    }

    /**
     * Post请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String post(String url, Object body, Map<String, String> header) throws RTIOException {
        return post(url, body, header, null, null, -1);
    }

    /**
     * Post请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String post(String url, Object body, String contentType) throws RTIOException {
        return post(url, body, null, contentType, null, -1);
    }

    /**
     * Post请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String post(String url, Object body, Map<String, String> header, String contentType, String charset, int timeoutMS) throws RTIOException {
        return request("POST", url, body, header, contentType, charset, timeoutMS).result;
    }

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap postWrap(String url, Object body) throws RTIOException {
        return postWrap(url, body, null, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求Header
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap postWrap(String url, Object body, Map<String, String> header) throws RTIOException {
        return postWrap(url, body, header, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap postWrap(String url, Object body, String contentType) throws RTIOException {
        return postWrap(url, body, null, contentType, null, -1);
    }

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap postWrap(String url, Object body, Map<String, String> header, String contentType, String charset, int timeoutMS)
            throws RTIOException {
        return request("POST", url, body, header, contentType, charset, timeoutMS);
    }

    /**
     * Put请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String put(String url, Object body) throws RTIOException {
        return put(url, body, null, null, null, -1);
    }

    /**
     * Put请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String put(String url, Object body, Map<String, String> header) throws RTIOException {
        return put(url, body, header, null, null, -1);
    }

    /**
     * Put请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String put(String url, Object body, String contentType) throws RTIOException {
        return put(url, body, null, contentType, null, -1);
    }

    /**
     * Put请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String put(String url, Object body, Map<String, String> header, String contentType, String charset, int timeoutMS) throws RTIOException {
        return request("PUT", url, body, header, contentType, charset, timeoutMS).result;
    }

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap putWrap(String url, Object body) throws RTIOException {
        return putWrap(url, body, null, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap putWrap(String url, Object body, Map<String, String> header) throws RTIOException {
        return putWrap(url, body, header, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap putWrap(String url, Object body, String contentType) throws RTIOException {
        return putWrap(url, body, null, contentType, null, -1);
    }

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap putWrap(String url, Object body, Map<String, String> header, String contentType, String charset, int timeoutMS)
            throws RTIOException {
        return request("PUT", url, body, header, contentType, charset, timeoutMS);
    }

    /**
     * Patch请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String patch(String url, Object body) throws RTIOException {
        return patch(url, body, null, null, null, -1);
    }

    /**
     * Patch请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String patch(String url, Object body, Map<String, String> header) throws RTIOException {
        return patch(url, body, header, null, null, -1);
    }

    /**
     * Patch请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String patch(String url, Object body, String contentType) throws RTIOException {
        return patch(url, body, null, contentType, null, -1);
    }

    /**
     * Patch请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String patch(String url, Object body, Map<String, String> header, String contentType, String charset, int timeoutMS)
            throws RTIOException {
        return request("PATCH", url, body, header, contentType, charset, timeoutMS).result;
    }

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap patchWrap(String url, Object body) throws RTIOException {
        return patchWrap(url, body, null, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap patchWrap(String url, Object body, Map<String, String> header) throws RTIOException {
        return patchWrap(url, body, header, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap patchWrap(String url, Object body, String contentType) throws RTIOException {
        return patchWrap(url, body, null, contentType, null, -1);
    }

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap patchWrap(String url, Object body, Map<String, String> header, String contentType, String charset, int timeoutMS)
            throws RTIOException {
        return request("PATCH", url, body, header, contentType, charset, timeoutMS);
    }

    /**
     * Delete请求.
     *
     * @param url 请求url
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String delete(String url) throws RTIOException {
        return delete(url, null, null, null, -1);
    }

    /**
     * Delete请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String delete(String url, Map<String, String> header) throws RTIOException {
        return delete(url, header, null, null, -1);
    }

    /**
     * Delete请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String delete(String url, String contentType) throws RTIOException {
        return delete(url, null, contentType, null, -1);
    }

    /**
     * Delete请求.
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 string
     * @throws RTIOException the rtio exception
     */
    public String delete(String url, Map<String, String> header, String contentType, String charset, int timeoutMS) throws RTIOException {
        return request("DELETE", url, null, header, contentType, charset, timeoutMS).result;
    }

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url 请求url
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap deleteWrap(String url) throws RTIOException {
        return deleteWrap(url, null, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap deleteWrap(String url, Map<String, String> header) throws RTIOException {
        return deleteWrap(url, header, null, null, -1);
    }

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap deleteWrap(String url, String contentType) throws RTIOException {
        return deleteWrap(url, null, contentType, null, -1);
    }

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap deleteWrap(String url, Map<String, String> header, String contentType, String charset, int timeoutMS) throws RTIOException {
        return request("DELETE", url, null, header, contentType, charset, timeoutMS);
    }

    /**
     * Head请求.
     *
     * @param url 请求url
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> head(String url) throws RTIOException {
        return head(url, null, null, null, -1);
    }

    /**
     * Head请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> head(String url, Map<String, String> header) throws RTIOException {
        return head(url, header, null, null, -1);
    }

    /**
     * Head请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> head(String url, String contentType) throws RTIOException {
        return head(url, null, contentType, null, -1);
    }

    /**
     * Head请求.
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> head(String url, Map<String, String> header, String contentType, String charset, int timeoutMS)
            throws RTIOException {
        return request("HEAD", url, null, header, contentType, charset, timeoutMS).head;
    }

    /**
     * Options请求.
     *
     * @param url 请求url
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> options(String url) throws RTIOException {
        return options(url, null, null, null, -1);
    }

    /**
     * Options请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> options(String url, Map<String, String> header) throws RTIOException {
        return options(url, header, null, null, -1);
    }

    /**
     * Options请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> options(String url, String contentType) throws RTIOException {
        return options(url, null, contentType, null, -1);
    }

    /**
     * Options请求.
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeoutMS   超时时间
     * @return 请求结果 map
     * @throws RTIOException the rtio exception
     */
    public Map<String, List<String>> options(String url, Map<String, String> header, String contentType, String charset, int timeoutMS)
            throws RTIOException {
        return request("OPTIONS", url, null, header, contentType, charset, timeoutMS).head;
    }

    /**
     * 发起请求.
     *
     * @param method         http方法
     * @param url            请求url
     * @param body           请求体
     *                       如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                       如果content-type是multipart/form-data时，body只能是File格式
     *                       其它情况下，body可以是任意格式
     * @param header         请求Header
     * @param contentType    content-type
     * @param requestCharset 请求内容编码
     * @param timeoutMS      超时时间
     * @return 请求结果 ，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    public ResponseWrap request(String method, String url, Object body, Map<String, String> header, String contentType, String requestCharset,
                                int timeoutMS) throws RTIOException {
        if (header == null) {
            header = new HashMap<>();
        }
        if (body instanceof File && (contentType == null || contentType.isEmpty())) {
            contentType = $.mime.getContentType((File) body);
        } else if (contentType == null) {
            contentType = "application/json; charset=utf-8";
        }
        if (requestCharset == null) {
            requestCharset = "UTF-8";
        }
        HttpRequest.BodyPublisher entity = null;
        if (body != null) {
            try {
                switch (contentType.toLowerCase()) {
                    case "application/x-www-form-urlencoded":
                        if (body instanceof Map<?, ?>) {
                            String finalRequestCharset = requestCharset;
                            var strBody = ((Map<String, String>) body).entrySet().stream().map(entry -> {
                                var key = URLEncoder.encode(entry.getKey(), Charset.forName(finalRequestCharset));
                                var value = URLEncoder.encode(entry.getValue(), Charset.forName(finalRequestCharset));
                                return key + "=" + value;
                            }).collect(Collectors.joining("&"));
                            entity = HttpRequest.BodyPublishers.ofString(strBody, Charset.forName(requestCharset));

                        } else if (body instanceof String) {
                            entity = HttpRequest.BodyPublishers.ofString((String) body, Charset.forName(requestCharset));
                        } else {
                            throw new IllegalArgumentException("The body only support Map OR String types" + " when content type is "
                                    + "application/x-www-form-urlencoded");
                        }
                        break;
                    case "multipart/form-data":
                        header.put("Content-Transfer-Encoding", "binary");
                        var fileBody = (File) body;
                        MultiPartBodyPublisher publisher = new MultiPartBodyPublisher().addPart(fileBody.getName(), () -> {
                            try {
                                return new FileInputStream(fileBody);
                            } catch (FileNotFoundException e) {
                                throw new RTIOException(e);
                            }
                        }, fileBody.getName(), $.mime.getContentType(fileBody));
                        header.put("Content-Type", "multipart/form-data; boundary=" + publisher.getBoundary());
                        entity = publisher.build();
                        break;
                    default:
                        if (body instanceof String) {
                            entity = HttpRequest.BodyPublishers.ofString((String) body, Charset.forName(requestCharset));
                        } else if (body instanceof Integer || body instanceof Long || body instanceof Float || body instanceof Double
                                || body instanceof BigDecimal || body instanceof Boolean) {
                            entity = HttpRequest.BodyPublishers.ofString(body.toString(), Charset.forName(requestCharset));
                        } else if (body instanceof Date) {
                            entity = HttpRequest.BodyPublishers.ofString(((Date) body).getTime() + "", Charset.forName(requestCharset));
                        } else if (body instanceof File) {
                            entity = HttpRequest.BodyPublishers.ofFile(((File) body).toPath());
                        } else if (body instanceof InputStream) {
                            entity = HttpRequest.BodyPublishers.fromPublisher(
                                    HttpRequest.BodyPublishers.ofInputStream(() -> (InputStream) body),
                                    ((InputStream) body).available());
                        } else {
                            entity = HttpRequest.BodyPublishers.ofString($.json.toJsonString(body), Charset.forName(requestCharset));
                        }
                }
            } catch (IOException e) {
                throw new RTIOException(e);
            }
        }
        if (!header.containsKey("Content-Type")) {
            header.put("Content-Type", contentType);
        }
        if (preRequestFun != null) {
            var preRequestContext =
                    preRequestFun.apply(new PreRequestContext(method.toUpperCase(), url, entity, header, timeoutMS));
            method = preRequestContext.getMethod();
            url = preRequestContext.getUrl();
            entity = preRequestContext.getEntity();
            header = preRequestContext.getHeader();
            timeoutMS = preRequestContext.getTimeoutMS();
        }
        var builder = HttpRequest.newBuilder();
        switch (method.toUpperCase()) {
            case "GET":
                builder.GET();
                break;
            case "POST":
                builder.POST(entity);
                break;
            case "PUT":
                builder.PUT(entity);
                break;
            case "DELETE":
                builder.DELETE();
                break;
            case "HEAD":
                builder.method("HEAD", HttpRequest.BodyPublishers.noBody());
                break;
            case "OPTIONS":
                builder.method("OPTIONS", HttpRequest.BodyPublishers.noBody());
                break;
            case "TRACE":
                builder.method("TRACE", HttpRequest.BodyPublishers.noBody());
                break;
            case "PATCH":
                builder.method("PATCH", entity);
                break;
            default:
                throw new RTException("The method [" + method + "] is NOT exist.");
        }
        if (timeoutMS != -1 && timeoutMS != 0) {
            builder.timeout(Duration.ofMillis(timeoutMS));
        }
        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.setHeader(entry.getKey(), entry.getValue());
        }
        try {
            builder.uri(new URI(url));
        } catch (URISyntaxException e) {
            throw new RTException("The URL [" + url + "] is NOT valid.");
        }
        LOGGER.trace("HTTP [" + method + "]" + url);
        var httpRequest = builder.build();
        try {
            var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ResponseWrap responseWrap = new ResponseWrap();
            if (httpResponse.body() != null) {
                responseWrap.result = httpResponse.body();
            } else {
                responseWrap.result = "";
            }
            responseWrap.statusCode = httpResponse.statusCode();
            responseWrap.head = httpResponse.headers().map();
            return responseWrap;
        } catch (IOException | InterruptedException e) {
            LOGGER.warn("HTTP [" + httpRequest.method() + "] " + url + " ERROR.");
            throw new RTIOException(e);
        }
    }

    private static class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /**
     * 前置处理上下文.
     *
     * @author gudaoxuri
     */
    public static class PreRequestContext {

        private String method;
        private String url;
        private HttpRequest.BodyPublisher entity;
        private Map<String, String> header;
        private int timeoutMS;

        private PreRequestContext(String method, String url, HttpRequest.BodyPublisher entity, Map<String, String> header, int timeoutMS) {
            this.method = method;
            this.url = url;
            this.entity = entity;
            this.header = header;
            this.timeoutMS = timeoutMS;
        }

        /**
         * Gets method.
         *
         * @return the method
         */
        public String getMethod() {
            return method;
        }

        /**
         * Sets method.
         *
         * @param method the method
         */
        public void setMethod(String method) {
            this.method = method;
        }

        /**
         * Gets url.
         *
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * Sets url.
         *
         * @param url the url
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * Gets entity.
         *
         * @return the entity
         */
        public HttpRequest.BodyPublisher getEntity() {
            return entity;
        }

        /**
         * Sets entity.
         *
         * @param entity the entity
         */
        public void setEntity(HttpRequest.BodyPublisher entity) {
            this.entity = entity;
        }

        /**
         * Gets header.
         *
         * @return the header
         */
        public Map<String, String> getHeader() {
            return header;
        }

        /**
         * Sets header.
         *
         * @param header the header
         */
        public void setHeader(Map<String, String> header) {
            this.header = header;
        }

        /**
         * Gets timeout ms.
         *
         * @return the timeout ms
         */
        public int getTimeoutMS() {
            return timeoutMS;
        }

        /**
         * Sets timeout ms.
         *
         * @param timeoutMS the timeout ms
         */
        public void setTimeoutMS(int timeoutMS) {
            this.timeoutMS = timeoutMS;
        }
    }

    /**
     * 返回结果封装.
     *
     * @author gudaoxuri
     */
    public static class ResponseWrap {
        /**
         * The Status code.
         */
        public int statusCode;
        /**
         * The Result.
         */
        public String result;
        /**
         * The Head.
         */
        public Map<String, List<String>> head;
    }

    /**
     * From https://stackoverflow.com/questions/46392160/java-9-httpclient-send-a-multipart-form-data-request .
     */
    static class MultiPartBodyPublisher {
        private final List<PartsSpecification> partsSpecificationList = new ArrayList<>();
        private final String boundary = UUID.randomUUID().toString();

        /**
         * Build http request . body publisher.
         *
         * @return the http request . body publisher
         */
        public HttpRequest.BodyPublisher build() {
            if (partsSpecificationList.size() == 0) {
                throw new IllegalStateException("Must have at least one part to build multipart message.");
            }
            addFinalBoundaryPart();
            return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
        }

        /**
         * Gets boundary.
         *
         * @return the boundary
         */
        public String getBoundary() {
            return boundary;
        }

        /**
         * Add part multi part body publisher.
         *
         * @param name  the name
         * @param value the value
         * @return the multi part body publisher
         */
        public MultiPartBodyPublisher addPart(String name, String value) {
            PartsSpecification newPart = new PartsSpecification();
            newPart.type = PartsSpecification.TYPE.STRING;
            newPart.name = name;
            newPart.value = value;
            partsSpecificationList.add(newPart);
            return this;
        }

        /**
         * Add part multi part body publisher.
         *
         * @param name  the name
         * @param value the value
         * @return the multi part body publisher
         */
        public MultiPartBodyPublisher addPart(String name, Path value) {
            PartsSpecification newPart = new PartsSpecification();
            newPart.type = PartsSpecification.TYPE.FILE;
            newPart.name = name;
            newPart.path = value;
            partsSpecificationList.add(newPart);
            return this;
        }

        /**
         * Add part multi part body publisher.
         *
         * @param name        the name
         * @param value       the value
         * @param filename    the filename
         * @param contentType the content type
         * @return the multi part body publisher
         */
        public MultiPartBodyPublisher addPart(String name, Supplier<InputStream> value, String filename, String contentType) {
            PartsSpecification newPart = new PartsSpecification();
            newPart.type = PartsSpecification.TYPE.STREAM;
            newPart.name = name;
            newPart.stream = value;
            newPart.filename = filename;
            newPart.contentType = contentType;
            partsSpecificationList.add(newPart);
            return this;
        }

        private void addFinalBoundaryPart() {
            PartsSpecification newPart = new PartsSpecification();
            newPart.type = PartsSpecification.TYPE.FINAL_BOUNDARY;
            newPart.value = "--" + boundary + "--";
            partsSpecificationList.add(newPart);
        }

        /**
         * The type Parts specification.
         */
        static class PartsSpecification {

            private PartsSpecification.TYPE type;
            private String name;
            private String value;
            private Path path;
            private Supplier<InputStream> stream;
            private String filename;
            private String contentType;

            /**
             * The enum Type.
             */
            public enum TYPE {
                /**
                 * String type.
                 */
                STRING,
                /**
                 * File type.
                 */
                FILE,
                /**
                 * Stream type.
                 */
                STREAM,
                /**
                 * Final boundary type.
                 */
                FINAL_BOUNDARY
            }

        }

        /**
         * The type Parts iterator.
         */
        class PartsIterator implements Iterator<byte[]> {

            private final Iterator<PartsSpecification> iter;
            private InputStream currentFileInput;

            private boolean done;
            private byte[] next;

            /**
             * Instantiates a new Parts iterator.
             */
            PartsIterator() {
                iter = partsSpecificationList.iterator();
            }

            @Override
            public boolean hasNext() {
                if (done) {
                    return false;
                }
                if (next != null) {
                    return true;
                }
                try {
                    next = computeNext();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                if (next == null) {
                    done = true;
                    return false;
                }
                return true;
            }

            @Override
            public byte[] next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                byte[] res = next;
                next = null;
                return res;
            }

            private byte[] computeNext() throws IOException {
                if (currentFileInput == null) {
                    if (!iter.hasNext()) {
                        return null;
                    }
                    PartsSpecification nextPart = iter.next();
                    if (PartsSpecification.TYPE.STRING.equals(nextPart.type)) {
                        String part = "--" + boundary + "\r\n" + "Content-Disposition: form-data; name=" + nextPart.name + "\r\n" + "Content-Type: " +
                                "text/plain; charset=UTF-8\r\n\r\n" + nextPart.value + "\r\n";
                        return part.getBytes(StandardCharsets.UTF_8);
                    }
                    if (PartsSpecification.TYPE.FINAL_BOUNDARY.equals(nextPart.type)) {
                        return nextPart.value.getBytes(StandardCharsets.UTF_8);
                    }
                    String filename;
                    String contentType;
                    if (PartsSpecification.TYPE.FILE.equals(nextPart.type)) {
                        Path path = nextPart.path;
                        filename = path.getFileName().toString();
                        contentType = Files.probeContentType(path);
                        if (contentType == null) {
                            contentType = "application/octet-stream";
                        }
                        currentFileInput = Files.newInputStream(path);
                    } else {
                        filename = nextPart.filename;
                        contentType = nextPart.contentType;
                        if (contentType == null) {
                            contentType = "application/octet-stream";
                        }
                        currentFileInput = nextPart.stream.get();
                    }
                    String partHeader =
                            "--" + boundary + "\r\n" + "Content-Disposition: form-data; name="
                                    + nextPart.name + "; filename=" + filename + "\r\n" + "Content-Type: " + contentType + "\r\n\r\n";
                    return partHeader.getBytes(StandardCharsets.UTF_8);
                } else {
                    byte[] buf = new byte[8192];
                    int r = currentFileInput.read(buf);
                    if (r > 0) {
                        byte[] actualBytes = new byte[r];
                        System.arraycopy(buf, 0, actualBytes, 0, r);
                        return actualBytes;
                    } else {
                        currentFileInput.close();
                        currentFileInput = null;
                        return "\r\n".getBytes(StandardCharsets.UTF_8);
                    }
                }
            }
        }
    }
}
