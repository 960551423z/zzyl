package com.zzyl.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName AliOssConfigProperties.java
 * 
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@Configuration
@ConfigurationProperties(prefix = "zzyl.framework.oss")
public class AliOssConfigProperties {

    /**
     * 域名站点
     */
    private String endpoint ;

    /**
     * 秘钥Id
     */
    private String accessKeyId ;

    /**
     * 秘钥
     */
    private String accessKeySecret ;

    /**
     * 桶名称
     */
    private String bucketName ;

}

