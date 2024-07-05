package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Dept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父部门编号
     */
    private String parentDeptNo;

    /**
     * 部门编号:
     */
    private String deptNo;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

    /**
     * 负责人Id
     */
    private Long leaderId;

    /**
     * 负责人姓名
     */
    private String leaderName;


}
