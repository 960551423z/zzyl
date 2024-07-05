package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRole extends BaseEntity {

    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户ID
     */
    private Long userId;

    @Builder
    public UserRole(Long userId, Long roleId, String dataState) {
        this.userId = userId;
        this.roleId = roleId;
        this.dataState = dataState;
    }
}
