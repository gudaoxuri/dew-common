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
