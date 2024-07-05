package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.BillDto;
import com.zzyl.dto.PrepaidRechargeRecordDto;
import com.zzyl.dto.RetreatClearingBillDto;
import com.zzyl.entity.Bill;
import com.zzyl.vo.BillVo;
import com.zzyl.vo.PrepaidRechargeRecordVo;
import com.zzyl.vo.TradingVo;
import com.zzyl.vo.retreat.RetreatBillVo;

import java.time.LocalDateTime;
import java.util.List;


/**
 * BillService
 * 账单
 */
public interface BillService {

    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return 删除结果
     */
    int deleteByElderId(Long id);

    /**
     * 生成月度账单
     *
     * @param billDto 账单实体
     * @return 插入结果
     */
    int createMonthBill(BillDto billDto);

    /**
     * 生成费用账单
     *
     * @param billDto 账单实体
     * @return 插入结果
     */
    int createProjectBill(BillDto billDto);

    /**
     * 选择性插入账单
     *
     * @param record 账单实体
     * @return 插入结果
     */
    int insertSelective(Bill record);

    /**
     * 根据主键选择账单
     *
     * @param id 主键
     * @return 账单实体
     */
    BillVo selectByPrimaryKey(Long id);

    /**
     * 选择性更新账单
     *
     * @param billDto 账单实体
     * @return 更新结果
     */
    int updateBytradingOrderNoSelective(BillDto billDto);

    /**
     * 取消账单
     *
     * @param billDto 取消账单
     * @return 取消结果
     */
    int cancelById(BillDto billDto);

    /**
     * 退住结算查询
     *
     * @param elderId       老人ID
     * @param localDateTime 实际退住时间
     * @param status
     * @return 退住结算详情
     */
    RetreatBillVo retreatSettlement(Long elderId, LocalDateTime localDateTime, Integer status);

    /**
     * 退住清算
     *
     * @return 退住清算详情
     */
    void retreatClearingBill(RetreatClearingBillDto retreatClearingBillDto);


    /**
     * 分页查询账单
     *
     * @param billNo      账单编号
     * @param elderName   老人姓名
     * @param elderIdCard 老人身份证号
     * @param startTime   账单月份
     * @param endTime
     * @param pageNum     页码
     * @param pageSize    每页数量
     * @return 分页结果
     */
    PageResponse<BillVo> getBillPage(String billNo, String elderName, String elderIdCard, LocalDateTime startTime, LocalDateTime endTime, Integer transactionStatus, Long elderId, Integer pageNum, Integer pageSize);

    /**
     * 分页查询欠费账单
     *
     * @param bedNo     床位号
     * @param elderName 老人姓名
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 分页结果
     */
    PageResponse<BillVo> arrears(String bedNo, String elderName, Integer pageNum, Integer pageSize);

    /**
     * 保存预充值记录
     *
     * @param dto 预充值记录实体
     */
    void savePrepaidRechargeRecord(PrepaidRechargeRecordDto dto);

    /**
     * 分页查询预充值记录
     *
     * @param bedNo     床位号
     * @param elderName 老人姓名
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 分页结果
     */
    PageResponse<PrepaidRechargeRecordVo> prepaidRechargeRecordPage(String bedNo, String elderName, Integer pageNum, Integer pageSize);


    /**
     * 支付账单线下
     * @param tradingVo
     */
    void payRecord(TradingVo tradingVo);

    /**
     * 线上支付
     * @param billDto
     */
    TradingVo pay(BillDto billDto);

    /**
     * 线上支付完成
     * @param tradingOrderNos 交易号
     */
    void payOrder(List<Long> tradingOrderNos);

    /**
     * 自动关闭账单
     * @param tradingOrderNos 交易号
     */
    void close(List<Long> tradingOrderNos);

    /**
     * 线上退款
     * @param tradingOrderNos 交易号
     */
    void refundOrder(List<Long> tradingOrderNos);
}