package com.googlecode.easyec.security.utils;

import com.googlecode.easyec.security.IllegalPemFileException;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.Certificate;

import static java.security.Security.addProvider;
import static java.security.Security.getProvider;

/**
 * 读取PEM文件的工具类
 *
 * @author JunJie
 */
public class PemUtils {

    static {
        if (getProvider("BC") == null) {
            addProvider(new BouncyCastleProvider());
        }
    }

    private PemUtils() { /* no op */ }

    /**
     * 判断是否为<code>KeyPair</code>对象
     *
     * @param obj 从PEM文件解析出来的对象
     * @return bool值
     */
    public static boolean isKeyPair(Object obj) {
        return obj != null && obj instanceof KeyPair;
    }

    /**
     * 判断是否为<code>Certificate</code>对象
     *
     * @param obj 从PEM文件解析出来的对象
     * @return bool值
     */
    public static boolean isCertificate(Object obj) {
        return obj != null && obj instanceof Certificate;
    }

    /**
     * 判断是否为<code>PublicKey</code>对象
     *
     * @param obj 从PEM文件解析出来的对象
     * @return bool值
     */
    public static boolean isPublicKey(Object obj) {
        return obj != null && obj instanceof PublicKey;
    }

    /**
     * 读取PEM文件并解析成相应的对象
     *
     * @param in PEM文件流
     * @return 证书对象实例或是秘钥对象实例
     * @throws IllegalPemFileException PEM文件格式不正确的异常信息
     */
    public static Object read(InputStream in) throws IllegalPemFileException {
        return read(in, null, "utf-8");
    }

    /**
     * 读取PEM文件并解析成相应的对象
     *
     * @param in       PEM文件流
     * @param password PEM文件密码
     * @return 证书对象实例或是秘钥对象实例
     * @throws IllegalPemFileException PEM文件格式不正确的异常信息
     */
    public static Object read(InputStream in, char[] password) throws IllegalPemFileException {
        return read(in, password, "utf-8");
    }

    /**
     * 读取PEM文件并解析成相应的对象
     *
     * @param in      PEM文件流
     * @param charset 解析文件的字符集
     * @return 证书对象实例或是秘钥对象实例
     * @throws IllegalPemFileException PEM文件格式不正确的异常信息
     */
    public static Object read(InputStream in, String charset) throws IllegalPemFileException {
        return read(in, null, charset);
    }

    /**
     * 读取PEM文件并解析成相应的对象
     *
     * @param in       PEM文件流
     * @param password PEM文件密码
     * @param password PEM文件密码
     * @return 证书对象实例或是秘钥对象实例
     * @throws IllegalPemFileException PEM文件格式不正确的异常信息
     */
    public static Object read(InputStream in, char[] password, String charset) throws IllegalPemFileException {
        try {
            return new PEMReader(
                new InputStreamReader(in, Charset.forName(charset)),
                new DefaultPasswordFinder(password)
            ).readObject();
        } catch (Exception e) {
            throw new IllegalPemFileException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /* 默认的存储密码的对象类 */
    private static class DefaultPasswordFinder implements PasswordFinder {

        private char[] password;

        private DefaultPasswordFinder(char[] password) {
            this.password = password;
        }

        @Override
        public char[] getPassword() {
            return password;
        }
    }
}
