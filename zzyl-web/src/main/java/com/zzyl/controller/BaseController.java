package com.zzyl.controller;

import com.zzyl.base.ResponseResult;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseController
 * web层通用数据处理
 * @author 阿庆
 **/
@Api(tags = "基础控制器，提供一些公共方法")
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ResponseResult toAjax(int rows) {
        return rows > 0 ? ResponseResult.success() : ResponseResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected ResponseResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public ResponseResult success() {
        return ResponseResult.success();
    }

    /**
     * 返回失败消息
     */
    public ResponseResult error() {
        return ResponseResult.error();
    }

    /**
     * 返回成功消息
     */
    public ResponseResult success(String message) {
        return ResponseResult.success(message);
    }

    /**
     * 返回成功消息
     */
    public ResponseResult success(Object message) {
        return ResponseResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public ResponseResult error(String message) {
        return ResponseResult.error(message);
    }


}
