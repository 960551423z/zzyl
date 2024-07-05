package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resource extends BaseEntity {

    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限标识
     */
    private String label;

    /**
     * 父资源编号
     */
    private String parentResourceNo;

    /**
     * 请求地址
     */
    private String requestPath;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源编号

     */
    private String resourceNo;

    /**
     * 资源类型：s平台 c目录 m菜单 r按钮
     */
    private String resourceType;

    /**
     * 排序
     */
    private Integer sortNo;

}
