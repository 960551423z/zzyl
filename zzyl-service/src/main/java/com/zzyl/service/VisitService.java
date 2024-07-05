package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.VisitDto;
import com.zzyl.vo.VisitVo;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {
    /**
     * 添加来访信息
     * @param dto 来访信息
     */
    void add(VisitDto dto);

    /**
     * 更新来访信息
     * @param id 来访信息id
     * @param dto 来访信息
     */
    void update(Long id, VisitDto dto);

    /**
     * 取消来访
     */
    void cancelVisit(Long id);

    /**
     * 根据id删除来访信息
     * @param id 来访信息id
     */
    void deleteById(Long id);

    /**
     * 根据id查找来访信息
     * @param id 来访信息id
     * @return 来访信息
     */
    VisitVo findById(Long id);

    /**
     * 查找所有来访信息
     * @return 所有来访信息
     * @param mobile
     * @param time
     */
    List<VisitVo> findAll(String mobile, LocalDateTime time);

    /**
         * 分页查找来访信息
         * @param page 页码
         * @param size 每页大小
         * @param name 来访人姓名
         * @param phone 来访人手机号
         * @param status 来访状态
         * @param type 来访类型
         * @param of
     * @param localDateTime
     * @return 来访信息列表
         */
    PageResponse<VisitVo> findByPage(int page, int size, String name, String phone, Integer status, Integer type, LocalDateTime of, LocalDateTime localDateTime);
}
