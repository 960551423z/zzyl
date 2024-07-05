package com.zzyl.service;

import com.zzyl.dto.BedDto;
import com.zzyl.vo.BedVo;

import java.util.List;

/**
 * 服务接口：BedService（床位管理服务）
 */
public interface BedService {

    /**
     * 添加新的床位
     * @param bed 床位数据传输对象
     */
    void addBed(BedDto bed);

    /**
     * 更新现有的床位
     * @param bed 床位数据传输对象
     */
    void updateBed(BedDto bed);

    /**
     * 通过ID删除床位
     * @param id 床位ID
     */
    void deleteBedById(Long id);

    /**
     * 通过ID检索床位
     * @param id 床位ID
     * @return 床位视图对象
     */
    BedVo getBedById(Long id);

    /**
     * 检索所有床位
     * @return 床位视图对象列表
     */
    List<BedVo> getAllBeds();

    /**
     * 通过房间ID检索床位
     * @param roomId 房间ID
     * @return 床位视图对象列表
     */
    List<BedVo> getBedsByRoomId(Long roomId);

    BedVo getDevice(Long id, Integer type);
}
