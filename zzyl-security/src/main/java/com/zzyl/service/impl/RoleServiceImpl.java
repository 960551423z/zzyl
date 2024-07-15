package com.zzyl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zzyl.base.PageResponse;
import com.zzyl.constant.SecurityConstant;
import com.zzyl.constant.SuperConstant;
import com.zzyl.dto.RoleDto;
import com.zzyl.entity.Role;
import com.zzyl.entity.RoleDept;
import com.zzyl.entity.RoleResource;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.RoleMapper;
import com.zzyl.mapper.UserRoleMapper;
import com.zzyl.service.*;
import com.zzyl.utils.BeanConv;
import com.zzyl.utils.EmptyUtil;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.DeptVo;
import com.zzyl.vo.ResourceVo;
import com.zzyl.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色表服务实现类
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    ResourceService resourceService;

    @Autowired
    DeptService deptService;

    @Autowired
    RoleResourceService roleResourceService;

    @Autowired
    RoleDeptService roleDeptService;

    @Autowired
    UserRoleMapper userRoleMapper;

    //自定义
    public static final String DATA_SCOPE_0 = "0";

    @Override
    public PageResponse<RoleVo> findRolePage(RoleDto roleDto, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<List<Role>> page = roleMapper.selectPage(roleDto);
        PageResponse<RoleVo> pageResponse = PageResponse.of(page, RoleVo.class);
        if (!EmptyUtil.isNullOrEmpty(pageResponse.getRecords())){
            List<Long> roleIdSet = pageResponse.getRecords().stream().map(RoleVo::getId).collect(Collectors.toList());
            //查询对应资源
            List<ResourceVo> resourceList = resourceService.findResourceVoListInRoleId(roleIdSet);
            //查询对应数据权限
            List<DeptVo> deptVoList = deptService.findDeptVoListInRoleId(roleIdSet);
            pageResponse.getRecords().forEach(n->{
                //装配资源
                Set<String> resourceNoSet = Sets.newHashSet();
                if (!EmptyUtil.isNullOrEmpty(resourceList)){
                    resourceList.forEach(r->{
                        if (String.valueOf(n.getId()).equals(r.getRoleId())){
                            resourceNoSet.add(r.getResourceNo());
                        }
                    });
                    if (!EmptyUtil.isNullOrEmpty(resourceNoSet)){
                        n.setCheckedResourceNos(resourceNoSet.toArray(new String[resourceNoSet.size()]));
                    }
                }
                //装配数据权限
                Set<String> deptNoSet = Sets.newHashSet();
                if (!EmptyUtil.isNullOrEmpty(deptVoList)){
                    deptVoList.forEach(d->{
                        if (String.valueOf(n.getId()).equals(d.getRoleId())){
                            deptNoSet.add(d.getDeptNo());
                        }
                    });
                    if (!EmptyUtil.isNullOrEmpty(deptNoSet)){
                        n.setCheckedDeptNos(deptNoSet.toArray(new String[deptNoSet.size()]));
                    }
                }
            });
        }
        return pageResponse;
    }

    @Override
    @Transactional
    public RoleVo createRole(RoleDto roleDto) {
        //转换RoleVo为Role
        Role role = BeanConv.toBean(roleDto, Role.class);
        int flag = roleMapper.insert(role);
        if (flag==0){
            throw new RuntimeException("保存角色信息出错");
        }
        return BeanConv.toBean(role, RoleVo.class);
    }


    @Override
    @Transactional
    public Boolean updateRole(RoleDto roleDto) {
        //转换RoleVo为Role
        Role role = BeanConv.toBean(roleDto, Role.class);
        if (ObjectUtil.isNotEmpty(role.getDataState()) && role.getDataState().equals("1")) {
            if (countUserRoleByRoleId(role.getId()) > 0) {
                throw new RuntimeException("该角色已分配用户,不能禁用");
            }
        }
        int flag = roleMapper.updateByPrimaryKeySelective(role);
        if (flag==0){
            throw new RuntimeException("修改角色信息出错");
        }

        if (ObjectUtil.isNotEmpty(role.getDataState())) {
            return true;
        }

        if (ObjectUtil.isEmpty(roleDto.getDataScope())) {
            //删除原有角色资源中间信息
            roleResourceService.deleteRoleResourceByRoleId(role.getId());

            //保存角色资源中间信息
            if (roleDto.getCheckedResourceNos() == null || roleDto.getCheckedResourceNos().length == 0) {
                return true;
            }
            List<RoleResource> roleResourceList = Lists.newArrayList();
            Arrays.asList(roleDto.getCheckedResourceNos()).forEach(n->{
                RoleResource roleResource = RoleResource.builder()
                        .roleId(role.getId())
                        .resourceNo(n)
                        .dataState(SuperConstant.DATA_STATE_0)
                        .build();
                roleResourceList.add(roleResource);
            });
            if (roleResourceList.size() == 0) {
                return true;
            }
            flag = roleResourceService.batchInsert(roleResourceList);
            if (flag==0){
                throw  new RuntimeException("保存角色资源中间信息失败");
            }
            return true;
        }

        //删除原有角色数据权限:这里不需要判断返回结果，有可能之前就没有自定义数据权限
        roleDeptService.deleteRoleDeptByRoleId(role.getId());
        //保存先的数据权限
        if (SecurityConstant.DATA_SCOPE_0.equals(roleDto.getDataScope())){
            //保存角色部门中间信息
            List<RoleDept> roleDeptList = Lists.newArrayList();
            Arrays.asList(roleDto.getCheckedDeptNos()).forEach(n->{
                RoleDept roleDept = RoleDept.builder()
                    .roleId(role.getId())
                    .deptNo(n)
                    .dataState(SuperConstant.DATA_STATE_0)
                    .build();
                roleDeptList.add(roleDept);
            });
            flag = roleDeptService.batchInsert(roleDeptList);
            if (flag==0){
                throw new RuntimeException("保存角色部门中间信息出错");
            }
        }
        return true;
    }

    @Override
    public List<RoleVo> findRoleVoListInUserId(List<Long> userIds) {
        return roleMapper.findRoleVoListInUserId(userIds);
    }

    @Override
    public List<RoleVo> findRoleVoListByUserId(Long userId) {
        return roleMapper.findRoleVoListByUserId(userId);
    }

    @Override
    public List<RoleVo> initRoles() {
        RoleVo roleVo = RoleVo.builder()
            .dataState(SuperConstant.DATA_STATE_0)
            .build();
        List<Role> roleList = roleMapper.selectList(roleVo);
        return BeanConv.toBeanList(roleList,RoleVo.class);
    }

    @Override
    public int deleteRoleByIds(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new BaseException("该角色已分配用户,不能删除");
            }
        }
        // 删除角色与菜单关联
        roleResourceService.deleteRoleResourceInRoleId(roleIds);
        // 删除角色与部门关联
        roleDeptService.deleteRoleDeptInRoleId(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }
}
