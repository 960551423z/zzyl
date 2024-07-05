package com.zzyl.mapper;

import com.github.pagehelper.Page;

import com.zzyl.entity.NursingProject;
import com.zzyl.vo.NursingProjectVo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface NursingProjectMapper {

    Page<NursingProjectVo> getByPage(String name, Integer pageNum, Integer pageSize, Integer status);

    void insert(NursingProject nursingProject);

    NursingProjectVo selectById(Long id);

    void update(NursingProject nursingProject);

    void updateStatus(Long id, Integer status);

    void deleteById(Long id);
}
