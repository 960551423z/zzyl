package com.zzyl.service;

import com.zzyl.entity.RoleResource;

import java.util.List;

/**
 * 角色资源关联表服务类
 */
public interface RoleResourceService {

    /***
     *  按角色ID删除角色资源中间表
     * @param roleId
     * @return
     */
    Boolean deleteRoleResourceByRoleId(Long roleId);

    /***
     *  按角色IDS删除角色资源中间表
     * @param roleIds
     * @return
     */
    Boolean deleteRoleResourceInRoleId(List<Long> roleIds);

    /***
     *  批量保存
     * @param roleResourceList
     * @return
     */
    int batchInsert(List<RoleResource> roleResourceList);
}
