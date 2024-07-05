package com.zzyl.vo;

import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色表
 */
@Data
@NoArgsConstructor
public class RoleVo extends BaseVo {

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    private String label;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "列表选择：选中角色Ids")
    private String[] checkedIds;

    @ApiModelProperty(value = "TREE结构：选中资源No")
    private String[] checkedResourceNos;

    @ApiModelProperty(value = "TREE结构：选中部门No")
    private String[] checkedDeptNos;

    @ApiModelProperty(value = "人员查询部门：当前人员Id")
    private String userId;

    @ApiModelProperty(value = "数据范围（0自定义  1本人 2本部门及以下 3本部门 4全部）")
    private String dataScope;

    private String dataState;

    @Builder
    public RoleVo(Long id, String dataState, String roleName, String label, Integer sortNo, String remark, String[] checkedIds, String[] checkedResourceNos, String[] checkedDeptNos, String userId, String dataScope, String dataState1) {
        super(id, dataState);
        this.roleName = roleName;
        this.label = label;
        this.sortNo = sortNo;
        this.remark = remark;
        this.checkedIds = checkedIds;
        this.checkedResourceNos = checkedResourceNos;
        this.checkedDeptNos = checkedDeptNos;
        this.userId = userId;
        this.dataScope = dataScope;
        this.dataState = dataState1;
    }
}
