package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.dto.MemberElderDto;
import com.zzyl.entity.MemberElder;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.MemberElderMapper;
import com.zzyl.service.ElderService;
import com.zzyl.service.MemberElderService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.MemberElderVo;
import com.zzyl.vo.retreat.ElderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户老人关联 Service 实现类
 */
@Service
public class MemberElderServiceImpl implements MemberElderService {

    @Autowired
    private MemberElderMapper memberElderMapper;

    @Resource
    private ElderService elderService;

    /**
     * 添加客户老人关联
     * @param memberElder 客户老人关联实体
     */
    @Override
    public void add(MemberElderDto memberElder) {

        ElderVo elder = elderService.selectByIdCard(memberElder.getIdCard(), memberElder.getName());
        if (elder == null || !elder.getStatus().equals(1)) {
            throw new BaseException("老人不存在");
        }

        MemberElder memberElder1 = BeanUtil.toBean(memberElder, MemberElder.class);
        memberElder1.setMemberId(UserThreadLocal.getUserId());
        memberElder1.setElderId(elder.getId());
        try {
            memberElderMapper.add(memberElder1);
        }catch (Exception e) {
            throw new BaseException("已绑定过此家人");
        }
    }

    /**
     * 更新客户老人关联
     * @param memberElder 客户老人关联实体
     */
    @Override
    public void update(MemberElderDto memberElder) {
        MemberElder memberElder1 = BeanUtil.toBean(memberElder, MemberElder.class);
        memberElderMapper.update(memberElder1);
    }

    /**
     * 根据ID删除客户老人关联
     * @param id 客户老人关联ID
     */
    @Override
    public void deleteById(Long id) {
        memberElderMapper.deleteById(id);
    }

    /**
     * 根据ID获取客户老人关联
     * @param id 客户老人关联ID
     * @return 客户老人关联实体
     */
    @Override
    public MemberElderVo getById(Long id) {
        MemberElder byId = memberElderMapper.getById(id);
        return BeanUtil.toBean(byId, MemberElderVo.class);
    }

    /**
     * 根据客户ID获取客户老人关联列表
     * @param memberId 客户ID
     * @return 客户老人关联列表
     */
    @Override
    public List<MemberElderVo> listByMemberId(Long memberId) {
        List<MemberElder> memberElders = memberElderMapper.listByMemberId(memberId);
        return BeanUtil.copyToList(memberElders, MemberElderVo.class);
    }

    /**
     * 根据老人ID获取客户老人关联列表
     * @param elderId 老人ID
     * @return 客户老人关联列表
     */
    @Override
    public List<MemberElderVo> listByElderId(Long elderId) {
        List<MemberElder> memberElders = memberElderMapper.listByElderId(elderId);
        return BeanUtil.copyToList(memberElders, MemberElderVo.class);
    }

    /**
     * 分页查询客户老人关联列表
     * @param memberId 客户ID
     * @param elderId 老人ID
     * @param pageNum 当前页码
     * @param pageSize 每页显示数量
     * @return 分页结果
     */
    @Override
    public PageResponse<MemberElderVo> listByPage(Long memberId, Long elderId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (ObjectUtil.isEmpty(memberId)) {
            memberId = UserThreadLocal.getUserId();
        }
        Page<List<MemberElder>> page = memberElderMapper.listByCondition(memberId, elderId);
        return PageResponse.of(page,  MemberElderVo.class);
    }

    @Override
    public List<MemberElderVo> my() {
        Long userId = UserThreadLocal.getUserId();
        return listByMemberId(userId);
    }

}

