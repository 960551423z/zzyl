package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * @author 阿庆
 */
@Data
@ApiModel(description = "退住实体类")
public class Retreat extends BaseEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 退住单号
     */
    @ApiModelProperty(value = "退住单号")
    private String retreatCode;

    /**
     * 退住标题
     */
    @ApiModelProperty(value = "退住标题")
    private String title;

    /**
     * 老人id
     */
    @ApiModelProperty(value = "老人id")
    private Long elderId;

    /**
     * 老人姓名姓名
     */
    @ApiModelProperty(value = "老人姓名")
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
    private LocalDateTime checkInStartTime;

    /**
     * 入住结束时间
     */
    @ApiModelProperty(value = "入住结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInEndTime;

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

    /**
     * 退住时间
     */
    @ApiModelProperty(value = "退住时间")
    private LocalDateTime checkOutTime;

    /**
     * 退住原因
     */
    @ApiModelProperty(value = "退住原因")
    private String reason;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人")
    private String applicat;

    /**
     * 申请人部门编号
     */
    @ApiModelProperty(value = "申请人")
    private String deptNo;

    /**
     * 申请人id
     */
    @ApiModelProperty(value = "申请人id")
    private Long applicatId;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;
    /**
     * 流程状态
     *  0:申请退住
     *  1:申请审批
     *  2:解除合同
     *  3:调整账单
     *  4:账单审批
     *  5:退住审批
     *  6:费用算清
     */
    @ApiModelProperty(value = "流程状态")
    private Integer flowStatus;
    /**
     * 状态（1：申请中，2:已完成,3:已关闭）
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
    //状态枚举类
    @Alias("RetreatFolwStatus")
    public enum FlowStatus{
        APPLY(0),
        APPLY_APPROVAL(1),
        RESCISSION_CONTRACT(2),
        RECONCILIATION_BILL(3),
        BILL_APPROVAL(4),
        RETREAT_APPROVAL(5),
        BILL_SETTLEMENT(6);
        Integer code;
        FlowStatus(Integer code){
            this.code = code;
        }
        public Integer getCode(){
            return this.code;
        }
    }

    //状态枚举类
    @Alias("RetreatStatus")
    public enum Status{
        APPLICATION(1),
        FINISHED(2),
        CLOSED(3);
        Integer code;
        Status(Integer code){
            this.code = code;
        }
        public Integer getCode(){
            return this.code;
        }
    }


    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "护理员名称")
    private String nursingName;

}
