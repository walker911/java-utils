package com.walker.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;

/**
 * @author walker
 * @date 2019/3/19
 */
public class UrlShortUtil {

    private static final int LENGTH = 6;

    private static char[] digits = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public static String[] shorten(String url) {
        String key = "secret";
        String hex = DigestUtils.md5Hex(key + url);
        int hexLen = hex.length();
        int subHexLen = hexLen / 8;
        String[] shortStr = new String[subHexLen];

        for (int i = 0; i < subHexLen; i++) {
            StringBuilder builder = new StringBuilder();
            int j = i + 1;
            String subHex = hex.substring(i * 8, j * 8);
            long idx = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16);
            for (int k = 0; k < LENGTH; k++) {
                int index = (int) (Long.valueOf("0000003D", 16) & idx);
                builder.append(digits[index]);
                idx = idx >> 5;
            }
            shortStr[i] = builder.toString();
        }
        return shortStr;
    }

    public static void main(String[] args) {
        String url = "https://www.baidu.com/";
        String[] strings = shorten(url);
        System.out.println(Arrays.toString(strings));
        System.out.println(Long.valueOf("3FFFFFFF", 16));
    }
}
