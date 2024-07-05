package com.zzyl.utils;

import com.google.common.net.InetAddresses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求人的ip
 * @author 阿庆
 */
@Slf4j
public class ClientIpUtil {

    private static final String HN_CND_SRC_IP = "cdn-src-ip";
    private static final String HN_X_REAL_IP = "x-real-ip";
    private static final String HN_X_FORWARDED_FOR = "x-forwarded-for";
    private static final String SR_IP = ",";
    private static final String IP_UNKNOWN = "127.0.0.1";


    public static String clientIp(HttpServletRequest request) {
        String ip = null;
        String cdnSrcIp = RequestUtil.headerValue(request, "cdn-src-ip");
        String realIp = RequestUtil.headerValue(request, "x-real-ip");
        String forwardedFor = RequestUtil.headerValue(request, "x-forwarded-for");
        String remoteAddr = request.getRemoteAddr();
        log.info("cdnSrcIp:{}, realIp:{}, forwardedFor:{}, remoteAddr:{}", cdnSrcIp, realIp, forwardedFor, remoteAddr);
        if (StringUtils.isNotBlank(cdnSrcIp)) {
            ip = ip(cdnSrcIp);
        } else if (StringUtils.isNotBlank(realIp)) {
            ip = ip(realIp);
        } else if (StringUtils.isNotBlank(forwardedFor)) {
            ip = ip(forwardedFor);
        } else if (StringUtils.isNotBlank(remoteAddr)) {
            ip = remoteAddr;
        }

        ip = ipValid(ip) ? ip : "127.0.0.1";
        log.debug("client_ip:{}", ip);
        return ip;
    }

    private static String ip(String ips) {
        return StringUtils.split(ips, ",")[0];
    }

    private static boolean ipValid(String ip) {
        return StringUtils.isNotBlank(ip) && InetAddresses.isInetAddress(ip);
    }
}
