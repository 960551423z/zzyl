package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.AlertRuleDto;
import com.zzyl.vo.AlertRuleVo;

public interface AlertRuleService {
    /**
     * 查询报警数据列表
     * @param pageNum
     * @param pageSize
     * @param alertRuleName
     * @param productId
     * @param functionName
     * @return
     */
    PageResponse<AlertRuleVo> getAlertRulePage(Integer pageNum, Integer pageSize, String alertRuleName, String productId, String functionName);

    /**
     * 删除报警
     * @param id
     */
    void deleteAlertRule(Long id);

    /**
     * 修改报警
     * @param id
     * @param alertRuleDto
     */
    void updateAlertRule(Long id, AlertRuleDto alertRuleDto);

    /**
     * 查询报警信息
     * @param id
     * @return
     */
    AlertRuleVo readAlertRule(Long id);

    /**
     * 创建报警
     * @param alertRuleDto
     */
    void createAlertRule(AlertRuleDto alertRuleDto);

    /**
     * 启用或禁用
     * @param id
     * @param status
     */
    void enableOrDisable(Long id, Integer status);
}
