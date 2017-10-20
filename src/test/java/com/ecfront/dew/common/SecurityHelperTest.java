package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Objects;

public class SecurityHelperTest {

    @Test
    public void digest() throws Exception {
        Assert.assertEquals("70C0CC2B7BF8A8EBCD7B59C49DDDA9A1E551122BA5D7AB3B7B02141D4CE4C626".toLowerCase(),
                $.security.digest.digest("gudaoxuri", "SHA-256"));
        Assert.assertTrue($.security.digest.validate("gudaoxuri", $.security.digest.digest("gudaoxuri", "SHA-256"), "SHA-256"));
        Assert.assertTrue($.security.digest.validate("password", $.security.digest.digest("password", "SHA-512"), "SHA-512"));
        Assert.assertTrue($.security.digest.validate("password", $.security.digest.digest("password", "bcrypt"), "bcrypt"));
    }

    @Test
    public void symmetric() throws Exception {
        try {
            Assert.assertNotEquals("gudaoxuri", $.security.symmetric.decrypt(
                    $.security.symmetric.encrypt("gudaoxuri", "pwd", "aes"), "pwd2", "aes"));
            Assert.assertTrue(1 == 2);
        } catch (BadPaddingException exception) {
            Assert.assertTrue(1 == 1);
        }
        Assert.assertEquals("gudaoxuri", $.security.symmetric.decrypt(
                $.security.symmetric.encrypt("gudaoxuri", "pwd", "aes"), "pwd", "aes"));
        String d = "Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。";
        try {
            Assert.assertNotEquals(d, $.security.symmetric.decrypt(
                    $.security.symmetric.encrypt(d, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43", "aes"), "pwd2", "aes"));
            Assert.assertTrue(1 == 2);
        } catch (BadPaddingException exception) {
            Assert.assertTrue(1 == 1);
        }
        Assert.assertEquals(d, $.security.symmetric.decrypt(
                $.security.symmetric.encrypt(d, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43", "aes"), "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43", "aes"));

    }

    @Test
    public void asymmetric() throws Exception {
        String privateStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43+LE3JhT8y8TE39vDK22GJZWJFXYfWwasavknIfepBIVrnuMidtcPqUY3bhrDZN+J6MtYaSPSEwRcS2PgF/065CEdSbLy6cvKA64GUiG188un1xIsGBVUdu3fdu41OQvt+90TZT0HclXJAgMBAAECgYEAjXFndVhHCPU3P637PGppBqW06pREeybYUkNKH1dTS4cBaYcXmke2S290OMq2xp3tm++wbUqbKKkt97AOkWNrJfq8Ecpdw9s3c7yQGWaPuwiX38Cgtq6r0utjT20YgR6etGpqafoBt93RZpEm0eEzFPUnS7qYc86HprL0RJ0/i7kCQQDaOmvO82cYIK1ESkA0GdDVQoz2A1V8HvEWOsccRGqlWuasLUccyBnx1G/LDZUxcPOraDyxI8sdl7VbweLR0H9LAkEA2O/rWXwnSYKqdpt+OhpUBHNnMs3IMvRzefJ1zObnIMyYR3KXtpQ/fL4gEquNwJgFIaPJVg5/3zHISEw3e8XOuwJAIDrGl07tZ+vTiyVoLAmwBP8KMH83jdhIBN9zbqJQGdG+Bam+Oer3ofac+CEuapni8uq3I/ZEVj+EomOVKyWe1wJAATztROd2ee7q9h5RDBfWXughsKKH//JxLkL59R9kNkW0oMPApeQWsKmNGU4tUuoLLXP31CvlAusPz4nPzz8DvQJBAJXpICPNJw84fONzS0raRqlFoZMMI0cqeGtPIiCHKaRHyzQv/FFu2KxUcCrod8PngaBFRselzrwZILmXHqrHc1M=";
        String publicStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC47dAhLLB3xhDWccgEheqTbRimAhluN/ixNyYU/MvExN/bwytthiWViRV2H1sGrGr5JyH3qQSFa57jInbXD6lGN24aw2TfiejLWGkj0hMEXEtj4Bf9OuQhHUmy8unLygOuBlIhtfPLp9cSLBgVVHbt33buNTkL7fvdE2U9B3JVyQIDAQAB";

        String d = "Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。";
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
        Assert.assertTrue($.security.asymmetric.verify(publicKey, decryptByPub, $.security.asymmetric.sign(privateKey, d.getBytes("UTF-8"), "SHA1withRSA"), "SHA1withRSA"));
    }

}