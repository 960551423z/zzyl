package com.zzyl.service;


import com.zzyl.base.ResponseResult;
import com.zzyl.dto.ApplicationsDto;
import com.zzyl.vo.ApplicationsVo;

/**
* <p>
* applications Service 接口
* </p>
*
* @author 阿庆
*/
public interface ApplicationsService {

    /**
     * 分页查询我的申请
     * @param applicationsDto
     * @return
     */
    ResponseResult<ApplicationsVo> selectByPage(ApplicationsDto applicationsDto);

}