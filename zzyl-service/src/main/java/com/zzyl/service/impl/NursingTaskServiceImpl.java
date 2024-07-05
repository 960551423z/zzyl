package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zzyl.base.PageResponse;
import com.zzyl.entity.*;
import com.zzyl.mapper.BillMapper;
import com.zzyl.mapper.NursingElderMapper;
import com.zzyl.mapper.NursingTaskMapper;
import com.zzyl.mapper.UserMapper;
import com.zzyl.service.*;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.NursingLevelVo;
import com.zzyl.vo.NursingPlanVo;
import com.zzyl.vo.NursingProjectPlanVo;
import com.zzyl.vo.NursingTaskVo;
import com.zzyl.vo.retreat.ElderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NursingTaskServiceImpl implements NursingTaskService {

    @Resource
    private NursingTaskMapper nursingTaskMapper;

    @Resource
    private CheckInConfigService checkInConfigService;

    @Resource
    private NursingLevelService nursingLevelService;

    @Resource
    private NursingPlanService nursingPlanService;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private BillMapper billMapper;

    @Override
    public int insert(NursingTask record) {
        // 实现方法
        return nursingTaskMapper.insert(record);
    }

    @Override
    public NursingTaskVo selectByPrimaryKey(Long id) {
        // 实现方法
        NursingTask nursingTask = nursingTaskMapper.selectByPrimaryKey(id);
        CheckInConfig checkInConfig = checkInConfigService.findCurrentConfigByElderId(nursingTask.getElderId());
        NursingLevelVo byId = nursingLevelService.getById(checkInConfig.getNursingLevelId());
        nursingTask.setLName(byId.getName());

        List<NursingElder> nursingElders = nursingElderMapper.selectByElderId(Lists.newArrayList(nursingTask.getElderId()));
        List<Long> collect = nursingElders.stream().map(NursingElder::getNursingId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect)) {
            List<User> users = userMapper.selectUserByIds(collect);
            nursingTask.setNursingName(users.stream().map(User::getRealName).collect(Collectors.toList()));
        }
        return BeanUtil.toBean(nursingTask, NursingTaskVo.class);
    }


    @Override
    public int batchInsert(List<NursingTask> list) {
        // 实现方法
        return nursingTaskMapper.batchInsert(list);
    }

    /**
     * 取消任务
     * @param taskId 任务ID
     */
    @Override
    public void cancelTask(Long taskId, String reason) {
        // 实现方法
        NursingTask nursingTask = new NursingTask();
        nursingTask.setId(taskId);
        nursingTask.setStatus(3);
        nursingTask.setCancelReason(reason);
        nursingTaskMapper.updateByPrimaryKeySelective(nursingTask);
        NursingTask nursingTask1 = nursingTaskMapper.selectByPrimaryKey(taskId);
    }

    /**
     * 任务改期
     * @param taskId 任务ID
     * @param newTime 新的执行时间
     */
    @Override
    public void rescheduleTask(Long taskId, LocalDateTime newTime) {
        NursingTask nursingTask = new NursingTask();
        nursingTask.setId(taskId);
        nursingTask.setEstimatedServerTime(newTime);
        nursingTaskMapper.updateByPrimaryKeySelective(nursingTask);
    }

    /**
     * 任务执行
     * @param taskId 任务ID
     */
    @Override
    public void executeTask(Long taskId, LocalDateTime newTime, String image, String mark) {
        NursingTask nursingTask = new NursingTask();
        nursingTask.setId(taskId);
        nursingTask.setStatus(2);
        nursingTask.setEstimatedServerTime(newTime);
        nursingTask.setTaskImage(image);
        nursingTask.setMark(mark);
        nursingTaskMapper.updateByPrimaryKeySelective(nursingTask);
        NursingTask nursingTask1 = nursingTaskMapper.selectByPrimaryKey(taskId);
    }


    @Autowired
    private NursingElderMapper nursingElderMapper;

    @Autowired
    private UserService userService;

    /**
     * 分页查询任务
     * @param page 页码
     * @param size 每页大小
     * @return 任务列表
     */
    @Override
    public PageResponse<NursingTaskVo> getTasksByPage(int page, int size, String elderName, Long nurseId, Long projectId, LocalDateTime startTime, LocalDateTime endTime, Integer status) {
        // 实现方法
        PageHelper.startPage(page, size);
        Page<NursingTask> nursingTasks = nursingTaskMapper.selectByParams(elderName, nurseId, projectId, startTime, endTime, status);
        return PageResponse.of(nursingTasks, NursingTaskVo.class);
    }

    

    /**
     * 生成护理任务
     * @param elderVo
     * @param signDate
     * @param startTime
     */
    @Override
    public void createMonthTask(ElderVo elderVo, LocalDateTime signDate, LocalDateTime startTime) {
        //获取老人的入住配置
        CheckInConfig checkInConfig = checkInConfigService.findCurrentConfigByElderId(elderVo.getId());
        if (ObjectUtil.isEmpty(startTime)) {
            startTime = checkInConfig.getCostStartTime();
        }
        //护理等级
        NursingLevelVo nursingLevelVo = nursingLevelService.getById(checkInConfig.getNursingLevelId());
        //护理计划
        NursingPlanVo nursingPlanVo = nursingPlanService.getById(nursingLevelVo.getPlanId());

        String format = LocalDateTimeUtil.format(startTime, "yyyy-MM");
        //查询老人月度账单
        Bill bill = billMapper.selectByElderAndMonth(elderVo.getId(), format);

        //组装护理任务
        List<NursingTask> nursingTasks = new ArrayList<>();
        for (NursingProjectPlanVo v : nursingPlanVo.getProjectPlans()) {
            Integer executeFrequency = v.getExecuteFrequency();
            String executeTime = v.getExecuteTime();

            LocalDateTime firstExecutionTime = LocalDateTime.of(startTime.toLocalDate(), LocalTime.of(Integer.parseInt(executeTime.substring(0, 2)), Integer.parseInt(executeTime.substring(3))));
            // 计算相差天数
            LocalDateTime monthEndTime = LocalDateTime.of(startTime.getYear(), checkInConfig.getCostStartTime().getMonth(), startTime.toLocalDate().lengthOfMonth(), 23, 59);
            long diffDays = monthEndTime.toLocalDate().toEpochDay() - startTime.toLocalDate().toEpochDay() + 1;
            if (v.getExecuteCycle().equals(0)) {
                // 日
                createTaskByDay(firstExecutionTime, diffDays, nursingTasks, executeFrequency, bill, elderVo, signDate, v);
            } else if (v.getExecuteCycle().equals(1)) {
                // 周
                createTaskByWeek(firstExecutionTime, diffDays, nursingTasks, executeFrequency, bill, elderVo, signDate, v, monthEndTime);
            } else {
                // 月
                createTaskByMonth(firstExecutionTime, monthEndTime, nursingTasks, executeFrequency, bill, elderVo, signDate, v);
            }
        }

        if (CollUtil.isEmpty(nursingTasks)) {
            return;
        }
        nursingTaskMapper.batchInsert(nursingTasks);
    }

    private void createTaskByMonth(LocalDateTime firstExecutionTime, LocalDateTime monthEndTime, List<NursingTask> nursingTasks, Integer executeFrequency, Bill bill, ElderVo elderVo, LocalDateTime signDate, NursingProjectPlanVo v) {
        LocalDateTime executionTime = firstExecutionTime;
        Integer diffDay = (monthEndTime.plusSeconds(1).getDayOfMonth() - executionTime.getDayOfMonth()) / executeFrequency;
        for (int x = 0; x < executeFrequency; x++) {
            LocalDateTime seconds = executionTime.plusDays(diffDay * x);
            NursingTask nursingTask = new NursingTask();
            nursingTask.setStatus(1);
            nursingTask.setTaskType((byte) 2);
            nursingTask.setRelNo(bill.getBillNo());
            nursingTask.setEstimatedServerTime(seconds);
            nursingTask.setCreateTime(signDate);
            nursingTask.setProjectId(v.getProjectId());
            nursingTask.setElderId(elderVo.getId());
            nursingTasks.add(nursingTask);
        }
    }

    private void createTaskByWeek(LocalDateTime firstExecutionTime, long diffDays, List<NursingTask> nursingTasks, Integer executeFrequency, Bill bill, ElderVo elderVo, LocalDateTime signDate, NursingProjectPlanVo v, LocalDateTime monthEndTime) {
        int i;
        for (i = 0; i < diffDays-7; i = i + 7) {
            LocalDateTime dayEndTime = LocalDateTime.of(firstExecutionTime.plusDays(i + 7).toLocalDate(), LocalTime.of(23, 59));
            LocalDateTime executionTime = firstExecutionTime.plusDays(i);
            Integer diffDay = (dayEndTime.plusSeconds(1).getDayOfYear() - executionTime.getDayOfYear()) / executeFrequency;
            for (int x = 0; x < executeFrequency; x++) {
                LocalDateTime seconds = executionTime.plusDays(diffDay * x);
                NursingTask nursingTask = new NursingTask();
                nursingTask.setStatus(1);
                nursingTask.setTaskType((byte) 2);
                nursingTask.setRelNo(bill.getBillNo());
                nursingTask.setEstimatedServerTime(seconds);
                nursingTask.setCreateTime(signDate);
                nursingTask.setProjectId(v.getProjectId());
                nursingTask.setElderId(elderVo.getId());
                nursingTasks.add(nursingTask);
            }
        }

        if (i > diffDays-7 && i < diffDays) {
            LocalDateTime dayEndTime = LocalDateTime.of(firstExecutionTime.plusDays(i + 7).toLocalDate(), LocalTime.of(23, 59));
            LocalDateTime executionTime = firstExecutionTime.plusDays(i);
            Integer diffDay = (dayEndTime.plusSeconds(1).getDayOfYear() - executionTime.getDayOfYear()) / executeFrequency;
            for (int x = 0; x < executeFrequency; x++) {
                LocalDateTime seconds = executionTime.plusDays(diffDay * x);
                if (seconds.isAfter(monthEndTime)) {
                    break;
                }
                NursingTask nursingTask = new NursingTask();
                nursingTask.setStatus(1);
                nursingTask.setTaskType((byte) 2);
                nursingTask.setRelNo(bill.getBillNo());
                nursingTask.setEstimatedServerTime(seconds);
                nursingTask.setCreateTime(signDate);
                nursingTask.setProjectId(v.getProjectId());
                nursingTask.setElderId(elderVo.getId());
                nursingTasks.add(nursingTask);
            }
        }

    }

    private void createTaskByDay(LocalDateTime firstExecutionTime, long diffDays, List<NursingTask> nursingTasks, Integer executeFrequency, Bill bill, ElderVo elderVo, LocalDateTime signDate, NursingProjectPlanVo v) {
        for (int i = 0; i < diffDays; i++) {
            LocalDateTime executionTime = firstExecutionTime.plusDays(i);
            LocalDateTime dayEndTime = LocalDateTime.of(executionTime.toLocalDate(), LocalTime.of(23, 59));
            Integer diffHour = (dayEndTime.plusSeconds(1).getHour() - executionTime.getHour()) / executeFrequency;
            for (int x = 0; x < executeFrequency; x++) {
                LocalDateTime seconds = executionTime.plusHours(diffHour * x);
                NursingTask nursingTask = new NursingTask();
                nursingTask.setStatus(1);
                nursingTask.setTaskType((byte) 2);
                nursingTask.setRelNo(bill.getBillNo());
                nursingTask.setEstimatedServerTime(seconds);
                nursingTask.setCreateTime(signDate);
                nursingTask.setProjectId(v.getProjectId());
                nursingTask.setElderId(elderVo.getId());
                nursingTasks.add(nursingTask);
            }
        }
    }
}
