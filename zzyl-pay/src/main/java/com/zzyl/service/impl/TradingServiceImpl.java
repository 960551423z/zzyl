
package com.zzyl.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.zzyl.entity.Trading;
import com.zzyl.enums.TradingStateEnum;
import com.zzyl.mapper.TradingMapper;
import com.zzyl.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易订单表 服务实现类
 */
@Service
public class TradingServiceImpl implements TradingService {

    @Autowired
    private TradingMapper tradingMapper;

    /**
     * 根据交易订单号查询交易信息
     */
    @Override
    public Trading findTradByTradingOrderNo(Long tradingOrderNo) {
        return tradingMapper.selectByTradingOrderNo(tradingOrderNo);
    }

    /**
     * 根据商品订单号查询交易信息
     */
    @Override
    public Trading findTradByProductOrderNo(Long productOrderNo, String tradingType) {
        return tradingMapper.selectByProductOrderNo(productOrderNo, tradingType);
    }

    /**
     * 根据交易状态查询交易列表
     */
    @Override
    public List<Trading> findListByTradingState(TradingStateEnum tradingState, Integer count) {
        count = NumberUtil.max(count, 10);
        return tradingMapper.selectListByTradingState(tradingState.getCode(), count);
    }

    /**
     * 新增或修改交易信息
     */
    @Override
    public Boolean saveOrUpdate(Trading trading) {
        if (trading.getId() == null) {
            return tradingMapper.insert(trading) > 0;
        } else {
            return tradingMapper.updateByPrimaryKey(trading) > 0;
        }
    }

}


