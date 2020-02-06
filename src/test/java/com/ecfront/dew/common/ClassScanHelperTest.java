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

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Class scan helper test.
 *
 * @author gudaoxuri
 */
public class ClassScanHelperTest {

    /**
     * Scan.
     *
     * @throws Exception the exception
     */
    @Test
    public void scan() throws Exception {
        Set<Class<?>> resultInFile = $.clazz.scan("com.ecfront.dew.common.test", new HashSet<Class<? extends Annotation>>() {
            {
                add(Deprecated.class);
            }
        }, null);
        Assert.assertEquals(2, resultInFile.size());
        Set<Class<?>> resultInJar = $.clazz.scan("org.junit", null, new HashSet<>() {
            {
                add("Before\\w*");
            }
        });
        Assert.assertEquals(4, resultInJar.size());
    }

}
