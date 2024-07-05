package com.zzyl.config;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;
import com.zzyl.properties.AliOssConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class OSSAliyunFileStorageService {

    @Autowired
    OSS ossClient;

    @Autowired
    AliOssConfigProperties aliOssConfigProperties;

    /**
     * 上传文件
     * @param objectName  文件名
     * @param inputStream  文件流对象
     * @return
     */
    public String store(String objectName, InputStream inputStream) {
        //文件读取路径
        String url = null;
        // 判断文件
        if (inputStream == null) {
            log.error("上传文件：objectName{}文件流为空", objectName);
            return url;
        }
        log.info("OSS文件上传开始：{}", objectName);
        try {
            String bucketName = aliOssConfigProperties.getBucketName();

            // 指定文件夹
            objectName  = "zzyl/" + objectName;

            // 上传文件
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, inputStream);


            PutObjectResult result = ossClient.putObject(request);
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                log.info("OSS文件上传成功：{}", objectName);

            }
        } catch (OSSException oe) {
            log.error("OSS文件上传错误:{}", oe);
        } catch (ClientException ce) {
            log.error("OSS文件上传客户端错误:{}", ce);
        }
        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(aliOssConfigProperties.getBucketName())
                .append(".")
                .append(aliOssConfigProperties.getEndpoint())
                .append("/")
                .append(objectName);

        return stringBuilder.toString();
    }

    /**
     * 根据url删除文件
     * @param pathUrl   url地址（全路径）
     */
    public void delete(String pathUrl) {
        String prefix = "https://"+aliOssConfigProperties.getBucketName()+"."+ aliOssConfigProperties.getEndpoint()+"/zzyl/";
        String key = pathUrl.replace(prefix, "");
        List<String> keys = Lists.newArrayList();
        keys.add(key);
        // 删除Objects
        ossClient.deleteObjects(new DeleteObjectsRequest(aliOssConfigProperties.getBucketName()).withKeys(keys));
    }

}
