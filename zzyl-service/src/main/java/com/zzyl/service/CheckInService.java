package com.zzyl.service;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.CheckInDto;
import com.zzyl.vo.retreat.TasVo;

import java.time.LocalDateTime;

/**
 * @author 阿庆
 */
public interface CheckInService extends IActFlowCustomService {
    /**
     * 入住申请
     * @param checkInDto
     */
    ResponseResult createCheckIn(CheckInDto checkInDto);

    /**
     * 查询入住信息
     * @param      * @param status
     * @param code
     * @return
     */
    ResponseResult<TasVo> getCheckIn(String code, String assigneeId, Integer flowStatus, String taskId);

    /**
     * 同意入住申请
     * @param id
     * @return
     */
    ResponseResult submitCheckIn(Long id, String info, String taskId);

    /**
     * 审核拒绝
     * @param id  入住单code
     * @param reject   拒绝原因
     * @return
     */
    ResponseResult auditReject(Long id, String reject, String taskId);

    /**
     * 撤回
     * @param id
     * @param flowStatus
     * @return
     */
    ResponseResult revocation(Long id, Integer flowStatus, String taskId);

    /**
     * 驳回
     * @param id
     * @return
     */
    ResponseResult disapprove(Long id, String message, String taskId);

    /**
     * 入住管理列表查询
     * @param checkInCode    入住code
     * @param name           老人姓名
     * @param idCardNo       身份证号
     * @param start          开始时间
     * @param end            结束时间
     * @param pageNum        当前页
     * @param pageSize       每页显示条数
     * @param deptNo         部门编号
     * @param userId         用户id
     * @return
     */
    ResponseResult selectByPage(String checkInCode, String name, String idCardNo, LocalDateTime start, LocalDateTime end, Integer pageNum, Integer pageSize, String deptNo, Long userId);

    /**
     * 撤销
     * @param checkInCode   入住编码
     * @return
     */
    ResponseResult cancel(Long checkInCode, String taskId);

    /**
     * 评估
     * @param checkInDto
     * @return
     */
    ResponseResult review(CheckInDto checkInDto);
}
