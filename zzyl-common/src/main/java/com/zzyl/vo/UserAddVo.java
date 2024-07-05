package com.zzyl.vo;

import com.zzyl.base.BaseVo;
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
public class UserAddVo extends BaseVo {

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "查询用户：用户角色Ids")
    private Set<String>  roleVoIds;

    @ApiModelProperty(value = "部门编号【当前】")
    private String deptNo;

    @ApiModelProperty(value = "职位编号【当前】")
    private String postNo;

    @Builder
    public UserAddVo(Long id, String dataState, String username, String password, String email, String realName, String mobile, String sex, Set<String> roleVoIds, String deptNo, String postNo) {
        super(id, dataState);
        this.username = username;
        this.password = password;
        this.email = email;
        this.realName = realName;
        this.mobile = mobile;
        this.sex = sex;
        this.roleVoIds = roleVoIds;
        this.deptNo = deptNo;
        this.postNo = postNo;
    }
}
