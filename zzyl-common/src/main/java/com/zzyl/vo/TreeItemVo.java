package com.zzyl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源树结构体
 */
@Data
@NoArgsConstructor
public class TreeItemVo implements Serializable {

    @ApiModelProperty(value = "节点ID")
    public String id;

    @ApiModelProperty(value = "显示内容")
    public String label;

    @ApiModelProperty(value = "显示内容")
    public List<TreeItemVo> children = new ArrayList<>();

    @Builder
    public TreeItemVo(String id, String label, List<TreeItemVo> children) {
        this.id = id;
        this.label = label;
        this.children = children;
    }
}
