package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import com.zzyl.dto.ElderDto;
import com.zzyl.vo.BedVo;
import com.zzyl.vo.CheckInConfigVo;
import com.zzyl.vo.NursingLevelVo;
import com.zzyl.vo.RoomVo;
import com.zzyl.vo.retreat.ElderVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * @author 阿庆
 */
@Data
@ApiModel(description = "入住实体类")
public class CheckIn extends BaseEntity {

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
     * 老人id
     */
    @ApiModelProperty(value = "老人id")
    private Long elderId;

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
     *  1:入住评估
     *  2:入住审批
     *  3:入住配置
     *  4:签约办理
     */
    @ApiModelProperty(value = "流程状态")
    private Integer flowStatus;
    /**
     * 状态（1：申请中，2:已完成,3:已关闭）
     */
    @ApiModelProperty(value = "状态")
    private Integer status;


    private ElderVo elderVo;

    private RoomVo roomVo;

    private CheckInConfigVo checkInConfigVo;

    private NursingLevelVo nursingLevelVo;
    private BedVo bedVo;

    //状态枚举类
    @Alias("CheckInFolwStatus")
    public enum FlowStatus{
        APPLY(0, "入住申请", "养老顾问"),
        REVIEW(1, "入住评估", "护理组主管"),
        APPROVAL(2, "入住审核", "院长"),
        CONFIG(3, "入住配置", "养老顾问"),
        SIGN(4, "签约办理", "法务");
        Integer code;
        String name;
        String role;

        FlowStatus(Integer code, String name, String role){
            this.code = code;
            this.name = name;
            this.role = role;
        }
        public Integer getCode(){
            return this.code;
        }

        public String getName(){
            return this.name;
        }

        public String getRole(){
            return this.role;
        }
    }

    //状态枚举类
    @Alias("CheckInStatus")
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
