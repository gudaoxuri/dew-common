package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Objects;

public class EncryptHelperTest {

    @Test
    public void symmetric() throws Exception {
        Assert.assertEquals("70C0CC2B7BF8A8EBCD7B59C49DDDA9A1E551122BA5D7AB3B7B02141D4CE4C626".toLowerCase(),
                $.encrypt.symmetric.encrypt("gudaoxuri", "SHA-256"));
        Assert.assertTrue($.encrypt.symmetric.validate("gudaoxuri", $.encrypt.symmetric.encrypt("gudaoxuri", "SHA-256"), "SHA-256"));
        Assert.assertTrue($.encrypt.symmetric.validate("password", $.encrypt.symmetric.encrypt("password", "bcrypt"), "bcrypt"));
    }

    @Test
    public void asymmetric() throws Exception {
        String privateStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALjt0CEssHfGENZxyASF6pNtGKYCGW43+LE3JhT8y8TE39vDK22GJZWJFXYfWwasavknIfepBIVrnuMidtcPqUY3bhrDZN+J6MtYaSPSEwRcS2PgF/065CEdSbLy6cvKA64GUiG188un1xIsGBVUdu3fdu41OQvt+90TZT0HclXJAgMBAAECgYEAjXFndVhHCPU3P637PGppBqW06pREeybYUkNKH1dTS4cBaYcXmke2S290OMq2xp3tm++wbUqbKKkt97AOkWNrJfq8Ecpdw9s3c7yQGWaPuwiX38Cgtq6r0utjT20YgR6etGpqafoBt93RZpEm0eEzFPUnS7qYc86HprL0RJ0/i7kCQQDaOmvO82cYIK1ESkA0GdDVQoz2A1V8HvEWOsccRGqlWuasLUccyBnx1G/LDZUxcPOraDyxI8sdl7VbweLR0H9LAkEA2O/rWXwnSYKqdpt+OhpUBHNnMs3IMvRzefJ1zObnIMyYR3KXtpQ/fL4gEquNwJgFIaPJVg5/3zHISEw3e8XOuwJAIDrGl07tZ+vTiyVoLAmwBP8KMH83jdhIBN9zbqJQGdG+Bam+Oer3ofac+CEuapni8uq3I/ZEVj+EomOVKyWe1wJAATztROd2ee7q9h5RDBfWXughsKKH//JxLkL59R9kNkW0oMPApeQWsKmNGU4tUuoLLXP31CvlAusPz4nPzz8DvQJBAJXpICPNJw84fONzS0raRqlFoZMMI0cqeGtPIiCHKaRHyzQv/FFu2KxUcCrod8PngaBFRselzrwZILmXHqrHc1M=";
        String publicStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC47dAhLLB3xhDWccgEheqTbRimAhluN/ixNyYU/MvExN/bwytthiWViRV2H1sGrGr5JyH3qQSFa57jInbXD6lGN24aw2TfiejLWGkj0hMEXEtj4Bf9OuQhHUmy8unLygOuBlIhtfPLp9cSLBgVVHbt33buNTkL7fvdE2U9B3JVyQIDAQAB";

        String d = "Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。Scala是一门多范式的编程语言，一种类似java的编程语言[1]  ，设计初衷是实现可伸缩的语言[2]  、并集成面向对象编程和函数式编程的各种特性。";
        // 生成公钥密钥
        Map<String, String> keys = $.encrypt.asymmetric.generateKeys("RSA", 1024, "UTF-8");
        PublicKey publicKey = $.encrypt.asymmetric.getPublicKey(publicStr, "RSA");
        PrivateKey privateKey = $.encrypt.asymmetric.getPrivateKey(privateStr, "RSA");

        // 公钥加密/私钥解密
        byte[] encryptByPub = $.encrypt.asymmetric.encrypt(d.getBytes("UTF-8"), publicKey, 1024, "RSA");
        String result = new String($.encrypt.asymmetric.decrypt(encryptByPub, privateKey, 1024, "RSA"), "UTF-8");
        Assert.assertTrue(Objects.equals(result, d));

        // 私钥加密/公钥解密
        byte[] encryptByPriv = $.encrypt.asymmetric.encrypt(d.getBytes("UTF-8"), privateKey, 1024, "RSA");
        byte[] decryptByPub = $.encrypt.asymmetric.decrypt(encryptByPriv, publicKey, 1024, "RSA");
        Assert.assertTrue(Objects.equals(new String(decryptByPub, "UTF-8"), d));
        Assert.assertTrue($.encrypt.asymmetric.verify(publicKey, decryptByPub, $.encrypt.asymmetric.sign(privateKey, d.getBytes("UTF-8"), "SHA1withRSA"), "SHA1withRSA"));
    }

}