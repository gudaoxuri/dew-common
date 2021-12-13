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

package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Test path.
     */
    @Test
    public void testPath() {
        JsonNode jsonNode = $.json.toJson("{'a_key':'a_val','child':{'c_key':'c_val'}}");
        Assertions.assertEquals("a_val", $.json.path(jsonNode, "a_key").asText());
        Assertions.assertEquals("c_val", $.json.path(jsonNode, "child.c_key").asText());
    }

    /**
     * To json string.
     */
    @Test
    public void toJsonString() {
        Assertions.assertEquals("{\"\":{\"a_key\":\"a_val\"}}",
                $.json.toJsonString($.json.createObjectNode().set("", $.json.createObjectNode().put("a_key", "a_val"))));
    }

    /**
     * To json.
     */
    @Test
    public void toJson() {
        Assertions.assertEquals("a_val", $.json.toJson("{'a_key':'a_val'}").get("a_key").asText());
        Assertions.assertEquals("a_val", $.json.toJson("{\r\n'a_key':'a_val' // 注释\r\n}").get("a_key").asText());
    }

    /**
     * To list.
     */
    @Test
    public void toList() {
        TestIdModel model =
                $.json.toList("[{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}]", TestIdModel.class).get(0);
        Assertions.assertEquals("sunisle", model.getName());
        Assertions.assertEquals("1", model.getCid());
        Assertions.assertEquals("123456789", model.getCreateTime());
        Assertions.assertEquals("2016-07-12 12:00:00", DF.format(model.getDate()));
    }

    /**
     * To set.
     */
    @Test
    public void toSet() {
        TestIdModel model =
                $.json.toSet("[{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}]", TestIdModel.class)
                        .iterator().next();
        Assertions.assertEquals("sunisle", model.getName());
        Assertions.assertEquals("1", model.getCid());
        Assertions.assertEquals("123456789", model.getCreateTime());
        Assertions.assertEquals("2016-07-12 12:00:00", DF.format(model.getDate()));
    }

    /**
     * To map.
     */
    @Test
    public void toMap() {
        Map<String, TestIdModel> model =
                $.json.toMap("{'sunisle':{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}}",
                        String.class, TestIdModel.class);
        Assertions.assertEquals("sunisle", model.keySet().iterator().next());
        TestIdModel val = model.get("sunisle");
        Assertions.assertEquals("1", val.getCid());
        Assertions.assertEquals("123456789", val.getCreateTime());
        Assertions.assertEquals("2016-07-12 12:00:00", DF.format(val.getDate()));
    }

    /**
     * To object.
     */
    @Test
    public void toObject() {
        TestIdModel model = $.json.toObject("{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}", TestIdModel.class);
        Assertions.assertEquals("sunisle", model.getName());
        Assertions.assertEquals("1", model.getCid());
        Assertions.assertEquals("123456789", model.getCreateTime());
        Assertions.assertEquals("2016-07-12 12:00:00", DF.format(model.getDate()));
    }

    /**
     * To generic object.
     */
    @Test
    public void toGenericObject() {
        GenericModel model =
                $.json.toObject(
                        "{'strs':['sunisle'],'exts':[{'createTime':'123456789','cid':'1'}],'extMap':{'a':{'createTime':'123456789','cid':'1'}}}",
                        GenericModel.class);
        Assertions.assertEquals("sunisle", model.getStrs().get(0));
        Assertions.assertEquals("1", model.getExts().get(0).getCid());
        Assertions.assertEquals("123456789", model.getExts().get(0).getCreateTime());
        Assertions.assertEquals("1", model.getExtMap().get("a").getCid());
        Assertions.assertEquals("123456789", model.getExtMap().get("a").getCreateTime());
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
        Assertions.assertTrue(model.getLocalDateTime().isEqual(model2.getLocalDateTime()));
        Assertions.assertTrue(model.getLocalDate().isEqual(model2.getLocalDate()));
        Assertions.assertEquals(model.getLocalTime().toString(), model2.getLocalTime().toString());
    }

    /**
     * Test optional.
     */
    @Test
    public void testOptional() {
        TestIdModel model = new TestIdModel();
        TestIdModel model2 = $.json.toObject($.json.toJsonString(model), TestIdModel.class);
        Assertions.assertFalse(model2.getOpt().isPresent());

        model = new TestIdModel();
        model.setOpt(Optional.empty());
        model2 = $.json.toObject($.json.toJsonString(model), TestIdModel.class);
        Assertions.assertFalse(model2.getOpt().isPresent());

        model = new TestIdModel();
        model.setOpt(Optional.of(new HashMap<>() {
            {
                put("h", "001");
            }
        }));
        model2 = $.json.toObject($.json.toJsonString(model), TestIdModel.class);
        Assertions.assertEquals("001", model2.getOpt().get().get("h"));
    }

    /**
     * Custom mapper.
     */
    @Test
    public void customMapper() {
        // Normal operation
        Assertions.assertEquals("1", $.json.toJson("{'a':'1'}").get("a").asText());
        // Custom Mapper operation
        $.json("otherInst").getMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);
        try {
            $.json("otherInst").toJson("{'a':'1'}");
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertTrue(e.getMessage().contains("com.fasterxml.jackson.core.JsonParseException: Unexpected character"));
        }
    }

}
