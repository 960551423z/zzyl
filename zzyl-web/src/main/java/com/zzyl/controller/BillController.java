package com.zzyl.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.BillDto;
import com.zzyl.dto.PrepaidRechargeRecordDto;
import com.zzyl.service.BalanceService;
import com.zzyl.service.BillService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.BalanceVo;
import com.zzyl.vo.BillVo;
import com.zzyl.vo.PrepaidRechargeRecordVo;
import com.zzyl.vo.TradingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Bill控制器
 */
@Api(tags = "账单管理")
@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private BalanceService balanceService;
    /**
     * 创建账单
     */
    @ApiOperation("创建账单")
    @PostMapping
    public ResponseResult create(@RequestBody BillDto bill) {
        billService.createMonthBill(bill);
        return ResponseResult.success();
    }

    /**
     * 创建账单线下支付记录
     */
    @ApiOperation("创建账单线下支付记录")
    @PostMapping("payRecord")
    public ResponseResult payRecord(@RequestBody TradingVo tradingVo) {
        this.billService.payRecord(tradingVo);
        return ResponseResult.success();
    }

    /**
     * 取消账单
     */
    @ApiOperation("取消账单")
    @PutMapping("cancel/{id}")
    public ResponseResult cancelById(@RequestBody BillDto billDto, @PathVariable("id") Long id) {
        billDto.setId(id);
        return ResponseResult.success(billService.cancelById(billDto));
    }

    /**
     * 根据id查询账单
     */
    @ApiOperation("根据id查询账单")
    @GetMapping("/{id}")
    public ResponseResult<BillVo> getById(@PathVariable Long id) {
        BillVo billVo = billService.selectByPrimaryKey(id);
        return ResponseResult.success(billVo);
    }

    /**
     * 分页查询账单
     */
    @ApiOperation("分页查询账单")
    @GetMapping("/page/")
    public ResponseResult<PageResponse<BillVo>> getBillPage(@ApiParam(value = "账单编号") @RequestParam(name = "billNo", required = false) String billNo,
                                                            @ApiParam(value = "老人姓名") @RequestParam(name = "elderName", required = false) String elderName,
                                                            @ApiParam(value = "老人身份证号") @RequestParam(name = "elderIdCard", required = false) String elderIdCard,
                                                            Long startTime,
                                                            Long endTime,
                                                            @ApiParam(value = "支付状态") @RequestParam(name = "transactionStatus", required = false)  Integer transactionStatus,
                                                            @ApiParam(value = "页码（默认为1）") @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                            @ApiParam(value = "每页数量（默认为10）") @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResponse<BillVo> billPage = billService.getBillPage(billNo, elderName, elderIdCard, ObjectUtil.isEmpty(startTime)? null : LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime)? null : LocalDateTimeUtil.of(endTime), transactionStatus, null, pageNum, pageSize);
        return ResponseResult.success(billPage);
    }

    /**
     * 分页查询欠费账单
     */
    @ApiOperation("分页查询欠费账单")
    @GetMapping("/arrears/")
    public ResponseResult<PageResponse<BillVo>> arrears(@ApiParam(value = "床位编号") @RequestParam(name = "bedNo", required = false) String bedNo,
                                                        @ApiParam(value = "老人姓名") @RequestParam(name = "elderName", required = false) String elderName,
                                                        @ApiParam(value = "页码（默认为1）") @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                        @ApiParam(value = "每页数量（默认为10）") @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResponse<BillVo> arrears = billService.arrears(bedNo, elderName, pageNum, pageSize);
        return ResponseResult.success(arrears);
    }

    /**
     * 分页查询余额
     */
    @ApiOperation("分页查询余额")
    @GetMapping("/balance/")
    public ResponseResult<PageResponse<BalanceVo>> balance(@ApiParam(value = "床位编号") @RequestParam(name = "bedNo", required = false) String bedNo,
                                                           @ApiParam(value = "老人姓名") @RequestParam(name = "elderName", required = false) String elderName,
                                                           @ApiParam(value = "页码（默认为1）") @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                           @ApiParam(value = "每页数量（默认为10）") @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResponse<BalanceVo> balanceVoPageResponse = balanceService.page(bedNo, elderName, pageNum, pageSize);
        return ResponseResult.success(balanceVoPageResponse);
    }

    /**
     * 创建预付费充值记录
     */
    @ApiOperation("创建预付费充值记录")
    @PostMapping("prepaidRechargeRecord")
    public ResponseResult createPrepaidRechargeRecord(@RequestBody PrepaidRechargeRecordDto dto) {
        billService.savePrepaidRechargeRecord(dto);
        return ResponseResult.success();
    }


    /**
     * 分页查询预付费充值记录
     */
    @ApiOperation("分页查询预付费充值记录")
    @GetMapping("/prepaidRechargeRecord/page")
    public ResponseResult<PageResponse<PrepaidRechargeRecordVo>> prepaidRechargeRecordPage(@ApiParam(value = "床位编号") @RequestParam(name = "bedNo", required = false) String bedNo,
                                                                                           @ApiParam(value = "老人姓名") @RequestParam(name = "elderName", required = false) String elderName,
                                                                                           @ApiParam(value = "页码（默认为1）") @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                                                                           @ApiParam(value = "每页数量（默认为10）") @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageResponse<PrepaidRechargeRecordVo> prepaidRechargeRecordVoPageResponse = billService.prepaidRechargeRecordPage(bedNo, elderName, pageNum, pageSize);
        return ResponseResult.success(prepaidRechargeRecordVoPageResponse);
    }

}
