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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The type Json helper test.
 *
 * @author gudaoxuri
 */
public class JsonHelperTest {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Test path.
     *
     * @throws Exception the exception
     */
    @Test
    public void testPath() throws Exception {
        JsonNode jsonNode = $.json.toJson("{'a_key':'a_val','child':{'c_key':'c_val'}}");
        Assert.assertEquals("a_val", $.json.path(jsonNode, "a_key").asText());
        Assert.assertEquals("c_val", $.json.path(jsonNode, "child.c_key").asText());
    }

    /**
     * To json string.
     *
     * @throws Exception the exception
     */
    @Test
    public void toJsonString() throws Exception {
        Assert.assertEquals(
                "{\"\":{\"a_key\":\"a_val\"}}",
                $.json.toJsonString($.json.createObjectNode().set("", $.json.createObjectNode().put("a_key", "a_val"))));
    }

    /**
     * To json.
     *
     * @throws Exception the exception
     */
    @Test
    public void toJson() throws Exception {
        Assert.assertEquals("a_val",
                $.json.toJson("{'a_key':'a_val'}").get("a_key").asText());
        Assert.assertEquals("a_val",
                $.json.toJson("{\r\n'a_key':'a_val' // 注释\r\n}").get("a_key").asText());
    }

    /**
     * To list.
     *
     * @throws Exception the exception
     */
    @Test
    public void toList() throws Exception {
        TestIdModel model =
                $.json.toList("[{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}]",
                        TestIdModel.class).get(0);
        Assert.assertEquals("sunisle", model.getName());
        Assert.assertEquals("1", model.getCid());
        Assert.assertEquals("123456789", model.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(model.getDate()));
    }

    /**
     * To set.
     *
     * @throws Exception the exception
     */
    @Test
    public void toSet() throws Exception {
        TestIdModel model =
                $.json.toSet("[{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}]",
                        TestIdModel.class).iterator().next();
        Assert.assertEquals("sunisle", model.getName());
        Assert.assertEquals("1", model.getCid());
        Assert.assertEquals("123456789", model.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(model.getDate()));
    }

    /**
     * To map.
     *
     * @throws Exception the exception
     */
    @Test
    public void toMap() throws Exception {
        Map<String, TestIdModel> model =
                $.json.toMap("{'sunisle':{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}}",
                        String.class, TestIdModel.class);
        Assert.assertEquals("sunisle", model.keySet().iterator().next());
        TestIdModel val = model.get("sunisle");
        Assert.assertEquals("1", val.getCid());
        Assert.assertEquals("123456789", val.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(val.getDate()));
    }

    /**
     * To object.
     *
     * @throws Exception the exception
     */
    @Test
    public void toObject() throws Exception {
        TestIdModel model = $.json.toObject("{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}", TestIdModel.class);
        Assert.assertEquals("sunisle", model.getName());
        Assert.assertEquals("1", model.getCid());
        Assert.assertEquals("123456789", model.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(model.getDate()));
    }

    /**
     * To generic object.
     *
     * @throws Exception the exception
     */
    @Test
    public void toGenericObject() throws Exception {
        GenericModel model = $.json.toObject(
                "{'strs':['sunisle'],'exts':[{'createTime':'123456789','cid':'1'}],'extMap':{'a':{'createTime':'123456789','cid':'1'}}}",
                GenericModel.class);
        Assert.assertEquals("sunisle", model.getStrs().get(0));
        Assert.assertEquals("1", model.getExts().get(0).getCid());
        Assert.assertEquals("123456789", model.getExts().get(0).getCreateTime());
        Assert.assertEquals("1", model.getExtMap().get("a").getCid());
        Assert.assertEquals("123456789", model.getExtMap().get("a").getCreateTime());
    }

    /**
     * Test local date time.
     */
    @Test
    public void testLocalDateTime() {
        TestIdModel model = new TestIdModel();
        model.setLocalDateTime(LocalDateTime.now());
        model.setLocalDate(LocalDate.now());
        model.setLocalTime(LocalTime.now());
        TestIdModel model2 = $.json.toObject($.json.toJsonString(model), TestIdModel.class);
        Assert.assertTrue(model.getLocalDateTime().isEqual(model2.getLocalDateTime()));
        Assert.assertTrue(model.getLocalDate().isEqual(model2.getLocalDate()));
        Assert.assertEquals(model.getLocalTime().toString(), model2.getLocalTime().toString());
    }

    /**
     * Test optional.
     */
    @Test
    public void testOptional() {
        TestIdModel model = new TestIdModel();
        TestIdModel model2 = $.json.toObject($.json.toJsonString(model), TestIdModel.class);
        Assert.assertTrue(!model2.getOpt().isPresent());

        model = new TestIdModel();
        model.setOpt(Optional.ofNullable(null));
        model2 = $.json.toObject($.json.toJsonString(model), TestIdModel.class);
        Assert.assertTrue(!model2.getOpt().isPresent());

        model = new TestIdModel();
        model.setOpt(Optional.of(new HashMap<>() {
            {
                put("h", "001");
            }
        }));
        model2 = $.json.toObject($.json.toJsonString(model), TestIdModel.class);
        Assert.assertEquals("001", model2.getOpt().get().get("h"));
    }

    /**
     * Custom mapper.
     */
    @Test
    public void customMapper() {
        // Normal operation
        Assert.assertEquals("1", $.json.toJson("{'a':'1'}").get("a").asText());
        // Custom Mapper operation
        $.json("otherInst").getMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);
        try {
            $.json("otherInst").toJson("{'a':'1'}");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("com.fasterxml.jackson.core.JsonParseException: Unexpected character"));
        }
    }

}
