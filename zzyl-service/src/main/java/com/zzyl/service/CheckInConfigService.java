package com.zzyl.service;

import com.zzyl.dto.CheckInConfigDto;
import com.zzyl.entity.CheckInConfig;

/**
* <p>
* check_in_config Service 接口
* </p>
*
* @author 阿庆
*/
public interface CheckInConfigService {

    /**
     * 根据老人id查询入住配置
     * @param elderId
     * @return
     */
    CheckInConfig findCurrentConfigByElderId(Long elderId);

    /**
     * 入住选择配置
     *
     * @param checkInConfigDto 入住选择配置
     * @return 受影响的行数
     */
    int checkIn(CheckInConfigDto checkInConfigDto);
}