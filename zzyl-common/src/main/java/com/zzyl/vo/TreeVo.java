package com.zzyl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 资源树显示类
 */
@Data
@NoArgsConstructor
public class TreeVo implements Serializable {

	@ApiModelProperty(value = "tree数据")
	private List<TreeItemVo> items;

	@Builder
	public TreeVo(List<TreeItemVo> items) {
		this.items = items;
	}
}
