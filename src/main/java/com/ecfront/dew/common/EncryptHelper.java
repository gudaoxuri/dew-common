package com.ecfront.dew.common;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EncryptHelper {

    /**
     * Base64 转 数组
     */
    public static byte[] decodeBase64ToBytes(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * Base64 转 字符串
     */
    public static String decodeBase64ToString(String str, String encode) throws UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(str), encode);
    }

    /**
     * 数组  转 Base64
     */
    public static String encodeBytesToBase64(byte[] str, String encode) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(str), encode);
    }

    /**
     * 字符串 转 Base64
     */
    public static String encodeStringToBase64(String str, String encode) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(str.getBytes(encode)), encode);
    }

    /**
     * 对称加密
     */
    public static class Symmetric {

        /**
         * 对称加密
         *
         * @param strSrc    原始值
         * @param algorithm 加密算法 ，如 bcrypt SHA-256 MD5
         * @return 加密后的值
         */
        public static String encrypt(String strSrc, String algorithm) throws NoSuchAlgorithmException {
            String encryptStr;
            switch (algorithm.toLowerCase()) {
                case "bcrypt":
                    encryptStr = BCrypt.hashpw(strSrc, BCrypt.gensalt());
                    break;
                default:
                    MessageDigest md = MessageDigest.getInstance(algorithm);
                    md.update(strSrc.getBytes());
                    byte[] digest = md.digest();
                    encryptStr = String.format("%064x", new java.math.BigInteger(1, digest));
                    break;
            }
            return encryptStr;
        }

        /**
         * 验证
         * <p>
         * 对于bcrypt之类默认使用随机slat的加密算法必须使用此方法验证
         *
         * @param strSrc       原始值
         * @param strEncrypted 加密后的值
         * @param algorithm    加密算法，如 bcrypt
         * @return 是否匹配
         */
        public static boolean validate(String strSrc, String strEncrypted, String algorithm) throws NoSuchAlgorithmException {
            boolean result;
            switch (algorithm.toLowerCase()) {
                case "bcrypt":
                    result = BCrypt.checkpw(strSrc, strEncrypted);
                    break;
                default:
                    result = Objects.equals(encrypt(strSrc, algorithm), strEncrypted);
                    break;
            }
            return result;
        }

    }

    /**
     * 非对称加密
     */
    public static class Asymmetric {

        /**
         * 生成公钥和私钥
         *
         * @param algorithm 非对称算法，如 RSA
         * @param length    密钥长度
         * @param encode    转Base64的编码
         * @return PublicKey -> Base64编码后的值  ， PrivateKey -> Base64编码后的值
         */
        public static Map<String, String> generateKeys(String algorithm, int length, String encode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(length);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return new HashMap<String, String>() {{
                put("PublicKey", EncryptHelper.encodeBytesToBase64(keyPair.getPublic().getEncoded(), encode));
                put("PrivateKey", EncryptHelper.encodeBytesToBase64(keyPair.getPrivate().getEncoded(), encode));
            }};
        }

        /**
         * 获取私钥文件
         *
         * @param key       Base64编码的私钥
         * @param algorithm 非对称算法，如 RSA
         * @return 私钥文件
         */
        public static PrivateKey getPrivateKey(String key, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
            byte[] privateKey = EncryptHelper.decodeBase64ToBytes(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
            return KeyFactory.getInstance(algorithm).generatePrivate(spec);
        }

        /**
         * 获取公钥文件
         *
         * @param key       Base64编码的公钥
         * @param algorithm 非对称算法，如 RSA
         * @return 公钥文件
         */
        public static PublicKey getPublicKey(String key, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
            byte[] privateKey = EncryptHelper.decodeBase64ToBytes(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(privateKey);
            return KeyFactory.getInstance(algorithm).generatePublic(spec);
        }

        /**
         * 加密
         *
         * @param data      要加密的数据
         * @param key       公钥或私钥文件
         * @param algorithm 非对称算法，如 RSA
         * @return 加密后的数据
         */
        public static byte[] encrypt(byte[] data, Key key, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        /**
         * 解密
         *
         * @param data      要解密的数据
         * @param key       公钥或私钥文件
         * @param algorithm 非对称算法，如 RSA
         * @return 解密后的数据
         */
        public static byte[] decrypt(byte[] data, Key key, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(data);
        }

        /**
         * 签名
         *
         * @param key       私钥文件
         * @param data      要签名的数据
         * @param algorithm 签名算法（如 SHA1withRSA、MD5withRSA）
         * @return 签名数据
         */
        public static byte[] sign(PrivateKey key, byte[] data, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
            Signature signer = Signature.getInstance(algorithm);
            signer.initSign(key);
            signer.update(data);
            return signer.sign();
        }

        /**
         * 验证
         *
         * @param key       公钥文件
         * @param data      解密后的数据
         * @param signature 签名数据
         * @param algorithm 签名算法（如 SHA1withRSA、MD5withRSA）
         * @return 是否通过
         */
        public static boolean verify(PublicKey key, byte[] data, byte[] signature, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
            Signature verifier = Signature.getInstance(algorithm);
            verifier.initVerify(key);
            verifier.update(data);
            return verifier.verify(signature);
        }

    }

}
