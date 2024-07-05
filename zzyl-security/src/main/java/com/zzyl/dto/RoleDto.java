package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表
 */
@Data
@NoArgsConstructor
public class RoleDto extends BaseDto {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String label;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "人员查询部门：当前人员Id")
    private String userId;

    @ApiModelProperty(value = "数据范围（0自定义  1本人 2本部门及以下 3本部门 4全部）")
    private String dataScope;

    @ApiModelProperty(value = "是否启用（0:启用，1:禁用）")
    private String dataState;

    @ApiModelProperty(value = "TREE结构：选中资源No")
    private String[] checkedResourceNos;

    @ApiModelProperty(value = "TREE结构：选中部门No")
    private String[] checkedDeptNos;

    @Builder
    public RoleDto(String roleName, String label, Integer sortNo, String remark, String userId, String dataScope, String dataState, String[] checkedResourceNos, String[] checkedDeptNos) {
        this.roleName = roleName;
        this.label = label;
        this.sortNo = sortNo;
        this.remark = remark;
        this.userId = userId;
        this.dataScope = dataScope;
        this.dataState = dataState;
        this.checkedResourceNos = checkedResourceNos;
        this.checkedDeptNos = checkedDeptNos;
    }
}
