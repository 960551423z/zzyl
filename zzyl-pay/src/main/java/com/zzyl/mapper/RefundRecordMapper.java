
package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.RefundRecord;
import com.zzyl.vo.RefundRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退款记录Mapper
 */
@Mapper
public interface RefundRecordMapper {

    /**
     * 根据主键删除
     */
    int deleteByPrimaryKey(@Param("id")Long id);

    /**
     * 插入记录
     */
    int insert(RefundRecord record);

    /**
     * 选择性插入记录
     */
    int insertSelective(RefundRecord record);

    /**
     * 插入交易记录
     * @param records 交易记录
     * @return 插入结果
     */
    int insertBatch(@Param("list") List<RefundRecord> records);

    /**
     * 根据退款编号查询记录
     */
    RefundRecordVo selectByRefundNo(@Param("refundNo")Long refundNo);

    /**
     * 选择性更新记录
     */
    int updateByPrimaryKeySelective(RefundRecord record);

    /**
     * 根据主键更新记录（包含大字段）
     */
    int updateByPrimaryKeyWithBLOBs(RefundRecord record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(RefundRecord record);

    /**
     * 根据交易订单号查询记录列表
     */
    List<RefundRecord> selectListByTradingOrderNo(@Param("tradingOrderNo")Long tradingOrderNo);

    /**
     * 根据商品订单号查询记录列表
     */
    List<RefundRecord> selectListByProductOrderNo(@Param("productOrderNo")Long productOrderNo);

    /**
     * 根据退款状态查询记录列表
     */
    List<RefundRecord> selectListByRefundStatus(@Param("refundStatus")Integer refundStatus, @Param("count")Integer count);

    Page<List<RefundRecordVo>> queryRefundRecord(RefundRecordVo refundRecordVo);
}

