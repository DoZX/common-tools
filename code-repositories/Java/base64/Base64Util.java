import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Description: Base64 转码/解码工具类
 * Author: DUHAIHANG - ABCC
 * Date: 2018/10/19 12:06
 * Version: V1.0
 */
public class Base64Util {

    private static class BASE64 {
        private static final BASE64Encoder ENCODER = new BASE64Encoder();
        private static final BASE64Decoder DECODER = new BASE64Decoder();
    }

    /**
     * @Author DUHAIHANG
     * @Description BASE64 转码
     * @Date 15:55 2018/10/19
     * @Param [str 字符串]
     * @return java.lang.String
     */
    public static String getEncryptionStr(String str) {
        return BASE64.ENCODER.encode(str.getBytes());
    }

    /**
     * @Author DUHAIHANG
     * @Description BASE64 解码
     * @Date 15:55 2018/10/19
     * @Param [str 字符串 ,charsetName 编码格式]
     * @return java.lang.String
     */
    public static String getDecryptStr(String str) {
        try {
            return new String(BASE64.DECODER.decodeBuffer(str), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getDecryptStr(String str, String charsetName) {
        try {
            return new String(BASE64.DECODER.decodeBuffer(str), charsetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
