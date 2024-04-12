package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.BeanHelper;
import com.ecfront.dew.common.test.bean.IdxController;
import com.ecfront.dew.common.test.bean.TestAnnotation;
import com.ecfront.dew.common.test.bean.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Bean helper test.
 *
 * @author gudaoxuri
 */
public class BeanHelperTest {

    /**
     * Copy properties.
     */
    @Test
    public void copyProperties() {
        User ori = new User();
        ori.setName("张三");
        User dest = new User();
        dest.setAge(11);
        dest.setWorkAge(11);
        $.bean.copyProperties(dest, ori);
        Assertions.assertTrue(Objects.equals(dest.getName(), "张三") && dest.getAge() == 0 && dest.getWorkAge() == 11);
    }

    /**
     * Find class annotation.
     */
    @Test
    public void findClassAnnotation() {
        TestAnnotation.RPC ann = $.bean.getClassAnnotation(IdxController.class, TestAnnotation.RPC.class);
        Assertions.assertEquals("/idx/", ann.path());
    }

    /**
     * Find field info.
     */
    @Test
    public void findFieldInfo() {
        Map<String, BeanHelper.FieldInfo> fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, null, null, null);
        Assertions.assertEquals(2, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, new HashSet<>() {
            {
                add(Deprecated.class);
            }
        }, null, null);
        Assertions.assertEquals(1, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, new HashSet<>() {
            {
                add("parentField");
            }
        }, new HashSet<>() {
            {
                add(Deprecated.class);
            }
        }, null, null);
        Assertions.assertEquals(0, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, null, null, new HashSet<>() {
            {
                add(Deprecated.class);
            }
        });
        Assertions.assertEquals(1, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, null, new HashSet<>() {
            {
                add("parentField");
            }
        }, new HashSet<>() {
            {
                add(TestAnnotation.RPC.class);
            }
        });
        Assertions.assertEquals(1, fieldsInfo.size());
    }

    /**
     * Find method info.
     */
    @Test
    public void findMethodInfo() {
        List<BeanHelper.MethodInfo> methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, null, null, null);
        Assertions.assertEquals(methodsInfo.size(), 7);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, new HashSet<>() {
            {
                add(TestAnnotation.GET.class);
            }
        }, null, null);
        Assertions.assertEquals(methodsInfo.size(), 6);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, new HashSet<>() {
            {
                add("find");
            }
        }, new HashSet<>() {
            {
                add(TestAnnotation.GET.class);
            }
        }, null, null);
        Assertions.assertEquals(methodsInfo.size(), 4);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, null, null, new HashSet<>() {
            {
                add(TestAnnotation.POST.class);
            }
        });
        Assertions.assertEquals(methodsInfo.size(), 3);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, null, new HashSet<>() {
            {
                add("childFind");
            }
        }, new HashSet<>() {
            {
                add(TestAnnotation.POST.class);
            }
        });
        Assertions.assertEquals(methodsInfo.size(), 1);
    }

    /**
     * Parse rel field and method.
     */
    @Test
    public void parseRelFieldAndMethod() {
        Map<String, Method[]> rel = $.bean.parseRelFieldAndMethod(User.class, null, null, null, null);
        Assertions.assertEquals(6, rel.size());
        Assertions.assertEquals(2, rel.get("enable").length);
    }

    /**
     * Find values by rel.
     */
    @Test
    public void findValuesByRel() {
        User user = new User();
        user.setName("张三");
        Map<String, Object> values = $.bean.findValuesByRel(user, $.bean.parseRelFieldAndMethod(User.class, null, null, null, null));
        Assertions.assertEquals("张三", values.get("name"));
    }

    /**
     * Find values.
     */
    @Test
    public void findValues() {
        User user = new User();
        user.setName("张三");
        Map<String, Object> values = $.bean.findValues(user, null, null, null, null);
        Assertions.assertEquals("张三", values.get("name"));
    }

    /**
     * Gets value.
     */
    @Test
    public void getValue() {
        User user = new User();
        user.setName("张三");
        Assertions.assertEquals("张三", $.bean.getValue(user, "name"));
        user.setName("李四");
        Assertions.assertEquals("李四", $.bean.getValue(user, "name"));
    }

    /**
     * Sets value.
     */
    @Test
    public void setValue() {
        User user = new User();
        $.bean.setValue(user, "name", "李四");
        $.bean.setValue(user, "name", "张三");
        Assertions.assertEquals("张三", user.getName());
    }

}
