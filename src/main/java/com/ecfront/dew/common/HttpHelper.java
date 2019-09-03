/*
 * Copyright 2019. the original author or authors.
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

import com.ecfront.dew.common.exception.RTIOException;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The interface Http helper.
 *
 * @author gudaoxuri
 */
public interface HttpHelper {

    /**
     * 设置请求前置拦截器.
     *
     * @param <T>           传入参数类型
     * @param preRequestFun 拦截处理方法
     */
    <T> void setPreRequest(Consumer<T> preRequestFun);

    /**
     * Get请求.
     *
     * @param url 请求url
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String get(String url) throws RTIOException;

    /**
     * Get请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String get(String url, Map<String, String> header) throws RTIOException;

    /**
     * Get请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String get(String url, String contentType) throws RTIOException;

    /**
     * Get请求.
     *
     * @param url              请求url
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String get(String url, Map<String, String> header, String contentType, String charset,
               int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url 请求url
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap getWrap(String url) throws RTIOException;

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap getWrap(String url, Map<String, String> header) throws RTIOException;

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap getWrap(String url, String contentType) throws RTIOException;

    /**
     * 包含返回扩展信息的Get请求.
     *
     * @param url              请求url
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap getWrap(String url, Map<String, String> header, String contentType, String charset,
                         int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * Post请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     *             \     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String post(String url, Object body) throws RTIOException;

    /**
     * Post请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是xml时，body只能是Document或Xml的String格式
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String post(String url, Object body, Map<String, String> header) throws RTIOException;

    /**
     * Post请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式     * @param contentType content-type
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String post(String url, Object body, String contentType) throws RTIOException;

    /**
     * Post请求.
     *
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String post(String url, Object body, Map<String, String> header, String contentType, String charset,
                int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap postWrap(String url, Object body) throws RTIOException;

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     *             、     * @param header 请求头
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap postWrap(String url, Object body, Map<String, String> header) throws RTIOException;

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap postWrap(String url, Object body, String contentType) throws RTIOException;

    /**
     * 包含返回扩展信息的Post请求.
     *
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap postWrap(String url, Object body, Map<String, String> header, String contentType, String charset,
                          int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * Put请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String put(String url, Object body) throws RTIOException;

    /**
     * Put请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是xml时，body只能是Document或Xml的String格式
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String put(String url, Object body, Map<String, String> header) throws RTIOException;

    /**
     * Put请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String put(String url, Object body, String contentType) throws RTIOException;

    /**
     * Put请求.
     *
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String put(String url, Object body, Map<String, String> header, String contentType, String charset,
               int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap putWrap(String url, Object body) throws RTIOException;

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是xml时，body只能是Document或Xml的String格式
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap putWrap(String url, Object body, Map<String, String> header) throws RTIOException;

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap putWrap(String url, Object body, String contentType) throws RTIOException;

    /**
     * 包含返回扩展信息的Put请求.
     *
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap putWrap(String url, Object body, Map<String, String> header, String contentType, String charset,
                         int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * Patch请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String patch(String url, Object body) throws RTIOException;

    /**
     * Patch请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是xml时，body只能是Document或Xml的String格式
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String patch(String url, Object body, Map<String, String> header) throws RTIOException;

    /**
     * Patch请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String patch(String url, Object body, String contentType) throws RTIOException;

    /**
     * Patch请求.
     *
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String patch(String url, Object body, Map<String, String> header, String contentType, String charset,
                 int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url  请求url
     * @param body 请求体
     *             如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *             如果content-type是xml时，body只能是Document或Xml的String格式
     *             如果content-type是multipart/form-data时，body只能是File格式
     *             其它情况下，body可以是任意格式
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap patchWrap(String url, Object body) throws RTIOException;

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url    请求url
     * @param body   请求体
     *               如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *               如果content-type是xml时，body只能是Document或Xml的String格式
     *               如果content-type是multipart/form-data时，body只能是File格式
     *               其它情况下，body可以是任意格式
     * @param header 请求头
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap patchWrap(String url, Object body, Map<String, String> header) throws RTIOException;

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url         请求url
     * @param body        请求体
     *                    如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                    如果content-type是xml时，body只能是Document或Xml的String格式
     *                    如果content-type是multipart/form-data时，body只能是File格式
     *                    其它情况下，body可以是任意格式
     * @param contentType content-type
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap patchWrap(String url, Object body, String contentType) throws RTIOException;

    /**
     * 包含返回扩展信息的Patch请求.
     *
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap patchWrap(String url, Object body, Map<String, String> header, String contentType, String charset,
                           int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * Delete请求.
     *
     * @param url 请求url
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String delete(String url) throws RTIOException;

    /**
     * Delete请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String delete(String url, Map<String, String> header) throws RTIOException;

    /**
     * Delete请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String delete(String url, String contentType) throws RTIOException;

    /**
     * Delete请求.
     *
     * @param url              请求url
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    String delete(String url, Map<String, String> header, String contentType, String charset,
                  int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url 请求url
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap deleteWrap(String url) throws RTIOException;

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap deleteWrap(String url, Map<String, String> header) throws RTIOException;

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap deleteWrap(String url, String contentType) throws RTIOException;

    /**
     * 包含返回扩展信息的Delete请求.
     *
     * @param url              请求url
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap deleteWrap(String url, Map<String, String> header, String contentType, String charset,
                            int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * Head请求.
     *
     * @param url 请求url
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> head(String url) throws RTIOException;

    /**
     * Head请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> head(String url, Map<String, String> header) throws RTIOException;

    /**
     * Head请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> head(String url, String contentType) throws RTIOException;

    /**
     * Head请求.
     *
     * @param url              请求url
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> head(String url, Map<String, String> header, String contentType, String charset,
                                   int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * Options请求.
     *
     * @param url 请求url
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> options(String url) throws RTIOException;

    /**
     * Options请求.
     *
     * @param url    请求url
     * @param header 请求头
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> options(String url, Map<String, String> header) throws RTIOException;

    /**
     * Options请求.
     *
     * @param url         请求url
     * @param contentType content-type
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> options(String url, String contentType) throws RTIOException;

    /**
     * Options请求.
     *
     * @param url              请求url
     * @param header           请求头
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果
     * @throws RTIOException the rtio exception
     */
    Map<String, List<String>> options(String url, Map<String, String> header, String contentType, String charset,
                                      int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * 发起请求.
     *
     * @param method           http方法
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param contentType      content-type
     * @param charset          请求与返回内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 返回结果 response wrap
     * @throws RTIOException the rtio exception
     */
    ResponseWrap request(String method, String url, Object body,
                         Map<String, String> header, String contentType,
                         String charset, int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * 发起请求.
     *
     * @param method           http方法
     * @param url              请求url
     * @param body             请求体
     *                         如果content-type是application/x-www-form-urlencoded 且 body是map时，会以form形式提交，即视为表单内容
     *                         如果content-type是xml时，body只能是Document或Xml的String格式
     *                         如果content-type是multipart/form-data时，body只能是File格式
     *                         其它情况下，body可以是任意格式
     * @param contentType      content-type
     * @param requestCharset   请求内容编码
     * @param responseCharset  返回内容编码，默认等于请求内容编码
     * @param connectTimeoutMS 连接超时时间
     * @param socketTimeoutMS  读取超时时间
     * @return 请求结果，包含扩展信息
     * @throws RTIOException the rtio exception
     */
    ResponseWrap request(String method, String url, Object body,
                         Map<String, String> header, String contentType,
                         String requestCharset, String responseCharset,
                         int connectTimeoutMS, int socketTimeoutMS) throws RTIOException;

    /**
     * The type Response wrap.
     *
     * @author gudaoxuri
     */
    class ResponseWrap {
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
}
