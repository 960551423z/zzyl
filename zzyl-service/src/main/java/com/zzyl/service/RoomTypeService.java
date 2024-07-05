package com.zzyl.service;

import com.zzyl.dto.RoomTypeDto;
import com.zzyl.vo.RoomTypeVo;

import java.util.List;

/**
 * RoomTypeService接口，定义了对房间类型的增删改查操作
 */

public interface RoomTypeService {
    /**
     * 添加房间类型
     * @param roomTypeDTO 房间类型DTO对象
     */
    void addRoomType(RoomTypeDto roomTypeDTO);

    /**
     * 删除房间类型
     * @param id 房间类型id
     */
    void removeRoomType(Long id);

    /**
     * 修改房间类型
     * @param id 房间类型id
     * @param roomTypeDTO 房间类型DTO对象
     */
    void modifyRoomType(Long id, RoomTypeDto roomTypeDTO);

    /**
     * 根据id查找房间类型
     * @param id 房间类型id
     * @return 房间类型VO对象
     */
    RoomTypeVo findRoomTypeById(Long id);

    /**
     * 查找所有房间类型
     * @return 房间类型VO对象列表
     */
    List<RoomTypeVo> findRoomTypeList();

    /**
     * 根据状态查找房间类型
     * @param status 状态码
     * @return 房间类型VO对象列表
     */
    List<RoomTypeVo> findRoomTypeListByStatus(Integer status);

    /**
     * 根据类型名查找房间类型
     * @param typeName 类型名
     * @return 房间类型VO对象列表
     */
    List<RoomTypeVo> findRoomTypeListByTypeName(String typeName);


        /**
     * 启用或禁用
     * @param id ID
     * @param status 状态
     */
    void enableOrDisable(Long id, Integer status);

}
