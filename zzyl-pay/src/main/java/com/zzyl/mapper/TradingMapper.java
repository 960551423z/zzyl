
package com.zzyl.mapper;

import com.zzyl.entity.Trading;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易Mapper接口
 */
@Mapper
public interface TradingMapper {

    /**
     * 根据主键删除交易记录
     * @param id 主键
     * @return 删除结果
     */
    int deleteByPrimaryKey(@Param("id")Long id);

    /**
     * 插入交易记录
     * @param record 交易记录
     * @return 插入结果
     */
    int insert(Trading record);

    /**
     * 插入交易记录
     * @param records 交易记录
     * @return 插入结果
     */
    int insertBatch(@Param("list") List<Trading> records);

    /**
     * 选择性插入交易记录
     * @param record 交易记录
     * @return 插入结果
     */
    int insertSelective(Trading record);

    /**
     * 根据主键查询交易记录
     * @param id 主键
     * @return 查询结果
     */
    Trading selectByPrimaryKey(@Param("id")Long id);

    /**
     * 选择性更新交易记录
     * @param record 交易记录
     * @return 更新结果
     */
    int updateByPrimaryKeySelective(Trading record);

    /**
     * 根据主键更新交易记录（包含大字段）
     * @param record 交易记录
     * @return 更新结果
     */
    int updateByPrimaryKeyWithBLOBs(Trading record);

    /**
     * 根据主键更新交易记录
     * @param record 交易记录
     * @return 更新结果
     */
    int updateByPrimaryKey(Trading record);

    /**
     * 根据交易订单号查询交易记录
     * @param tradingOrderNo 交易订单号
     * @return 查询结果
     */
    Trading selectByTradingOrderNo(@Param("tradingOrderNo")Long tradingOrderNo);

    /**
     * 根据商品订单号查询交易记录
     * @param productOrderNo 商品订单号
     * @param tradingType
     * @return 查询结果
     */
    Trading selectByProductOrderNo(@Param("productOrderNo") Long productOrderNo, @Param("tradingType")String tradingType);

    /**
     * 根据交易状态查询交易记录列表
     * @param tradingState 交易状态
     * @param count 查询数量
     * @return 查询结果列表
     */
    List<Trading> selectListByTradingState(@Param("tradingState") Integer tradingState, @Param("count")Integer count);
}

