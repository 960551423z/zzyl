package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.PendingTasksDto;
import com.zzyl.entity.*;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

/**
 * @author 阿庆
 */
public interface ActFlowCommService {


    /**
     * 获取当前办理人id
     * @param formKey
     * @param bussinessKey
     * @return
     */
    public Long getNextAssignee(String formKey, String bussinessKey);


    /**
     * 部署流程定义
     */
    public void saveNewDeploy(FlowInfo flowInfo) ;

    /**
     * 启动流程实例
     */
    public ProcessInstance startProcess(String formKey, Map<String, Object> variables, String bussinessKey, Long id) ;

    /**
     * 查看个人任务列表
     */
    public List<Map<String, Object>> myTaskList(String userid);

    /**
     * 查看个人任务信息
     * @param pendingTasksDto
     */
    public PageResponse<PendingTasks> myTaskInfoList(PendingTasksDto pendingTasksDto) ;


    /**
     * 完成提交任务
     */
    public void completeProcess(String title, String taskId, String userId, Integer code, Integer status) ;

    public void start(Long id, String formKey, User user, Map<String, Object> variables, boolean autoFinished) ;

    /**
     * 关闭 思路：改变流程当前节点的下一个节点为空 并完成这个节点的任务，并删除痕迹
     * @param taskId 任务id
     * @param status 状态 1申请中 2已完成 3已关闭
     */
    public void closeProcess(String taskId, Integer status) ;

    /**
     * 是否查看当前审核用户的任务
     * @param taskId
     * @param status
     * @param checkIn
     * @return
     */
    public Integer isCurrentUserAndStep(String taskId, Integer status, CheckIn checkIn) ;


    /**
     * 是否查看当前审核用户的任务
     * @param taskId
     * @param status
     * @param retreat
     * @return
     */
    public Integer isCurrentUserAndStep(String taskId, Integer status, Retreat retreat) ;


    /**
     * 驳回任务
     * @param taskId
     * @param first 是否默认退回流程第一个节点，true 是,false默认是上一个节点，
     */
    public void rollBackTask(String taskId, boolean first);

    /**
     * 撤回任务
     * @param taskId
     * @param first 是否默认退回流程第一个节点，true 是,false默认是上一个节点，
     */
    public void withdrawTask(String taskId, boolean first);

    /**
     * 跳转任务
     * @param taskId
     * @param first 是否默认跳转流程第一个节点，true 是,false默认是上一个节点，
     */
    public void jumpTask(String taskId, boolean first);

    /**
     * 跳转任意节点
     *
     * @param taskId 当前操作节点
     * @param first 是否默认第一 是否驳回
     */
    public void anyJump(String taskId, boolean first) ;

}
