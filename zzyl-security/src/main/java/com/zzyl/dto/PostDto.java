package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import com.zzyl.vo.DeptVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 阿庆
 */
@Data
@NoArgsConstructor
public class PostDto extends BaseDto {

    @ApiModelProperty(value = "部门编号")
    private String deptNo;

    @ApiModelProperty(value = "岗位编码：父部门编号+001【3位】")
    private String postNo;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "显示顺序")
    private Integer sortNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "职位对应部门")
    private DeptVo deptVo;

    @ApiModelProperty(value = "是否启用(0:启用,1:禁用)")
    private String dataState;

    @Builder
    public PostDto(String deptNo, String postNo, String postName, Integer sortNo, String remark, DeptVo deptVo, String dataState) {
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.postName = postName;
        this.sortNo = sortNo;
        this.remark = remark;
        this.deptVo = deptVo;
        this.dataState = dataState;
    }
}
