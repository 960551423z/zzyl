package com.zzyl.config;

import com.aliyun.iot20180120.Client;
import com.aliyun.teaopenapi.models.Config;
import com.zzyl.properties.AliIoTConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IotClientConfig {

    @Autowired
    private AliIoTConfigProperties aliIoTConfigProperties;

    /**
     * @return
     * @throws Exception
     */
    @Bean
    public Client instance() throws Exception {
        Config config = new Config();
        config.accessKeyId = aliIoTConfigProperties.getAccessKeyId();
        config.accessKeySecret = aliIoTConfigProperties.getAccessKeySecret();
        // 您的可用区ID 默认上海
        config.regionId = aliIoTConfigProperties.getRegionId();
        return new Client(config);
    }
}
