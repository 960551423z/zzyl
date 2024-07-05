package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.dto.NursingLevelDto;
import com.zzyl.entity.NursingLevel;
import com.zzyl.mapper.NursingLevelMapper;
import com.zzyl.service.NursingLevelService;
import com.zzyl.vo.NursingLevelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NursingLevelServiceImpl implements NursingLevelService {
    @Autowired
    private NursingLevelMapper nursingLevelMapper;

    // 获取所有护理等级数据
    @Override
    public List<NursingLevelVo> listAll() {
        List<NursingLevel> nursingLevels = nursingLevelMapper.listAll();
        return nursingLevels.stream().map(v -> BeanUtil.toBean(v, NursingLevelVo.class)).collect(Collectors.toList());
    }

    // 批量插入护理等级数据
    @Override
    public void insertBatch(List<NursingLevelDto> nursingLevels) {
        List<NursingLevel> levels = BeanUtil.copyToList(nursingLevels, NursingLevel.class);
        nursingLevelMapper.insertBatch(levels);
    }

    // 根据id获取护理等级数据
    @Override
    public NursingLevelVo getById(Long id) {
        return BeanUtil.toBean(nursingLevelMapper.getById(id), NursingLevelVo.class);
    }

    // 更新护理等级数据
    @Override
    public void update(NursingLevelDto nursingLevel) {
        NursingLevel level = BeanUtil.toBean(nursingLevel, NursingLevel.class);
        nursingLevelMapper.update(level);
    }

    // 删除护理等级数据
    @Override
    public void delete(Long id) {
        nursingLevelMapper.delete(id);
    }

    // 分页查询护理等级数据
    @Override
    public PageResponse<NursingLevelVo> listByPage(Integer pageNum, Integer pageSize, String name, Integer status) {
        // 使用pagehelper进行分页查询护理等级数据
        PageHelper.startPage(pageNum, pageSize);
        Page<List<NursingLevel>> page = nursingLevelMapper.listByPage(pageNum, pageSize, name, status);
        // 封装分页信息
        return  PageResponse.of(page, NursingLevelVo.class);
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        nursingLevelMapper.updateStatus(id, status);
    }

    @Override
    public List<NursingLevel> listAllByPlanIds(List<Long> ids) {
        return nursingLevelMapper.listAllByPlanIds(ids);
    }

    // 插入护理等级数据
    @Override
    public void insert(NursingLevelDto nursingLevel) {
        NursingLevel level = BeanUtil.toBean(nursingLevel, NursingLevel.class);
        nursingLevelMapper.insert(level);
    }
}

