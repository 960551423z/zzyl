package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role extends BaseEntity {

    /**
     * 数据范围：0自定义  1本人 2本部门及以下 3本部门 4全部
     */
    private String dataScope;

    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

    /**
     * 权限标识
     */
    private String label;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 排序
     */
    private Integer sortNo;

}
