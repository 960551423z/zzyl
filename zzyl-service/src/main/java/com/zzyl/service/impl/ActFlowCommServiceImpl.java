package com.zzyl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.constant.PendingTasksConstant;
import com.zzyl.dto.PendingTasksDto;
import com.zzyl.entity.*;
import com.zzyl.mapper.AccraditationRecordMapper;
import com.zzyl.mapper.HiActinstMapper;
import com.zzyl.service.ActFlowCommService;
import com.zzyl.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ActFlowCommServiceImpl implements ActFlowCommService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HiActinstMapper hiActinstMapper;

    @Autowired
    private AccraditationRecordMapper accraditationRecordMapper;


    @Override
    public Long getNextAssignee(String formKey, String bussinessKey) {
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(formKey) //流程Key
                .processInstanceBusinessKey(bussinessKey)
                .singleResult();
        if (task != null) {
            return Long.valueOf(task.getAssignee());
        }
        return null;
    }

    /**
     * 部署流程定义
     */
    @Override
    public void saveNewDeploy(FlowInfo flowInfo) {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(flowInfo.getFilepath())
                .name(flowInfo.getFlowname())
                .key(flowInfo.getFlowkey())
                .deploy();
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Override
    public ProcessInstance startProcess(String formKey, Map<String, Object> variables, String bussinessKey, Long id) {
        variables.put("bussinessKey", bussinessKey);
        //启动流程
        log.info("【启动流程】，formKey ：{},bussinessKey:{}", formKey, bussinessKey);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(formKey, bussinessKey, variables);
        //流程实例ID
        String processDefinitionId = processInstance.getProcessDefinitionId();
        log.info("【启动流程】- 成功，processDefinitionId：{}", processDefinitionId);
        return processInstance;
    }

    /**
     * 查看个人任务列表
     */
    @Override
    public List<Map<String, Object>> myTaskList(String userid) {

        /**
         * 根据负责人id  查询任务
         */
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(userid);

        List<Task> list = taskQuery.orderByTaskCreateTime().desc().list();

        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        for (Task task : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", task.getId());
            map.put("taskname", task.getName());
            map.put("description", task.getDescription());
            map.put("priority", task.getPriority());
            map.put("owner", task.getOwner());
            map.put("assignee", task.getAssignee());
            map.put("delegationState", task.getDelegationState());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("executionId", task.getExecutionId());
            map.put("processDefinitionId", task.getProcessDefinitionId());
            map.put("createTime", task.getCreateTime());
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            map.put("dueDate", task.getDueDate());
            map.put("category", task.getCategory());
            map.put("parentTaskId", task.getParentTaskId());
            map.put("tenantId", task.getTenantId());
            listmap.add(map);
        }

        return listmap;
    }

    /**
     * 查看个人任务信息
     * @param pendingTasksDto
     */
    @Override
    public PageResponse<PendingTasks> myTaskInfoList(PendingTasksDto pendingTasksDto) {

        /**
         * 根据负责人id  查询任务
         */
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery();

        if (ObjectUtil.isNotEmpty(pendingTasksDto.getIsHandle())) {
            //是否是待处理
            if (pendingTasksDto.getIsHandle() == 1) {
                taskQuery.finished();
            } else {
                taskQuery.unfinished();
            }
        }

        //判断是我的申请还是我的待办
        if (ObjectUtil.isNotEmpty(pendingTasksDto.getApplicatId())) {
            taskQuery.taskAssignee(pendingTasksDto.getApplicatId().toString());
            taskQuery.processVariableValueEquals("assignee0", pendingTasksDto.getApplicatId());
            taskQuery.taskNameLike("%申请");
        } else {
            taskQuery.taskAssignee(pendingTasksDto.getAssigneeId().toString());
            taskQuery.taskNameLike("%处理");
        }
        //时间范围
        if (ObjectUtil.isNotEmpty(pendingTasksDto.getStartTime()) && ObjectUtil.isNotEmpty(pendingTasksDto.getEndTime())) {
            taskQuery.taskCreatedAfter(pendingTasksDto.getStartTime()).taskCreatedBefore(pendingTasksDto.getEndTime());
        }
        //单据类别（1：退住，2：请假，3：入住）
        if (ObjectUtil.isNotEmpty(pendingTasksDto.getType())) {
            taskQuery.processVariableValueEquals("processType", pendingTasksDto.getType());
        }
        //请求的任务ID
        if (ObjectUtil.isNotEmpty(pendingTasksDto.getCode())) {
            taskQuery.processVariableValueEquals("processCode", pendingTasksDto.getCode());
        }

        //任务状态（1：申请中，2:已完成，3:已关闭）
        if (ObjectUtil.isNotEmpty(pendingTasksDto.getStatus())) {
            taskQuery.processVariableValueEquals("processStatus", pendingTasksDto.getStatus());
        }
        //模糊查询当前申请人的单据
        if (ObjectUtil.isNotEmpty(pendingTasksDto.getApplicat())) {
            taskQuery.processVariableValueLike("assignee0Name", "%" + pendingTasksDto.getApplicat() + "%");
        }

        //统计条数
        long count = taskQuery.count();
        //执行查询
        List<HistoricTaskInstance> list = taskQuery.includeProcessVariables().orderByHistoricTaskInstanceStartTime().desc().listPage((pendingTasksDto.getPageNum() - 1) * pendingTasksDto.getPageSize(), pendingTasksDto.getPageSize());


        //log.info(" 任务查询 {} " , taskQuery);
        //封装数据
        List<PendingTasks> pendingTasks = new ArrayList<>();
        for (HistoricTaskInstance task : list) {

            Map<String, Object> variableInstances = task.getProcessVariables();
            PendingTasks pendingTask = new PendingTasks();
            pendingTask.setId(task.getId());
            pendingTask.setCode(variableInstances.get("processCode").toString());

            pendingTask.setType(Integer.parseInt(variableInstances.get("processType").toString()));

            pendingTask.setTitle(variableInstances.get("processTitle").toString());
            pendingTask.setApplicat(variableInstances.get("assignee0Name").toString());
            pendingTask.setStatus(Integer.parseInt(variableInstances.get("processStatus").toString()));
            pendingTask.setAssigneeId(Long.valueOf(task.getAssignee()));

            Long bussinessId = Long.parseLong(variableInstances.get("bussinessKey").toString().split(":")[1]);
            pendingTask.setApplicationTime(getStartTime(bussinessId, pendingTask.getType()));
            if (!pendingTask.getStatus().equals(PendingTasksConstant.TASK_STATUS_APPLICATION)) {
                pendingTask.setFinishTime(getFinishTime(bussinessId, pendingTask.getType()));
            }
            pendingTask.setCheckInId(bussinessId);
            pendingTasks.add(pendingTask);
        }
        //按照申请时间倒序查询
        List<PendingTasks> tasks = pendingTasks.stream()
                .sorted(Comparator.comparing(PendingTasks::getApplicationTime).reversed())
                .collect(Collectors.toList());
        //数据返回，逻辑分页
        return PageResponse.of(tasks, pendingTasksDto.getPageNum(), pendingTasksDto.getPageSize(), (count + pendingTasksDto.getPageSize() - 1) / pendingTasksDto.getPageSize(), count);
    }

    private LocalDateTime getStartTime(Long id, Integer type) {
        return accraditationRecordMapper.getFirstByBuisId(id, type).getCreateTime();
    }

    private LocalDateTime getFinishTime(Long id, Integer type) {
        return accraditationRecordMapper.getLastByBuisId(id, type).getCreateTime();
    }


    /**
     * 完成提交任务
     */
    @Override
    public void completeProcess(String title, String taskId, String userId, Integer code, Integer status) {
        //任务Id 查询任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            log.error("completeProcess - task is null!!");
            return;
        }


        //任务对象  获取流程实例Id
        String processInstanceId = task.getProcessInstanceId();

        Authentication.setAuthenticatedUserId(userId);

        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId);
        List<HistoricTaskInstance> list = historicTaskInstanceQuery.list();
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(v -> {
                if (v.getFormKey().equals((Integer.parseInt(task.getFormKey()) + 1) + "")) {
                    historyService.deleteHistoricTaskInstance(v.getId());
                }
                if (code.equals(3) && (v.getFormKey().equals("0"))) {
                    historyService.deleteHistoricTaskInstance(v.getId());
                }
            });
        }
        //完成办理
        Map<String, Object> variables = new HashMap<>();
        if (ObjectUtil.isNotEmpty(status)) {
            variables.put("processStatus", status);
        }
        if (ObjectUtil.isNotEmpty(title)) {
            variables.put("processTitle", title);
        }
        variables.put("ops", code);
        taskService.complete(taskId, variables);
    }

    @Override
    public void start(Long id, String formKey, User user, Map<String, Object> variables, boolean autoFinished) {
        //使用流程变量设置字符串（格式 ： Evection:Id 的形式）
        //使用正在执行对象表中的一个字段BUSINESS_KEY(Activiti提供的一个字段)，让启动的流程（流程实例）关联业务
        String bussinessKey = formKey + ":" + id;
        ProcessInstance processInstance = startProcess(formKey, variables, bussinessKey, id);
        //	流程实例ID
        String processDefinitionId = processInstance.getProcessDefinitionId();
        log.info("processDefinitionId is {}", processDefinitionId);
        List<Map<String, Object>> taskList = myTaskList(user.getId().toString());
        if (!CollectionUtils.isEmpty(taskList)) {
            for (Map<String, Object> map : taskList) {
                if (map.get("assignee").toString().equals(user.getId().toString()) &&
                        map.get("processDefinitionId").toString().equals(processDefinitionId)) {
                    log.info("processDefinitionId is {}", map.get("processDefinitionId").toString());
                    log.info("taskid is {}", map.get("taskid").toString());
                    if (autoFinished) {
                        completeProcess("", map.get("taskid").toString(), user.getId().toString(), 1, PendingTasksConstant.TASK_STATUS_APPLICATION);
                    }
                }

            }
        }
    }

    /**
     * 撤销思路
     *  - 设置流程变量为已结束
     *  - 删除流程实例
     *
     * @param taskId 任务id
     * @param status 状态 1申请中 2已完成 3已关闭
     */
    @Override
    public void closeProcess(String taskId, Integer status) {

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        // 从任务中拿到流程实例id
        String processInstanceId = historicTaskInstance.getProcessInstanceId();

        //设置流程变量
        String executionId = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0).getExecutionId();
        // 设置参数
        Map<String, Object> variable = new HashMap<>();
        // 设置为已关闭
        variable.put("processStatus", status);
        //记录流程变量
        runtimeService.setVariables(executionId, variable);
        //删除流程实例
        runtimeService.deleteProcessInstance(processInstanceId,"申请人撤销");

    }

    /**
     * 是否查看当前审核用户的任务
     *
     * @param taskId
     * @param status
     * @param checkIn
     * @return
     */
    @Override
    public Integer isCurrentUserAndStep(String taskId, Integer status, CheckIn checkIn) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (checkIn.getFlowStatus().equals(status) && checkIn.getStatus().equals(CheckIn.Status.APPLICATION.getCode())) {
            if (historicTaskInstance.getFormKey().equals(checkIn.getFlowStatus().toString())) {
                return 1;
            }
            return 0;
        }
        if (historicTaskInstance.getFormKey().equals((checkIn.getFlowStatus() - 1) + "") && checkIn.getStatus().equals(CheckIn.Status.APPLICATION.getCode())) {
            return 2;
        }
        return 3;
    }


    /**
     * 是否查看当前审核用户的任务
     * @param taskId
     * @param status
     * @param retreat
     * @return
     */
    @Override
    public Integer isCurrentUserAndStep(String taskId, Integer status, Retreat retreat) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (retreat.getFlowStatus().equals(status) && retreat.getStatus().equals(CheckIn.Status.APPLICATION.getCode())) {
            if (historicTaskInstance.getFormKey().equals(retreat.getFlowStatus().toString())) {
                return 1;
            }
            return 0;
        }
        if (historicTaskInstance.getFormKey().equals((retreat.getFlowStatus() - 1) + "") && retreat.getStatus().equals(CheckIn.Status.APPLICATION.getCode())) {
            return 2;
        }
        return 3;
    }


    /**
     * 驳回任务
     * @param taskId
     * @param first  是否默认退回流程第一个节点，true 是,false默认是上一个节点
     */
    @Override
    public void rollBackTask(String taskId, boolean first) {
        anyJump(taskId, first);
    }

    /**
     * 撤回任务
     *
     * @param taskId
     * @param first  是否默认退回流程第一个节点，true 是,false默认是上一个节点，
     */
    @Override
    public void withdrawTask(String taskId, boolean first) {
        anyJump(taskId, first);
    }

    /**
     * 跳转任务
     *
     * @param taskId
     * @param first  是否默认跳转流程第一个节点，true 是,false默认是上一个节点，
     */
    @Override
    public void jumpTask(String taskId, boolean first) {
        anyJump(taskId, first);
    }

    /**
     * 跳转任意节点
     *
     * @param taskId 当前操作节点
     * @param first  是否默认第一 是否驳回
     */
    @Override
    public void anyJump(String taskId, boolean first) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        //实例定义id：checkIn:1:0f97a26d-5697-11ee-bf3f-5405db5be13e
        String processDefinitionId = historicTaskInstance.getProcessDefinitionId();
        //实例id：16ea626d-5755-11ee-849a-5405db5be13e
        String processInstanceId = historicTaskInstance.getProcessInstanceId();
        // 对上一个节点和发起节点的支持:Activity_0pnd103
        String activityId = null;
        //找到需要驳回的节点中，比如：现在是：养老顾问-入住配置，那么要找的就是上一个节点：副院长-审批
        HistoricActivityInstance targetActivity = getRejectTargetActivity(null, processInstanceId, first);
        if (targetActivity != null) {
            activityId = targetActivity.getActivityId();
        }
        if (StrUtil.isEmpty(activityId)) {
            return;
        }
        try {
            //获取流程中的bpmn文件
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            //流程实例
            Process process = bpmnModel.getMainProcess();
            // 解析调整的目标节点
            //找到目标节点  -->  副院长-审批
            FlowNode targetFlowNode = (FlowNode) process.getFlowElement(activityId);
            //找到当前节点的所有连线
            List<SequenceFlow> incomingFlows = targetFlowNode.getIncomingFlows();

            List<SequenceFlow> targetSequenceFlow = new ArrayList<>();
            //遍历所有连线
            for (SequenceFlow incomingFlow : incomingFlows) {
                //连线的入节点
                FlowNode source = (FlowNode) incomingFlow.getSourceFlowElement();
                List<SequenceFlow> sequenceFlows;
                if (source instanceof ParallelGateway) {// 如果是并行网关同级节点，则跳转到所有节点
                    sequenceFlows = source.getOutgoingFlows();
                } else {
                    sequenceFlows = source.getOutgoingFlows();// 否则直接跳转到对应节点，包括为执行过的节点
                }
                targetSequenceFlow.addAll(sequenceFlows);
            }
            //获取当前任务中的所有待执行的任务
            List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            for (Task t : list) {
                //把一个任务动态转向目标节点
                //参数1：目标节点   参数2：当前任务   参数3：当前任务id    参数4:目标节点所有连线   参数5：默认flase，找上一个节点
                trunToTarget(process, t, first ? taskId : list.get(0).getId(), targetSequenceFlow, first);
            }
            if (!first) { // 撤回 删除最后的节点
                historyService.deleteHistoricTaskInstance(taskId);
                hiActinstMapper.deleteHiActivityInstByTaskId(taskId);
            } else {
                // 撤回 删除第一个
                List<HistoricTaskInstance> list1 = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished().orderByTaskCreateTime().asc().list();
                if (CollUtil.isNotEmpty(list1)) {
                    HistoricTaskInstance firstTask = list1.get(0);
                    historyService.deleteHistoricTaskInstance(firstTask.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把一个任务动态转向目标节点
     * @param process   目标节点
     * @param task  当前任务
     * @param taskId             当前任务id
     * @param targetSequenceFlow  目标节点所有连线
     * @param first  默认flase，找上一个节点
     */
    private void trunToTarget(Process process, TaskInfo task, String
            taskId, List<SequenceFlow> targetSequenceFlow, boolean first) {

        //当前节点:入住选配-处理
        FlowNode curFlowNode = (FlowNode) process.getFlowElement(task.getTaskDefinitionKey());
        if (curFlowNode == null) {
            //遍历节点中的所有子模块
            for (FlowElement flowElement : process.getFlowElements()) {
                if (flowElement instanceof SubProcess) {
                    SubProcess subProcess = (SubProcess) flowElement;
                    FlowElement fe = subProcess.getFlowElement(task.getTaskDefinitionKey());
                    if (fe != null) {
                        curFlowNode = (FlowNode) fe;
                        break;
                    }
                }
            }
        }
        //备份原始连线
        List<SequenceFlow> tempOutgoingSequenceFlows = new ArrayList<>(curFlowNode.getOutgoingFlows());
        //最新任务id与要删除的id一致
        if (taskId.equals(task.getId())) {
            //当前节点设置流出的连线
            curFlowNode.setOutgoingFlows(targetSequenceFlow);
            //完成当前任务
            taskService.complete(task.getId());
            if (!first) {
                //删除任务实例
                historyService.deleteHistoricTaskInstance(task.getId());
                //删除历史任务
                hiActinstMapper.deleteHiActivityInstByTaskId(task.getId());
            }
        }
        //恢复之前的连线
        curFlowNode.setOutgoingFlows(tempOutgoingSequenceFlows);
    }

    /**
     * 获取历史撤回或回退目标节点,支持上一节点，第一个节点
     *
     * @param taskId            要回退的taskId
     * @param processInstanceId
     * @return
     */
    private HistoricActivityInstance getRejectTargetActivity(String taskId, String processInstanceId, boolean first) {

        HistoricActivityInstance targetActivity = null;
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityType("userTask");

        // 取得所有历史任务按时间降序排序
        List<HistoricActivityInstance> historicActivityInstances = null;
        if (first) {// 退到第一个节点
            historicActivityInstances = query.orderByHistoricActivityInstanceStartTime().asc().list();
            return historicActivityInstances.get(0);
        } else { // 找到最近一个节点
            historicActivityInstances = query.orderByHistoricActivityInstanceStartTime().desc().list();
        }

        if (CollectionUtils.isEmpty(historicActivityInstances) || historicActivityInstances.size() < 2) {
            return null;
        }
        if (!StringUtils.isBlank(taskId)) {
            return targetActivity;
        }
        // 不传活动id的情况直接找第一个任务
        // 最后一条是当前正在进行的任务 需要找到最近的但是名称和当前任务不一样的任务去撤回
        HistoricActivityInstance current = historicActivityInstances.get(0);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            if (!current.getActivityId().equals(historicActivityInstance.getActivityId())) {
                if (historicActivityInstance.getActivityType().equals("userTask")) {
                    targetActivity = historicActivityInstance;
                    break;
                }
            }
        }
        return targetActivity;
    }
}