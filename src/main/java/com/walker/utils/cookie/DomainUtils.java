package com.walker.utils.cookie;

import java.net.MalformedURLException;
import java.net.URL;

public class DomainUtils {
    public static void main(String[] args){
        System.out.println(getDomain());
    }

    /**
     * 从 URL 中获取域名
     * @return
     */
    public static String getDomain() {
        URL url = null;
        try {
            url = new URL("https://www.baidu.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String domain = url.getHost();
        return domain;
    }
}
