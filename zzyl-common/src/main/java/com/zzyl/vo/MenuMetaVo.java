package com.zzyl.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单meta属性
 */
@Data
@NoArgsConstructor
public class MenuMetaVo implements Serializable {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "角色")
    private List<String> roles;

    @Builder
    public MenuMetaVo(String title, String icon, List<String> roles) {
        this.title = title;
        this.icon = icon;
        this.roles = roles;
    }
}
