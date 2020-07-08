import java.security.MessageDigest;

/**
 * Description: MD5加密工具类
 * Author: DUHAIHANG - ABCC
 * Date: 2018/11/16 11:48
 * Version: V1.0
 */
public class MD5Util {

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String MD5Encode(String s) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(s.getBytes());
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return s;
        }
    }
}
