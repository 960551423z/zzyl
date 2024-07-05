package com.zzyl.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName BasicVo.java
 *  基础请求
 */
@Data
@NoArgsConstructor
public class BaseVo implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//get
    protected LocalDateTime createTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")//get
    protected String createDay;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//get
    protected LocalDateTime updateTime;

    /**
     * 创建者:username
     */
    @ApiModelProperty(value = "创建者:username")
    private Long createBy;

    /**
     * 更新者:username
     */
    @ApiModelProperty(value = "更新者:username")
    private Long updateBy;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效")
    protected String dataState;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建人类型 1 前台 2后台
     */
    @ApiModelProperty(value = "创建人类型 1 前台 2后台")
    private Integer createType;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String creator;

    /**
     * 后台管理端创建人名称
     */
    @ApiModelProperty(value = "后台管理端创建人名称")
    private String adminCreator;

    /**
     * 更新人名称
     */
    @ApiModelProperty(value = "更新人名称")
    private String updater;

    public BaseVo(Long id, String dataState) {
        this.id = id;
        this.dataState = dataState;
    }
}
