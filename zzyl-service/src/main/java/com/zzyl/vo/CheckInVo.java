package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import com.zzyl.dto.ElderDto;
import com.zzyl.dto.MemberElderDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 阿庆
 */
@Data
@ApiModel(description = "入住实体类")
public class CheckInVo extends BaseVo {

    /**
     * 入住单号
     */
    @ApiModelProperty(value = "入住单号")
    private String checkInCode;

    /**
     * 入住标题
     */
    @ApiModelProperty(value = "入住标题")
    private String title;


    @ApiModelProperty(value = "老人")
    private ElderDto elderDto;

    /**
     * 老人id
     */
    @ApiModelProperty(value = "老人id")
    private Long elderId;
    
    /**
     * 其他申请信息
     */
    @ApiModelProperty(value = "其他信息")
    private String otherApplyInfo;

    /**
     * 评估信息
     */
    @ApiModelProperty(value = "评估信息")
    private String reviewInfo;

    /**
     * 养老顾问
     */
    @ApiModelProperty(value = "养老顾问")
    private String counselor;

    /**
     * 入住时间
     */
    @ApiModelProperty(value = "入住时间")
    private LocalDateTime checkInTime;

    /**
     * 入住原因
     */
    @ApiModelProperty(value = "入住原因")
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
     *  0:申请入住
     *  1:申请审批
     *  2:解除合同
     *  3:调整账单
     *  4:账单审批
     *  5:入住审批
     *  6:费用算清
     */
    @ApiModelProperty(value = "流程状态")
    private Integer flowStatus;
    /**
     * 状态（1：申请中，2:已完成,3:已关闭）
     */
    @ApiModelProperty(value = "状态")
    private Integer status;


    /**
     * 是否展示退住数据
     * 0:其他用户，审核中
     * 1:当前为审核人，可以正常查看数据
     */
    @ApiModelProperty(value = "是否展示退住数据")
    private Integer isShow;

    /**
     * 家属信息
     */
    @ApiModelProperty(value = "家属信息")
    private List<MemberElderDto> memberElderDtos;

    /**
     * 一寸照片
     */
    @ApiModelProperty(value = "一寸照片")
    private String url1;

    /**
     * 身份证人像面
     */
    @ApiModelProperty(value = "身份证人像面")
    private String url2;

    /**
     * 身份证国徽面
     */
    @ApiModelProperty(value = "身份证国徽面")
    private String url3;

    /**
     * 能力评估
     */
    @ApiModelProperty(value = "能力评估")
    private String reviewInfo1;
    /**
     * 评估报告
     */
    @ApiModelProperty(value = "评估信息")
    private String reviewInfo2;

    @ApiModelProperty(value = "房间")
    private RoomVo roomVo;

    @ApiModelProperty(value = "入住配置")
    private CheckInConfigVo checkInConfigVo;

    @ApiModelProperty(value = "护理等级")
    private NursingLevelVo nursingLevelVo;
    @ApiModelProperty(value = "床位")
    private com.zzyl.vo.BedVo bedVo;

    @ApiModelProperty(value = "签约信息")
    private ContractVo contractVo;

    //状态枚举类
    @Alias("RetreatFolwStatus")
    public enum FlowStatus{
        APPLY(0, ""),
        APPLY_APPROVAL(1, ""),
        RESCISSION_CONTRACT(2, ""),
        RECONCILIATION_BILL(3, ""),
        BILL_APPROVAL(4, ""),
        RETREAT_APPROVAL(5, ""),
        BILL_SETTLEMENT(6, "");
        Integer code;
        String name;
        FlowStatus(Integer code, String name){
            this.code = code;
            this.name = name;
        }
        public Integer getCode(){
            return this.code;
        }

        public String getName(){
            return this.name;
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


}
