package com.walker.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author walker
 * @date 2019/5/14
 */
public class RegexDemo {

    private static Pattern strPattern = Pattern.compile("<a>.*?</a>");

    public static void main(String[] args) {
        find();
        findGroup();
        replace();
        replaceCat();
        findStr();
    }

    public static void findStr() {
        String str = "<a>first</a><a>second</a>";
        Matcher matcher = strPattern.matcher(str);
        while (matcher.find()) {
            System.out.println("find: " + matcher.group());
        }
    }

    public static void find() {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        Pattern datePattern = Pattern.compile(regex);
        String str = "today is 2019-05-15, yesterday is 2019-05-14";
        Matcher matcher = datePattern.matcher(str);
        while (matcher.find()) {
            System.out.println("find: " + matcher.group() + " position: " + matcher.start() + "-" + matcher.end());
        }
    }

    public static void findGroup() {
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        String str = "today is 2019-05-15, yesterday is 2019-05-14";
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println("year: " + matcher.group(1) + " month: " + matcher.group(2) + " day: " + matcher.group(3));
        }
    }

    public static void replace() {
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})";
        String str = "today is 2019-05-15.";
        System.out.println(str.replaceFirst(regex, "$1/$2/$3"));
    }

    public static void replaceCat() {
        Pattern catPattern = Pattern.compile("cat");
        Matcher matcher = catPattern.matcher("one cat, two cat, three cat");
        StringBuffer buffer = new StringBuffer();
        int foundNum = 0;
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "dog");
            foundNum++;
            if (foundNum == 2) {
                break;
            }
        }
        matcher.appendTail(buffer);
        System.out.println(buffer.toString());
    }
}
