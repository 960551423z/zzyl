
package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zzyl.base.PageResponse;
import com.zzyl.dto.BillDto;
import com.zzyl.dto.PrepaidRechargeRecordDto;
import com.zzyl.dto.RetreatClearingBillDto;
import com.zzyl.entity.*;
import com.zzyl.enums.BillStatus;
import com.zzyl.enums.BillType;
import com.zzyl.exception.BaseException;
import com.zzyl.handler.wechat.WechatWapPayHandler;
import com.zzyl.mapper.*;
import com.zzyl.service.*;
import com.zzyl.utils.CodeUtil;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.*;
import com.zzyl.vo.retreat.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Resource
    private BillMapper billMapper;

    @Resource
    private BalanceService balanceService;

    @Resource
    private CheckInConfigService checkInConfigService;

    @Resource
    private PrepaidRechargeRecordMapper prepaidRechargeRecordMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TradingService tradingService;

    @Resource
    private RefundRecordService refundRecordService;

    @Resource
    private RefundRecordMapper refundRecordMapper;

    @Resource
    private WechatWapPayHandler wechatWapPayHandler;

    @Resource
    private MemberService memberService;

    @Resource
    private MemberElderService memberElderService;

    @Resource
    private NursingLevelService nursingLevelService;

    @Resource
    private RoomService roomService;

    /**
     * 根据id删除账单
     * @param id 账单id
     * @return 删除结果
     */
    @Override
    public int deleteByElderId(Long id) {
        return billMapper.deleteByElderId(id);
    }

    /**
     * 生成月度账单
     * @param billDto 账单实体
     * @return 插入结果
     */
    @Override
    public int createMonthBill(BillDto billDto) {
        Bill bill1 = billMapper.selectByElderAndMonth(billDto.getElderId(), billDto.getBillMonth());
        if (ObjectUtil.isNotEmpty(bill1)) {
            throw new BaseException("该老人的月度账单已生成，不可重复生成");
        }
        Bill bill = BeanUtil.toBean(billDto, Bill.class);
        // 生成账单

        bill.setBillType(BillType.MONTH.getOrdinal());
        bill.setTransactionStatus(BillStatus.UN_PAY.getOrdinal());
        // 编号
        String zd = CodeUtil.generateCode("ZD", stringRedisTemplate, 5);
        bill.setBillNo(zd);
        // 查询入住配置
        CheckInConfig checkInConfig = checkInConfigService.findCurrentConfigByElderId(bill.getElderId());
        NursingLevelVo byId = nursingLevelService.getById(checkInConfig.getNursingLevelId());
        bill.setLname(byId.getName());
        RoomVo room = roomService.getRoom(Long.parseLong(checkInConfig.getRemark().split(":")[1]));
        bill.setTypeName(room.getTypeName());
        // 账单开始结束时间
        int year = Integer.parseInt(bill.getBillMonth().substring(0, 4));
        int monthOfYear = Integer.parseInt(bill.getBillMonth().substring(5, 7));

        LocalDateTime firstDayOfMonth = LocalDateTime.of(year, monthOfYear, 1, 0, 0, 0);
        if (checkInConfig.getCostEndTime().isBefore(firstDayOfMonth)) {
            throw new BaseException("该月不在入住期限内");
        }
        // 押金
        BigDecimal depositAmount = new BigDecimal(0);
        if (checkInConfig.getCostStartTime().isAfter(firstDayOfMonth) || checkInConfig.getCostStartTime().isEqual(firstDayOfMonth)) {
            // 首月
            bill.setBillStartTime(checkInConfig.getCostStartTime());
            depositAmount = checkInConfig.getDepositAmount();
        } else {
            bill.setBillStartTime(firstDayOfMonth);
        }
        // 支付截止时间
        bill.setPaymentDeadline(bill.getBillStartTime().plusDays(6));
        // 押金金额
        bill.setDepositAmount(depositAmount);

        LocalDateTime lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.toLocalDate().lengthOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        if (checkInConfig.getCostEndTime().isAfter(lastDayOfMonth)) {
            bill.setBillEndTime(lastDayOfMonth);
        } else {
            // 末月
            bill.setBillEndTime(checkInConfig.getCostEndTime());
        }

        // 费用共计天数
        int i = bill.getBillEndTime().getDayOfMonth() - bill.getBillStartTime().getDayOfMonth() + 1;
        bill.setTotalDays(i);
        // 每月应付
        BigDecimal cost = checkInConfig.getBedCost().add(checkInConfig.getOtherCost()).add(checkInConfig.getNursingCost())
                .subtract(checkInConfig.getMedicalInsurancePayment()).subtract(checkInConfig.getGovernmentSubsidy());

        // 当月天数
        int days = lastDayOfMonth.getDayOfMonth() - firstDayOfMonth.getDayOfMonth() + 1;
        // 本期应付 = （每月应付 / 当月天数）* 共计天数
        // 首月和最后一个月需要扣减
        BigDecimal currentCost = cost.divide(new BigDecimal(days), 60, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(i));
        bill.setCurrentCost(currentCost);
        // 账单金额 = 本期应付 + 押金
        BigDecimal billAmount = currentCost.add(depositAmount);
        bill.setBillAmount(billAmount);

        if (billAmount.compareTo(new BigDecimal(0)) == 0) {
            bill.setTransactionStatus(BillStatus.PAY.ordinal());
            billMapper.insert(bill);
        }

        // 应付金额 = 本期应付 - 预交款
        // 查询余额表
        Balance balance = balanceService.selectByElderId(bill.getElderId());
        if (balance.getPrepaidBalance().compareTo(billAmount) >= 0) {
            // 预交款足够 应付金额 = 0
            bill.setPayableAmount(new BigDecimal(0));
            // 预交款扣减
            balance.setPrepaidBalance(balance.getPrepaidBalance().subtract(billAmount));
            bill.setPrepaidAmount(billAmount);
            bill.setTransactionStatus(BillStatus.PAY.ordinal());

        } else {
            // 预交款不足
            BigDecimal payableAmount = billAmount.subtract(balance.getPrepaidBalance());
            bill.setPayableAmount(payableAmount);
            bill.setPrepaidAmount(balance.getPrepaidBalance());
            // 预交款扣减
            balance.setPrepaidBalance(new BigDecimal(0));
            bill.setTransactionStatus(BillStatus.UN_PAY.ordinal());

        }
        // 预交款扣减后回写数据库
        balanceService.save(balance);

        return billMapper.insert(bill);
    }

    /**
     * 生成费用账单
     * @param billDto 账单实体
     * @return 插入结果
     */
    @Override
    public int createProjectBill(BillDto billDto) {
        Bill bill = BeanUtil.toBean(billDto, Bill.class);
        // 生成账单
        bill.setBillType(BillType.PROJECT.getOrdinal());
        bill.setTransactionStatus(BillStatus.UN_PAY.getOrdinal());
        billDto.setBillAmount(billDto.getPayableAmount());
        // 编号
        String zd = CodeUtil.generateCode("ZD", stringRedisTemplate, 5);
        bill.setBillNo(zd);
        bill.setCurrentCost(bill.getBillAmount());
        bill.setPrepaidAmount(bill.getBillAmount());
        bill.setDepositAmount(bill.getBillAmount());
        bill.setPayableAmount(bill.getBillAmount());
        bill.setPaymentDeadline(LocalDateTime.now().plusMinutes(15));
        String format = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM");
        bill.setBillMonth(format);
        return billMapper.insert(bill);
    }


    /**
     * 插入账单
     * @param record 账单实体
     * @return 插入结果
     */
    @Override
    public int insertSelective(Bill record) {
        return 0;
    }

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public BillVo selectByPrimaryKey(Long id) {
        Bill bill = billMapper.selectByPrimaryKey(id);
        BillVo billVo = BeanUtil.toBean(bill, BillVo.class);
        CheckInConfigVo checkInConfigVo = billVo.getCheckInConfigVo();
        BigDecimal add = checkInConfigVo.getBedCost().add(checkInConfigVo.getNursingCost()).add(checkInConfigVo.getOtherCost());
        checkInConfigVo.setAdd1(add);
        BigDecimal add1 = checkInConfigVo.getGovernmentSubsidy().add(checkInConfigVo.getMedicalInsurancePayment());
        checkInConfigVo.setAdd2(add1);
        checkInConfigVo.setMonthCost(add.subtract(add1));
        billVo.setCheckInConfigVo(checkInConfigVo);
        BigDecimal subtract = billVo.getBillAmount().subtract(billVo.getPrepaidAmount());

        // 已支付 已关闭并且应付大于0的情况 展示其他支付方式的金额
        if (billVo.getTransactionStatus().equals(1) || (billVo.getTransactionStatus().equals(2) && billVo.getPayableAmount().compareTo(new BigDecimal(0))== 0)) {
            billVo.setOtherAmount(subtract);
        }

        if (billVo.getBillType().equals(BillType.PROJECT.getOrdinal())) {
            Trading tradByTradingOrderNo = tradingService.findTradByTradingOrderNo(billVo.getTradingOrderNo());
            if (ObjectUtil.isNotEmpty(tradByTradingOrderNo)) {
                RefundRecordVo refundRecordVo = new RefundRecordVo();
                List<RefundRecord> listByTradingOrderNo = refundRecordService.findListByTradingOrderNo(bill.getTradingOrderNo());
                if (CollUtil.isNotEmpty(listByTradingOrderNo)) {
                    refundRecordVo = BeanUtil.toBean(listByTradingOrderNo.get(0), RefundRecordVo.class);

                }
                Member byId = memberService.getById(tradByTradingOrderNo.getCreateBy());
                if (ObjectUtil.isNotEmpty(byId)) {
                    tradByTradingOrderNo.setMemberCreator(byId.getName());
                    tradByTradingOrderNo.setPhone(byId.getPhone());
                    billVo.setMemberCreator(byId.getName());
                    refundRecordVo.setCreator(byId.getName());
                }
                billVo.setRefundRecordVo(refundRecordVo);
                List<Order> orders = orderMapper.selectByTradingOrderNo(Lists.newArrayList(billVo.getTradingOrderNo()));
                if (CollUtil.isNotEmpty(orders)) {
                    billVo.setOrderId(orders.get(0).getId());
                    billVo.setOrderNo(orders.get(0).getOrderNo());
                    String format = LocalDateTimeUtil.format(orders.get(0).getCreateTime(), "yyyy-MM");
                    billVo.setBillMonth(format);
                }
                billVo.setTradingVo(Lists.newArrayList(BeanUtil.toBean(tradByTradingOrderNo, TradingVo.class)));
            }
        }
        return billVo;
    }

    @Override
    public int updateBytradingOrderNoSelective(BillDto billDto) {
        Bill bill = BeanUtil.toBean(billDto, Bill.class);
        return billMapper.updateBytradingOrderNoSelective(bill);
    }

    @Resource
    private NursingTaskMapper nursingTaskMapper;

    @Override
    public int cancelById(BillDto billDto) {
        Bill bill = BeanUtil.toBean(billDto, Bill.class);
        bill.setTransactionStatus(2);
        Bill bill1 = billMapper.selectByPrimaryKey(bill.getId());
        nursingTaskMapper.updateByBillNoSelective(Lists.newArrayList(bill1.getBillNo()));
        return billMapper.updateByPrimaryKey(bill);
    }

    /**
     * 退住结算查询
     * @param elderId 老人ID
     * @param localDateTime 实际退住时间
     * @param status
     * @return 退住结算详情
     */
    @Override
    public RetreatBillVo retreatSettlement(Long elderId, LocalDateTime localDateTime, Integer status) {
        RetreatBillVo retreatBillVo = new RetreatBillVo();

        // 应退
        List<DueBack> dueBacks = getDueBack(elderId, localDateTime);

        // 欠费 欠费可能转应退
        List<Arrearage> arrearages = getArrearage(elderId, localDateTime, dueBacks);
        retreatBillVo.setArrearageList(arrearages);

        retreatBillVo.setDueBackList(dueBacks);

        // 余额
        // 押金是否已经交过
        Boolean flag = false;
        Bill bill = billMapper.selectDepositByElderAndStatus(elderId, BillStatus.PAY.getOrdinal());
        if (ObjectUtil.isNotEmpty(bill)) {
            flag = true;
        }
        Balance balance = balanceService.selectByElderId(elderId);
        if (ObjectUtil.isEmpty(balance)) {
            balance = new Balance();
            balance.setDepositAmount(new BigDecimal(0));
            balance.setPrepaidBalance(new BigDecimal(0));
            balance.setArrearsAmount(new BigDecimal(0));
        }
        if (Boolean.FALSE.equals(flag)) {
            balance.setDepositAmount(new BigDecimal(0));
        }
        retreatBillVo.setBalanceVo(BeanUtil.toBean(balance, BalanceVo.class));

        // 未缴订单
        List<Bill> unpaidBills = billMapper.selectUnpaidByElder(elderId, BillStatus.UN_PAY.getOrdinal());
        List<Unpaid> unpaids = unpaidBills.stream().map(v -> {
            Unpaid unpaid = new Unpaid();
            unpaid.setCode(v.getBillNo());
            unpaid.setType(v.getBillType());
            unpaid.setAmount(v.getCurrentCost());
            // 备注是护理项目名称
            unpaid.setNursingName(v.getRemark());
            return unpaid;
        }).collect(Collectors.toList());
        retreatBillVo.setUnpaidList(unpaids);
        return retreatBillVo;
    }

    /**
     * 查询应退月度账单
     * @param elderId
     * @param localDateTime
     * @return
     */
    private List<DueBack> getDueBack(Long elderId, LocalDateTime localDateTime) {
        List<Bill> dueBackBills = billMapper.selectDueBackByElder(elderId, BillStatus.PAY.getOrdinal(), localDateTime);
        List<Bill> bills = billMapper.selectOrderDueBackByElder(elderId, BillStatus.PAY.getOrdinal());
        dueBackBills.addAll(bills);
        return dueBackBills.stream().map(v -> {
            DueBack dueBack = new DueBack();
            dueBack.setCode(v.getBillNo());
            dueBack.setType(v.getBillType());
            dueBack.setBillMonth(v.getBillMonth());
            dueBack.setTradingOrderNo(v.getTradingOrderNo());
            // 备注是护理项目名称
            dueBack.setNursingName(v.getRemark());

            if (v.getBillType().equals(0) && v.getBillStartTime().isBefore(localDateTime) && v.getBillEndTime().isAfter(localDateTime)) {
                // 扣款天数
                int days = v.getTotalDays() - (localDateTime.getDayOfMonth() - v.getBillStartTime().getDayOfMonth());
                // 本期应付 = （每月应付 / 费用天数）* 扣款天数
                // 首月和最后一个月需要扣减
                BigDecimal currentCost = v.getCurrentCost().divide(new BigDecimal(v.getTotalDays()), 60, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(days));
                dueBack.setAmount(currentCost);
                dueBack.setRealDay(v.getTotalDays() - days);
                dueBack.setDueBackDay(days);
            } else {
                dueBack.setAmount(v.getCurrentCost());
                dueBack.setRealDay(0);
                dueBack.setDueBackDay(v.getTotalDays());
            }

            return dueBack;
        }).collect(Collectors.toList());
    }

    /**
     * 查询欠费月度账单
     * @param elderId
     * @param localDateTime
     * @param dueBacks
     * @return
     */
    private List<Arrearage> getArrearage(Long elderId, LocalDateTime localDateTime, List<DueBack> dueBacks) {
        List<Bill> arrearageBills = billMapper.selectArrearageByElder(elderId, BillStatus.UN_PAY.getOrdinal(), localDateTime);
        return arrearageBills.stream().map(v -> {
            Arrearage arrearage = new Arrearage();
            arrearage.setCode(v.getBillNo());
            arrearage.setBillMonth(v.getBillMonth());
            arrearage.setAmount(v.getCurrentCost());
            if (v.getBillType().equals(0) && v.getBillStartTime().isBefore(localDateTime) && v.getBillEndTime().isAfter(localDateTime)) {
                // 扣款天数
                int days = localDateTime.getDayOfMonth() - v.getBillStartTime().getDayOfMonth();
                // 本期应付 = （每月应付 / 费用天数）* 扣款天数
                // 首月和最后一个月需要扣减
                BigDecimal billAmount = v.getBillAmount(); // 总费 3000
                // 当前月月费 1000多
                BigDecimal currentCost = v.getCurrentCost().divide(new BigDecimal(v.getTotalDays()), 60, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(days));
                //当前月月费 - 已付
                arrearage.setAmount(currentCost.subtract(v.getPrepaidAmount()));
                if (arrearage.getAmount().compareTo(new BigDecimal(0)) < 0) {
                    // 转为应退
                    DueBack dueBack = new DueBack();
                    dueBack.setCode(v.getBillNo());
                    dueBack.setType(v.getBillType());
                    dueBack.setBillMonth(v.getBillMonth());
                    dueBack.setTradingOrderNo(v.getTradingOrderNo());
                    // 备注是护理项目名称
                    dueBack.setNursingName(v.getRemark());
                    dueBack.setAmount(billAmount.subtract(v.getPayableAmount()).subtract(currentCost));
                    dueBack.setRealDay(days);
                    dueBack.setDueBackDay(v.getTotalDays() - days);
                    dueBacks.add(dueBack);
                    return null;
                }
            }
            else {
                arrearage.setAmount(v.getCurrentCost().subtract(v.getPrepaidAmount()));
                // 首月和最后一个月需要扣减
                BigDecimal billAmount = v.getBillAmount(); // 总费 3000
                if (arrearage.getAmount().compareTo(new BigDecimal(0)) < 0) {
                    // 转为应退
                    DueBack dueBack = new DueBack();
                    dueBack.setCode(v.getBillNo());
                    dueBack.setType(v.getBillType());
                    dueBack.setBillMonth(v.getBillMonth());
                    dueBack.setTradingOrderNo(v.getTradingOrderNo());
                    // 备注是护理项目名称
                    dueBack.setNursingName(v.getRemark());
                    dueBack.setAmount(billAmount.subtract(v.getPayableAmount()).subtract(v.getCurrentCost()));
                    dueBack.setRealDay(v.getTotalDays());
                    dueBack.setDueBackDay(0);
                    dueBacks.add(dueBack);
                    return null;
                }
            }
            return arrearage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Autowired
    private NursingElderMapper nursingElderMapper;

    /**
     * 退住清算
     *
     * @return 退住清算详情
     */
    @Transactional
    @Override
    public void retreatClearingBill(RetreatClearingBillDto retreatClearingBillDto) {
        log.info(JSONUtil.toJsonStr(retreatClearingBillDto));

        // 押金转预交款
        Balance balance = balanceService.selectByElderId(retreatClearingBillDto.getElderId());
        var ref = new Object() {
            BigDecimal prepaid = balance.getPrepaidBalance();
        };

        Bill bill0 = billMapper.selectDepositByElderAndStatus(retreatClearingBillDto.getElderId(), BillStatus.PAY.getOrdinal());
        if (ObjectUtil.isNotEmpty(bill0)) {
            ref.prepaid = ref.prepaid.add(balance.getDepositAmount()).subtract(retreatClearingBillDto.getDepositDeductions());
            balance.setDepositAmount(new BigDecimal(0));
            balance.setPrepaidBalance(ref.prepaid);
            balanceService.save(balance);
        }

        // 应退转预交款
        dueBackToPrepaidAmount(retreatClearingBillDto, balance);

        // 预交款抵扣欠费账单
        prepaidAmountToArrearage(retreatClearingBillDto, balance);

        // 退住的删除掉任务
        NursingTask nursingTask = new NursingTask();
        nursingTask.setElderId(retreatClearingBillDto.getElderId());
        nursingTask.setStatus(1);
        nursingTaskMapper.deleteByExample(nursingTask);

        nursingElderMapper.deleteByElderId(retreatClearingBillDto.getElderId());
    }

    /**
     * 预交款抵扣欠费月度账单
     * @param retreatClearingBillDto 清算参数
     * @param balance 余额
     */
    private void prepaidAmountToArrearage(RetreatClearingBillDto retreatClearingBillDto, Balance balance) {
        List<Bill> arrearageBills = billMapper.selectArrearageByElder(retreatClearingBillDto.getElderId(), BillStatus.UN_PAY.getOrdinal(), retreatClearingBillDto.getLocalDateTime());
        ArrayList<Trading> tradings = Lists.newArrayList();
        ArrayList<RefundRecord> refundRecords = Lists.newArrayList();
        arrearageBills.forEach(bill -> {
            bill.setDepositAmount(new BigDecimal(0));
            if (ObjectUtil.isEmpty(bill.getTradingOrderNo())) {
                bill.setTradingOrderNo(bill.getId());
            }
            BigDecimal billAmount = bill.getPayableAmount();
            if (bill.getBillType().equals(0) && bill.getBillStartTime().isBefore(retreatClearingBillDto.getLocalDateTime()) && bill.getBillEndTime().isAfter(retreatClearingBillDto.getLocalDateTime())) {
                // 扣款天数
                int days = retreatClearingBillDto.getLocalDateTime().getDayOfMonth() - bill.getBillStartTime().getDayOfMonth();
                // 本期应付 = （每月应付 / 费用天数）* 扣款天数
                // 首月和最后一个月需要扣减

                BigDecimal currentCost = bill.getCurrentCost().divide(new BigDecimal(bill.getTotalDays()), 60, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(days));
                bill.setCurrentCost(currentCost);
                BigDecimal oldCost = bill.getBillAmount();
                bill.setBillAmount(currentCost);
                Integer totalDays = bill.getTotalDays();
                bill.setTotalDays(days);
                // 扣减后 需要重新计算已经付了的部分

                BigDecimal paydAmount = oldCost.subtract(billAmount);  // 已经付了的部分
                billAmount = currentCost.subtract(paydAmount); // 当前应付

                if (billAmount.compareTo(new BigDecimal(0))  < 0 ) {
                    // 增加预交款
                    balance.setPrepaidBalance(balance.getPrepaidBalance().add(paydAmount.subtract(currentCost)));
                    bill.setPayableAmount(new BigDecimal(0));
                    bill.setTransactionStatus(BillStatus.PAY.ordinal());
                    billMapper.updateByIdSelective(bill);
                    RefundRecord refundRecord = new RefundRecord();
                    refundRecord.setMemo(days + "");
                    refundRecord.setRefundMsg(totalDays - days + "");

                    refundRecord.setTradingOrderNo(bill.getTradingOrderNo());
                    refundRecord.setProductOrderNo(bill.getId());
                    refundRecord.setRemark("退款清算");
                    refundRecord.setRefundAmount(paydAmount.subtract(currentCost));
                    refundRecord.setCreateBy(retreatClearingBillDto.getUserId());
                    refundRecord.setRefundStatus(2);
                    refundRecord.setCreateType(2);
                    refundRecords.add(refundRecord);
                    return;
                }
            }
            else {
                // 扣减后 需要重新计算已经付了的部分
                BigDecimal oldCost = bill.getBillAmount();
                BigDecimal paydAmount = oldCost.subtract(billAmount);  // 已经付了的部分
                billAmount = bill.getCurrentCost().subtract(paydAmount); // 当前应付
                bill.setBillAmount(bill.getCurrentCost());
                if (billAmount.compareTo(new BigDecimal(0))  < 0 ) {
                    // 增加预交款
                    balance.setPrepaidBalance(balance.getPrepaidBalance().add(paydAmount.subtract(bill.getCurrentCost())));
                    bill.setPayableAmount(new BigDecimal(0));
                    bill.setTransactionStatus(BillStatus.PAY.ordinal());
                    billMapper.updateByIdSelective(bill);

                    RefundRecord refundRecord = new RefundRecord();
                    refundRecord.setMemo(0 + "");
                    refundRecord.setRefundMsg(bill.getTotalDays() + "");

                    refundRecord.setTradingOrderNo(bill.getTradingOrderNo());
                    refundRecord.setProductOrderNo(bill.getId());
                    refundRecord.setRemark("退款清算");
                    refundRecord.setRefundAmount(paydAmount.subtract(bill.getCurrentCost()));
                    refundRecord.setCreateBy(retreatClearingBillDto.getUserId());
                    refundRecord.setRefundStatus(2);
                    refundRecord.setCreateType(2);
                    refundRecords.add(refundRecord);
                    return;
                }
            }

            // 预交款抵扣欠费
            if (balance.getPrepaidBalance().compareTo(billAmount) >= 0) {
                // 预交款足够 应付金额 = 0
                bill.setPayableAmount(new BigDecimal(0));
                // 预交款扣减
                balance.setPrepaidBalance(balance.getPrepaidBalance().subtract(billAmount));
                bill.setPrepaidAmount(bill.getPrepaidAmount().add(billAmount));
                bill.setTransactionStatus(BillStatus.PAY.ordinal());

                Trading trading = new Trading();
                trading.setProductOrderNo(bill.getId());
                trading.setTradingChannel("预交款支付");
                trading.setTradingAmount(billAmount);
                trading.setTradingType("1");
                trading.setTradingState(4);
                tradings.add(trading);
                billMapper.updateByIdSelective(bill);
                return;
            }

            // 预交款不足
            BigDecimal payableAmount = billAmount.subtract(balance.getPrepaidBalance());
            bill.setPayableAmount(payableAmount);
            // 扣完
            if (balance.getPrepaidBalance().compareTo(new BigDecimal(0)) == 0) {
                billMapper.updateByIdSelective(bill);
                return;
            }

            // 预交款扣减
            bill.setPrepaidAmount(bill.getPrepaidAmount().add(balance.getPrepaidBalance()));


            bill.setTransactionStatus(BillStatus.UN_PAY.ordinal());

            Trading trading = new Trading();
            trading.setProductOrderNo(bill.getId());
            trading.setTradingChannel("预交款支付");
            trading.setTradingAmount(balance.getPrepaidBalance());
            trading.setTradingType("1");
            trading.setTradingState(4);
            tradings.add(trading);
            billMapper.updateByIdSelective(bill);
            balance.setPrepaidBalance(new BigDecimal(0));

        });
        if (CollUtil.isNotEmpty(tradings)) {
            tradingMapper.insertBatch(tradings);
        }
        if (CollUtil.isNotEmpty(refundRecords)) {
            refundRecordMapper.insertBatch(refundRecords);
        }
        balanceService.save(balance);
    }

    /**
     * 应退月度账单转预交款
     * @param retreatClearingBillDto 清算参数
     * @param balance 余额
     */
    private void dueBackToPrepaidAmount(RetreatClearingBillDto retreatClearingBillDto, Balance balance) {

        Map<String, BigDecimal> decimalMap = retreatClearingBillDto.getDueBackList().stream().collect(Collectors.toMap(DueBack::getCode, v -> v.getAmount().subtract(v.getRealAmount())));

        List<Bill> dueBackBills = billMapper.selectDueBackByElder(retreatClearingBillDto.getElderId(), BillStatus.PAY.getOrdinal(), retreatClearingBillDto.getLocalDateTime());
        var ref = new Object() {
            BigDecimal prepaid = balance.getPrepaidBalance();
        };

        ArrayList<RefundRecord> refundRecords = Lists.newArrayList();
        dueBackBills.forEach(bill -> {
            if (ObjectUtil.isEmpty(bill.getTradingOrderNo())) {
                bill.setTradingOrderNo(bill.getId());
            }
            // 账单退款记录
            RefundRecord refundRecord = new RefundRecord();
            BigDecimal old = bill.getCurrentCost();
            if (bill.getBillStartTime().isBefore(retreatClearingBillDto.getLocalDateTime()) && bill.getBillEndTime().isAfter(retreatClearingBillDto.getLocalDateTime())) {
                // 入住天数
                int days = (retreatClearingBillDto.getLocalDateTime().getDayOfMonth() - bill.getBillStartTime().getDayOfMonth());
                // 本期应付 = （每月应付 / 费用天数）* 扣款天数
                // 首月和最后一个月需要扣减
                BigDecimal currentCost = bill.getCurrentCost().divide(new BigDecimal(bill.getTotalDays()), 60, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(days));
                bill.setCurrentCost(currentCost);

                bill.setBillAmount(bill.getDepositAmount().add(currentCost));

                bill.setPayableAmount(new BigDecimal(0));

                refundRecord.setMemo(days + ""); // 实际天数
                refundRecord.setRefundMsg(bill.getTotalDays() - days  + ""); // 退款天数
                bill.setTotalDays(days);

            } else {
                refundRecord.setMemo(0 + "");
                refundRecord.setRefundMsg(bill.getTotalDays() + "");
                bill.setTotalDays(0);
            }

            if (ObjectUtil.isEmpty(bill.getTradingOrderNo())) {
                bill.setTradingOrderNo(bill.getId());
            }
            billMapper.updateByIdSelective(bill);


            refundRecord.setTradingOrderNo(bill.getTradingOrderNo());
            refundRecord.setProductOrderNo(bill.getId());
            refundRecord.setRemark(retreatClearingBillDto.getRemark());
            refundRecord.setCreateBy(retreatClearingBillDto.getUserId());
            refundRecord.setRefundStatus(2);
            refundRecord.setCreateType(2);

            // 押金扣减备注
            balance.setRemark(retreatClearingBillDto.getDepositRemark());

            BigDecimal refundAmount = null;
            if (old.compareTo(bill.getCurrentCost()) == 0) {
                refundAmount = bill.getCurrentCost();
            } else {
                refundAmount = old.subtract(bill.getCurrentCost());
            }
            if (decimalMap.keySet().contains(bill.getBillNo())) {
                BigDecimal subtract = refundAmount.subtract(decimalMap.get(bill.getBillNo()));
                ref.prepaid = ref.prepaid.add(subtract);
                refundRecord.setRefundAmount(subtract);
            } else {
                ref.prepaid = ref.prepaid.add(refundAmount);
                refundRecord.setRefundAmount(refundAmount);
            }
           refundRecords.add(refundRecord);
        });
        balance.setPrepaidBalance(ref.prepaid);
        balanceService.save(balance);
        if (CollUtil.isEmpty(refundRecords)) {
            return;
        }
        refundRecordMapper.insertBatch(refundRecords);
    }


    /**
     * 分页查询账单
     * @param billNo 账单编号
     * @param elderName 老人姓名
     * @param elderIdCard 老人身份证号
     * @param startTime 账单月份
     * @param endTime
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @Override
    public PageResponse<BillVo> getBillPage(String billNo, String elderName, String elderIdCard, LocalDateTime startTime, LocalDateTime endTime, Integer transactionStatus, Long elderId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Long> elderIds = null;
        if (ObjectUtil.isNotEmpty(elderId)) {
            elderIds = Arrays.asList(elderId);
        } else {
            Long userId = UserThreadLocal.getUserId();
            if (ObjectUtil.isNotEmpty(userId)) {
                List<MemberElderVo> memberElderVos = memberElderService.listByMemberId(userId);
                elderIds = memberElderVos.stream().map(MemberElderVo::getElderId).distinct().collect(Collectors.toList());
                if (CollUtil.isEmpty(elderIds)) {
                    return PageResponse.getInstance();
                }
            }
        }
        Page<BillVo> page = billMapper.page(billNo, elderName, elderIdCard, startTime, endTime, transactionStatus, elderIds);
        return PageResponse.of(page, BillVo.class);
    }

    /**
     * 查询欠费账单
     * @param bedNo 床位号
     * @param elderName 老人姓名
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @Override
    public PageResponse<BillVo> arrears(String bedNo, String elderName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<BillVo> page =  billMapper.arrears(bedNo, elderName);
        return PageResponse.of(page, BillVo.class);
    }

    @Resource
    private ElderService elderService;

    @Resource
    private RetreatMapper retreatMapper;

    /**
     * 保存预交款充值记录
     * @param dto 预交款充值记录实体
     */
    @Transactional
    @Override
    public void savePrepaidRechargeRecord(PrepaidRechargeRecordDto dto) {
        PrepaidRechargeRecord prepaidRechargeRecord = BeanUtil.toBean(dto, PrepaidRechargeRecord.class);
        String yj = CodeUtil.generateCode("YJ", stringRedisTemplate, 5);
        prepaidRechargeRecord.setPrepaidRechargeNo(yj);
        prepaidRechargeRecordMapper.insert(prepaidRechargeRecord);
        Balance balance = balanceService.selectByElderId(prepaidRechargeRecord.getElderId());
        balance.setPrepaidBalance(balance.getPrepaidBalance().add(prepaidRechargeRecord.getRechargeAmount()));

        Boolean flag = false;
        ElderVo elderVo = elderService.selectByPrimaryKey(dto.getElderId());
        if (elderVo.getStatus().equals(3)) {
            Retreat retreat = retreatMapper.selectByElderId(elderVo.getId());
            if (retreat.getFlowStatus().equals(6)) {
                flag = true;
            }
        }
        if (!elderVo.getStatus().equals(3) || flag) {

            Bill lastByElder = billMapper.selectLastByElder(dto.getElderId());
            // 抵扣账单
            // 预交款抵扣欠费账单
            List<Bill> arrearageBills = billMapper.selectArrearageByElder(dto.getElderId(), BillStatus.UN_PAY.getOrdinal(), lastByElder.getBillEndTime());

            ArrayList<Trading> tradings = Lists.newArrayList();
            arrearageBills.forEach(bill -> {

                BigDecimal billAmount = bill.getPayableAmount();

                if (balance.getPrepaidBalance().compareTo(billAmount) >= 0) {
                    // 预交款足够 应付金额 = 0
                    bill.setPayableAmount(new BigDecimal(0));
                    // 预交款扣减
                    balance.setPrepaidBalance(balance.getPrepaidBalance().subtract(billAmount));
                    bill.setPrepaidAmount(bill.getPrepaidAmount().add(billAmount));

                    Trading trading = new Trading();
                    trading.setProductOrderNo(bill.getId());
                    trading.setTradingChannel("预交款支付");
                    trading.setTradingAmount(billAmount);
                    trading.setTradingType("1");
                    trading.setTradingState(4);
                    tradings.add(trading);

                    Long productOrderNo = trading.getProductOrderNo();

                    bill.setTradingOrderNo(productOrderNo);
                    if (elderVo.getStatus().equals(3)) {
                        bill.setTransactionStatus(BillStatus.PAY.getOrdinal());
                    } else {
                        bill.setTransactionStatus(BillStatus.PAY.getOrdinal());
                    }

                    billMapper.updateByIdSelective(bill);
                    return;
                }

                // 扣完
                if (balance.getPrepaidBalance().compareTo(new BigDecimal(0)) == 0) {
                    return;
                }

                // 预交款不足
                BigDecimal payableAmount = billAmount.subtract(balance.getPrepaidBalance());
                bill.setPayableAmount(payableAmount);
                // 预交款扣减
                bill.setPrepaidAmount(bill.getPrepaidAmount().add(balance.getPrepaidBalance()));

                Trading trading = new Trading();
                trading.setProductOrderNo(bill.getId());
                trading.setTradingChannel("预交款支付");
                trading.setTradingAmount(balance.getPrepaidBalance());
                trading.setTradingType("1");
                trading.setTradingState(4);
                tradings.add(trading);
                Long productOrderNo = trading.getProductOrderNo();

                bill.setTradingOrderNo(productOrderNo);
                bill.setTransactionStatus(BillStatus.UN_PAY.getOrdinal());
                billMapper.updateByIdSelective(bill);

                balance.setPrepaidBalance(new BigDecimal(0));
            });
            if (CollUtil.isNotEmpty(tradings)) {
                tradingMapper.insertBatch(tradings);
            }
        }
        balanceService.save(balance);
    }

    /**
     * 分页查询预交款充值记录
     * @param bedNo 床位号
     * @param elderName 老人姓名
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @Override
    public PageResponse<PrepaidRechargeRecordVo> prepaidRechargeRecordPage(String bedNo, String elderName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<PrepaidRechargeRecord> page =  prepaidRechargeRecordMapper.prepaidRechargeRecordPage(bedNo, elderName);
        return PageResponse.of(page, PrepaidRechargeRecordVo.class);
    }

    /**
     * 账单线下支付记录
     * @param tradingVo
     */
    @Override
    public void payRecord(TradingVo tradingVo) {
        Trading trading = BeanUtil.toBean(tradingVo, Trading.class);
        trading.setTradingType("1");
        trading.setTradingState(4);
        tradingService.saveOrUpdate(trading);
        Long productOrderNo = trading.getProductOrderNo();
        Bill bill = new Bill();
        bill.setId(productOrderNo);
        bill.setTradingOrderNo(productOrderNo);
        bill.setTransactionStatus(1);
        bill.setPayableAmount(new BigDecimal(0));
        billMapper.updateByIdSelective(bill);
    }

    @Resource
    private TradingMapper tradingMapper;

    /**
     * 线上支付
     * @param billDto
     */
    @Override
    public TradingVo pay(BillDto billDto) {
        Bill bill = BeanUtil.toBean(billDto, Bill.class);
        // 交易单
        Trading trading = tradingMapper.selectByProductOrderNo(bill.getId(), "1");
        Long userId = UserThreadLocal.getUserId();
        Member byId = memberService.getById(userId);
        if (ObjectUtil.isNotEmpty(trading)) {
            TradingVo tradingVo = new TradingVo();
            tradingVo.setTradingOrderNo(trading.getTradingOrderNo());
            tradingVo.setEnterpriseId(1561414331L);
            wechatWapPayHandler.closeTrading(tradingVo);
            tradingMapper.deleteByPrimaryKey(trading.getId());
        }
        TradingVo tradingVo = new TradingVo();
        tradingVo.setMemo("服务下单");

        if (ObjectUtil.isNotEmpty(userId)) {
            tradingVo.setOpenId(byId.getOpenId());
        }
        tradingVo.setTradingType("1");
        tradingVo.setTradingAmount(bill.getPayableAmount());
        tradingVo.setProductOrderNo(bill.getId());
        TradingVo tradingVo1 = wechatWapPayHandler.wapTrading(tradingVo);

        bill.setTradingOrderNo(tradingVo1.getTradingOrderNo());
        billMapper.updateByPrimaryKey(bill);
        return tradingVo1;
    }


    /**
     * 线上支付
     * @param tradingOrderNos 交易号
     */
    @Override
    public void payOrder(List<Long> tradingOrderNos) {
        billMapper.batchUpdateByTradingOrderNoSelective(tradingOrderNos, BillStatus.PAY.getOrdinal());
    }

    @Override
    public void close(List<Long> tradingOrderNos) {
        billMapper.obatchUpdateByTradingOrderNoSelective(tradingOrderNos, BillStatus.CLOSE.getOrdinal(), null);
    }

    /**
     * 线上退款
     * @param tradingOrderNos 交易号
     */
    @Override
    public void refundOrder(List<Long> tradingOrderNos) {
        billMapper.obatchUpdateByTradingOrderNoSelective(tradingOrderNos, BillStatus.CLOSE.getOrdinal(), new BigDecimal(0));
    }

}
