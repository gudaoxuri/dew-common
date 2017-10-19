package com.ecfront.dew.common;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 加解密操作
 */
public class EncryptHelper {

    EncryptHelper() {
    }

    public Symmetric symmetric = new Symmetric();
    public Asymmetric asymmetric = new Asymmetric();

    /**
     * Base64 转 数组
     */
    public byte[] decodeBase64ToBytes(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * Base64 转 字符串
     */
    public String decodeBase64ToString(String str, String encode) throws UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(str), encode);
    }

    /**
     * 数组 转 Base64
     */
    public String encodeBytesToBase64(byte[] str) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(str));
    }

    /**
     * 字符串 转 Base64
     */
    public String encodeStringToBase64(String str, String encode) throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(str.getBytes(encode)), encode);
    }


    /**
     * 对称加密
     */
    public class Symmetric {

        /**
         * 对称加密
         *
         * @param strSrc    原始值
         * @param algorithm 加密算法 ，如 bcrypt SHA-256 MD5
         * @return 加密后的值
         */
        public String encrypt(String strSrc, String algorithm) throws GeneralSecurityException {
            return encrypt(strSrc, null, algorithm);
        }

        /**
         * 对称加密
         *
         * @param strSrc         原始值
         * @param passwordOrSalt 密码或盐值， AES DES等加密算法必须要密码，bcrypt可以自定义盐值
         * @param algorithm      加密算法 ，如 bcrypt SHA-256 MD5 AES DES
         * @return 加密后的值
         */
        public String encrypt(String strSrc, String passwordOrSalt, String algorithm) throws GeneralSecurityException {
            String encryptStr;
            switch (algorithm.toLowerCase()) {
                case "bcrypt":
                    encryptStr = BCrypt.hashpw(strSrc,
                            passwordOrSalt != null ?
                                    BCrypt.gensalt(10, new SecureRandom(passwordOrSalt.getBytes())) : BCrypt.gensalt());
                    break;
                case "des":
                case "aes":
                    if (passwordOrSalt == null) {
                        throw new RuntimeException(String.format("%s must input password", algorithm));
                    }
                    KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
                    kgen.init(128, new SecureRandom(passwordOrSalt.getBytes()));
                    SecretKeySpec key = new SecretKeySpec(kgen.generateKey().getEncoded(), algorithm);
                    Cipher cipher = Cipher.getInstance(algorithm);
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    try {
                        encryptStr = byte2HexStr(cipher.doFinal(strSrc.getBytes("utf-8")));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        encryptStr = null;
                    }
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
         * 解密
         *
         * @param strEncrypted 加密后的值
         * @param password     密码
         * @param algorithm    加密算法，如 AES DES
         * @return 解密后值（原始值）
         * @throws GeneralSecurityException 其它解密错误
         * @throws BadPaddingException      密码错误
         */
        public String decrypt(String strEncrypted, String password, String algorithm) throws GeneralSecurityException {
            KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKeySpec key = new SecretKeySpec(kgen.generateKey().getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            try {
                return new String(cipher.doFinal(hexStr2Byte(strEncrypted)), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 验证
         * <p>
         * 对于bcrypt之类默认使用随机slat的加密算法必须使用此方法验证
         *
         * @param strSrc       原始值
         * @param strEncrypted 加密后的值
         * @param algorithm    加密算法，如 bcrypt SHA-256
         * @return 是否匹配
         */
        public boolean validate(String strSrc, String strEncrypted, String algorithm) throws GeneralSecurityException {
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

        private String byte2HexStr(byte buf[]) {
            StringBuilder sb = new StringBuilder();
            for (byte aBuf : buf) {
                String hex = Integer.toHexString(aBuf & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }

        private byte[] hexStr2Byte(String hexStr) {
            if (hexStr.length() < 1) {
                return null;
            }
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }

    }

    /**
     * 非对称加密
     */
    public class Asymmetric {

        /**
         * 生成公钥和私钥
         *
         * @param algorithm 非对称算法，如 RSA
         * @param length    密钥长度
         * @return PublicKey -> Base64编码后的值  ， PrivateKey -> Base64编码后的值
         */
        public Map<String, String> generateKeys(String algorithm, int length) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(length);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return new HashMap<String, String>() {{
                put("PublicKey", encodeBytesToBase64(keyPair.getPublic().getEncoded()));
                put("PrivateKey", encodeBytesToBase64(keyPair.getPrivate().getEncoded()));
            }};
        }

        /**
         * 获取私钥文件
         *
         * @param key       Base64编码的私钥
         * @param algorithm 非对称算法，如 RSA
         * @return 私钥文件
         */
        public PrivateKey getPrivateKey(String key, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
            byte[] privateKey = decodeBase64ToBytes(key);
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
        public PublicKey getPublicKey(String key, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
            byte[] privateKey = decodeBase64ToBytes(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(privateKey);
            return KeyFactory.getInstance(algorithm).generatePublic(spec);
        }

        /**
         * 加密
         *
         * @param data      要加密的数据
         * @param key       公钥或私钥文件
         * @param keyLength 密钥长度
         * @param algorithm 非对称算法，如 RSA
         * @return 加密后的数据
         */
        public byte[] encrypt(byte[] data, Key key, int keyLength, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return packageCipher(data, cipher, keyLength / 8 - 11, data.length);
        }

        /**
         * 解密
         *
         * @param data      要解密的数据
         * @param key       公钥或私钥文件
         * @param keyLength 密钥长度
         * @param algorithm 非对称算法，如 RSA
         * @return 解密后的数据
         */
        public byte[] decrypt(byte[] data, Key key, int keyLength, String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return packageCipher(data, cipher, keyLength / 8, data.length);
        }

        private byte[] packageCipher(byte[] data, Cipher cipher, int maxLength, int inputLength) throws IllegalBlockSizeException, BadPaddingException, IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > maxLength) {
                    cache = cipher.doFinal(data, offSet, maxLength);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLength - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxLength;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }

        /**
         * 签名
         *
         * @param key       私钥文件
         * @param data      要签名的数据
         * @param algorithm 签名算法（如 SHA1withRSA、MD5withRSA）
         * @return 签名数据
         */
        public byte[] sign(PrivateKey key, byte[] data, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
        public boolean verify(PublicKey key, byte[] data, byte[] signature, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
            Signature verifier = Signature.getInstance(algorithm);
            verifier.initVerify(key);
            verifier.update(data);
            return verifier.verify(signature);
        }

    }

}
