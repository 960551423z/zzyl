package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.UserDto;
import com.zzyl.vo.UserVo;

import java.util.List;

/**
 * 用户表服务类
 */
public interface UserService {

    /**
     *  多条件查询用户表分页列表
     * @param userDto 查询条件
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return Page<User>
     */
    PageResponse<UserVo> findUserPage(UserDto userDto, int pageNum, int pageSize);

    /**
     *  创建用户表
     * @param userDto 对象信息
     * @return User
     */
    UserVo createUser(UserDto userDto);

    /**
     *  修改用户表
     * @param userDto 对象信息
     * @return Boolean
     */
    Boolean updateUser(UserDto userDto);

    /**
     *  多条件查询用户表列表
     * @param userVo 查询条件
     * @return: List<User>
     */
    List<UserVo> findUserList(UserVo userVo);

    /**
     *  部门下员工
     * @param deptNo 部门
     * @return: List<User>
     */
    List<UserVo> findUserVoListByDeptNo(String deptNo);

    /**
     *  职位下员工
     * @param postNo 职位
     * @return: List<User>
     */
    List<UserVo> findUserVoListByPostNo(String postNo);

    /**
     *  角色下员工
     * @param roleId 角色
     * @return: List<User>
     */
    List<UserVo> findUserVoListByRoleId(Long roleId);

    /***
     *  查询用户构建对象
     *
     * @param username
     * @return
     * @return: com.zzyl.vo.UserVo
     */
    UserVo findUserVoForLogin(String username);

    /**
     *  充值密碼
     * @param userId 用戶Id
     * @return: List<User>
     */
    Boolean resetPasswords(String userId);

    /**
     * 根据用户id集合删除用户
     * @param userIds
     * @return
     */
    int deleteUserByIds(List<Long> userIds);

    /**
     * 根据用户和部门修改用户是否是leader
      * @param leaderId
     * @param deptNo
     */
    void updateIsLeaderByUserIdAndDeptNo(Long leaderId, String deptNo);
}
