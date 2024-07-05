package com.zzyl.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 老人实体类
 */
@Data
@ApiModel(description = "选择老人实体类")
public class ChoiceElderVo {

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
    private String idCardNo;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String phone;
    /**
     * 入住开始时间
     */
    @ApiModelProperty(value = "入住开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String checkInStartTime;

    /**
     * 入住结束时间
     */
    @ApiModelProperty(value = "入住结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String checkInEndTime;

    /**
     * 护理等级
     */
    @ApiModelProperty(value = "护理等级")
    private String nursingLevelName;

    /**
     * 入住床位
     */
    @ApiModelProperty(value = "入住床位")
    private String bedNo;

    /**
     * 签约合同
     */
    @ApiModelProperty(value = "签约合同")
    private String contractName;

    /**
     * 合同URL
     */
    @ApiModelProperty(value = "合同URL")
    private String contractUrl;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    /**
     * 养老顾问
     */
    @ApiModelProperty(value = "养老顾问")
    private String counselor;


    @ApiModelProperty(value = "护理员名称")
    private String nursingName;

}
