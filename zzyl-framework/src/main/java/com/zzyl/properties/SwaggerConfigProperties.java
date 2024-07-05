package com.zzyl.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 *  SwaggerConfigProperties配置类
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "zzyl.framework.swagger")
public class SwaggerConfigProperties implements Serializable {

    public String swaggerPath;

    public String title;

    public String description;

    public String contactName;

    public String contactUrl;

    public String contactEmail;
}
