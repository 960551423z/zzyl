package com.zzyl.mapper;

import com.zzyl.entity.RoomType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomTypeMapper {

    /**
     * 添加房间类型
     * @param roomType 房间类型
     */
    void addRoomType(RoomType roomType);

    /**
     * 删除房间类型
     * @param id 房间类型id
     */
    void removeRoomType(Long id);

    /**
     * 修改房间类型
     * @param roomType 房间类型
     */
    void modifyRoomType(RoomType roomType);

    /**
     * 根据id查找房间类型
     * @param id 房间类型id
     * @return 房间类型
     */
    RoomType findRoomTypeById(Long id);

    /**
     * 查找所有房间类型
     * @return 房间类型列表
     */
    List<RoomType> findRoomTypeList();

    /**
     * 根据状态查找房间类型
     * @param status 状态
     * @return 房间类型列表
     */
    List<RoomType> findRoomTypeListByStatus(Integer status);

    /**
     * 根据类型名称查找房间类型
     * @param name 类型名称
     * @return 房间类型列表
     */
    List<RoomType> findRoomTypeListByTypeName(String name);

    /**
     * 启用或禁用
     * @param id ID
     * @param status 状态，0：禁用，1：启用
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

}

