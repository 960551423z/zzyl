package com.zzyl.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限表
 */
@Data
@NoArgsConstructor
public class ResourceVo extends BaseVo {

    @ApiModelProperty(value = "资源编号")
    private String resourceNo;

    @ApiModelProperty(value = "父资源编号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String parentResourceNo;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "资源类型：s平台 c目录 m菜单 r按钮")
    private String resourceType;

    @ApiModelProperty(value = "请求地址")
    private String requestPath;

    @ApiModelProperty(value = "权限标识")
    private String label;

    @ApiModelProperty(value = "排序")
    private Integer sortNo;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "列表选择：选中职位Ids")
    private String[] checkedIds;

    @ApiModelProperty(value = "TREE结构：选中资源编号")
    private String[] checkedResourceNos;

    @ApiModelProperty(value = "角色查询资源：资源对应角色id")
    private String roleId;

    @ApiModelProperty(value = "层级")
    private Integer level = 4;

    @Builder
    public ResourceVo(Long id, String dataState, String resourceNo, String parentResourceNo, String resourceName, String resourceType, String requestPath, String label, Integer sortNo, String icon, String remark, String[] checkedIds, String[] checkedResourceNos, String roleId, Integer level) {
        super(id, dataState);
        this.resourceNo = resourceNo;
        this.parentResourceNo = parentResourceNo;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.requestPath = requestPath;
        this.label = label;
        this.sortNo = sortNo;
        this.icon = icon;
        this.remark = remark;
        this.checkedIds = checkedIds;
        this.checkedResourceNos = checkedResourceNos;
        this.roleId = roleId;
        this.level = level;
    }
}
