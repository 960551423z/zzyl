
package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import com.zzyl.vo.retreat.ElderVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "ContractVo", description = "合同信息")
public class ContractVo extends BaseVo {
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String name;

    /**
     * 丙方手机号
     */
    @ApiModelProperty(value = "丙方手机号")
    private String memberPhone;

    /**
     * 丙方名称
     */
    @ApiModelProperty(value = "丙方名称")
    private String memberName;

    /**
     * 老人名称
     */
    @ApiModelProperty(value = "老人名称")
    private String elderName;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    /**
     * 合同pdf文件地址
     */
    @ApiModelProperty(value = "合同pdf文件地址")
    private String pdfUrl;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 老人id
     */
    @ApiModelProperty(value = "老人id")
    private Long elderId;

    /**
     * 合同开始时间
     */
    @ApiModelProperty(value = "合同开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 合同结束时间
     */
    @ApiModelProperty(value = "合同结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 合同状态
     */
    @ApiModelProperty(value = "合同状态  0：未生效，1：已生效，2：已过期, 3：已失效")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 级别描述
     */
    @ApiModelProperty(value = "级别描述")
    private String levelDesc;


    /**
     * 入住编号
     */
    @ApiModelProperty(value = "入住编号")
    private String checkInNo;

    /**
     * 签约时间
     */
    @ApiModelProperty(value = "签约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signDate;

    /**
     * 解除提交人
     */
    @ApiModelProperty(value = "解除提交人")
    private String releaseSubmitter;

    /**
     * 解除时间
     */
    @ApiModelProperty(value = "解除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseDate;

    /**
     * 解除pdf文件地址
     */
    @ApiModelProperty(value = "解除pdf文件地址")
    private String releasePdfUrl;

    @ApiModelProperty(value = "老人")
    private ElderVo elderVo;

    @ApiModelProperty(value = "房间")
    private RoomVo roomVo;
}


