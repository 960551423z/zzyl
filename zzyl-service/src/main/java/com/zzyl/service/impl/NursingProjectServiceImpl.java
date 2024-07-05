package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.config.OSSAliyunFileStorageService;
import com.zzyl.dto.NursingProjectDto;
import com.zzyl.entity.NursingProject;
import com.zzyl.mapper.NursingProjectMapper;
import com.zzyl.service.NursingProjectService;
import com.zzyl.vo.NursingProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 阿庆
 * @date: 2024/7/4 上午9:16
 */
@Service
public class NursingProjectServiceImpl implements NursingProjectService {

    @Autowired
    private NursingProjectMapper nursingProjectMapper;



    @Override
    public PageResponse<NursingProjectVo> getByPage(String name, Integer pageNum, Integer pageSize, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        Page<NursingProjectVo> page = nursingProjectMapper.getByPage(name, pageNum, pageSize, status);
        return PageResponse.of(page, NursingProjectVo.class);
    }

    @Override
    public void add(NursingProjectDto nursingProjectDto) {

        // 对象转换
        NursingProject nursingProject = BeanUtil.copyProperties(nursingProjectDto, NursingProject.class);

        nursingProjectMapper.insert(nursingProject);
    }

    @Override
    public NursingProjectVo getById(Long id) {
        return nursingProjectMapper.selectById(id);
    }

    @Override
    public void update(NursingProjectDto nursingProjectDto) {
        NursingProject nursingProject = BeanUtil.copyProperties(nursingProjectDto, NursingProject.class);
        nursingProjectMapper.update(nursingProject);
    }

    @Override
    public void updateByStatus(Long id, Integer status) {
        nursingProjectMapper.updateStatus(id,status);
    }

    @Override
    public void removeById(Long id) {
        // 删除图片地址

        nursingProjectMapper.deleteById(id);
    }
}
