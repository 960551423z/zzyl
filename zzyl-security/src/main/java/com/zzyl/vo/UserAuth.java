package com.zzyl.vo;
import com.zzyl.utils.EmptyUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  自定认证用户
 */
@Data
@NoArgsConstructor
public class UserAuth implements UserDetails {

    private String id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限内置
     */
    private Collection<SimpleGrantedAuthority> authorities;

    /**
     * 用户类型（00系统用户）
     */
    private String userType;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门编号【当前】
     */
    private String deptNo;

    /**
     * 职位编号【当前】
     */
    private String postNo;

    public UserAuth(UserVo userVo) {
        this.setId(userVo.getId().toString());
        this.setUsername(userVo.getUsername());
        this.setPassword(userVo.getPassword());
        if (!EmptyUtil.isNullOrEmpty(userVo.getResourceRequestPaths())) {
            authorities = new ArrayList<>();
            userVo.getResourceRequestPaths().forEach(resourceRequestPath -> authorities.add(new SimpleGrantedAuthority(resourceRequestPath)));
        }
        this.setUserType(userVo.getUserType());
        this.setNickName(userVo.getNickName());
        this.setEmail(userVo.getEmail());
        this.setRealName(userVo.getRealName());
        this.setMobile(userVo.getMobile());
        this.setSex(userVo.getSex());
        this.setCreateTime(userVo.getCreateTime());
        this.setCreateBy(userVo.getCreateBy());
        this.setUpdateTime(userVo.getUpdateTime());
        this.setUpdateBy(userVo.getUpdateBy());
        this.setRemark(userVo.getRemark());
        this.setDeptNo(userVo.getDeptNo());
        this.setPostNo(userVo.getPostNo());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
