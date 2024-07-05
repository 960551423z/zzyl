package com.zzyl.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *  忽略配置及跨域
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "zzyl.framework.security")
@Configuration
public class SecurityConfigProperties {

    String defaulePassword ;

    List<String> ignoreUrl = new ArrayList<>();

    List<String> origins = new ArrayList<>();

    String loginPage;

    //令牌有效时间
    Integer accessTokenValiditySeconds = 3*24*3600;

    //刷新令牌有效时间
    Integer refreshTokenValiditySeconds= 7*24*3600;
}
