package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.AlertRule;
import com.zzyl.vo.AlertRuleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertRuleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(AlertRule record);

    AlertRule selectByPrimaryKey(Long id);

    List<AlertRule> selectByFunctionId(@Param("functionId") String functionId, @Param("deviceId") String deviceId, @Param("productKey")String productKey);

    int updateByPrimaryKeySelective(AlertRule record);

    Page<AlertRuleVo> page(@Param("alertRuleName")String alertRuleName, @Param("productId")String productId, @Param("functionName")String functionName);

    /**
     * 启用或禁用
     * @param id ID
     * @param status 状态，0：禁用，1：启用
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
}