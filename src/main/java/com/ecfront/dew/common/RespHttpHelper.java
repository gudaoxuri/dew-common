package com.ecfront.dew.common;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Resp封装的Http操作 TBD
 */
public class RespHttpHelper {

    private static final Logger logger = LoggerFactory.getLogger(RespHttpHelper.class);

    RespHttpHelper() {
        this("", null);
    }

    RespHttpHelper(String baseUrl) {
        this(baseUrl, null);
    }

    /**
     * @param baseUrl   基础URL，如果操作中不包含host时会加上此url
     * @param tokenFlag Token标识，作为key加到query中
     */
    RespHttpHelper(String baseUrl, String tokenFlag) {
        this.baseUrl = baseUrl;
        this.tokenFlag = tokenFlag;
    }

    private String baseUrl;
    private String tokenFlag;

    private String currToken = "";

    /**
     * 设置token值
     *
     * @param token token值
     */
    public void setToken(String token) {
        currToken = token;
    }

    public <E> Resp<E> get(String url, Class<E> respClazz) throws Exception {
        return get(url, null, respClazz);
    }

    public <E> Resp<E> get(String url, Map<String, String> header, Class<E> respClazz) throws Exception {
        return get(url, header, null, null, 0, respClazz);
    }

    public <E> Resp<E> get(String url, Map<String, String> header, String contentType, String charset, int timeout, Class<E> respClazz) throws Exception {
        return request("GET", url, null, header, contentType, charset, timeout, respClazz);
    }

    public <E> Resp<E> post(String url, Object body, Class<E> respClazz) throws Exception {
        return post(url, body, null, respClazz);
    }

    public <E> Resp<E> post(String url, Object body, Map<String, String> header, Class<E> respClazz) throws Exception {
        return post(url, body, header, null, null, 0, respClazz);
    }

    public <E> Resp<E> post(String url, Object body, Map<String, String> header, String contentType, String charset, int timeout, Class<E> respClazz) throws Exception {
        return request("POST", url, body, header, contentType, charset, timeout, respClazz);
    }

    public <E> Resp<E> put(String url, Object body, Class<E> respClazz) throws Exception {
        return put(url, body, null, respClazz);
    }

    public <E> Resp<E> put(String url, Object body, Map<String, String> header, Class<E> respClazz) throws Exception {
        return put(url, body, header, null, null, 0, respClazz);
    }

    public <E> Resp<E> put(String url, Object body, Map<String, String> header, String contentType, String charset, int timeout, Class<E> respClazz) throws Exception {
        return request("PUT", url, body, header, contentType, charset, timeout, respClazz);
    }

    public <E> Resp<E> delete(String url, Class<E> respClazz) throws Exception {
        return delete(url, null, respClazz);
    }

    public <E> Resp<E> delete(String url, Map<String, String> header, Class<E> respClazz) throws Exception {
        return delete(url, header, null, null, 0, respClazz);
    }

    public <E> Resp<E> delete(String url, Map<String, String> header, String contentType, String charset, int timeout, Class<E> respClazz) throws Exception {
        return request("DELETE", url, null, header, contentType, charset, timeout, respClazz);
    }

    private <E> Resp<E> request(String method, String url, Object body, Map<String, String> header, String contentType, String charset, int timeout, Class<E> respClazz) throws Exception {
        if (!url.toLowerCase().startsWith("http://")) {
            if (!baseUrl.endsWith("/") && !url.startsWith("/")) {
                url = "/" + url;
            }
            if (tokenFlag == null) {
                url = baseUrl + url;
            } else {
                if (url.contains("?")) {
                    url = baseUrl + url + "&" + tokenFlag + "=" + currToken;
                } else {
                    url = baseUrl + url + "?" + tokenFlag + "=" + currToken;
                }
            }
        }
        HttpHelper.WrapHead wrapHead = $.http.request(method, url, body, header, contentType, charset, timeout, 0);
        JsonNode json = $.json.toJson(wrapHead.result);
        String code = json.get("code").asText();
        if (code.equals(StandardCode.SUCCESS.toString())) {
            JsonNode jsonBody = json.get("body");
            if (jsonBody == null) {
                return Resp.success(null);
            } else {
                return Resp.success($.json.toObject(jsonBody, respClazz));
            }
        } else {
            logger.warn("HTTP [" + method + "] " + url + " ERROR  <" + code + ">:" + json.get("message").asText());
            return new Resp<>(code, json.get("message").asText(), null);
        }
    }

}
