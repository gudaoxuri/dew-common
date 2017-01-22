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
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BeanHelperTest {

    @Test
    public void copyProperties() throws Exception {
        User ori = new User();
        ori.setName("张三");
        User dest = new User();
        dest.setAge(11);
        dest.setWorkAge(11);
        BeanHelper.copyProperties(dest, ori);
        Assert.assertTrue(Objects.equals(dest.getName(), "张三") && dest.getAge() == 0 && dest.getWorkAge()==11);
    }

    @Test
    public void findClassAnnotation() throws Exception {
        TestAnnotation.RPC ann = BeanHelper.getClassAnnotation(IdxController.class, TestAnnotation.RPC.class);
        Assert.assertEquals(ann.path(), "/idx/");
    }

    @Test
    public void findFieldInfo() throws Exception {
        Map<String, FieldInfo> fieldsInfo= BeanHelper.findFieldsInfo(IdxController.class,null,null,null,null);
        Assert.assertEquals(fieldsInfo.size(),2);
        fieldsInfo=BeanHelper.findFieldsInfo(IdxController.class,null,new HashSet<Class<? extends Annotation>>(){{add(Deprecated.class);}},null,null);
        Assert.assertEquals(fieldsInfo.size(),1);
        fieldsInfo=BeanHelper.findFieldsInfo(IdxController.class,new HashSet<String>(){{add("parentField");}},new HashSet<Class<? extends Annotation>>(){{add(Deprecated.class);}},null,null);
        Assert.assertEquals(fieldsInfo.size(),0);
        fieldsInfo=BeanHelper.findFieldsInfo(IdxController.class,null,null,null,new HashSet<Class<? extends Annotation>>(){{add(Deprecated.class);}});
        Assert.assertEquals(fieldsInfo.size(),1);
        fieldsInfo=BeanHelper.findFieldsInfo(IdxController.class,null,null,new HashSet<String>(){{add("parentField");}},new HashSet<Class<? extends Annotation>>(){{add(Resource.class);}});
        Assert.assertEquals(fieldsInfo.size(),1);
    }

    @Test
    public void findMethodInfo() throws Exception {
        Set<MethodInfo> methodsInfo= BeanHelper.findMethodsInfo(IdxController.class,null,null,null,null);
        Assert.assertEquals(methodsInfo.size(),7);
        methodsInfo=BeanHelper.findMethodsInfo(IdxController.class,null,new HashSet<Class<? extends Annotation>>(){{add(TestAnnotation.GET.class);}},null,null);
        Assert.assertEquals(methodsInfo.size(),6);
        methodsInfo=BeanHelper.findMethodsInfo(IdxController.class,new HashSet<String>(){{add("find");}},new HashSet<Class<? extends Annotation>>(){{add(TestAnnotation.GET.class);}},null,null);
        Assert.assertEquals(methodsInfo.size(),4);
        methodsInfo=BeanHelper.findMethodsInfo(IdxController.class,null,null,null,new HashSet<Class<? extends Annotation>>(){{add(TestAnnotation.POST.class);}});
        Assert.assertEquals(methodsInfo.size(),3);
        methodsInfo=BeanHelper.findMethodsInfo(IdxController.class,null,null,new HashSet<String>(){{add("childFind");}},new HashSet<Class<? extends Annotation>>(){{add(TestAnnotation.POST.class);}});
        Assert.assertEquals(methodsInfo.size(),1);
    }

    @Test
    public void parseRelFieldAndMethod() throws Exception {
        Map<String, Method[]> rel =  BeanHelper.parseRelFieldAndMethod(User.class,null,null,null,null);
        Assert.assertEquals(rel.size(),6);
        Assert.assertEquals(rel.get("enable").length,2);
    }

    @Test
    public void findValuesByRel() throws Exception {
        User user=new User();
        user.setName("张三");
        Map<String, Object> values= BeanHelper.findValuesByRel(user,BeanHelper.parseRelFieldAndMethod(User.class,null,null,null,null));
        Assert.assertEquals(values.get("name"),"张三");
    }

    @Test
    public void findValues() throws Exception {
        User user=new User();
        user.setName("张三");
        Map<String, Object> values=BeanHelper.findValues(user,null,null,null,null);
        Assert.assertEquals(values.get("name"),"张三");
    }

    @Test
    public void getValue() throws Exception {
        User user=new User();
        user.setName("张三");
        Assert.assertEquals(BeanHelper.getValue(user,"name"),"张三");
    }

    @Test
    public void setValue() throws Exception {
        User user=new User();
        BeanHelper.setValue(user,"name","张三");
        Assert.assertEquals(user.getName(),"张三");
    }

}