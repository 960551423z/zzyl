package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.RefundRecord;
import com.zzyl.enums.RefundStatusEnum;
import com.zzyl.vo.RefundRecordVo;

import java.util.List;

/**
 *  退款记录表服务类
 */
public interface RefundRecordService {

    /**
     * 根据退款单号查询退款记录
     *
     * @param refundNo 退款单号
     * @return 退款记录数据
     */
    RefundRecordVo findByRefundNo(Long refundNo);

    /**
     * 根据交易单号查询退款列表
     *
     * @param tradingOrderNo 交易单号
     * @return 退款列表
     */
    List<RefundRecord> findListByTradingOrderNo(Long tradingOrderNo);

    /**
     * 根据订单号查询退款列表
     *
     * @param productOrderNo 订单号
     * @return 退款列表
     */
    List<RefundRecord> findListByProductOrderNo(Long productOrderNo);

    /***
     * 按状态查询退款单，按照时间正序排序
     *
     * @param refundStatus 状态
     * @param count 查询数量，默认查询10条
     * @return 退款单数据列表
     */
    List<RefundRecord> findListByRefundStatus(RefundStatusEnum refundStatus, Integer count);

    void saveOrUpdate(RefundRecord refundRecord);

    RefundRecord findRefundRecordByProductOrderNoAndSending(Long productOrderNo);


    PageResponse<RefundRecordVo> queryRefundRecord(RefundRecordVo refundRecordVo);
}
