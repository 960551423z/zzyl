package com.zzyl.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RequestUtil {

    private static final String HN_REFERER = "referer";

    public static String headerValue(HttpServletRequest request, String name) {
        return request.getHeader(name);
    }

    public static String parameterValue(HttpServletRequest request, String name) {
        return request.getParameter(name);
    }

    public static String referer(HttpServletRequest request) {
        return request.getHeader("referer");
    }
}
