package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.dto.ApplicationsDto;
import com.zzyl.dto.PendingTasksDto;
import com.zzyl.entity.PendingTasks;
import com.zzyl.service.ApplicationsService;
import com.zzyl.vo.ApplicationsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 阿庆
 */
@Service
public class ApplicationsServiceImpl implements ApplicationsService {

    @Autowired
    private ActFlowCommServiceImpl actFlowCommService;

    /**
     * 分页查询我的申请
     * @param applicationsDto
     * @return
     */
    @Override
    public ResponseResult<ApplicationsVo> selectByPage(ApplicationsDto applicationsDto) {
        PendingTasksDto pendingTasksDto = BeanUtil.toBean(applicationsDto, PendingTasksDto.class);
        PageResponse<PendingTasks> pendingTasksPageResponse = actFlowCommService.myTaskInfoList(pendingTasksDto);
        return ResponseResult.success(pendingTasksPageResponse);
    }

}
