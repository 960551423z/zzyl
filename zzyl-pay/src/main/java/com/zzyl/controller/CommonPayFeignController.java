
package com.zzyl.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.constant.TradingCacheConstant;
import com.zzyl.enums.TradingEnum;
import com.zzyl.exception.ProjectException;
import com.zzyl.handler.CommonPayHandler;
import com.zzyl.service.RefundRecordService;
import com.zzyl.utils.ExceptionsUtil;
import com.zzyl.vo.RefundRecordVo;
import com.zzyl.vo.TradingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CommonPayController.java
 *  基础支付控制器
 */
@RequestMapping("trade-common-feign")
@RestController
@Api(tags = "支付基础服务")
@Slf4j
public class CommonPayFeignController {

    @Autowired
    CommonPayHandler wechatCommonPayHandler;

    @Autowired
    RedissonClient redissonClient;

    @Resource
    private RefundRecordService refundRecordService;

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

    /***
     *  申请退款查询接口
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
     * 将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
     * @param refundRecordNo 退款交易单
     * @return
     */
    @GetMapping("query-refund")
    @ApiOperation(value = "根据退款单号进行退款结果查询",notes = "根据退款单号进行退款结果查询")
    public ResponseResult queryRefundDownLineTrading(@RequestParam Long refundRecordNo){
        return ResponseResult.success(refundRecordService.findByRefundNo(refundRecordNo));
    }


    /***
     *  根据退款编号，订单编号 申请人 申请时间 状态 分页查询退款记录
     * @param refundRecordVo 退款交易单
     * @return
     */
    @PostMapping("query-refund-record")
    @ApiOperation(value = "分页查询退款记录",notes = "分页查询退款记录")
    @ApiImplicitParam(name = "refundRecordVo",value = "退款交易单",required = true,dataType = "RefundRecordVo")
    public ResponseResult<PageResponse<RefundRecordVo>> queryRefundRecord(@RequestBody RefundRecordVo refundRecordVo){
        return ResponseResult.success(refundRecordService.queryRefundRecord(refundRecordVo));
    }

}

