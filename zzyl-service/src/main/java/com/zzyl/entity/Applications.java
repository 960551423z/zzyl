package com.zzyl.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @author 阿庆
*/
@Data
public class Applications implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    private Long id;

    /**
    * 申请人
    */
    private String applicat;

    /**
    * 申请人id
    */
    private Long applicatId;

    /**
    * 申请时间
    */
    private LocalDateTime applicationTime;

    /**
    * 编码
    */
    private String code;

    /**
    * 完成时间
    */
    private LocalDateTime finishTime;

    /**
    * 状态（1：申请中，2:已完成，3:已关闭）
    */
    private Integer status;

    /**
    * 标题
    */
    private String title;

    /**
    * 类型（1：退住，2：请假，3：入住）
    */
    private Integer type;


}
