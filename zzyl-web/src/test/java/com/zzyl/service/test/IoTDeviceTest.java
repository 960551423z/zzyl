package com.zzyl.service.test;

import com.alibaba.fastjson.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.QueryProductListRequest;
import com.aliyun.iot20180120.models.QueryProductListResponse;
import com.zzyl.properties.AliIoTConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 阿庆
 */
@SpringBootTest
public class IoTDeviceTest {


    @Autowired
    private Client client;

    @Autowired
    private AliIoTConfigProperties aliIoTConfigProperties;

    /**
     * 查询公共实例下的所有产品
     * @throws Exception
     */
    @Test
    public void selectProduceList() throws Exception {

        QueryProductListRequest queryProductListRequest = new QueryProductListRequest();
        //分页条件
        queryProductListRequest.setPageSize(10);
        queryProductListRequest.setCurrentPage(1);
        //公共实例
        queryProductListRequest.setIotInstanceId(aliIoTConfigProperties.getIotInstanceId());
        //查询公共实例下的所有产品
        QueryProductListResponse queryProductListResponse = client.queryProductList(queryProductListRequest);

        //打印数据
        System.out.println(JSON.toJSONString(queryProductListResponse.getBody().getData()));
    }
}
