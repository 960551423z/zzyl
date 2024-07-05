package com.zzyl.service;

import com.zzyl.dto.RoomDto;
import com.zzyl.vo.RoomVo;

import java.util.List;
import java.util.Map;

public interface RoomService {
    /**
     * 添加房间
     * @param roomDto 房间信息
     * @return 添加结果
     */
    int addRoom(RoomDto roomDto);

    /**
     * 删除房间
     * @param id 房间id
     * @return 删除结果
     */
    int deleteRoom(Long id);

    /**
     * 更新房间信息
     * @param roomDto 房间信息
     * @return 更新结果
     */
    int updateRoom(RoomDto roomDto);

    /**
     * 获取房间信息
     * @param id 房间id
     * @return 房间信息
     */
    RoomVo getRoom(Long id);

    /**
     * 获取所有房间视图信息
     * @return 所有房间视图信息
     */
    List<RoomVo> getAllRoomVos();

    /**
     * 根据楼层id获取房间视图信息
     * @param floorId 楼层id
     * @return 房间视图信息
     */
    List<RoomVo> getRoomsByFloorId(Long floorId);

    /**
     * 统计房间和类型
     * @param collect
     * @return
     */
    Map<String, Integer> countRoomByTypeName(List<String> collect);

    /**
     * 根据楼层id查询所有的设备数据
     * @param floorId
     * @return
     */
    List<RoomVo> getRoomsWithDeviceByFloorId(Long floorId);

    /**
     * 根据楼层ID查询所有的房间和床位
     * @param floorId
     * @return
     */
    List<RoomVo> getRoomsWithNurByFloorId(Long floorId);

    /**
     * 获取所有房间（床位房型）
     * @param floorId
     * @return
     */
    List<RoomVo> getRoomsCheckedByFloorId(Long floorId);

}

