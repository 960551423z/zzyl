package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.Balance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * BalanceMapper接口
 */
@Mapper
public interface BalanceMapper {
    /**
     * 根据id删除余额信息
     * @param id 余额id
     * @return 删除结果
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入余额信息
     * @param record 余额信息
     * @return 插入结果
     */
    int insert(Balance record);

    /**
     * 选择性插入余额信息
     * @param record 余额信息
     * @return 插入结果
     */
    int insertSelective(Balance record);

    /**
     * 根据id选择余额信息
     * @param id 余额id
     * @return 余额信息
     */
    Balance selectByPrimaryKey(Long id);

    /**
     * 根据id选择性更新余额信息
     * @param record 余额信息
     * @return 更新结果
     */
    int updateByPrimaryKeySelective(Balance record);

    /**
     * 根据id更新余额信息
     * @param record 余额信息
     * @return 更新结果
     */
    int updateByPrimaryKey(Balance record);

    @Select("select * from balance where elder_id = #{elderId}")
    Balance selectByElderId(Long elderId);

    /**
     * 分页查询余额信息
     * @param bedNo 床位号
     * @param elderName 老人姓名
     * @return 余额信息列表
     */
    Page<Balance> page(@Param("bedNo")String bedNo, @Param("elderName")String elderName);

}
