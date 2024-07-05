package com.zzyl.mapper;

import com.zzyl.entity.Room;
import com.zzyl.vo.RoomVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface RoomMapper {
    /**
     * 插入一条room记录
     */
    int insert(Room room);

    /**
     * 通过id删除一条记录
     */
    int deleteById(Long id);

    /**
     * 通过id更新一条记录
     */
    int updateById(Room room);

    /**
     * 通过id查询一条记录
     */
    Room selectById(Long id);

    /**
     * 查询所有记录
     */
    List<Room> selectAll();

    /**
     * 根据floorId查找记录
     */
    List<Room> selectRoomsByFloorId(Long floorId);

    @MapKey(value = "type_name")
    Map<String, Integer> countRoomByTypeName(List<String> collect);

    List<RoomVo> selectByFloorId(Long floorId);

    List<RoomVo> selectByFloorIdWithNur(Long floorId);

    List<RoomVo> selectByFloorIdWithDevice(Long floorId);

    List<RoomVo> selectRoomsCheckedByFloorId(Long floorId);
}


