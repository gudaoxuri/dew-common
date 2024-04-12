package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.HttpHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Http helper test.
 *
 * @author gudaoxuri
 */
public class HttpHelperTest {

    private String currentPath;

    {
        currentPath = HttpHelperTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(currentPath);
        currentPath = file.getPath();
        if (file.isFile()) {
            currentPath = file.getParentFile().getPath();
        }
        System.out.println("Current Path:" + currentPath);
    }

    /**
     * Test http.
     */
    @Test
    public void testHttp() {
        // get
        String result = $.http.get("https://httpbin.org/get");
        Assertions.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        result = $.http.get("https://httpbin.org/get", new HashMap<>() {
            {
                put("Customer-A", "AAA");
                put("Accept", "json");
                put("Date", "xx");
            }
        });
        Assertions.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assertions.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        result = $.http.get("https://httpbin.org/get", new HashMap<>() {
            {
                put("Customer-A", "AAA");
                put("Accept", "json");
            }
        }, "application/json; charset=utf-8", "utf-8", 5000);
        Assertions.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assertions.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        Assertions.assertEquals("application/json; charset=utf-8", $.json.toJson(result).get("headers").get("Content-Type").asText());
        // delete
        result = $.http.delete("https://httpbin.org/delete");
        Assertions.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        // post - data
        result = $.http.post("https://httpbin.org/post", "some data");
        Assertions.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // post - form
        result = $.http.post("https://httpbin.org/post", new HashMap<>() {
            {
                put("a", "1");
            }
        }, "application/x-www-form-urlencoded");
        result = $.http.post("https://httpbin.org/post", "custname=%E5%8C%BF%E5%90%8D&size=small&topping=cheese&topping=onion", "application/x-www-form-urlencoded");
        Assertions.assertEquals("匿名", $.json.toJson(result).get("form").get("custname").asText());
        Assertions.assertEquals("small", $.json.toJson(result).get("form").get("size").asText());
        Assertions.assertEquals("onion", $.json.toJson(result).get("form").get("topping").get(1).asText());
        // post - file
        result = $.http.post("https://httpbin.org/post", new File(currentPath + "/conf1.json"), "multipart/form-data");
        Assertions.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        result = $.http.post("https://httpbin.org/post", new File(currentPath + "/conf1.json"));
        Assertions.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());
        // put - data
        result = $.http.put("https://httpbin.org/put", "some data");
        Assertions.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // put - form
        result = $.http.put("https://httpbin.org/put", new HashMap<>() {
            {
                put("a", "1");
            }
        }, "application/x-www-form-urlencoded");
        Assertions.assertEquals("1", $.json.toJson(result).get("form").get("a").asText());
        // put - file
        result = $.http.put("https://httpbin.org/put", new File(currentPath + "/conf1.json"));
        Assertions.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());
        // put with head
        HttpHelper.ResponseWrap responseWrap = $.http.putWrap("https://httpbin.org/put", new File(currentPath + "/conf1.json"));
        Assertions.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());
        Assertions.assertEquals("application/json", responseWrap.head.get("Content-Type").get(0));
        // head
        Map<String, List<String>> head = $.http.head("https://httpbin.org/get");
        Assertions.assertEquals("application/json", head.get("Content-Type").get(0));
        // options
        head = $.http.options("https://httpbin.org/get");
        Assertions.assertTrue(head.get("Allow").get(0).contains("GET"));
        // patch - data
        result = $.http.patch("https://httpbin.org/patch", new HashMap<>() {
            {
                put("Customer-A", "AAA");
                put("Accept", "json");
            }
        });
        Assertions.assertEquals("AAA", $.json.toJson($.json.toJson(result).get("data").asText()).get("Customer-A").asText());
        Assertions.assertEquals("json", $.json.toJson($.json.toJson(result).get("data").asText()).get("Accept").asText());

        result = $.http.post("https://httpbin.org/post", new File(currentPath + "/conf1.json"));
        Assertions.assertEquals("1", $.json.toJson(result).get("json").get("a").asText());

    }

}
