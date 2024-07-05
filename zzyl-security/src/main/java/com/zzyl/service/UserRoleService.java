package com.zzyl.service;

import com.zzyl.entity.UserRole;

import java.util.List;

/**
 * 用户角色关联表服务类
 */
public interface UserRoleService {

    /***
     *  按用户ID删除用户角色中间表
     * @param userId 用户id
     * @return
     */
    boolean deleteUserRoleByUserId(Long userId);

    /***
     *  按用户IDS删除用户角色中间表
     * @param userIds 用户id
     * @return
     */
    boolean deleteUserRoleInUserId(List<Long> userIds);

    /***
     *  批量刪除
     * @param userRoles 用户id
     * @return
     */
    int batchInsert(List<UserRole> userRoles);
}
