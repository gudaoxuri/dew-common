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

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Http helper test.
 *
 * @author gudaoxuri
 */
public class HttpHelperTest {

    /**
     * Test http.
     *
     * @throws Exception the exception
     */
    @Test
    public void testHttp() {
        doTestHttp($.http);
        doTestHttp($.http(HttpHelperFactory.BACKEND.JDK));
    }

    private void doTestHttp(HttpHelper httpHelper) {
        // get
        String result = httpHelper.get("https://httpbin.org/get");
        Assert.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        result = httpHelper.get("https://httpbin.org/get", new HashMap<>() {
            {
                put("Customer-A", "AAA");
                put("Accept", "json");
            }
        });
        Assert.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assert.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        result = httpHelper.get("https://httpbin.org/get", new HashMap<>() {
            {
                put("Customer-A", "AAA");
                put("Accept", "json");
            }
        }, "application/json; charset=utf-8", "utf-8", 5000, 5000);
        Assert.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assert.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        Assert.assertEquals("application/json; charset=utf-8", $.json.toJson(result).get("headers").get("Content-Type").asText());
        // delete
        result = httpHelper.delete("https://httpbin.org/delete");
        Assert.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        // post - data
        result = httpHelper.post("https://httpbin.org/post", "some data");
        Assert.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // post - form
        result = httpHelper.post("https://httpbin.org/post", new HashMap<>() {
            {
                put("a", "1");
            }
        }, "application/x-www-form-urlencoded");
        result = httpHelper.post("https://httpbin.org/post", "custname=%E5%8C%BF%E5%90%8D&size=small&topping=cheese&topping=onion", "application/x-www-form-urlencoded");
        Assert.assertEquals("匿名", $.json.toJson(result).get("form").get("custname").asText());
        Assert.assertEquals("small", $.json.toJson(result).get("form").get("size").asText());
        Assert.assertEquals("onion", $.json.toJson(result).get("form").get("topping").get(1).asText());
        // post - file
        result = httpHelper.post("https://httpbin.org/post",
                new File(this.getClass().getResource("/").getPath() + "conf1.json"), "multipart/form-data");
        Assert.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        result = httpHelper.post("https://httpbin.org/post", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());
        // put - data
        result = httpHelper.put("https://httpbin.org/put", "some data");
        Assert.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // put - form
        result = httpHelper.put("https://httpbin.org/put", new HashMap<>() {
            {
                put("a", "1");
            }
        }, "application/x-www-form-urlencoded");
        Assert.assertEquals("1", $.json.toJson(result).get("form").get("a").asText());
        // put - file
        result = httpHelper.put("https://httpbin.org/put", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());
        // put with head
        HttpHelper.ResponseWrap responseWrap = httpHelper.putWrap("https://httpbin.org/put", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());
        Assert.assertEquals("application/json", responseWrap.head.get("Content-Type").get(0));
        // head
        Map<String, List<String>> head = httpHelper.head("https://httpbin.org/get");
        Assert.assertEquals("application/json", head.get("Content-Type").get(0));
        // options
        head = httpHelper.options("https://httpbin.org/get");
        Assert.assertTrue(head.get("Allow").get(0).contains("GET"));

        httpHelper.setPreRequest(request -> {
            if (request instanceof HttpRequestBase) {
                ((HttpRequestBase) request).setHeader("X-Date", "sssss");
            } else {
                ((HttpRequest.Builder) request).setHeader("X-Date", "sssss");
            }
        });
        result = httpHelper.post("https://httpbin.org/post", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());

    }

}
