//package com.zzyl.job;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.NumberUtil;
//import com.xxl.job.core.context.XxlJobHelper;
//import com.xxl.job.core.handler.annotation.XxlJob;
//import com.zzyl.entity.RefundRecord;
//import com.zzyl.entity.Trading;
//import com.zzyl.enums.RefundStatusEnum;
//import com.zzyl.enums.TradingStateEnum;
//import com.zzyl.handler.wechat.WechatCommonPayHandler;
//import com.zzyl.service.BillService;
//import com.zzyl.service.OrderService;
//import com.zzyl.service.RefundRecordService;
//import com.zzyl.service.TradingService;
//import com.zzyl.vo.RefundRecordVo;
//import com.zzyl.vo.TradingVo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 交易任务，主要是查询订单的支付状态 和 退款的成功状态
// *
// * @author 阿庆
// */
//@Slf4j
//@Component
//public class TradeJob {
//
//    @Value("${sl.job.trading.count:100}")
//    private Integer tradingCount;
//    @Value("${sl.job.refund.count:100}")
//    private Integer refundCount;
//    @Resource
//    private TradingService tradingService;
//    @Resource
//    private RefundRecordService refundRecordService;
//    @Resource
//    private WechatCommonPayHandler wechatCommonPayHandler;
//
//    @Resource
//    private OrderService orderService;
//
//    @Resource
//    private BillService billService;
//
//
//    /**
//     * 分片广播方式查询支付状态
//     * 逻辑：每次最多查询{tradingCount}个未完成的交易单，交易单id与shardTotal取模，值等于shardIndex进行处理
//     */
//    @XxlJob("tradingJob")
//    @Scheduled(cron = "* * * * * ?")
//    public void tradingJob() {
//        // 分片参数
//        int shardIndex = NumberUtil.max(XxlJobHelper.getShardIndex(), 0);
//        int shardTotal = NumberUtil.max(XxlJobHelper.getShardTotal(), 1);
//
//        List<Trading> list = this.tradingService.findListByTradingState(TradingStateEnum.DFK, tradingCount);
//        if (CollUtil.isEmpty(list)) {
//            XxlJobHelper.log("查询到交易单列表为空！shardIndex = {}, shardTotal = {}", shardIndex, shardTotal);
//            return;
//        }
//
//        //定义消息通知列表，只要是状态不为【付款中】就需要通知其他系统
//        List<Trading> tradeMsgList = new ArrayList<>();
//        for (Trading trading : list) {
//            if (trading.getTradingOrderNo() % shardTotal != shardIndex) {
//                continue;
//            }
//            try {
//                //查询交易单
//                TradingVo tradingVo = this.wechatCommonPayHandler.queryTrading(BeanUtil.toBean(trading, TradingVo.class));
//                if (TradingStateEnum.YJS.getCode().equals(tradingVo.getTradingState())) {
//                    tradeMsgList.add(trading);
//                }
//            } catch (Exception e) {
//                XxlJobHelper.log("查询交易单出错！shardIndex = {}, shardTotal = {}, trading = {}", shardIndex, shardTotal, trading, e);
//            }
//        }
//
//        if (CollUtil.isEmpty(tradeMsgList)) {
//            return;
//        }
//
//        //发送消息通知其他系统
//        // 订单 账单
//        List<Long> tradingOrderNos = tradeMsgList.stream().map(Trading::getTradingOrderNo).distinct().collect(Collectors.toList());
//        orderService.payOrder(tradingOrderNos);
//        billService.payOrder(tradingOrderNos);
//    }
//
//    /**
//     * 分片广播方式查询退款状态
//     */
//    @Scheduled(cron = "0 * * * * ?")
//    public void refundJob() {
//        // 分片参数
//        int shardIndex = NumberUtil.max(XxlJobHelper.getShardIndex(), 0);
//        int shardTotal = NumberUtil.max(XxlJobHelper.getShardTotal(), 1);
//
//        List<RefundRecord> list = this.refundRecordService.findListByRefundStatus(RefundStatusEnum.SENDING, refundCount);
//        if (CollUtil.isEmpty(list)) {
//            XxlJobHelper.log("查询到退款单列表为空！shardIndex = {}, shardTotal = {}", shardIndex, shardTotal);
//            return;
//        }
//
//        //定义消息通知列表，只要是状态不为【退款中】就需要通知其他系统
//        List<RefundRecord> tradeMsgList = new ArrayList<>();
//
//        for (RefundRecord refundRecord : list) {
//            if (refundRecord.getRefundNo() % shardTotal != shardIndex) {
//                continue;
//            }
//            try {
//                //查询退款单
//                RefundRecordVo refundRecordVo = BeanUtil.toBean(refundRecord, RefundRecordVo.class);
//                RefundRecordVo refundRecordVo1 = this.wechatCommonPayHandler.queryRefundTrading(refundRecordVo);
//                if (RefundStatusEnum.SUCCESS.getCode().equals(refundRecordVo1.getRefundStatus())) {
//
//                    tradeMsgList.add(refundRecord);
//                }
//            } catch (Exception e) {
//                XxlJobHelper.log("查询退款单出错！shardIndex = {}, shardTotal = {}, refundRecord = {}", shardIndex, shardTotal, refundRecord, e);
//            }
//        }
//
//        if (CollUtil.isEmpty(tradeMsgList)) {
//            return;
//        }
//
//        //发送消息通知其他系统
//        // 订单 账单
//        List<Long> tradingOrderNos = tradeMsgList.stream().map(RefundRecord::getTradingOrderNo).collect(Collectors.toList());
//        orderService.refundOrder(tradingOrderNos);
//        billService.refundOrder(tradingOrderNos);
//    }
//}