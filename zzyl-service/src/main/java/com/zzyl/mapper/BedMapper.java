package com.zzyl.mapper;

import com.zzyl.entity.Bed;
import com.zzyl.vo.BedVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BedMapper {
    /**
     * 增加床位
     * @param bed 床位对象
     */
    void addBed(Bed bed);

    /**
     * 根据id删除床位
     * @param id 床位id
     */
    void deleteBedById(Long id);

    /**
     * 更新床位信息
     * @param bed 床位对象
     */
    void updateBed(Bed bed);

    /**
     * 根据id获取床位信息
     * @param id 床位id
     * @return 床位对象
     */
    Bed getBedById(Long id);

    /**
     * 根据id获取床位信息
     * @param bedNumber 床位id
     * @return 床位对象
     */
    Bed getBedByNum(String bedNumber);

    /**
     * 获取所有床位信息
     * @return 床位对象列表
     */
    List<Bed> getAllBeds();

    /**
     * 根据房间id获取床位信息列表
     * @param roomId 房间id
     * @return 床位对象列表
     */
    List<BedVo> getBedsByRoomId(Long roomId); // 新增方法，根据房间id查询床位
}

