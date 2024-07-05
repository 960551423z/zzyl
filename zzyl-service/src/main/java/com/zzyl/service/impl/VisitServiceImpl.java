package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.dto.VisitDto;
import com.zzyl.entity.Visit;
import com.zzyl.enums.ReservationStatus;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.VisitMapper;
import com.zzyl.service.VisitService;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.VisitVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitMapper visitMapper;

    /**
     * 添加来访
     */
    @Override
    public void add(VisitDto dto) {
        // 否则，允许添加来访
        Visit visit = new Visit();
        BeanUtils.copyProperties(dto, visit);
        visit.setStatus(ReservationStatus.PENDING.getOrdinal());
        Long userId = UserThreadLocal.getMgtUserId();;
        visit.setCreateBy(userId);
        try {
            visitMapper.insert(visit);
        }
        catch (Exception e) {
            log.info(e +"");
            throw new BaseException("此手机号已来访该时间");
        }
    }

    /**
     * 更新来访
     */
    @Override
    public void update(Long id, VisitDto dto) {
        Visit visit = visitMapper.findById(id);
        if (visit != null) {
            BeanUtils.copyProperties(dto, visit);
            visit.setId(id);
            visit.setUpdateTime(LocalDateTime.now());
            visitMapper.update(visit);
        }
    }

    /**
     * 取消来访
     */
    @Override
    public void cancelVisit(Long id) {
        Visit visit = visitMapper.findById(id);
        if (visit != null) {
            visit.setStatus(ReservationStatus.CANCELED.getOrdinal());
            visit.setUpdateTime(LocalDateTime.now());
            Long userId = UserThreadLocal.getMgtUserId();;
            visit.setUpdateBy(userId);
            visitMapper.update(visit);
        }
    }

    /**
     * 根据id删除来访
     */
    @Override
    public void deleteById(Long id) {
        visitMapper.deleteById(id);
    }

    /**
     * 根据id查找来访
     */
    @Override
    public VisitVo findById(Long id) {
        Visit visit = visitMapper.findById(id);
        if (visit != null) {
            return convertToVO(visit);
        }
        return null;
    }


    /**
     * 查找所有来访
     * @param mobile 来访人手机号
     * @param time 来访时间
     */
    @Override
    public List<VisitVo> findAll(String mobile, LocalDateTime time) {
        // 计算24小时后的时间
        LocalDateTime endTime = time.plusHours(24);
        // 根据mobile和时间范围查询来访
        List<Visit> visits = visitMapper.findAll(mobile, time, endTime);
        return convertToVOList(visits);
    }



/**
     * 分页查找来访
     * @param page 页码
     * @param size 每页大小
     * @param name 来访人姓名
     * @param phone 来访人手机号
     * @param status 来访状态
     * @param type 来访类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 来访列表
     */
    @Override
    public PageResponse<VisitVo> findByPage(int page, int size, String name, String phone, Integer status, Integer type, LocalDateTime startTime, LocalDateTime endTime) {
        PageHelper.startPage(page, size);
        Long userId = UserThreadLocal.getMgtUserId();;
        Page<Visit> byPage = visitMapper.findByPage(page, size, name, phone, status, type, userId, startTime, endTime);
        return PageResponse.of(byPage, VisitVo.class);
    }


    /**
     * 将Visit转换为VisitVO
     */
    private VisitVo convertToVO(Visit visit) {
        return BeanUtil.toBean(visit, VisitVo.class);
    }

    /**
     * 将List<Visit>转换为List<VisitVO>
     */
    private List<VisitVo> convertToVOList(List<Visit> visits) {
        return visits.stream().map(this::convertToVO).collect(Collectors.toList());
    }

}

