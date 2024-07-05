package com.zzyl.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * LoginVO
 *
 * @author 阿庆
 */
@Data
@ApiModel(value = "登录对象")
public class LoginVo {

    @ApiModelProperty(value = "JWT token")
    private String token;

    @ApiModelProperty(value = "昵称")
    private String nickName;
}
