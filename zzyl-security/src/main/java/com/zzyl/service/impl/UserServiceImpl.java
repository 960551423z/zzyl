package com.zzyl.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zzyl.base.PageResponse;
import com.zzyl.constant.SuperConstant;
import com.zzyl.dto.UserDto;
import com.zzyl.entity.User;
import com.zzyl.entity.UserRole;
import com.zzyl.mapper.UserMapper;
import com.zzyl.properties.SecurityConfigProperties;
import com.zzyl.service.RoleService;
import com.zzyl.service.UserRoleService;
import com.zzyl.service.UserService;
import com.zzyl.utils.BeanConv;
import com.zzyl.utils.EmptyUtil;
import com.zzyl.utils.NoProcessing;
import com.zzyl.vo.RoleVo;
import com.zzyl.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户表服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public PageResponse<UserVo> findUserPage(UserDto userDto, int pageNum, int pageSize) {

        if (!EmptyUtil.isNullOrEmpty(userDto.getDeptNo())){
            userDto.setDeptNo(NoProcessing.processString(userDto.getDeptNo()));
        }

        //构建分页对象
        PageHelper.startPage(pageNum, pageSize);
        Page<List<User>> page = userMapper.selectPage(userDto);
        PageResponse<UserVo> pageResponse = PageResponse.of(page, UserVo.class);
        if (!EmptyUtil.isNullOrEmpty(pageResponse.getRecords())){
            List<Long> userIds = pageResponse.getRecords().stream().map(UserVo::getId).collect(Collectors.toList());
            //查询对应角色
            List<RoleVo> roleVoList = roleService.findRoleVoListInUserId(userIds);

            //装配数据
            pageResponse.getRecords().forEach(n->{
                //装配角色
                Set<String> roleVoIds = Sets.newHashSet();
                //
                Set<String> roleVoLabels = Sets.newHashSet();
                roleVoList.forEach(r->{
                    if (String.valueOf(n.getId()).equals(r.getUserId())){
                        roleVoIds.add(String.valueOf(r.getId()));
                        roleVoLabels.add(r.getRoleName());

                    }
                });
                n.setRoleLabels(roleVoLabels);
                n.setRoleVoIds(roleVoIds);
            });
        }
        return pageResponse;
    }


    @Autowired
    SecurityConfigProperties securityConfigProperties;

    @Override
    @Transactional
    public UserVo createUser(UserDto userDto) {

        //转换UserVo为User
        User user = BeanConv.toBean(userDto, User.class);
        user.setUsername(user.getEmail());
        user.setNickName(user.getRealName());

        String password = bCryptPasswordEncoder.encode(securityConfigProperties.getDefaulePassword());
        user.setPassword(password);

        int flag = userMapper.insert(user);
        if (flag==0){
            throw new RuntimeException("保存用户信息出错");
        }
        //保存用户角色中间表
        List<UserRole> userRoles = Lists.newArrayList();
        userDto.getRoleVoIds().forEach(r->{
            userRoles.add(UserRole.builder()
                .userId(user.getId())
                .roleId(Long.valueOf(r))
                .dataState(SuperConstant.DATA_STATE_0)
                .build());
        });
        flag = userRoleService.batchInsert(userRoles);
        if (flag==0){
            throw new RuntimeException("保存用户角色中间表出错");
        }

        return BeanConv.toBean(user, UserVo.class);
    }

    @Override
    @Transactional
    public Boolean updateUser(UserDto userDto) {
        //转换UserVo为User
        User user = BeanConv.toBean(userDto, User.class);
        int flag = userMapper.updateByPrimaryKeySelective(user);
        if (flag==0){
            throw new RuntimeException("修改用户信息出错");
        }


        if (CollUtil.isNotEmpty(userDto.getRoleVoIds())) {
            //删除角色中间表
            boolean flagDel = userRoleService.deleteUserRoleByUserId(user.getId());
            if (!flagDel){
                throw new RuntimeException("删除角色中间表出错");
            }

            //重新保存角色中间表
            List<UserRole> userRoles = Lists.newArrayList();
            userDto.getRoleVoIds().forEach(r->{
                userRoles.add(UserRole.builder()
                        .userId(user.getId())
                        .roleId(Long.valueOf(r))
                        .dataState(SuperConstant.DATA_STATE_0)
                        .build());
            });
            flag = userRoleService.batchInsert(userRoles);
            if (flag==0){
                throw new RuntimeException("保存角色中间表出错");
            }
        }

        return true;
    }

    @Override
    public List<UserVo> findUserList(UserVo userVo) {
        if (!EmptyUtil.isNullOrEmpty(userVo.getDeptNo())){
            userVo.setDeptNo(NoProcessing.processString(userVo.getDeptNo()));
        }
        List<User> userList = userMapper.selectList(userVo);
        List<UserVo> userVoList = BeanConv.toBeanList(userList, UserVo.class);
        if (!EmptyUtil.isNullOrEmpty(userVoList)){
            List<Long> userIdSet = userVoList.stream().map(UserVo::getId).collect(Collectors.toList());
            //查询对应角色
            List<RoleVo> roleVoList = roleService.findRoleVoListInUserId(userIdSet);
            //装配数据
            userVoList.forEach(n->{
                //装配角色
                Set<String> roleVoIds = Sets.newHashSet();
                roleVoList.forEach(r->{
                    if (String.valueOf(n.getId()).equals(r.getUserId())){
                        roleVoIds.add(String.valueOf(r.getId()));
                    }
                });
                n.setRoleVoIds(roleVoIds);

            });
        }
        return userVoList;
    }

    @Override
    public  List<UserVo> findUserVoListByDeptNo(String deptNo) {
        return userMapper.findUserVoListByDeptNo(deptNo);
    }

    @Override
    public  List<UserVo> findUserVoListByPostNo(String postNo) {
        return userMapper.findUserVoListByPostNo(postNo);
    }

    @Override
    public List<UserVo> findUserVoListByRoleId(Long roleId) {
        return userMapper.findUserVoListByRoleId(roleId);
    }

    @Override
    public UserVo findUserVoForLogin(String username) {
        UserVo userVo = UserVo.builder()
            .username(username)
            .dataState(SuperConstant.DATA_STATE_0)
            .build();
        User user = userMapper.findUserVoForLogin(userVo);
        if (!EmptyUtil.isNullOrEmpty(user)){
            return BeanConv.toBean(user,UserVo.class);
        }
        throw new RuntimeException("用户名或密码错误");
    }

    @Override
    public Boolean resetPasswords(String userId) {
        String password = bCryptPasswordEncoder.encode(securityConfigProperties.getDefaulePassword());
        User user = User.builder().password(password).build();
        user.setId(Long.valueOf(userId));
        int flag = userMapper.updateByPrimaryKeySelective(user);
        if (flag>0){
            return true;
        }
        return false;
    }

    @Override
    public int deleteUserByIds(List<Long> userIds) {
        // 删除用户与角色关联
        userRoleService.deleteUserRoleInUserId(userIds);
        // 删除用户与岗位关联
        return userMapper.deleteUserByIds(userIds);
    }

    @Override
    public void updateIsLeaderByUserIdAndDeptNo(Long leaderId, String deptNo) {
        //根据部门编号找到对应的leader
        User user = userMapper.selectLeaderByDeptNo(deptNo);
        if(user != null){
            userMapper.clearIsLeader(user.getId());
        }
        userMapper.updateByUserIdAndLeaderId(leaderId,deptNo);

    }
}
