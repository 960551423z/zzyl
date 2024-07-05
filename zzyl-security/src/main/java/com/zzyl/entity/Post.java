package com.zzyl.entity;

import com.zzyl.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 部门编号
     */
    private String deptNo;

    /**
     * 岗位编码：父部门编号+01【2位】
     */
    private String postNo;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 显示顺序
     */
    private Integer sortNo;

    /**
     * 数据状态（0正常 1停用）
     */
    private String dataState;

}
