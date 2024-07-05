package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 用户表
 */
@Data
@NoArgsConstructor
public class UserDto extends BaseDto {

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "是否启用（0:启用，1:禁用）")
    private String dataState;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户职位")
    private String post;

    @ApiModelProperty(value = "用户部门")
    private String dept;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "选中节点")
    private String[] checkedIds;

    @ApiModelProperty(value = "查询用户：用户角色Ids")
    private Set<String>  roleVoIds;

    @ApiModelProperty(value = "部门编号【当前】")
    private String deptNo;

    @ApiModelProperty(value = "职位编号【当前】")
    private String postNo;

    @ApiModelProperty(value = "角色Id【当前】")
    private Long roleId;

    @Builder
    public UserDto(String username, String dataState, String email, String nickName, String post, String dept, String realName, String mobile, String sex, String remark, String[] checkedIds, Set<String> roleVoIds,  String deptNo, String postNo, Long roleId) {
        this.username = username;
        this.dataState = dataState;
        this.email = email;
        this.nickName = nickName;
        this.post = post;
        this.dept = dept;
        this.realName = realName;
        this.mobile = mobile;
        this.sex = sex;
        this.remark = remark;
        this.checkedIds = checkedIds;
        this.roleVoIds = roleVoIds;
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.roleId = roleId;
    }
}
