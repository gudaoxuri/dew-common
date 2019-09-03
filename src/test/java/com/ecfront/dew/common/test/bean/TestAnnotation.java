package com.ecfront.dew.common.test.bean;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The type Test annotation.
 *
 * @author gudaoxuri
 */
public class TestAnnotation {

    /**
     * The interface Rpc.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface RPC {
        /**
         * Path string.
         *
         * @return the string
         */
        String path();
    }

    /**
     * The interface Post.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface POST {
        /**
         * Path string.
         *
         * @return the string
         */
        String path();
    }

    /**
     * The interface Get.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface GET {
        /**
         * Path string.
         *
         * @return the string
         */
        String path();
    }

}
