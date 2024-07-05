package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDept extends BaseEntity {

    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

    /**
     * 部门编号
     */
    private String deptNo;

    /**
     * 角色ID
     */
    private Long roleId;

    @Builder
    public RoleDept(Long roleId, String deptNo, String dataState) {
        this.roleId = roleId;
        this.deptNo = deptNo;
        this.dataState = dataState;
    }
}
