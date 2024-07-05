package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.zzyl.constant.AccraditationRecordConstant;
import com.zzyl.dto.BedDto;
import com.zzyl.dto.CheckInConfigDto;
import com.zzyl.entity.CheckIn;
import com.zzyl.entity.CheckInConfig;
import com.zzyl.entity.Elder;
import com.zzyl.entity.User;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.CheckInConfigMapper;
import com.zzyl.mapper.CheckInMapper;
import com.zzyl.mapper.ElderMapper;
import com.zzyl.service.AccraditationRecordService;
import com.zzyl.service.ActFlowCommService;
import com.zzyl.service.BedService;
import com.zzyl.service.CheckInConfigService;
import com.zzyl.utils.CodeUtil;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.BedVo;
import com.zzyl.vo.RecoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class CheckInConfigServiceImpl implements CheckInConfigService {

    @Resource
    private CheckInConfigMapper checkInConfigMapper;

    @Autowired
    private ActFlowCommService actFlowCommService;

    @Autowired
    private AccraditationRecordService accraditationRecordService;

    @Autowired
    private CheckInMapper checkInMapper;

    @Autowired
    private ElderMapper elderMapper;

    @Resource
    private BedService bedService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 根据老人ID查询当前入住配置
     * @param elderId 老人ID
     * @return CheckInConfig
     */
    @Override
    public CheckInConfig findCurrentConfigByElderId(Long elderId) {
        return checkInConfigMapper.findCurrentConfigByElderId(elderId);
    }

    /**
     * 入住选择配置
     *
     * @param checkInConfigDto 入住选择配置
     * @return 受影响的行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int checkIn(CheckInConfigDto checkInConfigDto) {

        if (checkInConfigDto.getCheckInStartTime().isAfter(checkInConfigDto.getCostStartTime())
                || checkInConfigDto.getCheckInEndTime().isBefore(checkInConfigDto.getCostEndTime()))  {
            throw new BaseException("费用期限应该在入住期限内");
        }
        // 入住配置
        CheckInConfig checkInConfig = BeanUtil.toBean(checkInConfigDto, CheckInConfig.class);
        //查询选择的床位
        BedVo bedById = bedService.getBedById(checkInConfigDto.getBedId());
        //入住配置中设置床位号
        checkInConfig.setBedNo(bedById.getBedNumber());
        //组装床位信息，方便前端展示和回显 拼接规则：   楼层id:房间id:床位id:楼层名称:入住编码
        checkInConfig.setRemark(checkInConfigDto.getFloorId() + ":" + checkInConfigDto.getRoomId()  + ":" + checkInConfigDto.getBedId()
                + ":" + checkInConfigDto.getFloorName()
                + ":" + checkInConfigDto.getCode());
        if (ObjectUtil.isNotEmpty(checkInConfig.getId())) {
            //更新入住配置
            checkInConfigMapper.updateByPrimaryKeySelective(checkInConfig);

        } else {
            //新增入住配置
            checkInConfigMapper.insert(checkInConfig);
        }

        bedById.setBedStatus(2);
        bedById.setBedNumber(bedById.getBedNumber());
        BedDto bedDto = BeanUtil.toBean(bedById, BedDto.class);

        //更新床位状态
        bedService.updateBed(bedDto);

        //更新老人的床位数据
        Elder elder = elderMapper.selectByPrimaryKey(checkInConfig.getElderId());
        elder.setBedId(bedById.getId());
        elder.setBedNumber(bedById.getBedNumber());
        elderMapper.updateByPrimaryKeySelective(elder);

        // 流程状态 操作记录
        CheckIn checkIn = checkInMapper.selectByPrimaryKey(checkInConfigDto.getCheckInId());
        //设置流程状态
        checkIn.setFlowStatus(CheckIn.FlowStatus.SIGN.getCode());
        //生成合同编号
        String ht = CodeUtil.generateCode("HT", redisTemplate, 5);
        checkIn.setRemark(ht);
        //更新入住时间
        checkIn.setCheckInTime(checkInConfig.getCheckInStartTime());
        checkInMapper.updateByPrimaryKeySelective(checkIn);
        //  操作记录
        //从当前线程中获取用户数据
        String subject = UserThreadLocal.getSubject();
        User user = JSON.parseObject(subject, User.class);

        //完成任务
        actFlowCommService.completeProcess("", checkInConfigDto.getTaskId(), user.getId().toString(), 1, null);

        //保存操作记录
        Long nextAssignee = actFlowCommService.getNextAssignee("checkIn", "checkIn:" + checkIn.getId());

        RecoreVo recoreVo = getRecoreVo(checkIn,
                user, AccraditationRecordConstant.AUDIT_STATUS_PASS,
                "同意", "养老顾问处理-入住配置",
                "法务处理-签约办理", nextAssignee
        ,AccraditationRecordConstant.RECORD_HANDLE_TYPE_PROCESSED);
        accraditationRecordService.insert(recoreVo);

        return 0;

    }

    /**
     * 获取操作记录数据
     * @param checkIn  入住对象
     * @param user  当前登录用户
     * @param status  审核状态
     * @param option  操作意见
     * @param step    当前步骤
     * @param nextStep   下一步说明
     * @param nextAssignee  下一个审核人
     * @return
     */
    private RecoreVo getRecoreVo(CheckIn checkIn,User user,Integer status,String option,String step,String nextStep,Long nextAssignee,Integer handleType){
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
}

