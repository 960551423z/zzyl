package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author 阿庆
 */
@Data
@NoArgsConstructor
public class DeptDto extends BaseDto {

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

    @ApiModelProperty(value = "是否启用（0:启用，1:禁用）")
    private String dataState;

    @ApiModelProperty(value = "层级")
    private Integer level = 4;

    @Builder
    public DeptDto(String parentDeptNo, String deptNo, String deptName, Integer sortNo, Long leaderId, String dataState, Integer level) {
        this.parentDeptNo = parentDeptNo;
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.sortNo = sortNo;
        this.leaderId = leaderId;
        this.dataState = dataState;
        this.level = level;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), getParentDeptNo(), getDeptNo(), getDeptName(), getDataState(), getLeaderId());
        return result;
    }
}
