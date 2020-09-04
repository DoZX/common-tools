package com.mask.ct.util;

/**
 * String utils
 * @author mask(mask616@163.com)
 * @date 2020.08.04
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
