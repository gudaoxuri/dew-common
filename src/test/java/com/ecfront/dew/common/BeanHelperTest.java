package com.ecfront.dew.common;

import com.ecfront.dew.common.test.bean.IdxController;
import com.ecfront.dew.common.test.bean.TestAnnotation;
import com.ecfront.dew.common.test.bean.User;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BeanHelperTest {

    @Test
    public void copyProperties() throws Exception {
        User ori = new User();
        ori.setName("张三");
        User dest = new User();
        dest.setAge(11);
        dest.setWorkAge(11);
        DEW.bean.copyProperties(dest, ori);
        Assert.assertTrue(Objects.equals(dest.getName(), "张三") && dest.getAge() == 0 && dest.getWorkAge() == 11);
    }

    @Test
    public void findClassAnnotation() throws Exception {
        TestAnnotation.RPC ann = DEW.bean.getClassAnnotation(IdxController.class, TestAnnotation.RPC.class);
        Assert.assertEquals("/idx/", ann.path());
    }

    @Test
    public void findFieldInfo() throws Exception {
        Map<String, BeanHelper.FieldInfo> fieldsInfo = DEW.bean.findFieldsInfo(IdxController.class, null, null, null, null);
        Assert.assertEquals(2, fieldsInfo.size());
        fieldsInfo = DEW.bean.findFieldsInfo(IdxController.class, null, new HashSet<Class<? extends Annotation>>() {{
            add(Deprecated.class);
        }}, null, null);
        Assert.assertEquals(1, fieldsInfo.size());
        fieldsInfo = DEW.bean.findFieldsInfo(IdxController.class, new HashSet<String>() {{
            add("parentField");
        }}, new HashSet<Class<? extends Annotation>>() {{
            add(Deprecated.class);
        }}, null, null);
        Assert.assertEquals(0, fieldsInfo.size());
        fieldsInfo = DEW.bean.findFieldsInfo(IdxController.class, null, null, null, new HashSet<Class<? extends Annotation>>() {{
            add(Deprecated.class);
        }});
        Assert.assertEquals(1, fieldsInfo.size());
        fieldsInfo = DEW.bean.findFieldsInfo(IdxController.class, null, null, new HashSet<String>() {{
            add("parentField");
        }}, new HashSet<Class<? extends Annotation>>() {{
            add(Resource.class);
        }});
        Assert.assertEquals(1, fieldsInfo.size());
    }

    @Test
    public void findMethodInfo() throws Exception {
        List<BeanHelper.MethodInfo> methodsInfo = DEW.bean.findMethodsInfo(IdxController.class, null, null, null, null);
        Assert.assertEquals(methodsInfo.size(), 7);
        methodsInfo = DEW.bean.findMethodsInfo(IdxController.class, null, new HashSet<Class<? extends Annotation>>() {{
            add(TestAnnotation.GET.class);
        }}, null, null);
        Assert.assertEquals(methodsInfo.size(), 6);
        methodsInfo = DEW.bean.findMethodsInfo(IdxController.class, new HashSet<String>() {{
            add("find");
        }}, new HashSet<Class<? extends Annotation>>() {{
            add(TestAnnotation.GET.class);
        }}, null, null);
        Assert.assertEquals(methodsInfo.size(), 4);
        methodsInfo = DEW.bean.findMethodsInfo(IdxController.class, null, null, null, new HashSet<Class<? extends Annotation>>() {{
            add(TestAnnotation.POST.class);
        }});
        Assert.assertEquals(methodsInfo.size(), 3);
        methodsInfo = DEW.bean.findMethodsInfo(IdxController.class, null, null, new HashSet<String>() {{
            add("childFind");
        }}, new HashSet<Class<? extends Annotation>>() {{
            add(TestAnnotation.POST.class);
        }});
        Assert.assertEquals(methodsInfo.size(), 1);
    }

    @Test
    public void parseRelFieldAndMethod() throws Exception {
        Map<String, Method[]> rel = DEW.bean.parseRelFieldAndMethod(User.class, null, null, null, null);
        Assert.assertEquals(6, rel.size());
        Assert.assertEquals(2, rel.get("enable").length);
    }

    @Test
    public void findValuesByRel() throws Exception {
        User user = new User();
        user.setName("张三");
        Map<String, Object> values = DEW.bean.findValuesByRel(user, DEW.bean.parseRelFieldAndMethod(User.class, null, null, null, null));
        Assert.assertEquals("张三", values.get("name"));
    }

    @Test
    public void findValues() throws Exception {
        User user = new User();
        user.setName("张三");
        Map<String, Object> values = DEW.bean.findValues(user, null, null, null, null);
        Assert.assertEquals("张三", values.get("name"));
    }

    @Test
    public void getValue() throws Exception {
        User user = new User();
        user.setName("张三");
        Assert.assertEquals("张三", DEW.bean.getValue(user, "name"));
    }

    @Test
    public void setValue() throws Exception {
        User user = new User();
        DEW.bean.setValue(user, "name", "张三");
        Assert.assertEquals("张三", user.getName());
    }

}