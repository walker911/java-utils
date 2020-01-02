package com.walker.utils.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie 工具类
 */
public class CookieUtils {

    /**
     * 得到 cookie 的值，不解码
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 得到 cookie 的值
     *
     * @param request
     * @param cookieName
     * @param isDecoder
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(cookieName)) {
            return null;
        }
        String cookieValue = null;
        try {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        cookieValue = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
                    } else {
                        cookieValue = cookies[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cookieValue;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName, String enc) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(cookieName)) {
            return null;
        }
        String cookieValue = null;
        try {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(cookieName)) {
                    cookieValue = URLDecoder.decode(cookies[i].getValue(), enc);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cookieValue;
    }

    /**
     * 设置 cookie，不设置生效时间，默认浏览器关闭失效，不编码
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * 设置 cookie，设置生效时间，不编码
     *
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieAge
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieAge) {
        setCookie(request, response, cookieName, cookieValue, cookieAge, false);
    }

    /**
     * 设置 cookie，不设置生效时间，编码
     *
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param isEncode
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * 设置 cookie，设置生效时间，编码
     *
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieAge
     * @param isEncode
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieAge, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieAge, isEncode);
    }

    /**
     * 设置 cookie，设置生效时间，指定编码
     *
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieAge
     * @param enc
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieAge, String enc) {
        doSetCookie(request, response, cookieName, cookieValue, cookieAge, enc);
    }

    /**
     * 删除 cookie
     * @param request
     * @param response
     * @param cookieName
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * 设置 cookie，设置生效时间，编码
     *
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieAge
     * @param isEncode
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                   String cookieValue, int cookieAge, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieAge > 0) {
                cookie.setMaxAge(cookieAge);
            }
            setDomain(request, cookie);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置 cookie，设置生效时间，指定编码
     *
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieAge
     * @param enc
     */
    private static void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                   String cookieValue, int cookieAge, String enc) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, enc);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieAge > 0) {
                cookie.setMaxAge(cookieAge);
            }
            setDomain(request, cookie);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setDomain(HttpServletRequest request, Cookie cookie) {
        if (request != null) {
            String domainName = getDomainName(request);
            if (!"localhost".equals(domainName)) {
                cookie.setDomain(domainName);
            }
        }
    }

    private static String getDomainName(HttpServletRequest request) {
        String domainName;
        String serverName = request.getRequestURL().toString();
        if (StringUtils.isBlank(serverName)) {
            domainName = "";
        } else {
            serverName =serverName.toLowerCase();
            // ?
            serverName = serverName.substring(7);
            final int end =serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len =domains.length;
            if (len > 3) {
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        // port
        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] arr = domainName.split("\\:");
            domainName = arr[0];
        }
        return domainName;
    }
}
