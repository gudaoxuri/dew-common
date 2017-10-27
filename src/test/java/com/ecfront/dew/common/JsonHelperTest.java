package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class JsonHelperTest {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void toJsonString() throws Exception {
        Assert.assertEquals(
                "{\"\":{\"a_key\":\"a_val\"}}",
                $.json.toJsonString($.json.createObjectNode().set("", $.json.createObjectNode().put("a_key", "a_val"))));
    }

    @Test
    public void toJson() throws Exception {
        Assert.assertEquals("a_val",
                $.json.toJson("{'a_key':'a_val'}").get("a_key").asText());
        Assert.assertEquals("a_val",
                $.json.toJson("{\r\n'a_key':'a_val' // 注释\r\n}").get("a_key").asText());
    }

    @Test
    public void toList() throws Exception {
        TestIdModel model = $.json.toList("[{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}]", TestIdModel.class).get(0);
        Assert.assertEquals("sunisle", model.getName());
        Assert.assertEquals("1", model.getCid());
        Assert.assertEquals("123456789", model.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(model.getDate()));
    }

    @Test
    public void toSet() throws Exception {
        TestIdModel model = $.json.toSet("[{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}]", TestIdModel.class).iterator().next();
        Assert.assertEquals("sunisle", model.getName());
        Assert.assertEquals("1", model.getCid());
        Assert.assertEquals("123456789", model.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(model.getDate()));
    }

    @Test
    public void toMap() throws Exception {
        Map<String,TestIdModel> model = $.json.toMap("{'sunisle':{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}}",String.class, TestIdModel.class);
        Assert.assertEquals("sunisle", model.keySet().iterator().next());
        TestIdModel val=model.get("sunisle");
        Assert.assertEquals("1", val.getCid());
        Assert.assertEquals("123456789", val.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(val.getDate()));
    }

    @Test
    public void toObject() throws Exception {
        TestIdModel model = $.json.toObject("{'name':'sunisle','createTime':123456789,'cid':'1','date':'2016-07-12 12:00:00'}", TestIdModel.class);
        Assert.assertEquals("sunisle", model.getName());
        Assert.assertEquals("1", model.getCid());
        Assert.assertEquals("123456789", model.getCreateTime());
        Assert.assertEquals("2016-07-12 12:00:00", df.format(model.getDate()));
    }

    @Test
    public void toGenericObject() throws Exception {
        GenericModel model = $.json.toObject("{'strs':['sunisle'],'exts':[{'createTime':'123456789','cid':'1'}],'extMap':{'a':{'createTime':'123456789','cid':'1'}}}", GenericModel.class);
        Assert.assertEquals("sunisle", model.getStrs().get(0));
        Assert.assertEquals("1", model.getExts().get(0).getCid());
        Assert.assertEquals("123456789", model.getExts().get(0).getCreateTime());
        Assert.assertEquals("1", model.getExtMap().get("a").getCid());
        Assert.assertEquals("123456789", model.getExtMap().get("a").getCreateTime());
    }

    @Test
    public void testLocalDateTime(){
        TestIdModel model=new TestIdModel();
        model.setLocalDateTime(LocalDateTime.now());
        model.setLocalDate(LocalDate.now());
        model.setLocalTime(LocalTime.now());
        TestIdModel model2 = $.json.toObject($.json.toJsonString(model),TestIdModel.class);
        Assert.assertTrue(model.getLocalDateTime().isEqual(model2.getLocalDateTime()));
        Assert.assertTrue(model.getLocalDate().isEqual(model2.getLocalDate()));
        Assert.assertEquals(model.getLocalTime().toString(),model2.getLocalTime().toString());
    }

}