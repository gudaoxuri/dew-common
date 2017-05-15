package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;

public class JsonHelperTest {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toJsonString() throws Exception {
        Assert.assertEquals(
                JsonHelper.toJsonString(JsonHelper.createObjectNode().set("", JsonHelper.createObjectNode().put("a_key", "a_val"))),
                "{\"\":{\"a_key\":\"a_val\"}}");
    }

    @Test
    public void toJson() throws Exception {
        Assert.assertEquals(JsonHelper.toJson("{'a_key':'a_val'}").get("a_key").asText(), "a_val");
        Assert.assertEquals(JsonHelper.toJson("{\r\n'a_key':'a_val' // 注释\r\n}").get("a_key").asText(), "a_val");
    }

    @Test
    public void toList() throws Exception {
        TestIdModel model = JsonHelper.toList("[{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}]", TestIdModel.class).get(0);
        Assert.assertEquals(model.getName(), "sunisle");
        Assert.assertEquals(model.getCid(), "1");
        Assert.assertEquals(model.getCreateTime(), "123456789");
        Assert.assertEquals(df.format(model.getDate()), "2016-07-12 12:00:00");
    }

    @Test
    public void toObject() throws Exception {
        TestIdModel model = JsonHelper.toObject("{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}", TestIdModel.class);
        Assert.assertEquals(model.getName(), "sunisle");
        Assert.assertEquals(model.getCid(), "1");
        Assert.assertEquals(model.getCreateTime(), "123456789");
        Assert.assertEquals(df.format(model.getDate()), "2016-07-12 12:00:00");
    }

}