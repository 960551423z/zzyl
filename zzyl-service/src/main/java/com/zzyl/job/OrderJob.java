//package com.zzyl.job;
//
//import cn.hutool.core.collection.CollUtil;
//import com.zzyl.entity.Order;
//import com.zzyl.enums.OrderStatus;
//import com.zzyl.handler.CommonPayHandler;
//import com.zzyl.service.BillService;
//import com.zzyl.service.OrderService;
//import com.zzyl.vo.TradingVo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 账单
// * @author 阿庆
// */
//@Slf4j
//@Component
//public class OrderJob {
//
//    @Resource
//    OrderService orderService;
//
//    @Autowired
//    CommonPayHandler wechatCommonPayHandler;
//
//    @Resource
//    BillService billService;
//
//    /**
//     * 定时任务查询所有订单 判断下单时间过了15分钟还未付款 改为已关闭
//     * 过了3个月 改成已完成
//     */
//    @Scheduled(cron = "0 * * * * ?")
//    public void orderJob() {
//        List<Order> orders = orderService.selectByStatus(OrderStatus.PENDING_PAY.getCode());
//        List<Long> tradingOrderNos = new ArrayList<>();
//        for(Order order : orders){
//            if(order.getCreateTime().plusMinutes(15).isBefore(LocalDateTime.now())){
//                order.setStatus(OrderStatus.CLOSE.getCode());
//                order.setPaymentStatus(3);
//                order.setReason("超时未支付");
//                orderService.save(order);
//                TradingVo tradingVo = new TradingVo();
//                tradingVo.setTradingOrderNo(order.getTradingOrderNo());
//                tradingVo.setEnterpriseId(1561414331L);
//                wechatCommonPayHandler.closeTrading(tradingVo);
//                tradingOrderNos.add(order.getTradingOrderNo());
//            }
//        }
//        // 关闭账单
//        if (CollUtil.isNotEmpty(tradingOrderNos)) {
//            billService.close(tradingOrderNos);
//        }
//
//        List<Order> executedOrders = orderService.selectByStatus(OrderStatus.DONE.getCode());
//        for(Order order : executedOrders){
//            if(order.getUpdateTime().plusMinutes(30).isBefore(LocalDateTime.now())){
//                order.setStatus(OrderStatus.FINISHED.getCode());
//                System.out.println(order.getUpdateTime() + "--" + LocalDateTime.now());
//                log.info("######更新为已完成######" + order.getUpdateTime() + "--" + LocalDateTime.now());
//                orderService.save(order);
//            }
//        }
//    }
//}
