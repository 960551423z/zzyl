package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门表
 */
@Data
@NoArgsConstructor
public class DeptVo extends BaseVo {

    @ApiModelProperty(value = "父部门编号")
    private String parentDeptNo;

    @ApiModelProperty(value = "部门编号")
    private String deptNo;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "负责人Id")
    private Long leaderId;

    @ApiModelProperty(value = "负责人姓名")
    private String leaderName;

    @ApiModelProperty(value = "角色查询部门：部门对应角色id")
    private String roleId;

    @ApiModelProperty(value = "层级")
    private Integer level = 4;

    @Builder
    public DeptVo(Long id, String dataState, String parentDeptNo, String deptNo, String deptName, Integer sortNo, Long leaderId, String leaderName, String roleId, Integer level) {
        super(id, dataState);
        this.parentDeptNo = parentDeptNo;
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.sortNo = sortNo;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.roleId = roleId;
        this.level = level;
    }
}
