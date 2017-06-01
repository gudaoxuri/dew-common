package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HttpHelperTest {

    @Test
    public void testHttp() throws Exception {
        // get
        String result = $.http.get("https://httpbin.org/get");
        Assert.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        result = $.http.get("https://httpbin.org/get", new HashMap<String, String>() {{
            put("Customer-A", "AAA");
            put("Accept", "json");
        }});
        Assert.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assert.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        result = $.http.get("https://httpbin.org/get", new HashMap<String, String>() {{
            put("Customer-A", "AAA");
            put("Accept", "json");
        }}, "application/json; charset=utf-8", "utf-8", 5000);
        Assert.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assert.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        Assert.assertEquals("application/json; charset=utf-8", $.json.toJson(result).get("headers").get("Content-Type").asText());
        // delete
        result = $.http.delete("https://httpbin.org/delete");
        Assert.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        // post - data
        result = $.http.post("https://httpbin.org/post", "some data");
        Assert.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // post - form
        result = $.http.post("https://httpbin.org/post", new HashMap<String, Object>() {{
            put("a", "1");
        }}, "application/x-www-form-urlencoded");
        Assert.assertEquals("1", $.json.toJson(result).get("form").get("a").asText());
        // post - file
        result = $.http.post("https://httpbin.org/post", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        // put - data
        result = $.http.put("https://httpbin.org/put", "some data");
        Assert.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // put - form
        result = $.http.put("https://httpbin.org/put", new HashMap<String, Object>() {{
            put("a", "1");
        }}, "application/x-www-form-urlencoded");
        Assert.assertEquals("1", $.json.toJson(result).get("form").get("a").asText());
        // put - file
        result = $.http.put("https://httpbin.org/put", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        // put with head
        HttpHelper.WrapHead wrapHead = $.http.putWithHead("https://httpbin.org/put", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        Assert.assertEquals("application/json", wrapHead.head.get("Content-Type"));
        // head
        Map<String, String> head = $.http.head("https://httpbin.org/get");
        Assert.assertEquals("application/json", head.get("Content-Type"));
        // options
        head = $.http.options("https://httpbin.org/get");
        Assert.assertEquals("OPTIONS, GET, HEAD", head.get("Allow"));
    }

}