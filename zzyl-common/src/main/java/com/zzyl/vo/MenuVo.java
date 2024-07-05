package com.zzyl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 动态菜单VO对象
 */
@Data
@NoArgsConstructor
public class MenuVo implements Serializable {

	// 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
	@ApiModelProperty(value = "路由名字")
	private  String name;

	@ApiModelProperty(value = "请求路径")
	private String path;

	@ApiModelProperty(value = "模块跳转目标")
	private String component;

	@ApiModelProperty(value = "高亮子菜单")
	private String redirect;

	// 当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
	@ApiModelProperty(value = "是否显示")
	private Boolean hidden;

	@ApiModelProperty(value = "子菜单")
	private List<MenuVo> children;

	@ApiModelProperty(value = "meta属性")
	private MenuMetaVo meta;

	@Builder
	public MenuVo(String name, String path, String component, String redirect, Boolean hidden, List<MenuVo> children, MenuMetaVo meta) {
		this.name = name;
		this.path = path;
		this.component = component;
		this.redirect = redirect;
		this.hidden = hidden;
		this.children = children;
		this.meta = meta;
	}
}
