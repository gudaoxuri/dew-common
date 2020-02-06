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

import com.ecfront.dew.common.exception.RTGeneralSecurityException;
import com.ecfront.dew.common.exception.RTIOException;
import com.ecfront.dew.common.exception.RTUnsupportedEncodingException;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 加解密操作.
 *
 * @author gudaoxuri
 */
public class SecurityHelper {

    /**
     * Instantiates a new Security helper.
     */
    SecurityHelper() {
    }

    /**
     * The Symmetric.
     */
    public Symmetric symmetric = new Symmetric();
    /**
     * The Asymmetric.
     */
    public Asymmetric asymmetric = new Asymmetric();
    /**
     * The Digest.
     */
    public Digest digest = new Digest();

    /**
     * Base64 转 数组.
     *
     * @param str the str
     * @return the byte [ ]
     */
    public byte[] decodeBase64ToBytes(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * Base64 转 字符串.
     *
     * @param str    the str
     * @param encode the encode
     * @return the string
     * @throws RTUnsupportedEncodingException the rt unsupported encoding exception
     */
    public String decodeBase64ToString(String str, String encode) throws RTUnsupportedEncodingException {
        try {
            return new String(Base64.getDecoder().decode(str), encode);
        } catch (UnsupportedEncodingException e) {
            throw new RTUnsupportedEncodingException(e);
        }
    }

    /**
     * Base64 转 字符串.
     *
     * @param str    the str
     * @param encode the encode
     * @return the string
     */
    public String decodeBase64ToString(String str, Charset encode) {
        return new String(Base64.getDecoder().decode(str), encode);
    }

    /**
     * 数组 转 Base64.
     *
     * @param str the str
     * @return the string
     */
    public String encodeBytesToBase64(byte[] str) {
        return new String(Base64.getEncoder().encode(str));
    }

    /**
     * 字符串 转 Base64.
     *
     * @param str    the str
     * @param encode the encode
     * @return the string
     * @throws RTUnsupportedEncodingException the rt unsupported encoding exception
     */
    public String encodeStringToBase64(String str, String encode) throws RTUnsupportedEncodingException {
        try {
            return new String(Base64.getEncoder().encode(str.getBytes(encode)), encode);
        } catch (UnsupportedEncodingException e) {
            throw new RTUnsupportedEncodingException(e);
        }
    }

    /**
     * 字符串 转 Base64.
     *
     * @param str    the str
     * @param encode the encode
     * @return the string
     */
    public String encodeStringToBase64(String str, Charset encode) {
        return new String(Base64.getEncoder().encode(str.getBytes(encode)), encode);
    }


    /**
     * 摘要.
     *
     * @author gudaoxuri
     */
    public class Digest {

        /**
         * 摘要.
         *
         * @param text      原始值
         * @param algorithm 摘要算法 ，如 bcrypt SHA-x MD5
         * @return 摘要后的值 string
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public String digest(String text, String algorithm) throws RTGeneralSecurityException {
            switch (algorithm.toLowerCase()) {
                case "bcrypt":
                    return BCrypt.hashpw(text, BCrypt.gensalt());
                default:
                    return byte2HexStr(digest(text.getBytes(StandardCharsets.UTF_8), algorithm));
            }
        }

        /**
         * 摘要.
         *
         * @param text      原始值
         * @param algorithm 摘要算法 ，如 bcrypt SHA-x MD5
         * @return 摘要后的值 byte [ ]
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public byte[] digest(byte[] text, String algorithm) throws RTGeneralSecurityException {
            switch (algorithm.toLowerCase()) {
                case "bcrypt":
                    return BCrypt.hashpw(new String(text, StandardCharsets.UTF_8), BCrypt.gensalt()).getBytes(StandardCharsets.UTF_8);
                default:
                    try {
                        MessageDigest md = MessageDigest.getInstance(algorithm);
                        md.update(text);
                        return md.digest();
                    } catch (NoSuchAlgorithmException e) {
                        throw new RTGeneralSecurityException(e);
                    }
            }
        }

        /**
         * 摘要，带密钥，多为 Hmac* 算法.
         *
         * @param text      原始值
         * @param secretKey 密钥
         * @param algorithm 摘要算法 ，如 HmacSHA256
         * @return 摘要后的值 string
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public String digest(String text, String secretKey, String algorithm) throws RTGeneralSecurityException {
            return byte2HexStr(digest(text.getBytes(StandardCharsets.UTF_8), secretKey.getBytes(StandardCharsets.UTF_8), algorithm));
        }

        /**
         * 摘要，带密钥，多为 Hmac* 算法.
         *
         * @param text      原始值
         * @param secretKey 密钥
         * @param algorithm 摘要算法 ，如 HmacSHA256
         * @return 摘要后的值 byte [ ]
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public byte[] digest(byte[] text, byte[] secretKey, String algorithm) throws RTGeneralSecurityException {
            try {
                Mac mac = Mac.getInstance(algorithm);
                mac.init(new SecretKeySpec(secretKey, algorithm));
                return mac.doFinal(text);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        /**
         * 验证.
         * <p>
         * 对于bcrypt之类默认使用随机slat的加密算法必须使用此方法验证
         *
         * @param text       原始值
         * @param ciphertext 加密后的值
         * @param algorithm  加密算法，如 bcrypt SHA-256
         * @return 是否匹配 boolean
         */
        public boolean validate(String text, String ciphertext, String algorithm) {
            boolean result;
            switch (algorithm.toLowerCase()) {
                case "bcrypt":
                    result = BCrypt.checkpw(text, ciphertext);
                    break;
                default:
                    result = Objects.equals(digest(text, algorithm), ciphertext);
                    break;
            }
            return result;
        }

        /**
         * 验证，带密钥，多为 Hmac* 算法.
         * <p>
         *
         * @param text       原始值
         * @param secretKey  密钥
         * @param ciphertext 加密后的值
         * @param algorithm  加密算法，如 HmacSHA256
         * @return 是否匹配 boolean
         */
        public boolean validate(String text, String secretKey, String ciphertext, String algorithm) {
            return Objects.equals(digest(text, secretKey, algorithm), ciphertext);
        }

    }

    /**
     * Byte[] to hex.
     *
     * @param buf the buf
     * @return the string
     */
    public String byte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte abuf : buf) {
            String hex = Integer.toHexString(abuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * Hex string to byte[].
     *
     * @param hexStr the hex str
     * @return the byte [ ]
     */
    public byte[] hexStr2Byte(String hexStr) {
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


    /**
     * 对称加密.
     *
     * @author gudaoxuri
     */
    public class Symmetric {

        /**
         * 对称加密.
         *
         * @param strSrc    原始值
         * @param password  密码
         * @param algorithm 加密算法 ，如 AES DES
         * @return 加密后的值 string
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public String encrypt(String strSrc, String password, String algorithm) throws RTGeneralSecurityException {
            if (password == null) {
                throw new RTGeneralSecurityException(String.format("%s must input password", algorithm));
            }
            try {
                KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
                kgen.init(128, secureRandom);
                SecretKeySpec key = new SecretKeySpec(kgen.generateKey().getEncoded(), algorithm);
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return byte2HexStr(cipher.doFinal(strSrc.getBytes(StandardCharsets.UTF_8)));
            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        /**
         * 解密.
         *
         * @param strEncrypted 加密后的值
         * @param password     密码
         * @param algorithm    加密算法，如 AES DES
         * @return 解密后值 （原始值）
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public String decrypt(String strEncrypted, String password, String algorithm) throws RTGeneralSecurityException {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
                kgen.init(128, secureRandom);
                SecretKeySpec key = new SecretKeySpec(kgen.generateKey().getEncoded(), algorithm);
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.DECRYPT_MODE, key);
                return new String(cipher.doFinal(hexStr2Byte(strEncrypted)), StandardCharsets.UTF_8);
            } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

    }

    /**
     * 非对称加密.
     */
    public class Asymmetric {

        /**
         * 生成公钥和私钥.
         *
         * @param algorithm 非对称算法，如 RSA
         * @param length    密钥长度
         * @return PublicKey -> Base64编码后的值  ， PrivateKey -> Base64编码后的值
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public Map<String, String> generateKeys(String algorithm, int length) throws RTGeneralSecurityException {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
                keyPairGenerator.initialize(length);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                return new HashMap<>() {
                    {
                        put("PublicKey", encodeBytesToBase64(keyPair.getPublic().getEncoded()));
                        put("PrivateKey", encodeBytesToBase64(keyPair.getPrivate().getEncoded()));
                    }
                };
            } catch (NoSuchAlgorithmException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        /**
         * 获取私钥文件.
         *
         * @param key       Base64编码的私钥
         * @param algorithm 非对称算法，如 RSA
         * @return 私钥文件 private key
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public PrivateKey getPrivateKey(String key, String algorithm) throws RTGeneralSecurityException {
            byte[] privateKey = decodeBase64ToBytes(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
            try {
                return KeyFactory.getInstance(algorithm).generatePrivate(spec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        /**
         * 获取公钥文件.
         *
         * @param key       Base64编码的公钥
         * @param algorithm 非对称算法，如 RSA
         * @return 公钥文件 public key
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public PublicKey getPublicKey(String key, String algorithm) throws RTGeneralSecurityException {
            byte[] privateKey = decodeBase64ToBytes(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(privateKey);
            try {
                return KeyFactory.getInstance(algorithm).generatePublic(spec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        /**
         * 加密.
         *
         * @param data      要加密的数据
         * @param key       公钥或私钥文件
         * @param keyLength 密钥长度
         * @param algorithm 非对称算法，如 RSA
         * @return 加密后的数据 byte [ ]
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public byte[] encrypt(byte[] data, Key key, int keyLength, String algorithm) throws RTGeneralSecurityException {
            try {
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return packageCipher(data, cipher, keyLength / 8 - 11, data.length);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        /**
         * 解密.
         *
         * @param data      要解密的数据
         * @param key       公钥或私钥文件
         * @param keyLength 密钥长度
         * @param algorithm 非对称算法，如 RSA
         * @return 解密后的数据 byte [ ]
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public byte[] decrypt(byte[] data, Key key, int keyLength, String algorithm) throws RTGeneralSecurityException {
            try {
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(Cipher.DECRYPT_MODE, key);
                return packageCipher(data, cipher, keyLength / 8, data.length);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        private byte[] packageCipher(byte[] data, Cipher cipher, int maxLength, int inputLength) throws RTGeneralSecurityException, RTIOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            try {
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
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                throw new RTGeneralSecurityException(e);
            } catch (IOException e) {
                throw new RTIOException(e);
            }
        }

        /**
         * 签名.
         *
         * @param key       私钥文件
         * @param data      要签名的数据
         * @param algorithm 签名算法（如 SHA1withRSA、MD5withRSA）
         * @return 签名数据 byte [ ]
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public byte[] sign(PrivateKey key, byte[] data, String algorithm) throws RTGeneralSecurityException {
            try {
                Signature signer = Signature.getInstance(algorithm);
                signer.initSign(key);
                signer.update(data);
                return signer.sign();
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

        /**
         * 验证.
         *
         * @param key       公钥文件
         * @param data      解密后的数据
         * @param signature 签名数据
         * @param algorithm 签名算法（如 SHA1withRSA、MD5withRSA）
         * @return 是否通过 boolean
         * @throws RTGeneralSecurityException the rt general security exception
         */
        public boolean verify(PublicKey key, byte[] data, byte[] signature, String algorithm) throws RTGeneralSecurityException {
            try {
                Signature verifier = Signature.getInstance(algorithm);
                verifier.initVerify(key);
                verifier.update(data);
                return verifier.verify(signature);
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                throw new RTGeneralSecurityException(e);
            }
        }

    }

}
