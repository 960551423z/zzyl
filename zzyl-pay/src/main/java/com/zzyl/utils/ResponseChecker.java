package com.zzyl.utils;

import com.zzyl.client.response.BasicResponse;

/**
 * @ClassName ResponseChecker.java
 *  微信结果检查
 */
public class ResponseChecker {

    public static boolean success(BasicResponse response) {
        String code = response.getCode();
        return code.equals("200");
    }

}
