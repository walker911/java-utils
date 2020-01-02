package com.walker.utils.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author walker
 * @date 2019/5/15
 */
public class TemplateDemo {

    private static Pattern templatePattern = Pattern.compile("\\{(\\w+)\\}");

    public static String templateEngine(String template, Map<String, Object> params) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = templatePattern.matcher(template);
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = params.get(key);
            matcher.appendReplacement(buffer,  value == null ? "" : Matcher.quoteReplacement(value.toString()));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static void main(String[] args) {
        String template = "Hi {name}, your code is {code}";
        Map<String, Object> params = new HashMap<>();
        params.put("name", "老马");
        params.put("code", 6789);
        System.out.println(templateEngine(template, params));
    }
}
