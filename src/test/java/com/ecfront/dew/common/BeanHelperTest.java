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

/**
 * The type Bean helper test.
 *
 * @author gudaoxuri
 */
public class BeanHelperTest {

    /**
     * Copy properties.
     *
     * @throws Exception the exception
     */
    @Test
    public void copyProperties() throws Exception {
        User ori = new User();
        ori.setName("张三");
        User dest = new User();
        dest.setAge(11);
        dest.setWorkAge(11);
        $.bean.copyProperties(dest, ori);
        Assert.assertTrue(Objects.equals(dest.getName(), "张三") && dest.getAge() == 0 && dest.getWorkAge() == 11);
    }

    /**
     * Find class annotation.
     */
    @Test
    public void findClassAnnotation() {
        TestAnnotation.RPC ann = $.bean.getClassAnnotation(IdxController.class, TestAnnotation.RPC.class);
        Assert.assertEquals("/idx/", ann.path());
    }

    /**
     * Find field info.
     */
    @Test
    public void findFieldInfo() {
        Map<String, BeanHelper.FieldInfo> fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, null, null, null);
        Assert.assertEquals(2, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, new HashSet<Class<? extends Annotation>>() {
            {
                add(Deprecated.class);
            }
        }, null, null);
        Assert.assertEquals(1, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, new HashSet<String>() {
            {
                add("parentField");
            }
        }, new HashSet<Class<? extends Annotation>>() {
            {
                add(Deprecated.class);
            }
        }, null, null);
        Assert.assertEquals(0, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, null, null, new HashSet<Class<? extends Annotation>>() {
            {
                add(Deprecated.class);
            }
        });
        Assert.assertEquals(1, fieldsInfo.size());
        fieldsInfo = $.bean.findFieldsInfo(IdxController.class, null, null, new HashSet<String>() {
            {
                add("parentField");
            }
        }, new HashSet<Class<? extends Annotation>>() {
            {
                add(Resource.class);
            }
        });
        Assert.assertEquals(1, fieldsInfo.size());
    }

    /**
     * Find method info.
     */
    @Test
    public void findMethodInfo() {
        List<BeanHelper.MethodInfo> methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, null, null, null);
        Assert.assertEquals(methodsInfo.size(), 7);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, new HashSet<Class<? extends Annotation>>() {
            {
                add(TestAnnotation.GET.class);
            }
        }, null, null);
        Assert.assertEquals(methodsInfo.size(), 6);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, new HashSet<String>() {
            {
                add("find");
            }
        }, new HashSet<Class<? extends Annotation>>() {
            {
                add(TestAnnotation.GET.class);
            }
        }, null, null);
        Assert.assertEquals(methodsInfo.size(), 4);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, null, null, new HashSet<Class<? extends Annotation>>() {
            {
                add(TestAnnotation.POST.class);
            }
        });
        Assert.assertEquals(methodsInfo.size(), 3);
        methodsInfo = $.bean.findMethodsInfo(IdxController.class, null, null, new HashSet<String>() {
            {
                add("childFind");
            }
        }, new HashSet<Class<? extends Annotation>>() {
            {
                add(TestAnnotation.POST.class);
            }
        });
        Assert.assertEquals(methodsInfo.size(), 1);
    }

    /**
     * Parse rel field and method.
     *
     * @throws Exception the exception
     */
    @Test
    public void parseRelFieldAndMethod() throws Exception {
        Map<String, Method[]> rel = $.bean.parseRelFieldAndMethod(User.class, null, null, null, null);
        Assert.assertEquals(6, rel.size());
        Assert.assertEquals(2, rel.get("enable").length);
    }

    /**
     * Find values by rel.
     *
     * @throws Exception the exception
     */
    @Test
    public void findValuesByRel() throws Exception {
        User user = new User();
        user.setName("张三");
        Map<String, Object> values = $.bean.findValuesByRel(user, $.bean.parseRelFieldAndMethod(User.class, null, null, null, null));
        Assert.assertEquals("张三", values.get("name"));
    }

    /**
     * Find values.
     *
     * @throws Exception the exception
     */
    @Test
    public void findValues() throws Exception {
        User user = new User();
        user.setName("张三");
        Map<String, Object> values = $.bean.findValues(user, null, null, null, null);
        Assert.assertEquals("张三", values.get("name"));
    }

    /**
     * Gets value.
     *
     * @throws Exception the exception
     */
    @Test
    public void getValue() throws Exception {
        User user = new User();
        user.setName("张三");
        Assert.assertEquals("张三", $.bean.getValue(user, "name"));
        user.setName("李四");
        Assert.assertEquals("李四", $.bean.getValue(user, "name"));
    }

    /**
     * Sets value.
     *
     * @throws Exception the exception
     */
    @Test
    public void setValue() throws Exception {
        User user = new User();
        $.bean.setValue(user, "name", "李四");
        $.bean.setValue(user, "name", "张三");
        Assert.assertEquals("张三", user.getName());
    }

}
