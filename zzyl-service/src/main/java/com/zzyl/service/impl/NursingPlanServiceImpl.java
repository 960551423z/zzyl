package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.dto.NursingPlanDto;
import com.zzyl.entity.NursingLevel;
import com.zzyl.entity.NursingPlan;
import com.zzyl.mapper.NursingPlanMapper;
import com.zzyl.service.NursingLevelService;
import com.zzyl.service.NursingPlanService;
import com.zzyl.service.NursingProjectPlanService;
import com.zzyl.vo.NursingPlanVo;
import com.zzyl.vo.NursingProjectPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NursingPlanServiceImpl implements NursingPlanService {
    private final NursingPlanMapper nursingPlanMapper;

    @Resource
    private NursingLevelService nursingLevelService;

    @Autowired
    public NursingPlanServiceImpl(NursingPlanMapper nursingPlanMapper) {
        this.nursingPlanMapper = nursingPlanMapper;
    }

    @Autowired
    private NursingProjectPlanService nursingProjectPlanService;

    /**
     * 添加护理计划
     * @param nursingPlan 护理计划对象
     */
    @Transactional
    @Override
    public void add(NursingPlanDto nursingPlan) {
        NursingPlan plan = BeanUtil.toBean(nursingPlan, NursingPlan.class);
        plan.setStatus(1);
        nursingPlanMapper.insert(plan);
        nursingPlan.getProjectPlans().forEach(v -> v.setPlanId(plan.getId()));
        nursingProjectPlanService.addList(nursingPlan.getProjectPlans());
    }

    /**
     * 更新护理计划
     * @param nursingPlan 护理计划对象
     */
    @Transactional
    @Override
    public void update(NursingPlanDto nursingPlan) {
        NursingPlan plan = BeanUtil.toBean(nursingPlan, NursingPlan.class);
        // 删除原来的所有护理计划和项目关联表中的Ids
        NursingPlanVo byId = getById(nursingPlan.getId());
        List<Long> ids = byId.getProjectPlans().stream().map(NursingProjectPlanVo::getId).collect(Collectors.toList());
        nursingProjectPlanService.deleteByIds(ids);
        if (CollUtil.isNotEmpty(nursingPlan.getProjectPlans())) {
            nursingPlan.getProjectPlans().forEach(v -> v.setPlanId(nursingPlan.getId()));
            nursingProjectPlanService.addList(nursingPlan.getProjectPlans());
        }
        nursingPlanMapper.update(plan);
    }

    /**
     * 根据ID删除护理计划
     * @param id 护理计划ID
     */
    @Override
    public void deleteById(Long id) {
        nursingPlanMapper.deleteById(id);
    }

    /**
     * 根据ID查询护理计划
     * @param id 护理计划ID
     * @return 护理计划对象
     */
    @Override
    public NursingPlanVo getById(Long id) {
        return BeanUtil.toBean(nursingPlanMapper.getById(id), NursingPlanVo.class);
    }

    /**
     * 查询所有护理计划
     * @return 护理计划Dto列表
     */
    @Override
    public List<NursingPlanVo> listAll() {
        List<NursingPlan> nursingPlans = nursingPlanMapper.listAll();
        return nursingPlans.stream().map(v -> BeanUtil.toBean(v, NursingPlanVo.class)).collect(Collectors.toList());
    }

    /**
     * 分页查询护理计划
     * @param name 护理计划名称
     * @param status 护理计划状态
     * @param pageNum 当前页码
     * @param pageSize 每页显示的记录数
     * @return 分页查询结果
     */
    @Override
    public PageResponse<NursingPlanVo> listByPage(String name, Integer status, Integer pageNum, Integer pageSize) {
        // 使用 PageHelper 分页插件
        PageHelper.startPage(pageNum, pageSize);
        Page<List<NursingPlan>> lists = nursingPlanMapper.listByPage(pageNum, pageSize, name, status);// 获取所有护理计划
        // 增加护理项目和计划的绑定关系
        PageResponse<NursingPlanVo> nursingPlanVoPageResponse = PageResponse.of(lists, NursingPlanVo.class);
        List<Long> ids = nursingPlanVoPageResponse.getRecords().stream().map(NursingPlanVo::getId).distinct().collect(Collectors.toList());

        if (CollUtil.isEmpty(ids)) {
            return nursingPlanVoPageResponse;
        }
        // 查询db
        List<NursingLevel> levels = nursingLevelService.listAllByPlanIds(ids);
        // 转为map
        Map<Long, List<NursingLevel>> groupBy  = levels.stream().collect(Collectors.groupingBy(NursingLevel::getPlanId));
        // 设置绑定数量
        nursingPlanVoPageResponse.getRecords().forEach(v -> v.setCount(Optional.ofNullable(groupBy.get(v.getId())).orElse(new ArrayList<>()).size()));
        return nursingPlanVoPageResponse;
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        nursingPlanMapper.updateStatus(id, status);
    }

}
