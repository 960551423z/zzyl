package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.PrepaidRechargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 预交款充值记录Mapper
 */
@Mapper
public interface PrepaidRechargeRecordMapper {

    /**
     * 根据主键删除预交款充值记录
     * @param id 主键
     * @return 删除结果
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入预交款充值记录
     * @param record 预交款充值记录
     * @return 插入结果
     */
    int insert(PrepaidRechargeRecord record);

    /**
     * 选择性插入预交款充值记录
     * @param record 预交款充值记录
     * @return 插入结果
     */
    int insertSelective(PrepaidRechargeRecord record);

    /**
     * 根据主键选择预交款充值记录
     * @param id 主键
     * @return 预交款充值记录
     */
    PrepaidRechargeRecord selectByPrimaryKey(Long id);

    /**
     * 选择性更新预交款充值记录
     * @param record 预交款充值记录
     * @return 更新结果
     */
    int updateByPrimaryKeySelective(PrepaidRechargeRecord record);

    /**
     * 更新预交款充值记录
     * @param record 预交款充值记录
     * @return 更新结果
     */
    int updateByPrimaryKey(PrepaidRechargeRecord record);

    /**
     * 分页查询预充值记录
     *
     * @param bedNo     床位号
     * @param elderName 老人姓名
     * @return 分页结果
     */
    Page<PrepaidRechargeRecord> prepaidRechargeRecordPage(@Param("bedNo")String bedNo, @Param("elderName")String elderName);
}
