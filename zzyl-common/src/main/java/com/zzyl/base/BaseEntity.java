package com.zzyl.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体基础类
 */
@Data
@NoArgsConstructor
@ApiModel(description = "实体基础类")
public class BaseEntity implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    public Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    public LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    public LocalDateTime updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creator;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updater;


    @ApiModelProperty(value = "是否删除")

    private Integer isDelete;

    public BaseEntity(Long id) {
        this.id = id;
    }

    /**
     * 请求参数 (JsonIgnore is used to exclude it from Swagger docs)
     */
    @JsonIgnore
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }
}
