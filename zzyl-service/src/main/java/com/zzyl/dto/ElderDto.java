package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 老人实体类
 */
@Data
public class ElderDto extends BaseDto {

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String image;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 手机号
     */
    private String phone;

    @ApiModelProperty(value = "年龄")
    private String age;

    @ApiModelProperty(value = "性别")
    private String sex;
}

