package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends BaseEntity {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * OpenID
     */
    private String openId;

    /**
     * 性别
     */
    private int gender;

}
