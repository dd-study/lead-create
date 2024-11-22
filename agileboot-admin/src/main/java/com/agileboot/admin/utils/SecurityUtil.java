package com.agileboot.admin.utils;

import cn.hutool.core.codec.Base64;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密、编码相关工具（MD5 , SHA , AES , CRC32 等）
 *
 * @author chenzhaoju
 */
public final class SecurityUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * 字符集名称-UTF-8
     */
    public static final String CHARSETNAME_UTF_8 = "UTF-8";
    /**
     * 用来将字节转换成 16 进制表示的字符表
     */
    public static final char _hexDigits[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private SecurityUtil() {
    }

    /**
     * MD5散列计算
     *
     * @param data 要计算散列值的字节数组
     * @return MD5散列值（16进格式串）
     */
    public static String md5Hash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return encodeHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    /**
     * 按字串的UTF-8编码进行MD5散列计算
     *
     * @param str 要计算散列值的字串
     * @return MD5散列值（16进格式串）
     */
    public static String md5Hash(String str) {
        if (null == str) {
            return "";
        }
        try {
            return md5Hash(str.getBytes(CHARSETNAME_UTF_8));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    /**
     * 按字串的UTF-8编码进行MD5散列计算
     *
     * @param str 要计算散列值的字串
     * @return MD5散列值（16进格式串）
     */
    public static String md5Hash(String... str) {
        if (null == str) {
            return "";
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : str) {
                stringBuilder.append(s);
            }
            return md5Hash(stringBuilder.toString().getBytes(CHARSETNAME_UTF_8));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    /**
     * SHA256 散列计算
     *
     * @param data 要计算散列值的字节数组
     * @return SHA散列值（16进格式串）
     */
    public static String sha256Hash(byte[] data) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(data);
            return encodeHex(sha.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.toString());
        }
        return null;

    }

    /**
     * 按字串的UTF-8编码进行SHA256散列计算
     *
     * @param str 要计算散列值的字串
     * @return SHA256散列值（16进格式串）
     */
    public static String sha256Hash(String str) {
        if (null == str) {
            return "";
        }
        try {
            return sha256Hash(str.getBytes(CHARSETNAME_UTF_8));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    /**
     * SHA散列计算(SHA1)
     *
     * @param data 要计算散列值的字节数组
     * @return SHA散列值（16进格式串）
     */
    public static String shaHash(byte[] data) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(data);
            return encodeHex(sha.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.toString());
        }
        return null;

    }

    /**
     * 按字串的UTF-8编码进行SHA散列计算(SHA1)
     *
     * @param str 要计算散列值的字串
     * @return SHA散列值（16进格式串）
     */
    public static String shaHash(String str) {
        if (null == str) {
            return "";
        }
        try {
            return shaHash(str.getBytes(CHARSETNAME_UTF_8));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    /**
     * 按字串的UTF-8编码进行SHA散列计算(SHA1)
     *
     * @param str 要计算散列值的字串
     * @return SHA散列值（16进格式串）
     */
    public static String shaHash(String... str) {
        if (null == str) {
            return "";
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : str) {
                stringBuilder.append(s);
            }
            return shaHash(stringBuilder.toString().getBytes(CHARSETNAME_UTF_8));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.toString());
        }
        return null;
    }

    /**
     * 初始化 HmacSHA256 key
     *
     * @return
     */
    public static String initHmacSHA256Key() throws NoSuchAlgorithmException {
        KeyGenerator hmacSHA256 = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = hmacSHA256.generateKey();
        byte[] encoded = secretKey.getEncoded();
        // 十六进制
        String keyHex = Base64.encode(encoded);
        return keyHex;
    }

    /**
     * hmacSHA1 摘要
     *
     * @param content
     * @param key
     * @return
     *
     * @throws Exception
     */
    public static String hmacSHA1(String content, String key) throws Exception {
        if (null == content || null == key) {
            return "";
        }
        byte[] bytes = content.getBytes(CHARSETNAME_UTF_8);
        byte[] decodeHex = key.getBytes(CHARSETNAME_UTF_8);
        SecretKey secretKey = new SecretKeySpec(decodeHex, "HmacSHA1");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] doFinal = mac.doFinal(bytes);
        return Base64.encode(encodeHex(doFinal));
    }

    /**
     * hmacSHA256 摘要
     *
     * @param content
     * @param key
     * @return
     *
     * @throws Exception
     */
    public static String hmacSHA256(String content, String key) throws Exception {
        if (null == content || null == key) {
            return "";
        }
        byte[] bytes = content.getBytes(CHARSETNAME_UTF_8);
        byte[] decodeHex = key.getBytes(CHARSETNAME_UTF_8);
        SecretKey secretKey = new SecretKeySpec(decodeHex, "HmacSHA256");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] doFinal = mac.doFinal(bytes);
        return Base64.encode(doFinal);
    }

    /**
     * HEX编码
     *
     * @param data 字节数组
     * @return HEX编码串
     */
    public static String encodeHex(byte[] data) {
        if (null == data) {
            return null;
        }
        char[] hex = new char[data.length * 2];
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            // 转换成 16 进制字符的转换
            byte bt = data[i]; // 取第 i 个字节
            // 取字节中高 4 位的数字转换
            hex[k++] = _hexDigits[(bt >> 4) & 0x0F];
            // 取字节中低 4 位的数字转换
            hex[k++] = _hexDigits[bt & 0x0F];
        }
        return new String(hex);
    }

    /**
     * HEX解码
     *
     * @param hex HEX编码串
     * @return 解码后的字节数组
     */
    public static byte[] decodeHex(String hex) {
        if (null == hex || 0 == hex.length()) {
            return null;
        }
        if (1 == hex.length()) {
            throw new RuntimeException("HEX格式错误：" + hex);
        }
        byte[] data = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length() / 2; i++) {
            int high = Integer.parseInt(hex.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hex.substring(i * 2 + 1, i * 2 + 2), 16);
            data[i] = (byte) (high * 16 + low);
        }
        return data;
    }

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密钥
     * @return 密文
     */
    public static byte[] aesEncrypt(byte[] content, String password) {
        try {
            // 创建AES的Key生产者
            SecretKeySpec key = getSecretKeySpec(password);
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(content);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密钥
     * @return 密文 字符串
     */
    public static String aesEncrypt(String content, String password) {
        try {
            byte[] bytes = aesEncrypt(content.getBytes(CHARSETNAME_UTF_8), password);
            return encodeHex(bytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密钥
     * @return 明文
     */
    public static byte[] aesDecrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = getSecretKeySpec(password);

            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密钥
     * @return 明文 字符串
     */
    public static String aesDecrypt(String content, String password) {
        if (StringUtils.isEmpty(content)) {
            throw new RuntimeException("解密内容不能为空");
        }
        try {
            byte[] bytes = aesDecrypt(decodeHex(content), password);
            return new String(bytes);
        } catch (Throwable e) {
            throw new RuntimeException(content, e);
        }
    }

    private static SecretKeySpec getSecretKeySpec(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 创建AES的Key生产者
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes(CHARSETNAME_UTF_8));
        kgen.init(128, secureRandom);

        // 根据用户密码，生成一个密钥
        SecretKey secretKey = kgen.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    /**
     * RSA 初始密钥对
     *
     * @return
     */
    public static SecurityKeyPair initRsaKey() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            byte[] publicKeyEncoded = publicKey.getEncoded();
            byte[] privateKeyEncoded = keyPair.getPrivate().getEncoded();
            String modulus = publicKey.getModulus().toString(16);
            String exponent = publicKey.getPublicExponent().toString(16);

            // 十六进制
            String publicKeyHex = encodeHex(publicKeyEncoded);
            String privateKeyHex = encodeHex(privateKeyEncoded);
            // base64
            String publicKeyBase64 = Base64.encode(publicKeyEncoded);
            String privateKeyBase64 = Base64.encode(privateKeyEncoded);

            return new SecurityKeyPair(publicKeyHex, privateKeyHex, publicKeyBase64, privateKeyBase64, modulus,
                    exponent);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA 私钥解密
     *
     * @param content    RSA公钥加密过过的内容
     * @param privateKey 私钥
     * @return 明文
     */
    public static byte[] rsaDecryptByPrivateKey(byte[] content, byte[] privateKey) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey generatePrivate = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            // 创建密码器
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, generatePrivate);
            return cipher.doFinal(content);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA 私钥解密
     *
     * @param content    RSA公钥加密过过的内容
     * @param privateKey 私钥(Hex格式)
     * @return 明文
     */
    public static String rsaDecryptByPrivateKey(String content, String privateKey) {
        try {
            byte[] bytes = rsaDecryptByPrivateKey(decodeHex(content), decodeHex(privateKey));
            return new String(bytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA 私钥解密
     *
     * @param content    RSA公钥加密过过的内容(Base64格式)
     * @param privateKey 私钥(Base64格式)
     * @return 明文
     */
    public static String rsaDecryptByPrivateKeyBase64(String content, String privateKey) {
        try {
            byte[] bytes = rsaDecryptByPrivateKey(Base64.decode(content), Base64.decode(privateKey));
            return new String(bytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA 公钥加密
     *
     * @param content   需要被加密的字符串
     * @param publicKey 公钥
     * @return 密文
     */
    public static byte[] rsaEncryptByPublicKey(byte[] content, byte[] publicKey) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey generatePublic = keyFactory.generatePublic(x509EncodedKeySpec);

            // 创建密码器
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, generatePublic);
            return cipher.doFinal(content);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RSA 公钥加密
     *
     * @param content   需要被加密的字符串
     * @param publicKey 公钥(Hex格式)
     * @return 密文
     */
    public static String rsaEncryptByPublicKey(String content, String publicKey) {
        try {
            byte[] bytes = rsaEncryptByPublicKey(content.getBytes(CHARSETNAME_UTF_8), decodeHex(publicKey));
            return encodeHex(bytes);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取crc32 编码
     *
     * @param bytes
     * @return
     */
    public static String crc32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return Long.toHexString(crc32.getValue());
    }

    /**
     * 获取crc32 编码
     *
     * @param str
     * @return
     */
    public static String crc32(String str) {
        return crc32(str, CHARSETNAME_UTF_8);
    }

    /**
     * 获取crc32 编码
     *
     * @param str
     * @param charset
     * @return
     */
    public static String crc32(String str, String charset) {
        try {
            byte[] bytes = str.getBytes(charset);
            return crc32(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取crc32 编码
     *
     * @param file
     * @return
     */
    public static String crc32(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            return crc32(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取crc32 编码
     *
     * @param input
     * @return
     */
    public static String crc32(InputStream input) {
        CRC32 crc32 = new CRC32();
        CheckedInputStream checkInputStream = null;
        int test = 0;
        try {
            checkInputStream = new CheckedInputStream(input, crc32);
            do {
                test = checkInputStream.read();
            } while (test != -1);
            return Long.toHexString(crc32.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用于保存非对称加密的公钥和密钥
     */
    public static class SecurityKeyPair implements Serializable {

        public final String publicKeyHex;
        public final String publicKeyBase64;
        public final String privateKeyHex;
        public final String privateKeyBase64;
        public final String publicKeyModule;
        public final String publicKeyExponent;

        public SecurityKeyPair(String publicKeyHex, String privateKeyHex, String publicKeyBase64,
                String privateKeyBase64, String publicKeyModule, String publicKeyExponent) {
            this.publicKeyHex = publicKeyHex;
            this.privateKeyHex = privateKeyHex;
            this.publicKeyBase64 = publicKeyBase64;
            this.privateKeyBase64 = privateKeyBase64;
            this.publicKeyModule = publicKeyModule;
            this.publicKeyExponent = publicKeyExponent;
        }

        public PublicKeyInfo getPublicKeyInfo() {
            return new PublicKeyInfo(this.publicKeyModule, this.publicKeyExponent);
        }

        public static class PublicKeyInfo {

            private String module;
            private String exponent;

            public PublicKeyInfo(String module, String exponent) {
                this.module = module;
                this.exponent = exponent;
            }

            public String getModule() {
                return module;
            }

            public String getExponent() {
                return exponent;
            }
        }

        @Override
        public String toString() {
            return "SecurityKeyPair{" + "\npublicKeyHex='" + publicKeyHex + '\'' + ", \npublicKeyBase64='"
                    + publicKeyBase64 + '\'' + ", \nprivateKeyHex='" + privateKeyHex + '\'' + ", \nprivateKeyBase64='"
                    + privateKeyBase64 + '\'' + ", \npublicKeyModule='" + publicKeyModule + '\''
                    + ", \npublicKeyExponent='"
                    + publicKeyExponent + '\'' + '}';
        }
    }

}