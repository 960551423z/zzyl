package com.zzyl.mapper;

import com.zzyl.entity.CheckInConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 入住配置Mapper
 */
@Mapper
public interface CheckInConfigMapper {
    /**
     * 根据主键删除
     * @param id 主键
     * @return 删除结果
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入入住配置
     * @param record 入住配置
     * @return 插入结果
     */
    int insert(CheckInConfig record);

    /**
     * 选择性插入入住配置
     * @param record 入住配置
     * @return 插入结果
     */
    int insertSelective(CheckInConfig record);

    /**
     * 根据主键选择入住配置
     * @param id 主键
     * @return 入住配置
     */
    CheckInConfig selectByPrimaryKey(Long id);

    /**
     * 选择性更新入住配置
     * @param record 入住配置
     * @return 更新结果
     */
    int updateByPrimaryKeySelective(CheckInConfig record);

    /**
     * 更新入住配置
     * @param record 入住配置
     * @return 更新结果
     */
    int updateByPrimaryKey(CheckInConfig record);

    
    CheckInConfig findCurrentConfigByElderId(@Param("elderId") Long elderId);

    void deleteByElderId(@Param("elderId")Long elderId);
}
