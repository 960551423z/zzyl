package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User extends BaseEntity {

    /**
     * 头像地址
     */
    private String avatar;


    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

    /**
     * 部门编号
     */
    private String deptNo;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 是否是部门leader(0:否，1：是)
     */
    private Integer isLeader;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * open_id标识
     */
    private String openId;

    /**
     * 密码
     */
    private String password;

    /**
     * 岗位编号
     */
    private String postNo;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;


    /**
     * 用户类型（0:系统用户,1:客户）
     */
    private String userType;

    /**
     * 用户账号
     */
    private String username;



    @Builder
    public User(String username, String openId, String password, String userType, String avatar, String nickName, String email, String realName, String mobile, String sex, String dataState, String deptNo, String postNo, Integer isLeader) {
        this.username = username;
        this.openId = openId;
        this.password = password;
        this.userType = userType;
        this.avatar = avatar;
        this.nickName = nickName;
        this.email = email;
        this.realName = realName;
        this.mobile = mobile;
        this.sex = sex;
        this.dataState = dataState;
        this.deptNo = deptNo;
        this.postNo = postNo;
        this.isLeader = isLeader;
    }
}
