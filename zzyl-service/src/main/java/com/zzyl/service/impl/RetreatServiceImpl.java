package com.zzyl.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.constant.AccraditationRecordConstant;
import com.zzyl.constant.PendingTasksConstant;
import com.zzyl.constant.RetreatConstant;
import com.zzyl.dto.BillDto;
import com.zzyl.dto.RetreatClearingBillDto;
import com.zzyl.dto.RetreatDto;
import com.zzyl.dto.RetreatReqDto;
import com.zzyl.entity.*;
import com.zzyl.exception.BaseException;
import com.zzyl.handler.CommonPayHandler;
import com.zzyl.mapper.*;
import com.zzyl.service.*;
import com.zzyl.utils.CodeUtil;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.utils.StringUtils;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.BalanceVo;
import com.zzyl.vo.RecoreVo;
import com.zzyl.vo.TradingVo;
import com.zzyl.vo.UserVo;
import com.zzyl.vo.retreat.DueBack;
import com.zzyl.vo.retreat.RetreatBillVo;
import com.zzyl.vo.retreat.TasVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 阿庆
 */
@Service
public class RetreatServiceImpl implements RetreatService {

    private static final String RETREAT_CODE_PREFIX = "TZ";

    @Autowired
    private RetreatMapper retreatMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private AccraditationRecordMapper accraditationRecordMapper;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private ActFlowCommService actFlowCommService;

    @Autowired
    private AccraditationRecordService accraditationRecordService;

    /**
     * 退住申请
     * @param retreat
     */
    @Override
    @Transactional
    public ResponseResult createRetreat(Retreat retreat) {

        //1.验证状态
        Long elderId = retreat.getElderId();
        Retreat dbRetreat = retreatMapper.selectByElderId(elderId);
        if (dbRetreat != null && retreat.getFlowStatus() == null) {
            return ResponseResult.error(dbRetreat.getName() + "已经发起了申请退住");
        }

        if (ObjectUtil.isNotEmpty(retreat.getTaskId())) {
            // 修改
            Retreat oldRetreat = retreatMapper.selectByCode(retreat.getRetreatCode());
            if (!oldRetreat.getElderId().equals(retreat.getElderId())) {
                Elder elder = new Elder();
                elder.setId(retreat.getElderId());
                elder.setStatus(1);
                elderMapper.updateByPrimaryKeySelective(elder);
            }
        }

        //是否在入住期限内
        CheckInConfig currentConfigByElderId = checkInConfigService.findCurrentConfigByElderId(retreat.getElderId());
        if (currentConfigByElderId.getCostStartTime().isAfter(retreat.getCheckOutTime()) || currentConfigByElderId.getCostEndTime().isBefore(retreat.getCheckOutTime())) {
            return ResponseResult.error("请在费用期限内发起退住申请");
        }

        Bill bill = billMapper.selectFirstByElder(elderId);
        // 账单开始结束时间
        int year = Integer.parseInt(bill.getBillMonth().substring(0, 4));
        int monthOfYear = Integer.parseInt(bill.getBillMonth().substring(5, 7));
        LocalDateTime firstDayOfMonth = LocalDateTime.of(year, monthOfYear, 1, 0, 0, 0);
        while (firstDayOfMonth.plusMonths(1).isBefore(retreat.getCheckOutTime())) {
            // 生成首月账单
            BillDto billDto = new BillDto();
            billDto.setElderId(elderId);
            String format = LocalDateTimeUtil.format(firstDayOfMonth.plusMonths(1), "yyyy-MM");
            billDto.setBillMonth(format);
            try {
                billService.createMonthBill(billDto);
            }catch (Exception e) {
                e.printStackTrace();
            }
            firstDayOfMonth = firstDayOfMonth.plusMonths(1);
        }

        // 关闭不在账单周期内 并且未支付的账单
        billMapper.delete(elderId, retreat.getCheckOutTime());
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);
        //申请人部门编号
        String deptNo = user.getDeptNo();

        //退住标题
        String title = retreat.getName() + "的退住申请";
        //设置流程状态
        retreat.setFlowStatus(Retreat.FlowStatus.APPLY_APPROVAL.getCode());
        retreat.setStatus(Retreat.Status.APPLICATION.getCode());
        if (retreat.getRetreatCode() != null) {
            if (!user.getId().equals(retreat.getApplicatId())) {
                return ResponseResult.error("不是申请人，不是能提交数据");
            }
            //申请人的名字
            retreat.setTitle(title);
            //修改
            retreatMapper.update(retreat);

        } else {
            retreat.setApplicat(user.getRealName());
            retreat.setApplicatId(user.getId());
            //创建时间
            retreat.setCreateTime(LocalDateTime.now());
            retreat.setTitle(title);
            retreat.setCreateBy(user.getId());//创建人
            retreat.setStatus(Retreat.Status.APPLICATION.getCode());
            //设置下个操作人
            String retreatCode = CodeUtil.generateCode(RETREAT_CODE_PREFIX, redisTemplate, 5);
            retreat.setRetreatCode(retreatCode);
            retreat.setDeptNo(deptNo);
            //设置下一个状态
            retreatMapper.createRetreat(retreat);

            //修改老人状态为退住中....
            Elder elder = new Elder();
            elder.setId(retreat.getElderId());
            elder.setStatus(3);
            elderMapper.updateByPrimaryKeySelective(elder);
        }
        if (ObjectUtil.isNotEmpty(retreat.getTaskId())) {
            actFlowCommService.completeProcess(retreat.getTitle(), retreat.getTaskId(), user.getId().toString(), 1, retreat.getStatus());
        }else {
            Map<String, Object> setvariables = setvariables(retreat.getRetreatCode());
            actFlowCommService.start(retreat.getId(), "retreat", user, setvariables, true);
        }
        //保存审核（操作）记录
        Long nextAssignee = actFlowCommService.getNextAssignee("retreat", "retreat:" + retreat.getId());
        RecoreVo recoreVo = getRecoreVo(
                retreat,
                user,
                AccraditationRecordConstant.AUDIT_STATUS_PASS,
                "",
                "发起申请-申请退住",
                "护理组长审批-申请审批",
                nextAssignee,
                AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success("提交成功");
    }

    /**
     * 获取操作记录数据
     * @param retreat  退住对象
     * @param user  当前登录用户
     * @param status  审核状态
     * @param option  操作意见
     * @param step    当前步骤
     * @param nextStep   下一步说明
     * @param nextAssignee  下一个审核人
     * @return
     */
    private RecoreVo getRecoreVo(Retreat retreat, User user, Integer status, String option, String step, String nextStep, Long nextAssignee,Integer handleType){
        RecoreVo recoreVo = new RecoreVo();
        recoreVo.setId(retreat.getId());
        recoreVo.setType(AccraditationRecordConstant.RECORD_TYPE_RETREAT);
        recoreVo.setFlowStatus(retreat.getFlowStatus());
        recoreVo.setStatus(status);
        recoreVo.setOption(option);
        recoreVo.setNextStep(nextStep);
        recoreVo.setNextAssignee(nextAssignee);
        recoreVo.setUserId(user.getId());
        recoreVo.setRealName(user.getRealName());
        recoreVo.setHandleType(handleType);
        recoreVo.setStep(step);
        return recoreVo;
    }


    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private BillService billService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RetreatBillMapper retreatBillMapper;

    public Map<String, Object> setvariables(String id) {
        //设置流程变量
        Map<String,Object> variables = new HashMap<>();

        // 护理员
        Retreat retreat = retreatMapper.getRetreatByCode(id);
        Long applicatId = retreat.getApplicatId();
        variables.put("assignee0", applicatId);

        variables.put("assignee0Name", retreat.getApplicat());
        variables.put("processTitle", retreat.getTitle());

        // 护理部部门编号
        Dept dept = deptMapper.selectByDeptNo(RetreatConstant.NURSING_DEPT_CODE);
        //部门领导id
        Long leaderId = dept.getLeaderId();
        variables.put("assignee1",leaderId);

        // 法务员工
        //设置下一个审核人
        //找到法务部门编号--->通过编号查询法务中所有的员工（非leader）---->获取第一个人，找到这个人的id,然后查询对应的角色
        List<Long> legatUserIds = userMapper.selectByDeptNo(RetreatConstant.LEGAL_DEPT_CODE);
        variables.put("assignee2",legatUserIds.get(legatUserIds.size() - 1));

        // 结算员
        List<Long> jsUserIds = userMapper.selectByDeptNo(RetreatConstant.SETTLEMENT_DEPT_CODE);
        variables.put("assignee3",jsUserIds.get(jsUserIds.size() - 1));

        // 结算组长
        Dept jsDept = deptMapper.selectByDeptNo(RetreatConstant.SETTLEMENT_DEPT_CODE);
        //部门领导id
        Long jsLeaderId = jsDept.getLeaderId();
        variables.put("assignee4", jsLeaderId);

        // 副院长
        List<Long> fyzUserIdList = userMapper.selectByDeptNo(RetreatConstant.DEAN_OFFICE_DEPT_CODE);
        variables.put("assignee5", fyzUserIdList.get(0));

        // 结算员
        variables.put("assignee6", jsUserIds.get(jsUserIds.size() - 1));

        // 流程类型
        variables.put("processType", 1);

        // 流程类型
        variables.put("processCode", retreat.getRetreatCode());

        // 流程类型
        variables.put("processStatus", 1);
        return variables;
    }

    /**
     * 查询退住信息
     * @param retreatCode 退住单号
     * @param assigneeId  审核人
     * @return
     */
    @Override
    public ResponseResult<TasVo> getRetreat(String retreatCode, String assigneeId, Integer flowStatus, String taskId) {

        //当前登录人id
        Long mgtUserId = UserThreadLocal.getMgtUserId();
        //需要返回到前端的数据
        TasVo vo = new TasVo();

        //查询退住单数据
        Retreat retreat = retreatMapper.getRetreatByCode(retreatCode);
        //审批记录数据
        List<AccraditationRecord> accraditationRecordList = accraditationRecordMapper.getAccraditationRecordByBuisId(retreat.getId(), PendingTasksConstant.TASK_TYPE_RETREAT);
        vo.setAccraditationRecords(accraditationRecordList);
        vo.setRetreat(retreat);
        //默认显示退住数据
        vo.setIsShow(1);

        //默认不显示撤回按钮
        vo.setIsRevocation(false);

        //当前退住单状态
        Integer dbFlowstatus = retreat.getFlowStatus();

        //申请人撤回单独处理，如果前端传递过来的流程状态0(申请人状态),数据库目前的流程状态为1(护理组长待审批状态) 且 操作人是申请人  则显示撤回
        if (dbFlowstatus.equals(Retreat.FlowStatus.APPLY_APPROVAL.getCode())
                && retreat.getApplicatId().equals(mgtUserId)
                && flowStatus.equals(Retreat.FlowStatus.APPLY.getCode())
                && retreat.getStatus().equals(Retreat.Status.APPLICATION.getCode())) {
            vo.setIsRevocation(true);
        }

        if (flowStatus < 0 ) {
            flowStatus = retreat.getFlowStatus();
        }

        Integer isShow = 1;

        if (ObjectUtil.isNotEmpty(taskId)) {
            isShow = actFlowCommService.isCurrentUserAndStep(taskId, flowStatus, retreat);
        }

        //默认不显示撤回按钮
        vo.setIsRevocation(isShow == 2 && flowStatus.equals(retreat.getFlowStatus() - 1) && retreat.getStatus().equals(1));

        if (isShow.equals(2)) {
            isShow = 1;
        }
        if (isShow.equals(3)) {
            isShow = 1;
        }
        // 默认显示入住申请基本信息数据
        vo.setIsShow(isShow);

        vo.setType(1);
        //副院长审批完成之后，则不能撤回
        if (flowStatus.equals(Retreat.FlowStatus.RETREAT_APPROVAL.getCode())) {
            vo.setIsRevocation(false);
        }

        //查询解除协议
        Contract contract = contractMapper.selectByElderId(retreat.getElderId());
        RescissionContract rescissionContract = new RescissionContract();
        rescissionContract.setRescissionContractName(retreat.getName() + "的解除协议");
        rescissionContract.setContractUrl(contract.getReleasePdfUrl());
        rescissionContract.setRelieveTime(contract.getReleaseDate());
        rescissionContract.setCommitor(contract.getReleaseSubmitter());
        vo.setRescissionContract(rescissionContract);

        //如果状态为3:结算员调整账单，则需要查询账单列表
        if (dbFlowstatus.equals(Retreat.FlowStatus.RECONCILIATION_BILL.getCode())) {
            //账单列表
            RetreatBillVo retreatBillVo = billService.retreatSettlement(retreat.getElderId(), retreat.getCheckOutTime(), retreat.getStatus());
            vo.setRetreatBillVo(retreatBillVo);
        }

        //状态大于3 且 小于6 ，为结算审批、院长审批两个状态显示的账单内容
        if (dbFlowstatus > 3 && dbFlowstatus < 6) {
            RetreatBill retreatBill = retreatBillMapper.selectByRetreatId(retreat.getId());
            if (retreatBill != null && StringUtils.isNotEmpty(retreatBill.getBillJson())) {
                RetreatBillVo billVo = JSONUtil.toBean(retreatBill.getBillJson(), RetreatBillVo.class);

                vo.setRetreatBillVo(billVo);
            }
        }

        //订单已完成或已关闭
        if (!retreat.getStatus().equals(Retreat.Status.APPLICATION.getCode())) {
            //展示记录
            RetreatBill retreatBill = retreatBillMapper.selectByRetreatId(retreat.getId());
            vo.setIsShow(1);
            vo.setRetreatBill(retreatBill);
        }

        //展示最终账单数据(计算算清费用最终的账单显示)
        if (dbFlowstatus.equals(Retreat.FlowStatus.BILL_SETTLEMENT.getCode())) {
            if (flowStatus >= 3 && flowStatus < 6) {
                RetreatBill retreatBill = retreatBillMapper.selectByRetreatId(retreat.getId());
                if (retreatBill != null && StringUtils.isNotEmpty(retreatBill.getBillJson())) {
                    RetreatBillVo billVo = JSONUtil.toBean(retreatBill.getBillJson(), RetreatBillVo.class);
                    vo.setRetreatBillVo(billVo);
                }
            } else {
                //退款金额、预缴款金额
                RetreatBillVo retreatBillVo = billService.retreatSettlement(retreat.getElderId(), retreat.getCheckOutTime(), retreat.getStatus());
                vo.setRetreatBillVo(retreatBillVo);
            }

        }

        vo.setType(1);
        AccraditationRecord accraditationRecord = accraditationRecordMapper.getLastByBuisId(retreat.getId(), PendingTasksConstant.TASK_TYPE_RETREAT);
        if (ObjectUtil.isNotEmpty(accraditationRecord)) {
            vo.setNextApprover(accraditationRecord.getNextApproverRole());
        }
        return ResponseResult.success(vo);
    }

    @Autowired
    @Qualifier("wechatCommonPayHandler")
    private CommonPayHandler commonPayHandler;

    @Autowired
    private ContractMapper contractMapper;

    /**
     * -提交退住申请
     * -护理组长审核
     * -法务人员提交
     * -结算员提交
     * -结算员组长审核
     * -副院长审核
     * -结算员调整账单提交
     *
     * @param retreatDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult submitRetreat(RetreatDto retreatDto) {

        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        UserVo user = JSON.parseObject(subject, UserVo.class);

        /**
         * 判断当前操作人与登录人是否一致
         */
        if (retreatDto.getAssigneeId() == null || !retreatDto.getAssigneeId().equals(user.getId().toString())) {
            return ResponseResult.error("当前任务不属于你");
        }

        //查询退住数据
        Retreat retreat = retreatMapper.getRetreatByCode(retreatDto.getCode());

        //判断老人是否已经完成了退住
        if (CheckIn.Status.FINISHED.getCode().equals(retreat.getStatus())) {
            throw new BaseException("该老人已经完成退住办理");
        }

        //准备操作记录对象
        RecoreVo recoreVo = new RecoreVo();
        recoreVo.setRealName(user.getRealName());
        recoreVo.setUserId(user.getId());
        recoreVo.setStatus(AccraditationRecordConstant.AUDIT_STATUS_PASS);
        recoreVo.setId(retreat.getId());
        recoreVo.setType(AccraditationRecordConstant.RECORD_TYPE_RETREAT);
        recoreVo.setFlowStatus(retreat.getFlowStatus());
        //审核状态为1，护理员组长审核
        if (retreat.getFlowStatus().equals(Retreat.FlowStatus.APPLY_APPROVAL.getCode())) {
            //更新退住单状态
            retreatMapper.updateRetreatByFlowStatus(retreat.getId(), Retreat.FlowStatus.RESCISSION_CONTRACT.getCode());
            //操作记录
            recoreVo.setOption("同意");
            recoreVo.setStep("护理组长审批-申请审批");
            recoreVo.setNextStep("法务处理-解除合同");
            recoreVo.setHandleType(AccraditationRecordConstant.RECORD_HANDLE_TYPE_AUDIT);
        }

        //审核状态为2，法务提交解除协议
        if (retreat.getFlowStatus().equals(Retreat.FlowStatus.RESCISSION_CONTRACT.getCode())) {
            //操作记录
            recoreVo.setOption("同意");
            recoreVo.setStep("法务处理-解除合同");
            recoreVo.setNextStep("结算员处理-调整账单");
            recoreVo.setHandleType(AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
            //更新退住单流程状态
            retreatMapper.updateRetreatByFlowStatus(retreat.getId(), Retreat.FlowStatus.RECONCILIATION_BILL.getCode());

            //查询老人合同
            Contract contract = contractMapper.selectByElderId(retreat.getElderId());
            //修改合同
            contract.setReleaseDate(LocalDateTime.now());
            contract.setReleasePdfUrl(retreatDto.getRescissionContract().getContractUrl());
            contract.setReleaseSubmitter(user.getRealName());
            contractMapper.updateByPrimaryKeySelective(contract);
        }

        //审核状态为3，结算员调整账单
        if (retreat.getFlowStatus().equals(Retreat.FlowStatus.RECONCILIATION_BILL.getCode())) {
            //保存账单
            RetreatBill retreatBill = new RetreatBill();
            retreatBill.setRetreatId(retreat.getId());
            retreatBill.setBillJson(retreatDto.getBillJson());
            retreatBillMapper.insert(retreatBill);
            //更新退住单流程状态
            retreatMapper.updateRetreatByFlowStatus(retreat.getId(), Retreat.FlowStatus.BILL_APPROVAL.getCode());

            //操作记录
            recoreVo.setOption("同意");
            recoreVo.setStep("结算员处理-调整账单");
            recoreVo.setNextStep("结算组长审批-账单审批");
            recoreVo.setHandleType(AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        }

        //审核状态为4，结算组长审核
        if (retreat.getFlowStatus().equals(Retreat.FlowStatus.BILL_APPROVAL.getCode())) {

            //更新退住单流程状态
            retreatMapper.updateRetreatByFlowStatus(retreat.getId(), Retreat.FlowStatus.RETREAT_APPROVAL.getCode());

            //操作记录
            recoreVo.setOption("同意");
            recoreVo.setStep("结算组长审批-账单审批");
            recoreVo.setNextStep("院长审批-退住审批");
            recoreVo.setHandleType(AccraditationRecordConstant.RECORD_HANDLE_TYPE_AUDIT);
        }

        //审核状态为5，副院长审核
        if (retreat.getFlowStatus().equals(Retreat.FlowStatus.RETREAT_APPROVAL.getCode())) {
            //操作记录
            recoreVo.setOption("同意");
            recoreVo.setStep("院长审批-退住审批");
            recoreVo.setNextStep("结算处理-费用算清");
            recoreVo.setHandleType(AccraditationRecordConstant.RECORD_HANDLE_TYPE_AUDIT);
            //更新退住单流程状态
            retreatMapper.updateRetreatByFlowStatus(retreat.getId(), Retreat.FlowStatus.BILL_SETTLEMENT.getCode());

            //停止老人合同
            contractMapper.updateStatusByElderId(retreat.getElderId(), 3);

            //处理账单，应退&&转为余额
            RetreatBill retreatBill = retreatBillMapper.selectByRetreatId(retreat.getId());
            if (retreatBill != null && StringUtils.isNotEmpty(retreatBill.getBillJson())) {
                RetreatBillVo billVo = JSONUtil.toBean(retreatBill.getBillJson(), RetreatBillVo.class);
                //处理退款
                List<DueBack> dueBackList = billVo.getDueBackList();
                dueBackList.stream()
                        .filter(x -> x.getType() == 1)
                        .forEach(v -> {
                            TradingVo tradingVo = new TradingVo();
                            tradingVo.setTradingOrderNo(v.getTradingOrderNo());
                            tradingVo.setCreateType(2);
                            commonPayHandler.refundTrading(tradingVo);
                        });

                RetreatClearingBillDto retreatClearingBillDto = new RetreatClearingBillDto();

                retreatClearingBillDto.setDueBackList(dueBackList.stream().filter(v -> v.getType() == 0).collect(Collectors.toList()));

                retreatClearingBillDto.setLocalDateTime(retreat.getCheckOutTime());

                retreatClearingBillDto.setUserId(user.getId());
                //老人id
                retreatClearingBillDto.setElderId(retreat.getElderId());

                BalanceVo balanceVo = billVo.getBalanceVo();
                //余额扣减金额
                BigDecimal depositDeductions = balanceVo.getDepositAmount().subtract(balanceVo.getArrearsAmount());
                //押金备注
                retreatClearingBillDto.setDepositRemark(balanceVo.getRemark());
                //押金扣减金额
                retreatClearingBillDto.setDepositDeductions(depositDeductions.compareTo(BigDecimal.ZERO) == 1 ? depositDeductions : new BigDecimal(0));

                //关闭账户，清空数据
                billService.retreatClearingBill(retreatClearingBillDto);
            }
        }

        //审核状态为6，结算员算清费用
        if (retreat.getFlowStatus().equals(Retreat.FlowStatus.BILL_SETTLEMENT.getCode())) {
            //操作记录
            recoreVo.setOption("同意");
            recoreVo.setStep("结算处理-费用算清");
            recoreVo.setNextStep("");
            recoreVo.setHandleType(AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);

            //修改退住单状态 status为为2(已完成)
            retreat.setStatus(Retreat.Status.FINISHED.getCode());
            retreatMapper.updateStatus(retreat.getId(), Retreat.Status.FINISHED.getCode());

            RetreatBill retreatBill1 = retreatBillMapper.selectByRetreatId(retreat.getId());
            balanceService.close(retreat.getElderId(), retreatBill1.getRefundAmount());

            //老人状态改为禁用
            Elder elder = new Elder();
            elder.setId(retreat.getElderId());
            elder.setStatus(5);
            elderMapper.updateByPrimaryKeySelective(elder);

            CheckInConfig checkInConfig = checkInConfigService.findCurrentConfigByElderId(retreat.getElderId());
            if (ObjectUtil.isNotEmpty(checkInConfig)) {
                // 床位状态
                Bed bedByNum = bedMapper.getBedByNum(checkInConfig.getBedNo());
                bedByNum.setBedStatus(0);
                bedMapper.updateBed(bedByNum);
                elderMapper.clearBedNum(retreat.getElderId());
            }
        }

        //完成任务
        actFlowCommService.completeProcess("", retreatDto.getTaskId(), user.getId().toString(), 1, retreat.getStatus());

        //保存操作记录
        Long nextAssignee = actFlowCommService.getNextAssignee("retreat", "retreat:" + retreat.getId());
        recoreVo.setNextAssignee(nextAssignee);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success("提交成功");
    }

    @Autowired
    private ElderMapper elderMapper;

    @Autowired
    private CheckInConfigService checkInConfigService;

    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private BalanceService balanceService;

    /**
     * 获取操作步骤
     *
     * @return
     */
    private String getRecordCurrentStep(Integer flowStatus) {
        String currentStep = "";

        switch (flowStatus) {
            case 0:
                currentStep = "申请退住";
                break;
            case 1:
                currentStep = "申请审批";
                break;
            case 2:
                currentStep = "解除合同";
                break;
            case 3:
                currentStep = "调整账单";
                break;
            case 4:
                currentStep = "账单审批";
                break;
            case 5:
                currentStep = "退住审批";
                break;
            case 6:
                currentStep = "费用清算";
                break;
        }
        return currentStep;
    }

    /**
     * 审核拒绝
     *
     * @param retreatCode 退住单code
     * @param reject      拒绝意见
     * @return
     */
    @Transactional
    @Override
    public ResponseResult auditReject(String retreatCode, String reject, String taskId) {

        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //查询退住单
        Retreat retreat = retreatMapper.getRetreatByCode(retreatCode);
        if (retreat == null) {
            throw new BaseException("退住单不存在");
        }

        //修改退住单状态为3（已关闭）
        retreatMapper.updateStatus(retreat.getId(), Retreat.Status.CLOSED.getCode());

        //完成任务
        actFlowCommService.closeProcess(taskId, PendingTasksConstant.TASK_STATUS_CLOSED);
        //获取当前的审核步骤
        String step = getCurrentStep(retreat.getFlowStatus());
        //保存审核记录
        RecoreVo recoreVo = getRecoreVo(retreat,user,
                AccraditationRecordConstant.AUDIT_STATUS_REJECT,
                reject,
                step,"",null,AccraditationRecordConstant.RECORD_HANDLE_TYPE_AUDIT);
        accraditationRecordService.insert(recoreVo);

        //如果审核状态大于3，则需要恢复老人合同为生效
        if (retreat.getFlowStatus() > 3) {
            contractMapper.updateByElderId(retreat.getElderId(), 1);
        }

        //退住审核拒绝，则恢复老人状态
        Elder elder = new Elder();
        elder.setId(retreat.getElderId());
        elder.setStatus(1);
        elderMapper.updateByPrimaryKeySelective(elder);

        return ResponseResult.success();
    }

    /**
     * 获取当前操作步骤
     * @param flowStatus
     * @return
     */
    private String getCurrentStep(Integer flowStatus) {
        String step = "";
        switch (flowStatus) {
            case 1:
                step = "护理组长-申请审批";
                break;
            case 4:
                step = "结算组长-账单审批";
                break;
            case 5:
                step = "副院长-退住审批";
                break;
        }
        return step;
    }

    /**
     * 撤回
     *
     * @param retreatCode
     * @param flowStatus
     * @return
     */
    @Transactional
    @Override
    public ResponseResult revocation(String retreatCode, Integer flowStatus, String taskId) {

        //获取登录用户
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //查询退住单
        Retreat retreat = retreatMapper.getRetreatByCode(retreatCode);
        if (retreat == null) {
            throw new BaseException("退住单不存在");
        }

        //当前状态+1 == reteat.flowstatus
        if (retreat.getFlowStatus() > flowStatus) {
            throw new BaseException("退住单已审核，不能撤回");
        }

        //如果状态为3(则需要删除之前上传的合同)
        if (flowStatus.equals(Retreat.FlowStatus.RECONCILIATION_BILL.getCode())) {
//            rescissionContractMapper.deleteByRetreatId(retreat.getId());
            //如果审核状态大于3，则需要恢复老人合同为生效
            contractMapper.updateByElderId(retreat.getElderId(), 1);
        }

        //如果状态为4,删除账单信息
        if (flowStatus.equals(Retreat.FlowStatus.BILL_APPROVAL.getCode())) {
            retreatBillMapper.deleteByByRetreatId(retreat.getId());
        }

        //修改退住单  状态回退 -1
        Integer state = flowStatus - 1;
        retreatMapper.updateRetreatByFlowStatus(retreat.getId(), state);
        //撤回任务
        actFlowCommService.withdrawTask(taskId, false);

        //保存审核记录
        String currentStep = "撤回处理";
        if (retreat.getFlowStatus() == 1) {
            currentStep = "撤回处理";
        }
        if (retreat.getFlowStatus() == 4 || retreat.getFlowStatus() == 5) {
            currentStep = "撤回审批";
        }
        currentStep = currentStep + "-" + getRecordCurrentStep(retreat.getFlowStatus() - 1);
        RecoreVo recoreVo = getRecoreVo(retreat,user,
                AccraditationRecordConstant.AUDIT_STATUS_WITHDRAWS,"",
                currentStep,"",
                null,AccraditationRecordConstant.RECORD_HANDLE_TYPE_AUDIT);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success();
    }

    /**
     * 驳回
     *
     * @param retreatCode
     * @return
     */
    @Transactional
    @Override
    public ResponseResult disapprove(String retreatCode, String message, String taskId) {

        //获取登录用户
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //查询退住单
        Retreat retreat = retreatMapper.getRetreatByCode(retreatCode);
        if (retreat == null) {
            throw new BaseException("退住单不存在");
        }

        //删除账单数据
        retreatBillMapper.deleteByByRetreatId(retreat.getId());

        //修改退住状态为0
        retreatMapper.updateRetreatByFlowStatus(retreat.getId(), Retreat.FlowStatus.APPLY.getCode());

        //如果审核状态大于3，则需要恢复老人合同为生效
        if (retreat.getFlowStatus() > 3) {
            contractMapper.updateByElderId(retreat.getElderId(), 1);
        }

        actFlowCommService.rollBackTask(taskId, true);

        //保存审核记录
        RecoreVo recoreVo = getRecoreVo(retreat,user,
                AccraditationRecordConstant.AUDIT_STATUS_DISAPPROVE,message,
                "驳回申请-" + getRecordCurrentStep(retreat.getFlowStatus()),"",
                null,AccraditationRecordConstant.RECORD_HANDLE_TYPE_AUDIT);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success();


    }

    /**
     * 撤销
     *
     * @param retreatCode 退住编码
     * @return
     */
    @Transactional
    @Override
    public ResponseResult cancel(String retreatCode, String taskId) {
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //查询退住单
        Retreat retreat = retreatMapper.getRetreatByCode(retreatCode);
        if (retreat == null) {
            throw new BaseException("退住单不存在");
        }
        retreatMapper.updateRetreatByFlowStatus(retreat.getId(), retreat.getFlowStatus() -1);
        //修改退住单状态为3（已关闭）
        retreatMapper.updateStatus(retreat.getId(), Retreat.Status.CLOSED.getCode());

        //如果审核状态大于2，则需要恢复老人合同为生效
        if (retreat.getFlowStatus() > 2) {
            contractMapper.updateByElderId(retreat.getElderId(), 1);
        }

        //申请人撤销操作，则恢复老人状态
        Elder elder = new Elder();
        elder.setId(retreat.getElderId());
        elder.setStatus(1);
        elderMapper.updateByPrimaryKeySelective(elder);

        actFlowCommService.closeProcess(taskId, PendingTasksConstant.TASK_STATUS_CLOSED);

        //保存审核记录
        RecoreVo recoreVo = getRecoreVo(retreat,user,
                AccraditationRecordConstant.AUDIT_STATUS_DISAPPROVE,"",
                "撤销申请-申请退住","",
                null,AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success();
    }

    /**
     * 退住管理列表查询
     * @return
     */
    @Override
    public ResponseResult selectByPage(RetreatReqDto retreatReqDto) {
        PageHelper.startPage(retreatReqDto.getPageNum(), retreatReqDto.getPageSize());
        Page<List<Retreat>> pages = retreatMapper.selectByPage(retreatReqDto);
        PageResponse<Retreat> of = PageResponse.of(pages, Retreat.class);
        return ResponseResult.success(of);
    }
}
