
package com.zzyl.service;


import com.zzyl.dto.FloorDto;
import com.zzyl.vo.FloorVo;

import java.util.List;

/**
 * 楼层服务接口
 */
public interface FloorService {
    /**
     * 添加楼层
     * @param floorDto 楼层信息
     * @return 受影响的行数
     */
    int addFloor(FloorDto floorDto);

    /**
     * 删除楼层
     * @param id 楼层id
     * @return 受影响的行数
     */
    int deleteFloor(Long id);

    /**
     * 更新楼层
     * @param floorDto 楼层信息
     * @return 受影响的行数
     */
    int updateFloor(FloorDto floorDto);

    /**
     * 获取楼层信息
     * @param id 楼层id
     * @return 楼层信息
     */
    FloorVo getFloor(Long id);

    /**
     * 获取所有楼层信息
     * @return 所有楼层信息
     */
    List<FloorVo> getAllFloors();

    /**
     * 查询所有房间和床位
     * @return
     */
    List<FloorVo> getAllWithRoomAndBed();

    /**
     * 查询查询智能设备的所有楼层
     * @return
     */
    List<FloorVo> selectAllByDevice();

    /**
     * 查询所有楼层
     * @return
     */
    List<FloorVo> selectAllByNur();

}
