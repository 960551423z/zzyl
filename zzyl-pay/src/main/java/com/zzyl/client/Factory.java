package com.zzyl.client;

import com.zzyl.client.operators.Common;
import com.zzyl.client.operators.Wap;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * @ClassName Factory.java
 *  封装对于微信支付工程类
 */
@Data
@Log4j2
public class Factory {

    Config config;

    public void setOptions(Config config) {
        this.config = config;
    }

    //基础服务
    public Common Common(){
        return new Common(config);
    }



    //手机网页支付
    public Wap Wap(){
        return new Wap(config);
    }


}
