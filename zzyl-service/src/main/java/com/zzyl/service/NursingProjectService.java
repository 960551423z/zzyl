package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.NursingProjectDto;
import com.zzyl.vo.NursingProjectVo;
import org.springframework.stereotype.Service;


public interface NursingProjectService {

    PageResponse<NursingProjectVo> getByPage(String name, Integer pageNum, Integer pageSize, Integer status);

    /**
     * 添加护理项目
     * @param nursingProjectDto
     */
    void add(NursingProjectDto nursingProjectDto);


    /**
     * 根据id进行查询
     * @param id
     * @return
     */
    NursingProjectVo getById(Long id);

    /**
     * 修改
     * @param nursingProjectDto
     */
    void update(NursingProjectDto nursingProjectDto);

    /**
     * 启用 / 禁用
     * @param id
     * @param status
     */
    void updateByStatus(Long id, Integer status);

    /**
     * 删除
     * @param id
     */
    void removeById(Long id);
}
