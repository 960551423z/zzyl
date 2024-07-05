package com.zzyl.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseEntity;
import com.zzyl.vo.RoomVo;
import com.zzyl.vo.retreat.ElderVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 合同实体类
 */
@Data
public class Contract extends BaseEntity {

    /**
     * 合同名称
     */
    private String name;

    /**
     * 丙方手机号
     */
    private String memberPhone;

    /**
     * 丙方名称
     */
    private String memberName;

    /**
     * 老人名称
     */
    private String elderName;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同pdf文件地址
     */
    private String pdfUrl;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 老人id
     */
    private Long elderId;

    /**
     * 合同开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 合同结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 合同状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 级别描述
     */
    private String levelDesc;

    /**
     * 入住编号
     */
    private String checkInNo;

    /**
     * 签约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signDate;

    /**
     * 解除提交人
     */
    private String releaseSubmitter;

    /**
     * 解除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime releaseDate;

    /**
     * 解除pdf文件地址
     */
    private String releasePdfUrl;

    private ElderVo elderVo;

    private RoomVo roomVo;
}

