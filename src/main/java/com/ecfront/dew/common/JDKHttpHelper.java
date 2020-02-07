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
import com.ecfront.dew.common.exception.RTIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.*;
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
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.joox.JOOX.$;

/**
 * HTTP操作.
 *
 * @author gudaoxuri
 */
public class JDKHttpHelper implements HttpHelper {

    private static final Logger logger = LoggerFactory.getLogger(JDKHttpHelper.class);

    private HttpClient httpClient;
    private int defaultConnectTimeoutMS = -1;
    private int defaultSocketTimeoutMS = -1;
    private Consumer preRequestFun;

    /**
     * 初始化.
     *
     * @param defaultTimeoutMS 默认超时时间
     * @param autoRedirect     302状态下是否自动跳转
     */
    JDKHttpHelper(int defaultTimeoutMS, boolean autoRedirect) {
        try {
            var httpClientBuild = HttpClient.newBuilder()
                    .followRedirects(autoRedirect ? HttpClient.Redirect.ALWAYS : HttpClient.Redirect.NEVER)
                    .sslContext(SSLContext.getDefault())
                    .sslParameters(new SSLParameters());
            if (defaultTimeoutMS != -1 && defaultTimeoutMS != 0) {
                httpClientBuild.connectTimeout(Duration.ofMillis(defaultTimeoutMS));
            }
            httpClient = httpClientBuild.build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void setPreRequest(Consumer<T> preRequestFun) {
        this.preRequestFun = preRequestFun;
    }

    @Override
    public String get(String url) throws RTIOException {
        return get(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String get(String url, Map<String, String> header) throws RTIOException {
        return get(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String get(String url, String contentType) throws RTIOException {
        return get(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String get(String url, Map<String, String> header, String contentType, String charset,
                      int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("GET", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap getWrap(String url) throws RTIOException {
        return getWrap(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap getWrap(String url, Map<String, String> header) throws RTIOException {
        return getWrap(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap getWrap(String url, String contentType) throws RTIOException {
        return getWrap(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap getWrap(String url, Map<String, String> header, String contentType, String charset,
                                int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("GET", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public String post(String url, Object body) throws RTIOException {
        return post(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String post(String url, Object body, Map<String, String> header) throws RTIOException {
        return post(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String post(String url, Object body, String contentType) throws RTIOException {
        return post(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String post(String url, Object body, Map<String, String> header, String contentType, String charset,
                       int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("POST", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap postWrap(String url, Object body) throws RTIOException {
        return postWrap(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap postWrap(String url, Object body, Map<String, String> header) throws RTIOException {
        return postWrap(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap postWrap(String url, Object body, String contentType) throws RTIOException {
        return postWrap(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap postWrap(String url, Object body, Map<String, String> header, String contentType, String charset,
                                 int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("POST", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public String put(String url, Object body) throws RTIOException {
        return put(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String put(String url, Object body, Map<String, String> header) throws RTIOException {
        return put(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String put(String url, Object body, String contentType) throws RTIOException {
        return put(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String put(String url, Object body, Map<String, String> header, String contentType, String charset,
                      int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("PUT", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap putWrap(String url, Object body) throws RTIOException {
        return putWrap(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap putWrap(String url, Object body, Map<String, String> header) throws RTIOException {
        return putWrap(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap putWrap(String url, Object body, String contentType) throws RTIOException {
        return putWrap(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap putWrap(String url, Object body, Map<String, String> header, String contentType, String charset,
                                int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("PUT", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public String patch(String url, Object body) throws RTIOException {
        return patch(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String patch(String url, Object body, Map<String, String> header) throws RTIOException {
        return patch(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String patch(String url, Object body, String contentType) throws RTIOException {
        return patch(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String patch(String url, Object body, Map<String, String> header, String contentType, String charset,
                        int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("PATCH", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap patchWrap(String url, Object body) throws RTIOException {
        return patchWrap(url, body, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap patchWrap(String url, Object body, Map<String, String> header) throws RTIOException {
        return patchWrap(url, body, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap patchWrap(String url, Object body, String contentType) throws RTIOException {
        return patchWrap(url, body, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap patchWrap(String url, Object body, Map<String, String> header, String contentType, String charset,
                                  int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("PATCH", url, body, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public String delete(String url) throws RTIOException {
        return delete(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String delete(String url, Map<String, String> header) throws RTIOException {
        return delete(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String delete(String url, String contentType) throws RTIOException {
        return delete(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public String delete(String url, Map<String, String> header, String contentType, String charset,
                         int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("DELETE", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).result;
    }

    @Override
    public ResponseWrap deleteWrap(String url) throws RTIOException {
        return deleteWrap(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap deleteWrap(String url, Map<String, String> header) throws RTIOException {
        return deleteWrap(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap deleteWrap(String url, String contentType) throws RTIOException {
        return deleteWrap(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public ResponseWrap deleteWrap(String url, Map<String, String> header, String contentType, String charset,
                                   int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("DELETE", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public Map<String, List<String>> head(String url) throws RTIOException {
        return head(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, List<String>> head(String url, Map<String, String> header) throws RTIOException {
        return head(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, List<String>> head(String url, String contentType) throws RTIOException {
        return head(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, List<String>> head(String url, Map<String, String> header, String contentType, String charset,
                                          int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("HEAD", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).head;
    }

    @Override
    public Map<String, List<String>> options(String url) throws RTIOException {
        return options(url, null, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, List<String>> options(String url, Map<String, String> header) throws RTIOException {
        return options(url, header, null, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, List<String>> options(String url, String contentType) throws RTIOException {
        return options(url, null, contentType, null, defaultConnectTimeoutMS, defaultSocketTimeoutMS);
    }

    @Override
    public Map<String, List<String>> options(String url, Map<String, String> header, String contentType, String charset,
                                             int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request("OPTIONS", url, null, header, contentType, charset, connectTimeoutMS, socketTimeoutMS).head;
    }

    @Override
    public ResponseWrap request(String method, String url, Object body,
                                Map<String, String> header, String contentType,
                                String charset, int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request(method, url, body, header, contentType, charset, charset, connectTimeoutMS, socketTimeoutMS);
    }

    @Override
    public ResponseWrap request(String method, String url, Object body,
                                Map<String, String> header, String contentType,
                                String requestCharset, String responseCharset,
                                int connectTimeoutMS, int socketTimeoutMS) throws RTIOException {
        return request(method, url, body, header, contentType, requestCharset, responseCharset, connectTimeoutMS, socketTimeoutMS, 0);
    }

    private ResponseWrap request(String method, String url, Object body,
                                 Map<String, String> header, String contentType,
                                 String requestCharset, String responseCharset,
                                 int connectTimeoutMS, int socketTimeoutMS, int retry) throws RTIOException {
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
                            var strBody = ((Map<String, String>) body).entrySet().stream()
                                    .map(entry -> {
                                        var key = URLEncoder.encode(entry.getKey(), Charset.forName(finalRequestCharset));
                                        var value = URLEncoder.encode(entry.getValue(), Charset.forName(finalRequestCharset));
                                        return key + "=" + value;
                                    })
                                    .collect(Collectors.joining("&"));
                            entity = HttpRequest.BodyPublishers.ofString(strBody, Charset.forName(requestCharset));

                        } else if (body instanceof String) {
                            entity = HttpRequest.BodyPublishers.ofString((String) body, Charset.forName(requestCharset));
                        } else {
                            throw new IllegalArgumentException("The body only support Map OR String types"
                                    + " when content type is application/x-www-form-urlencoded");
                        }
                        break;
                    case "xml":
                        if (body instanceof Document) {
                            entity = HttpRequest.BodyPublishers.ofString($((Document) body).toString(), Charset.forName(requestCharset));
                        } else if (body instanceof String) {
                            entity = HttpRequest.BodyPublishers.ofString((String) body, Charset.forName(requestCharset));
                        } else {
                            logger.error("Not support return type [" + body.getClass().getName() + "] by xml");
                            entity = HttpRequest.BodyPublishers.ofString("", Charset.forName(requestCharset));
                        }
                        break;
                    case "multipart/form-data":
                        header.put("Content-Transfer-Encoding", "binary");
                        var fileBody = (File) body;
                        MultiPartBodyPublisher publisher = new MultiPartBodyPublisher()
                                .addPart(fileBody.getName(), () -> {
                                    try {
                                        return new FileInputStream(fileBody);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                }, fileBody.getName(), $.mime.getContentType(fileBody));
                        header.put("Content-Type", "multipart/form-data; boundary=" + publisher.getBoundary());
                        entity = publisher.build();
                        break;
                    default:
                        if (body instanceof String) {
                            entity = HttpRequest.BodyPublishers.ofString((String) body, Charset.forName(requestCharset));
                        } else if (body instanceof Integer || body instanceof Long || body instanceof Float
                                || body instanceof Double || body instanceof BigDecimal || body instanceof Boolean) {
                            entity = HttpRequest.BodyPublishers.ofString(body.toString(), Charset.forName(requestCharset));
                        } else if (body instanceof Date) {
                            entity = HttpRequest.BodyPublishers.ofString(((Date) body).getTime() + "", Charset.forName(requestCharset));
                        } else if (body instanceof File) {
                            entity = HttpRequest.BodyPublishers.ofFile(((File) body).toPath());
                        } else {
                            entity = HttpRequest.BodyPublishers.ofString($.json.toJsonString(body), Charset.forName(requestCharset));
                        }
                }
            } catch (IOException e) {
                throw new RTIOException(e);
            }
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
                builder.method("TRACE", entity);
                break;
            default:
                throw new RTException("The method [" + method + "] is NOT exist.");
        }
        if (connectTimeoutMS != -1 && connectTimeoutMS != 0) {
            builder.timeout(Duration.ofMillis(connectTimeoutMS));
        }

        for (Map.Entry<String, String> entry : header.entrySet()) {
            builder.setHeader(entry.getKey(), entry.getValue());
        }
        if (!header.containsKey("Content-Type")) {
            builder.setHeader("Content-Type", contentType);
        }
        try {
            builder.uri(new URI(url));
        } catch (URISyntaxException e) {
            throw new RTException("The URL [" + url + "] is NOT valid.");
        }
        logger.trace("HTTP [" + method + "]" + url);
        if (preRequestFun != null) {
            preRequestFun.accept(builder);
        }
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
            logger.warn("HTTP [" + httpRequest.method() + "] " + url + " ERROR.");
            throw new RTIOException(e);
        }
    }

    /**
     * From https://stackoverflow.com/questions/46392160/java-9-httpclient-send-a-multipart-form-data-request .
     */
    static class MultiPartBodyPublisher {
        private List<PartsSpecification> partsSpecificationList = new ArrayList<>();
        private String boundary = UUID.randomUUID().toString();

        public HttpRequest.BodyPublisher build() {
            if (partsSpecificationList.size() == 0) {
                throw new IllegalStateException("Must have at least one part to build multipart message.");
            }
            addFinalBoundaryPart();
            return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
        }

        public String getBoundary() {
            return boundary;
        }

        public MultiPartBodyPublisher addPart(String name, String value) {
            PartsSpecification newPart = new PartsSpecification();
            newPart.type = PartsSpecification.TYPE.STRING;
            newPart.name = name;
            newPart.value = value;
            partsSpecificationList.add(newPart);
            return this;
        }

        public MultiPartBodyPublisher addPart(String name, Path value) {
            PartsSpecification newPart = new PartsSpecification();
            newPart.type = PartsSpecification.TYPE.FILE;
            newPart.name = name;
            newPart.path = value;
            partsSpecificationList.add(newPart);
            return this;
        }

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

        static class PartsSpecification {

            public enum TYPE {
                STRING, FILE, STREAM, FINAL_BOUNDARY
            }

            private PartsSpecification.TYPE type;
            private String name;
            private String value;
            private Path path;
            private Supplier<InputStream> stream;
            private String filename;
            private String contentType;

        }

        class PartsIterator implements Iterator<byte[]> {

            private Iterator<PartsSpecification> iter;
            private InputStream currentFileInput;

            private boolean done;
            private byte[] next;

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
                        String part =
                                "--" + boundary + "\r\n" +
                                        "Content-Disposition: form-data; name=" + nextPart.name + "\r\n" +
                                        "Content-Type: text/plain; charset=UTF-8\r\n\r\n" +
                                        nextPart.value + "\r\n";
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
                            "--" + boundary + "\r\n" +
                                    "Content-Disposition: form-data; name=" + nextPart.name + "; filename=" + filename + "\r\n" +
                                    "Content-Type: " + contentType + "\r\n\r\n";
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
