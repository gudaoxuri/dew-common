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

package com.ecfront.dew.common.test;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.exception.RTException;
import org.junit.Assert;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Objects;

/**
 * The type Security helper test.
 *
 * @author gudaoxuri
 */
public class SecurityHelperTest {

    /**
     * Digest.
     *
     * @throws Exception the exception
     */
    @Test
    public void digest() throws Exception {
        Assert.assertEquals("70c0cc2b7bf8a8ebcd7b59c49ddda9a1e551122ba5d7ab3b7b02141d4ce4c626".toLowerCase(),
                $.security.digest.digest("gudaoxuri", "SHA-256"));
        Assert.assertEquals("7d9def92860187bf1150ebb6ec342becb50bc5d5".toLowerCase(),
                $.security.digest.digest("gudaoxuri", "SHA1"));
        Assert.assertEquals(("e2806245bd7235d0a8b76ca489d2984aabc5a71a9b1d39abfd6bf24980cd808333f15788c0ef9a1e0c8"
                        + "afcec7a427f74a6f39da47d282810028d113a0ea5b11a").toLowerCase(),
                $.security.digest.digest("gudaoxuri", "SHA-512"));
        Assert.assertEquals("0ef841c028908fca6e78c51490e4a0cf".toLowerCase(),
                $.security.digest.digest("gudaoxuri", "MD5"));

        Assert.assertEquals("300ef751875b206d9001a1fc5695ef4403e249ae".toLowerCase(),
                $.security.digest.digest("gudaoxuri", "test", "HmacSHA1"));
        Assert.assertEquals("5a6d9c078d427b752754a731016a3f0c5753a2117bd274712d31c9990477d35f".toLowerCase(),
                $.security.digest.digest("gudaoxuri", "test", "HmacSHA256"));
        Assert.assertEquals(("a958eafd9b36d3b90bc35f9c13fd6f82c0ad535022e4572e36f2e266b78eb44d0858d5ba1ac80baa2295a8"
                        + "b50804aa4c1f44a710375bc821d94dc799d2fc1193").toLowerCase(),
                $.security.digest.digest("gudaoxuri", "test", "HmacSHA512"));
        Assert.assertEquals("8e03e0f7c20e19c58a636a043b9296ae".toLowerCase(),
                $.security.digest.digest("gudaoxuri", "test", "HmacMD5"));

        Assert.assertTrue($.security.digest.validate("gudaoxuri", $.security.digest.digest("gudaoxuri", "SHA-256"), "SHA-256"));
        Assert.assertTrue(
                $.security.digest.validate("gudaoxuri", "test",
                        $.security.digest.digest("gudaoxuri", "test", "HmacSHA256"), "HmacSHA256"));
        Assert.assertTrue($.security.digest.validate("password", $.security.digest.digest("password", "SHA-512"), "SHA-512"));
        Assert.assertTrue($.security.digest.validate("password", $.security.digest.digest("password", "bcrypt"), "bcrypt"));
    }

    /**
     * Symmetric.
     */
    @Test
    public void symmetric() {
        try {
            Assert.assertNotEquals("gudaoxuri", $.security.symmetric.decrypt(
                    $.security.symmetric.encrypt("gudaoxuri", "pwd", "aes"), "pwd2", "aes"));
            Assert.assertTrue(1 == 2);
        } catch (RTException e) {
            Assert.assertTrue(1 == 1);
        }
        Assert.assertEquals("gudaoxuri", $.security.symmetric.decrypt(
                $.security.symmetric.encrypt("gudaoxuri", "pwd", "aes"), "pwd", "aes"));
        String d = "Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、"
                + "并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，"
                + "设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，"
                + "一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。";
        try {
            Assert.assertNotEquals(d, $.security.symmetric.decrypt(
                    $.security.symmetric.encrypt(d, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43",
                            "aes"), "pwd2", "aes"));
            Assert.assertTrue(1 == 2);
        } catch (RTException e) {
            Assert.assertTrue(1 == 1);
        }
        Assert.assertEquals(d, $.security.symmetric.decrypt(
                $.security.symmetric.encrypt(d, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43",
                        "aes"),
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43", "aes"));
    }

    /**
     * Asymmetric.
     *
     * @throws Exception the exception
     */
    @Test
    public void asymmetric() throws Exception {
        String privateStr =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43+LE"
                        + "3JhT8y8TE39vDK22GJZWJFXYfWwasavknIfepBIVrnuMidtcPqUY3bhrDZN+J6MtYaSPSEwRcS2"
                        + "PgF/065CEdSbLy6cvKA64GUiG188un1xIsGBVUdu3fdu41OQvt+90TZT0HclXJAgMBAAECgYEAj"
                        + "XFndVhHCPU3P637PGppBqW06pREeybYUkNKH1dTS4cBaYcXmke2S290OMq2xp3tm++wbUqbKKkt"
                        + "97AOkWNrJfq8Ecpdw9s3c7yQGWaPuwiX38Cgtq6r0utjT20YgR6etGpqafoBt93RZpEm0eEzFPU"
                        + "nS7qYc86HprL0RJ0/i7kCQQDaOmvO82cYIK1ESkA0GdDVQoz2A1V8HvEWOsccRGqlWuasLUccyB"
                        + "nx1G/LDZUxcPOraDyxI8sdl7VbweLR0H9LAkEA2O/rWXwnSYKqdpt+OhpUBHNnMs3IMvRzefJ1z"
                        + "ObnIMyYR3KXtpQ/fL4gEquNwJgFIaPJVg5/3zHISEw3e8XOuwJAIDrGl07tZ+vTiyVoLAmwBP8K"
                        + "MH83jdhIBN9zbqJQGdG+Bam+Oer3ofac+CEuapni8uq3I/ZEVj+EomOVKyWe1wJAATztROd2ee7"
                        + "q9h5RDBfWXughsKKH//JxLkL59R9kNkW0oMPApeQWsKmNGU4tUuoLLXP31CvlAusPz4nPzz8DvQ"
                        + "JBAJXpICPNJw84fONzS0raRqlFoZMMI0cqeGtPIiCHKaRHyzQv/FFu2KxUcCrod8PngaBFRselz"
                        + "rwZILmXHqrHc1M=";
        String publicStr =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC47dAhLLB3xhDWccgEheqTbRimAhluN/ixNyYU/MvExN/"
                        + "bwytthiWViRV2H1sGrGr5JyH3qQSFa57jInbXD6lGN24aw2TfiejLWGkj0hMEXEtj4Bf9OuQhHU"
                        + "my8unLygOuBlIhtfPLp9cSLBgVVHbt33buNTkL7fvdE2U9B3JVyQIDAQAB";

        String d = "Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。"
                + "Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。"
                + "Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。";
        // 生成公钥密钥
        Map<String, String> keys = $.security.asymmetric.generateKeys("RSA", 1024);
        PublicKey publicKey = $.security.asymmetric.getPublicKey(publicStr, "RSA");
        PrivateKey privateKey = $.security.asymmetric.getPrivateKey(privateStr, "RSA");

        // 公钥加密/私钥解密
        byte[] encryptByPub = $.security.asymmetric.encrypt(d.getBytes("UTF-8"), publicKey, 1024, "RSA");
        String result = new String($.security.asymmetric.decrypt(encryptByPub, privateKey, 1024, "RSA"), "UTF-8");
        Assert.assertTrue(Objects.equals(result, d));

        // 私钥加密/公钥解密
        byte[] encryptByPriv = $.security.asymmetric.encrypt(d.getBytes("UTF-8"), privateKey, 1024, "RSA");
        byte[] decryptByPub = $.security.asymmetric.decrypt(encryptByPriv, publicKey, 1024, "RSA");
        Assert.assertTrue(Objects.equals(new String(decryptByPub, "UTF-8"), d));
        Assert.assertTrue($.security.asymmetric.verify(publicKey, decryptByPub,
                $.security.asymmetric.sign(privateKey, d.getBytes("UTF-8"), "SHA1withRSA"), "SHA1withRSA"));
    }

}
