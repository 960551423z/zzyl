
package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户老人关联实体类
 */
@Data
@ApiModel(value = "MemberElderDto", description = "客户老人关联实体类")
public class MemberElderDto extends BaseDto {

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Long memberId;

    /**
     * 老人id
     */
    @ApiModelProperty(value = "老人id")
    private Long elderId;



    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idCard;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;


    /**
     * 关系
     */
    @ApiModelProperty(value = "关系")
    private String refName;

    /**
     * 关系Id
     */
    @ApiModelProperty(value = "关系Id")
    private String refId;
}


