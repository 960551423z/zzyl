package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.RoleDto;
import com.zzyl.vo.RoleVo;

import java.util.List;

/**
 * 角色表服务类
 */
public interface RoleService {

    /**
     *  多条件查询角色表分页列表
     * @param roleDto 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<ResourceVo>
     */
    PageResponse<RoleVo> findRolePage(RoleDto roleDto, int pageNum, int pageSize);

    /**
     *  创建角色表
     * @param roleDto 对象信息
     * @return ResourceVo
     */
    RoleVo createRole(RoleDto roleDto);

    /**
     *  修改角色表
     * @param roleDto 对象信息
     * @return Boolean
     */
    Boolean updateRole(RoleDto roleDto);

    /***
     *  查询用户对应的角色
     * @param userIds
     * @return
     */
    List<RoleVo> findRoleVoListInUserId(List<Long> userIds);

    /**
     *  员工对应角色
     * @param userId 查询条件
     * @return: List<Role>
     */
    List<RoleVo> findRoleVoListByUserId(Long userId);

    /***
     *  角色下拉框
     * @return
     */
    List<RoleVo> initRoles();

    /**
     * 删除角色
     * @param roleIds
     * @return
     */
    int deleteRoleByIds(List<Long> roleIds);
}
