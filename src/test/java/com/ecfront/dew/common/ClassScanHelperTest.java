package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassScanHelperTest {

    @Test
    public void scan() throws Exception {
        Set<Class<?>> resultInFile = DEW.clazz.scan("com.ecfront.dew.common.test", new HashSet<Class<? extends Annotation>>() {{
            add(Deprecated.class);
        }}, null);
        Assert.assertEquals(2, resultInFile.size());
        Set<Class<?>> resultInJar = DEW.clazz.scan("org.junit", null, new HashSet<String>() {{
            add("Before\\w*");
        }});
        Assert.assertEquals(4, resultInJar.size());
    }

}