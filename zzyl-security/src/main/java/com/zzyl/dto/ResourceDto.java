package com.zzyl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源DTO
 */
@Data
@NoArgsConstructor
public class ResourceDto extends BaseDto {

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

    @ApiModelProperty(value = "角色查询资源：资源对应角色id")
    private String roleId;

    @ApiModelProperty(value = "层级")
    private Integer level = 4;

    @ApiModelProperty(value = "是否启用（0:启用，1:禁用）")
    private String dataState;

    @Builder
    public ResourceDto(String resourceNo, String parentResourceNo, String resourceName, String resourceType, String requestPath, String label, Integer sortNo, String icon, String remark, String roleId, Integer level, String dataState) {
        this.resourceNo = resourceNo;
        this.parentResourceNo = parentResourceNo;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.requestPath = requestPath;
        this.label = label;
        this.sortNo = sortNo;
        this.icon = icon;
        this.remark = remark;
        this.roleId = roleId;
        this.level = level;
        this.dataState = dataState;
    }
}
