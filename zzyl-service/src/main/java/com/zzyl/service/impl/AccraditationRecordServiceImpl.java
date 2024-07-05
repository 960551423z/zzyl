package com.zzyl.service.impl;

import com.zzyl.entity.AccraditationRecord;
import com.zzyl.mapper.AccraditationRecordMapper;
import com.zzyl.mapper.UserRoleMapper;
import com.zzyl.service.AccraditationRecordService;
import com.zzyl.utils.ClientIpUtil;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.RecoreVo;
import com.zzyl.vo.UserRoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 阿庆
 */

@Service
@Transactional
@Slf4j
public class AccraditationRecordServiceImpl implements AccraditationRecordService {

    private static final String RECORD_STEP_NO_PRE = "RECORD:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private AccraditationRecordMapper accraditationRecordMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private HttpServletRequest request;

    /**
     * 保存操作记录
     *
     * @param recoreVo
     */
    @Override
    public void insert(RecoreVo recoreVo) {
        //记录操作记录
        AccraditationRecord accraditationRecord = new AccraditationRecord();
        accraditationRecord.setApproverId(recoreVo.getUserId());
        accraditationRecord.setBussniessId(recoreVo.getId());
        accraditationRecord.setCreateTime(LocalDateTime.now());
        accraditationRecord.setType(recoreVo.getType());
        accraditationRecord.setOpinion(recoreVo.getOption());
        accraditationRecord.setApproverName(recoreVo.getRealName());
        accraditationRecord.setCurrentStep(recoreVo.getStep());
        accraditationRecord.setAuditStatus(recoreVo.getStatus());
        accraditationRecord.setHandleType(recoreVo.getHandleType());
        accraditationRecord.setStepNo(redisTemplate.boundValueOps(RECORD_STEP_NO_PRE + recoreVo.getId()).increment());

        if (ObjectUtil.isNotEmpty(recoreVo.getNextAssignee())) {
            //查询下一个审核人的姓名和角色
            List<UserRoleVo> userRoleVoList = userRoleMapper.selectByUserId(recoreVo.getNextAssignee());
            //待办任务，下个节点审核人 设置下个操作人--->护理部主管
            UserRoleVo userRoleVo = userRoleVoList.get(0);

            //审核记录，设置下个节点审核人的角色和用户名
            accraditationRecord.setNextApprover(userRoleVo.getUserName());
            String name = userRoleVo.getRoleName();
            accraditationRecord.setNextApproverRole(name);
            accraditationRecord.setNextApproverId(userRoleVo.getId());
            accraditationRecord.setNextStep(recoreVo.getNextStep());
        }

        String clientIP = ClientIpUtil.clientIp(request);
        accraditationRecord.setApproverNameRole(clientIP);
        //保存审核（操作）记录
        accraditationRecordMapper.insert(accraditationRecord);
    }
}
