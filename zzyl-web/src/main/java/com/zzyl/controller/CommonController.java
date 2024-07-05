package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import com.zzyl.config.OSSAliyunFileStorageService;
import com.zzyl.exception.BaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private OSSAliyunFileStorageService fileStorageService;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 上传结果
     * @throws Exception 异常
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public ResponseResult<String> upload(
            @ApiParam(value = "上传的文件", required = true)
            @RequestPart("file") MultipartFile file) throws Exception {

        // 校验是否为图片文件
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        } catch (Exception e) {
            return ResponseResult.error();
        }

        if (file.getSize() == 0) {
            throw new BaseException("上传图片不能为空");
        }

        // 获得原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获得文件扩展名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        String filePath = fileStorageService.store(fileName, file.getInputStream());

        return ResponseResult.success("", filePath);
    }

}
