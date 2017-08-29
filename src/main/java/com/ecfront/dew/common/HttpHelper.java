package com.ecfront.dew.common;

import java.io.IOException;
import java.util.Map;

public interface HttpHelper {

    String get(String url) throws IOException;

    String get(String url, Map<String, String> header) throws IOException;

    String get(String url, String contentType) throws IOException;

    /**
     * Get请求
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     */
    String get(String url, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    ResponseWrap getWrap(String url) throws IOException;

    ResponseWrap getWrap(String url, Map<String, String> header) throws IOException;

    ResponseWrap getWrap(String url, String contentType) throws IOException;

    /**
     * 包含返回扩展信息的Get请求
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     */
    ResponseWrap getWrap(String url, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    String post(String url, Object body) throws IOException;

    String post(String url, Object body, Map<String, String> header) throws IOException;

    String post(String url, Object body, String contentType) throws IOException;

    /**
     * Post请求
     *
     * @param url         请求url
     * @param body        请求体，用于post、put
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     * @return 返回结果
     */
    String post(String url, Object body, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    ResponseWrap postWrap(String url, Object body) throws IOException;

    ResponseWrap postWrap(String url, Object body, Map<String, String> header) throws IOException;

    ResponseWrap postWrap(String url, Object body, String contentType) throws IOException;

    /**
     * 包含返回扩展信息的Post请求
     *
     * @param url         请求url
     * @param body        请求体，用于post、put
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     * @return 返回结果
     */
    ResponseWrap postWrap(String url, Object body, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    String put(String url, Object body) throws IOException;

    String put(String url, Object body, Map<String, String> header) throws IOException;

    String put(String url, Object body, String contentType) throws IOException;

    /**
     * Put请求
     *
     * @param url         请求url
     * @param body        请求体，用于post、put
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     * @return 返回结果
     */
    String put(String url, Object body, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    ResponseWrap putWrap(String url, Object body) throws IOException;

    ResponseWrap putWrap(String url, Object body, Map<String, String> header) throws IOException;

    ResponseWrap putWrap(String url, Object body, String contentType) throws IOException;

    /**
     * 包含返回扩展信息的Put请求
     *
     * @param url         请求url
     * @param body        请求体，用于post、put
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     * @return 返回结果
     */
    ResponseWrap putWrap(String url, Object body, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    String delete(String url) throws IOException;

    String delete(String url, Map<String, String> header) throws IOException;

    String delete(String url, String contentType) throws IOException;

    /**
     * Delete请求
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     */
    String delete(String url, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    ResponseWrap deleteWrap(String url) throws IOException;

    ResponseWrap deleteWrap(String url, Map<String, String> header) throws IOException;

    ResponseWrap deleteWrap(String url, String contentType) throws IOException;

    /**
     * 包含返回扩展信息的Delete请求
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     */
    ResponseWrap deleteWrap(String url, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    Map<String, String> head(String url) throws IOException;

    Map<String, String> head(String url, Map<String, String> header) throws IOException;

    Map<String, String> head(String url, String contentType) throws IOException;

    /**
     * Head请求
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     */
    Map<String, String> head(String url, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    Map<String, String> options(String url) throws IOException;

    Map<String, String> options(String url, Map<String, String> header) throws IOException;

    Map<String, String> options(String url, String contentType) throws IOException;

    /**
     * Options请求
     *
     * @param url         请求url
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     */
    Map<String, String> options(String url, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    /**
     * 发起请求
     *
     * @param method      http方法
     * @param url         请求url
     * @param body        请求体，用于post、put
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param header      请求头
     * @param contentType content-type
     * @param charset     请求与返回内容编码
     * @param timeout     connectTimeout和socketTimeout超时毫秒数，默认不超时
     * @return 返回结果
     */
    ResponseWrap request(String method, String url, Object body, Map<String, String> header, String contentType, String charset, int timeout) throws IOException;

    class ResponseWrap {
        public int statusCode;
        public String result;
        public Map<String, String> head;
    }
}
