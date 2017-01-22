package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ClassScanHelperTest {

    @Test
    public void scan() throws Exception {
        Set<Class<?>> resultInFile = ClassScanHelper.scan("com.ecfront.dew.common.test", new HashSet<Class<? extends Annotation>>(){{add(Deprecated.class);}},null);
        Assert.assertEquals(resultInFile.size(),2);
        Set<Class<?>> resultInJar = ClassScanHelper.scan("org.junit",null, new HashSet<String>(){{add("Before\\w*");}});
        Assert.assertEquals(resultInJar.size(),4);
    }

}