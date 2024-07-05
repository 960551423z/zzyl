package com.zzyl.service;

import com.zzyl.entity.Trading;
import com.zzyl.enums.TradingStateEnum;

import java.util.List;

/**
 * 交易订单表 服务类
 */
public interface TradingService {

    /***
     * 按交易单号查询交易单
     *
     * @param tradingOrderNo 交易单号
     * @return 交易单数据
     */
    Trading findTradByTradingOrderNo(Long tradingOrderNo);

    /***
     * 按订单单号查询交易单
     * @param productOrderNo 交易单号
     * @param tradingType
     * @return 交易单数据
     */
    Trading findTradByProductOrderNo(Long productOrderNo, String tradingType);

    /***
     * 按交易状态查询交易单，按照时间正序排序
     * @param tradingState 状态
     * @param count 查询数量，默认查询10条
     * @return 交易单数据列表
     */
    List<Trading> findListByTradingState(TradingStateEnum tradingState, Integer count);

    Boolean saveOrUpdate(Trading trading);
}
