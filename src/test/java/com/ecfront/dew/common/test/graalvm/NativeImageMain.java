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

package com.ecfront.dew.common.test.graalvm;

import com.ecfront.dew.common.test.*;

/**
 * The type Native image main.
 *
 * @author gudaoxuri
 */
public class NativeImageMain {

    private NativeImageMain() {
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(NativeImageMain.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.println("Test by Main...");
        new AmountHelperTest().testConvert();
        new AvgTest().test();
        new BeanHelperTest().copyProperties();
        new BeanHelperTest().findClassAnnotation();
        new BeanHelperTest().findFieldInfo();
        new BeanHelperTest().findMethodInfo();
        new BeanHelperTest().findValues();
        new BeanHelperTest().findValuesByRel();
        new BeanHelperTest().parseRelFieldAndMethod();
        new BeanHelperTest().getValue();
        new BeanHelperTest().setValue();
        //new ClassScanHelperTest().scan();
        new FallbackHelperTest().testGetUser();
        new FallbackHelperTest().testGetUserWithoutSomeException();
        new FallbackHelperTest().testGetUserWithStrategy();
        new FieldHelperTest().testField();
        new FileHelperTest().testFile();
        new HttpHelperTest().testHttp();
        new InterceptorTest().testInterceptor();
        new JsonHelperTest().testPath();
        new JsonHelperTest().toJsonString();
        new JsonHelperTest().toJson();
        new JsonHelperTest().toList();
        new JsonHelperTest().toSet();
        new JsonHelperTest().toMap();
        new JsonHelperTest().toObject();
        new JsonHelperTest().toGenericObject();
        new JsonHelperTest().testLocalDateTime();
        new JsonHelperTest().testOptional();
        new JsonHelperTest().customMapper();
        new ScriptHelperTest().testScript();
        new SecurityHelperTest().digest();
        new SecurityHelperTest().symmetric();
        new SecurityHelperTest().asymmetric();
        new ShellHelperTest().test();
        new TimerHelperTest().testTimer();
        System.out.println("Test finished.");
    }

}
