package com.walker.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author walker
 * @date 2019/5/15
 */
public class ValidateUtil {
    /**
     * 邮编
     */
    private static Pattern ZIP_CODE_PATTERN = Pattern.compile("(?<![0-9])[0-9]{6}(?![0-9])");

    /**
     * 2-9位中文
     */
    private static Pattern CHINESE_PATTERN = Pattern.compile("^[\\u4e00-\\u9fff]{2,9}$");

    /**
     * 手机
     */
    private static Pattern MOBILE_PHONE_PATTERN = Pattern.compile("(?<![0-9])((0|\\+86|0086)\\s?)" +
            "?1[3-9][0-9]-?[0-9]{4}-?[0-9]{4}(?![0-9])");

    /**
     * 固定电话
     */
    private static Pattern FIXED_PHONE_PATTERN = Pattern.compile("(?<![0-9])(\\(?0[0-9]{2,3}\\)?-?)?[0-9]{7,8}" +
            "(?![0-9])");

    /**
     * 日期
     */
    private static Pattern DATE_PATTERN = Pattern.compile("(?<![0-9])\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2](0-9)|3[01])" +
            "(?![0-9])");

    /**
     * 时间
     */
    private static Pattern TIME_PATTERN = Pattern.compile("(?<![0-9])([0-1][0-9]|2[0-3]):[0-5][0-9](?![0-9])");

    /**
     * 身份证
     */
    private static Pattern ID_CARD_PATTERN = Pattern.compile("(?<![0-9])[1-9](0-9){14}([0-9]{2}[0-9xX])?(?![0-9])");

    /**
     * IP地址
     */
    private static Pattern IP_PATTERN = Pattern.compile("(?<![0-9])" +
            "((0{0,2}[0-9]|0?[0-9]{2}|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "(0{0,2}[0-9]|0?[0-9]{2}|1[0-9]{2}|2[0-4][0-9]|25[0-5])(?![0-9])");

    /**
     * 新浪邮箱
     */
    private static Pattern SINA_EMAIL_PATTERN = Pattern.compile("[a-z0-9][a-z0-9_]{2,14}[a-z0-9]@sina\\.com");

    /**
     * QQ邮箱
     */
    private static Pattern QQ_EMAIL_PATTERN = Pattern.compile("(?![-0-9a-zA-Z._]*(--|\\.\\.|__))[a-zA-Z][-0-9a-zA-Z._]{1,16}[a-z0-9A-Z]@qq\\.com");

    /**
     * 邮编
     *
     * @param text
     * @return
     */
    public static boolean isZipCode(String text) {
        return ZIP_CODE_PATTERN.matcher(text).matches();
    }

    /**
     * 中文
     *
     * @param text
     * @return
     */
    public static boolean isChinese(String text) {
        return CHINESE_PATTERN.matcher(text).matches();
    }

    /**
     * 手机号
     *
     * @param text
     * @return
     */
    public static boolean isMobile(String text) {
        return MOBILE_PHONE_PATTERN.matcher(text).matches();
    }

    /**
     * 固定电话
     *
     * @param text
     * @return
     */
    public static boolean isFixedPhone(String text) {
        return FIXED_PHONE_PATTERN.matcher(text).matches();
    }

    /**
     * 日期
     *
     * @param text
     * @return
     */
    public static boolean isDate(String text) {
        return DATE_PATTERN.matcher(text).matches();
    }

    /**
     * 时间
     *
     * @param text
     * @return
     */
    public static boolean isTime(String text) {
        return TIME_PATTERN.matcher(text).matches();
    }

    /**
     * 身份证
     *
     * @param text
     * @return
     */
    public static boolean isIdCard(String text) {
        return ID_CARD_PATTERN.matcher(text).matches();
    }

    /**
     * IP地址
     *
     * @param text
     * @return
     */
    public static boolean isIp(String text) {
        return IP_PATTERN.matcher(text).matches();
    }

    public static void findZipCode(String text) {
        Matcher matcher = ZIP_CODE_PATTERN.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    public static void findMobile(String text) {
        Matcher matcher = MOBILE_PHONE_PATTERN.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    public static void main(String[] args) {
        String text = "邮政编码 100013, 电话18612345678";
        findZipCode(text);
        System.out.println(isZipCode("10001"));
        System.out.println(isMobile("13412345678"));
        findMobile(text);
    }
}
