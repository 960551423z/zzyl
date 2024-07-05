package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.NursingLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NursingLevelMapper {

    /**
     * 根据护理等级ID获取护理等级信息
     * @param id 护理等级ID
     * @return 护理等级信息
     */
    NursingLevel getById(Long id);

    /**
     * 更新护理等级信息
     * @param nursingLevel 护理等级信息
     */
    void update(NursingLevel nursingLevel);

    /**
     * 删除护理等级信息
     * @param id 护理等级ID
     */
    void delete(Long id);

    /**
     * 插入护理等级信息
     * @param record 护理等级信息
     * @return 添加成功的条数
     */
    int insert(NursingLevel record);

    /**
     * 获取所有护理等级信息
     * @return 所有护理等级信息
     */
    List<NursingLevel> listAll();

    /**
     * 批量插入护理等级信息
     * @param nursingLevels 护理等级信息列表
     * @return 添加成功的条数
     */
    int insertBatch(@Param("nursingLevels") List<NursingLevel> nursingLevels);

    /**
     * 根据指定名称和状态分页查询护理等级信息列表
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param name 护理等级名称
     * @param status 护理等级状态，0：禁用，1：启用
     * @return 满足条件的护理等级信息列表
     */
    Page<List<NursingLevel>> listByPage(
            @Param("page") int page, @Param("pageSize") int pageSize, @Param("name") String name,
            @Param("status") Integer status
    );



    /**
     * 启用或禁用
     * @param id ID
     * @param status 状态，0：禁用，1：启用
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据id集合查询护理等级列表
     * @param ids
     * @return
     */
    List<NursingLevel> listAllByPlanIds(List<Long> ids);
}
