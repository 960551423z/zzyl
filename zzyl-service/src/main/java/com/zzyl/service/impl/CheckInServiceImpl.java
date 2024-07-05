package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.constant.AccraditationRecordConstant;
import com.zzyl.constant.PendingTasksConstant;
import com.zzyl.constant.RetreatConstant;
import com.zzyl.dto.CheckInDto;
import com.zzyl.dto.CheckInOtherDto;
import com.zzyl.dto.ElderDto;
import com.zzyl.entity.*;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.*;
import com.zzyl.service.*;
import com.zzyl.utils.CodeUtil;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.*;
import com.zzyl.vo.retreat.ElderVo;
import com.zzyl.vo.retreat.TasVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CheckInServiceImpl implements CheckInService {

    @Autowired
    private CheckInMapper checkInMapper;

    @Autowired
    private ElderService elderService;

    @Autowired
    private DeptMapper deptMapper;

    private static final String CHECK_IN_CODE_PREFIX = "RZ";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private AccraditationRecordMapper accraditationRecordMapper;

    @Autowired
    private AccraditationRecordService accraditationRecordService;

    @Autowired
    private ActFlowCommService actFlowCommService;

    @Autowired
    private CheckInConfigService checkInConfigService;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private NursingLevelService nursingLevelService;

    @Autowired
    private BedMapper bedMapper;

    @Transactional
    @Override
    public ResponseResult<CheckInVo> createCheckIn(CheckInDto checkInDto) {
        //1.验证状态
        ElderVo elderVo = elderService.selectByIdCardAndName(checkInDto.getElderDto().getIdCardNo(), checkInDto.getElderDto().getName());
        if (elderVo != null && checkInDto.getId() == null && !elderVo.getStatus().equals(5)) {
            return ResponseResult.error(checkInDto.getElderDto().getName() + "已经发起了申请入住");
        }
        //获取当前登录人
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        CheckIn checkIn = BeanUtil.toBean(checkInDto, CheckIn.class);
        JSONObject jsonObject = JSON.parseObject(checkIn.getOtherApplyInfo(), JSONObject.class);
        ElderDto elderDto = checkIn.getElderDto();

        CheckInOtherDto checkInOtherDto = BeanUtil.toBean(checkInDto, CheckInOtherDto.class);
        elderDto.setAge(jsonObject.getInteger("age").toString());
        elderDto.setSex(jsonObject.getInteger("sex").toString());
        jsonObject.put("checkInOtherDto", JSONUtil.toJsonStr(checkInOtherDto));
        jsonObject.put("elderDto", JSONUtil.toJsonStr(checkIn.getElderDto()));

        // 插入新的
        elderDto.setImage(checkInDto.getUrl1());
        //设置流程状态
        checkIn.setFlowStatus(CheckIn.FlowStatus.REVIEW.getCode());
        checkIn.setStatus(CheckIn.Status.APPLICATION.getCode());
        checkIn.setApplicat(user.getRealName());
        checkIn.setApplicatId(user.getId());
        //设置修改时间-->方便前端判断数据新旧
        checkIn.setUpdateTime(LocalDateTime.now());

        //判断id是否为空
        //id为空，则新增老人和入住信息
        //id不为空，修改老人和修改入住信息
        if (checkIn.getId() != null) {
            CheckIn checkIn1 = checkInMapper.selectByPrimaryKey(checkIn.getId());
            if (!user.getId().equals(checkIn1.getApplicatId())) {
                return ResponseResult.error("不是申请人，不能提交数据");
            }
            JSONObject jsonObjectOld = JSON.parseObject(checkIn1.getOtherApplyInfo(), JSONObject.class);
            String elderDtostrOld = jsonObjectOld.getString("elderDto");
            ElderDto elderDtoOld = JSON.parseObject(elderDtostrOld, ElderDto.class);
            Boolean flag = !elderDtoOld.getName().equals(elderDto.getName());
            elderDto.setId(checkIn1.getElderId());
            Elder elder = elderService.updateByPrimaryKeySelective(elderDto, flag);
            //入住标题
            String title = elder.getName() + "的入住申请";
            checkIn.setTitle(title);
            String elderDtostr = jsonObject.getString("elderDto");
            ElderDto elderDto1 = JSON.parseObject(elderDtostr, ElderDto.class);
            elderDto1.setName(elder.getName());
            jsonObject.put("elderDto", JSONUtil.toJsonStr(elderDto1));
            jsonObject.put("name", elder.getName());
            checkIn.setOtherApplyInfo(JSONUtil.toJsonStr(jsonObject));
            checkInMapper.updateByPrimaryKeySelective(checkIn);
        } else {
            //保存老人信息
            Elder elder = elderService.insert(elderDto);
            String elderDtostr = jsonObject.getString("elderDto");
            ElderDto elderDto1 = JSON.parseObject(elderDtostr, ElderDto.class);
            elderDto1.setName(elder.getName());
            jsonObject.put("elderDto", JSONUtil.toJsonStr(elderDto1));
            checkIn.setOtherApplyInfo(JSONUtil.toJsonStr(jsonObject));
            checkIn.setElderId(elder.getId());
            //入住标题
            String title = elder.getName() + "的入住申请";
            checkIn.setTitle(title);
            //设置流程状态
            checkIn.setFlowStatus(CheckIn.FlowStatus.REVIEW.getCode());
            checkIn.setStatus(CheckIn.Status.APPLICATION.getCode());
            checkIn.setApplicat(user.getRealName());
            checkIn.setApplicatId(user.getId());

            //申请人部门编号
            String deptNo = user.getDeptNo();
            String code = CodeUtil.generateCode(CHECK_IN_CODE_PREFIX, redisTemplate, 5);
            checkIn.setCheckInCode(code);
            checkIn.setDeptNo(deptNo);
            checkIn.setCounselor(user.getRealName());
            checkIn.setCreateTime(LocalDateTime.now());
            checkInMapper.insert(checkIn);
        }

        //如果是修改的话，则直接完成流程即可
        if (ObjectUtil.isNotEmpty(checkInDto.getTaskId())) {
            actFlowCommService.completeProcess(checkIn.getTitle(), checkInDto.getTaskId(), user.getId().toString(), 1, checkIn.getStatus());
        } else {
            //准备流程变量
            Map<String, Object> setvariables = setvariables(checkIn.getId());
            //启动流程实例
            actFlowCommService.start(checkIn.getId(), "checkIn", user, setvariables, true);
        }

        //保存审核记录
        Long nextAssignee = actFlowCommService.getNextAssignee("checkIn", "checkIn:" + checkIn.getId());
        RecoreVo recoreVo = getRecoreVo(
                checkIn,
                user,
                AccraditationRecordConstant.AUDIT_STATUS_PASS,
                "同意", "发起申请-申请入住",
                "护理组组长处理-入住评估",
                nextAssignee, AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
    }

    /**
     * 获取操作记录数据
     *
     * @param checkIn      入住对象
     * @param user         当前登录用户
     * @param status       审核状态
     * @param option       操作意见
     * @param step         当前步骤
     * @param nextStep     下一步说明
     * @param nextAssignee 下一个审核人
     * @return
     */
    private RecoreVo getRecoreVo(CheckIn checkIn, User user, Integer status, String option, String step, String nextStep, Long nextAssignee, Integer handleType) {
        RecoreVo recoreVo = new RecoreVo();
        recoreVo.setId(checkIn.getId());
        recoreVo.setType(AccraditationRecordConstant.RECORD_TYPE_CHECK_IN);
        recoreVo.setFlowStatus(checkIn.getFlowStatus());
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

    /**
     * 查询入住信息
     *
     * @param *    @param status
     * @param code
     * @return
     */
    @Override
    public ResponseResult<TasVo> getCheckIn(String code, String assigneeId, Integer flowStatus, String taskId) {

        //查询入住信息
        if (ObjectUtil.isNull(code)) {
            throw new BaseException("入住编码为空");
        }
        CheckIn checkIn = checkInMapper.selectByCheckInCode(code);

        //方便前端展示，封装数据
        CheckInVo checkInVo = BeanUtil.toBean(checkIn, CheckInVo.class);
        JSONObject jsonObject = JSON.parseObject(checkInVo.getOtherApplyInfo(), JSONObject.class);
        if (ObjectUtil.isNotEmpty(jsonObject)) {
            String s = jsonObject.getString("checkInOtherDto");
            CheckInOtherDto checkInOtherDto = JSON.parseObject(s, CheckInOtherDto.class);
            BeanUtil.copyProperties(checkInOtherDto, checkInVo);
            jsonObject.remove("checkInOtherDto");
            checkInVo.setOtherApplyInfo(JSONUtil.toJsonStr(jsonObject));
            String elderDto = jsonObject.getString("elderDto");
            ElderDto elderDto1 = JSON.parseObject(elderDto, ElderDto.class);
            checkInVo.setElderDto(elderDto1);
        }
        //查询入住评估内容
        JSONObject review = JSON.parseObject(checkInVo.getReviewInfo(), JSONObject.class);
        if (ObjectUtil.isNotEmpty(review)) {
            String r1 = review.getString("r1");
            String r2 = review.getString("r2");
            checkInVo.setReviewInfo1(r1);
            checkInVo.setReviewInfo2(r2);
            review.remove("r1");
            review.remove("r2");
            checkInVo.setReviewInfo(JSONUtil.toJsonStr(review));
        } else {
            checkInVo.setReviewInfo(null);
        }

        //查询入住配置
        CheckInConfig currentConfigByElderId = checkInConfigService.findCurrentConfigByElderId(checkIn.getElderId());
        CheckInConfigVo checkInConfigVo = BeanUtil.toBean(currentConfigByElderId, CheckInConfigVo.class);
        if (ObjectUtil.isNotEmpty(checkInConfigVo)) {
            NursingLevelVo byId = nursingLevelService.getById(checkInConfigVo.getNursingLevelId());
            checkInVo.setNursingLevelVo(byId);
            if (ObjectUtil.isNotEmpty(checkInConfigVo.getRemark())) {
                checkInConfigVo.setFloorId(Long.parseLong(checkInConfigVo.getRemark().split(":")[0]));
                checkInConfigVo.setRoomId(Long.parseLong(checkInConfigVo.getRemark().split(":")[1]));
                checkInConfigVo.setBedId(Long.parseLong(checkInConfigVo.getRemark().split(":")[2]));
                checkInConfigVo.setFloorName(checkInConfigVo.getRemark().split(":")[3]);
                checkInConfigVo.setCode(checkInConfigVo.getRemark().split(":")[4]);
            }
        }
        checkInVo.setCheckInConfigVo(checkInConfigVo);

        //合同信息
        Contract contract = contractMapper.selectByElderId(checkIn.getElderId());
        ContractVo contractVo = BeanUtil.toBean(contract, ContractVo.class);
        checkInVo.setContractVo(contractVo);

        if (flowStatus < 0) {
            flowStatus = checkIn.getFlowStatus();
        }

        Integer isShow = 1;

        if (ObjectUtil.isNotEmpty(taskId)) {
            //查询当前流程在第几步
            isShow = actFlowCommService.isCurrentUserAndStep(taskId, flowStatus, checkIn);
        }

        TasVo tasVo = new TasVo();

        //只有在院长审批之后，养老顾问设置入住配置之前才会有撤回按钮
        tasVo.setIsRevocation(isShow == 2 && flowStatus.equals(checkIn.getFlowStatus() - 1) && checkIn.getStatus().equals(1));

        //如果当前是院长审批，如果是养老顾问设置入住配置，显示表单数据
        if (isShow.equals(2) || isShow.equals(3)) {
            isShow = 1;
        }

        //如果flowStatus是第3步：养老顾问-入住配置


        // 默认显示入住申请基本信息数据
        checkInVo.setIsShow(isShow);
        tasVo.setCheckIn(checkInVo);
        tasVo.setIsShow(isShow);
        //流程类型：入住
        tasVo.setType(3);

        //审批记录数据
        List<AccraditationRecord> accraditationRecordList = accraditationRecordMapper.getAccraditationRecordByBuisId(checkIn.getId(), PendingTasksConstant.TASK_TYPE_CHECK_IN);
        tasVo.setAccraditationRecords(accraditationRecordList);

        //查询下一个审核人
        AccraditationRecord accraditationRecord = accraditationRecordMapper.getLastByBuisId(checkIn.getId(), PendingTasksConstant.TASK_TYPE_CHECK_IN);
        if (ObjectUtil.isNotEmpty(accraditationRecord)) {
            tasVo.setNextApprover(accraditationRecord.getNextApproverRole());
        }
        return ResponseResult.success(tasVo);
    }

    /**
     * 同意入住申请
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public ResponseResult<CheckInVo> submitCheckIn(Long id, String info, String taskId) {
        // 流程状态 操作记录
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(id);
        //设置流程状态
        checkIn.setFlowStatus(CheckIn.FlowStatus.CONFIG.getCode());
        checkInMapper.updateByPrimaryKeySelective(checkIn);
        //  操作记录
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);
        //完成任务
        actFlowCommService.completeProcess("", taskId, user.getId().toString(), 1, null);

        // 养老顾问
        CheckIn checkIn1 = checkInMapper.selectByPrimaryKey(checkIn.getId());
        Long userId = checkIn1.getApplicatId();
        RecoreVo recoreVo = getRecoreVo(checkIn,
                user,
                AccraditationRecordConstant.AUDIT_STATUS_PASS,
                info,
                "院长处理-入住审批",
                "养老顾问处理-入住配置",
                userId,
                AccraditationRecordConstant.RECORD_HANDLE_TYPE_AUDIT);
        accraditationRecordService.insert(recoreVo);
        return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
    }

    /**
     * 审核拒绝
     *
     * @param id     入住单code
     * @param reject 拒绝原因
     * @return
     */
    @Transactional
    @Override
    public ResponseResult<CheckInVo> auditReject(Long id, String reject, String taskId) {
        // 流程状态 操作记录
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(id);
        //设置流程状态
        checkIn.setStatus(CheckIn.Status.CLOSED.getCode());
        checkInMapper.updateByPrimaryKeySelective(checkIn);
        close(checkIn);
        //  操作记录
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);
        actFlowCommService.completeProcess("", taskId, user.getId().toString(), 2, CheckIn.Status.CLOSED.getCode());
        //记录操作记录
        RecoreVo recoreVo = getRecoreVo(checkIn,
                user,
                AccraditationRecordConstant.AUDIT_STATUS_REJECT,
                reject,
                "院长处理-入住审批",
                "",
                null,
                AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
    }

    private void close(CheckIn checkIn) {
        //删除老人数据
        elderService.deleteByPrimaryKey(checkIn.getElderId());

        CheckInConfig checkInConfig = checkInConfigService.findCurrentConfigByElderId(checkIn.getElderId());
        if (ObjectUtil.isNotEmpty(checkInConfig)) {
            // 床位状态
            Bed bedByNum = bedMapper.getBedByNum(checkInConfig.getBedNo());
            bedByNum.setBedStatus(0);
            bedMapper.updateBed(bedByNum);
        }
    }

    /**
     * 撤回
     *
     * @param id
     * @param flowStatus
     * @return
     */
    @Transactional
    @Override
    public ResponseResult<CheckInVo> revocation(Long id, Integer flowStatus, String taskId) {
        // 流程状态 操作记录
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(id);
        //设置流程状态
        checkIn.setFlowStatus(checkIn.getFlowStatus() - 1);

        if (checkIn.getFlowStatus() <= (CheckIn.FlowStatus.CONFIG.getCode())) {
            // 删除床位配置
            CheckInConfig checkInConfig = checkInConfigService.findCurrentConfigByElderId(checkIn.getElderId());
            if (ObjectUtil.isNotEmpty(checkInConfig)) {
                // 床位状态
                Bed bedByNum = bedMapper.getBedByNum(checkInConfig.getBedNo());
                bedByNum.setBedStatus(0);
                bedMapper.updateBed(bedByNum);
                //bedElderMapper.deleteByElderId(checkIn.getElderId());
                //清除老人床位编号和id
                elderService.clearBedNum(checkIn.getElderId());
            }
        }

        if (checkIn.getFlowStatus().equals(CheckIn.FlowStatus.APPLY.getCode())) {
            // 删除数据
            checkIn.setReviewInfo("");
        }
        checkInMapper.updateByPrimaryKeySelective(checkIn);

        //  操作记录
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);
        //记录操作记录
        String name = EnumUtil.getBy(CheckIn.FlowStatus::getCode, checkIn.getFlowStatus()).getName();

        RecoreVo recoreVo = getRecoreVo(checkIn,
                user,
                AccraditationRecordConstant.AUDIT_STATUS_WITHDRAWS,
                "撤回",
                "撤回申请-" + name, "",
                user.getId(),
                AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        actFlowCommService.withdrawTask(taskId, false);
        return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
    }

    /**
     * 驳回
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public ResponseResult<CheckInVo> disapprove(Long id, String message, String taskId) {
        // 流程状态 操作记录
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(id);
        //设置流程状态
        checkIn.setFlowStatus(CheckIn.FlowStatus.APPLY.getCode());
        checkIn.setReviewInfo(null);
        //
        checkInMapper.updateByPrimaryKeySelective(checkIn);
        //  操作记录
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //完成任务
        actFlowCommService.completeProcess("", taskId, user.getId().toString(), 3, null);

        //保存审核记录
        RecoreVo recoreVo = getRecoreVo(checkIn,
                user,
                AccraditationRecordConstant.AUDIT_STATUS_DISAPPROVE,
                message, "驳回申请-入住审批", "养老顾问处理-入住申请",
                checkIn.getApplicatId(),
                AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
    }

    @Override
    public ResponseResult<PageResponse<CheckInVo>> selectByPage(String checkInCode, String name, String idCardNo, LocalDateTime start, LocalDateTime end, Integer pageNum, Integer pageSize, String deptNo, Long userId) {
        PageHelper.startPage(pageNum, pageSize);
        Page<List<CheckIn>> lists = checkInMapper.selectByPage(checkInCode, name, idCardNo, start, end, userId, deptNo);
        return ResponseResult.success(PageResponse.of(lists, CheckInVo.class));
    }

    /**
     * 撤销
     *
     * @param id 入住编码
     * @return
     */
    @Transactional
    @Override
    public ResponseResult<CheckInVo> cancel(Long id, String taskId) {
        // 流程状态 操作记录
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(id);
        //设置流程状态
        checkIn.setFlowStatus(checkIn.getFlowStatus() - 1);
        checkIn.setStatus(CheckIn.Status.CLOSED.getCode());
        checkInMapper.updateByPrimaryKeySelective(checkIn);

        //删除老人数据
        close(checkIn);
        //  操作记录
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //记录操作记录
        RecoreVo recoreVo = getRecoreVo(checkIn, user,
                AccraditationRecordConstant.AUDIT_STATUS_CANCEL, "撤销",
                "撤销申请-入住申请", "",
                null, AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        actFlowCommService.closeProcess(taskId, CheckIn.Status.CLOSED.getCode());
        return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
    }

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public ResponseResult<CheckInVo> review(CheckInDto checkInDto) {
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(checkInDto.getId());
        JSONObject jsonObject = JSON.parseObject(checkInDto.getReviewInfo(), JSONObject.class);
        jsonObject.put("r1", checkInDto.getReviewInfo1());
        jsonObject.put("r2", checkInDto.getReviewInfo2());
        checkIn.setReviewInfo(JSONUtil.toJsonStr(jsonObject));
        //设置流程状态
        checkIn.setFlowStatus(CheckIn.FlowStatus.APPROVAL.getCode());
        checkInMapper.updateByPrimaryKeySelective(checkIn);

        if (ObjectUtil.isNotEmpty(checkInDto.getSave()) && checkInDto.getSave().equals(1)) {
            return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
        }

        //  操作记录
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //完成流程
        actFlowCommService.completeProcess("", checkInDto.getTaskId(), user.getId().toString(), 1, null);

        //记录操作记录
        Long nextAssignee = actFlowCommService.getNextAssignee("checkIn", "checkIn:" + checkIn.getId());

        RecoreVo recoreVo = getRecoreVo(checkIn, user, AccraditationRecordConstant.AUDIT_STATUS_PASS,
                "同意", "护理部组长处理-入住评估",
                "院长处理-入住审批", nextAssignee
                , AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        return ResponseResult.success(BeanUtil.toBean(checkIn, CheckInVo.class));
    }

    @Override
    public Map<String, Object> setvariables(Long id) {
        //设置流程变量
        Map<String, Object> variables = new HashMap<>();

        // 养老顾问
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(id);
        Long applicatId = checkIn.getApplicatId();
        variables.put("assignee0", applicatId);

        variables.put("assignee0Name", checkIn.getApplicat());
        variables.put("processTitle", checkIn.getTitle());

        // 护理部部门编号
        Dept dept = deptMapper.selectByDeptNo(RetreatConstant.NURSING_DEPT_CODE);
        //部门领导id
        Long leaderId = dept.getLeaderId();
        variables.put("assignee1", leaderId);

        // 副院长
        List<Long> userIdList = userMapper.selectByDeptNo(RetreatConstant.DEAN_OFFICE_DEPT_CODE);
        Long userId = userIdList.get(0);
        variables.put("assignee2", userId);

        // 养老顾问
        variables.put("assignee3", applicatId);

        // 法务员工
        //设置下一个审核人
        //找到法务部门编号--->通过编号查询法务中所有的员工（非leader）---->获取第一个人，找到这个人的id,然后查询对应的角色
        List<Long> legatUser = userMapper.selectByDeptNo(RetreatConstant.LEGAL_DEPT_CODE);
        Long legatUserId = legatUser.get(legatUser.size() - 1);
        variables.put("assignee4", legatUserId);

        // 流程类型入住
        variables.put("processType", 3);

        // 流程类型入住
        variables.put("processCode", checkIn.getCheckInCode());

        // 流程类型入住
        variables.put("processStatus", 1);
        return variables;
    }
}
