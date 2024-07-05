package com.zzyl.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.constant.TradingCacheConstant;
import com.zzyl.enums.TradingEnum;
import com.zzyl.exception.ProjectException;
import com.zzyl.handler.CommonPayHandler;
import com.zzyl.service.OrderService;
import com.zzyl.utils.ExceptionsUtil;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.OrderVo;
import com.zzyl.vo.TradingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags = "订单管理")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    CommonPayHandler wechatCommonPayHandler;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation("取消")
    @PostMapping("/{orderId}/cancel")
    public ResponseResult<OrderVo> cancelOrder(@PathVariable("orderId") Long orderId, @RequestParam("reason") String reason) {
        OrderVo orderVo = orderService.cancelOrder(orderId, reason, 2);
        return ResponseResult.success(orderVo);
    }

    @ApiOperation("根据id查询")
    @GetMapping
    public ResponseResult<OrderVo> getOrderById(@RequestParam("orderId") Long orderId) {
        OrderVo orderList = orderService.getOrderById(orderId);
        return ResponseResult.success(orderList);
    }

    @ApiOperation("分页")
    @GetMapping("/search")
    public ResponseResult<OrderVo> searchOrders(
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "orderNo", required = false) String orderNo,
            @RequestParam(value = "elderlyName", required = false) String elderlyName,
            @RequestParam(value = "creator", required = false) String creator,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize) {
        PageResponse<OrderVo> listPageResponse = orderService.searchOrders(status, orderNo, elderlyName, creator, ObjectUtil.isEmpty(startTime)? null : LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime)? null : LocalDateTimeUtil.of(endTime), pageNum, pageSize);
        return ResponseResult.success(listPageResponse);
    }

    @ApiOperation("执行")
    @PostMapping("/{orderId}/do")
    public ResponseResult<OrderVo> doOrder(@PathVariable("orderId") Long orderId) {
        OrderVo orderVo = orderService.doOrder(orderId);
        return ResponseResult.success(orderVo);
    }

    /***
     *  申请退款接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param tradingVo 交易单
     * @return
     */
    @PostMapping("refund")
    @ApiOperation(value = "申请退款",notes = "申请退款")
    @ApiImplicitParam(name = "tradingVo",value = "交易单",required = true,dataType = "TradingVo")
    @ApiOperationSupport(includeParameters ={"tradingVo.tradingOrderNo",
            "tradingVo.operTionRefund","tradingVo.tradingChannel"})
    public ResponseResult refundTrading(@RequestBody TradingVo tradingVo){
        //1、对交易订单加锁
        Long productOrderNo = tradingVo.getProductOrderNo();
        String key = TradingCacheConstant.REFUND_PAY + productOrderNo;
        RLock lock = redissonClient.getFairLock(key);
        try {
            boolean flag = lock.tryLock(TradingCacheConstant.REDIS_WAIT_TIME, TimeUnit.SECONDS);
            if (flag){
                tradingVo.setCreateType(2);
                return ResponseResult.success(wechatCommonPayHandler.refundTrading(tradingVo));
            }else {
                throw new ProjectException(TradingEnum.REFUND_FAIL);
            }
        } catch (Exception e) {
            log.error("统一收单交易退款接口异常:{}", ExceptionsUtil.getStackTraceAsString(e));
            throw new ProjectException(TradingEnum.REFUND_FAIL);
        }finally {
            lock.unlock();
        }
    }
}

