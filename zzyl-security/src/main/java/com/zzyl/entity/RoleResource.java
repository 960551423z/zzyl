package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleResource extends BaseEntity {

    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

    /**
     * 资源编号

     */
    private String resourceNo;

    /**
     * 角色ID
     */
    private Long roleId;


    @Builder
    public RoleResource(Long roleId, String resourceNo, String dataState) {
        this.roleId = roleId;
        this.resourceNo = resourceNo;
        this.dataState = dataState;
    }
}
