package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.DeviceDataDto;
import com.zzyl.vo.DeviceDataVo;

import java.time.LocalDateTime;

public interface DeviceDataService {

    /**
     * 创建设备数据
     * @param deviceDataDto
     */
    void createDeviceData(DeviceDataDto deviceDataDto);

    /**
     * 读取设备数据
     * @param id
     * @return
     */
    DeviceDataVo readDeviceData(Long id);

    /**
     * 修改设备数据
     * @param id
     * @param deviceDataDto
     */
    void updateDeviceData(Long id, DeviceDataDto deviceDataDto);

    /**
     * 删除
     * @param id
     */
    void deleteDeviceData(Long id);

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param status
     * @param deviceName
     * @param accessLocation
     * @param locationType
     * @param functionId
     * @param startTime
     * @param endTime
     * @return
     */
    PageResponse<DeviceDataVo> getDeviceDataPage(Integer pageNum, Integer pageSize, Integer status, String deviceName, String accessLocation, Integer locationType, String functionId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按周统计设备数据
     * @param pageNum
     * @param pageSize
     * @param status
     * @param deviceName
     * @param accessLocation
     * @param locationType
     * @param functionId
     * @param time
     * @param time1
     * @return
     */
    PageResponse<DeviceDataVo> getDeviceWeekDataPage(Integer pageNum, Integer pageSize, Integer status, String deviceName, String accessLocation, Integer locationType, String functionId, LocalDateTime time, LocalDateTime time1);
}
