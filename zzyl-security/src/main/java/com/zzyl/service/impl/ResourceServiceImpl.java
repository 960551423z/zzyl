package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.zzyl.constant.SuperConstant;
import com.zzyl.dto.ResourceDto;
import com.zzyl.entity.Resource;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.ResourceMapper;
import com.zzyl.mapper.RoleMapper;
import com.zzyl.mapper.RoleResourceMapper;
import com.zzyl.service.ResourceService;
import com.zzyl.service.RoleService;
import com.zzyl.utils.BeanConv;
import com.zzyl.utils.EmptyUtil;
import com.zzyl.utils.NoProcessing;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限表服务实现类
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceMapper resourceMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RoleResourceMapper roleResourceMapper;

    @Override
    public Boolean createResource(ResourceDto resourceDto) {

        if (resourceDto.getResourceType().equals(SuperConstant.MENU)) {
            ResourceDto resourceDto1 = ResourceDto
                    .builder()
                    .resourceName(resourceDto.getResourceName())
                    .build();
            List<Resource> resources = resourceMapper.selectList(resourceDto1);
            if (resources.size() > 0) {
                throw new BaseException("菜单名称重复");
            }

            ResourceDto resourceDto2 = ResourceDto.builder().requestPath(resourceDto.getRequestPath()).build();
            List<Resource> resources2 = resourceMapper.selectList(resourceDto2);
            if (resources2.size() > 0) {
                throw new BaseException("菜单路径重复");
            }
        }
        //转换U
        Resource resource = BeanUtil.toBean(resourceDto, Resource.class);
        String resourceNo = createResourceNo(resource.getParentResourceNo());
        resource.setResourceNo(resourceNo);
        int flag = resourceMapper.insert(resource);
        if (flag != 1) {
            throw new RuntimeException("保存资源信息出错");
        }
        return true;
    }

    @Override
    public Boolean updateResource(ResourceDto resourceDto) {
        //转换
        Resource resource = BeanUtil.toBean(resourceDto, Resource.class);
        if (resource.getDataState().equals("1")) {
            if (hasChildByMenuId(resource.getResourceNo())) {
                throw new RuntimeException("存在子菜单,不允许禁用");
            }
            if (checkMenuExistRole(resource.getResourceNo())) {
                throw new RuntimeException("菜单已分配,不允许禁用");
            }
        }
        int flag = resourceMapper.updateByPrimaryKeySelective(resource);
        if (flag == 0) {
            throw new RuntimeException("修改资源信息出错");
        }
        return true;
    }

    @Override
    public List<ResourceVo> findResourceList(ResourceDto resourceDto) {
        List<Resource> resourceList = resourceMapper.selectList(resourceDto);
        return BeanConv.toBeanList(resourceList, ResourceVo.class);
    }

    @Override
    public TreeVo resourceTreeVo(ResourceDto resourceDto) {

        //指定节点查询树形结构
        ResourceDto resourceDto1 = ResourceDto.builder()
                .dataState(SuperConstant.DATA_STATE_0)
                .parentResourceNo(NoProcessing.processString(SuperConstant.ROOT_PARENT_ID))
                .build();
        //数据库查询资源数据
        List<Resource> resourceList = resourceMapper.selectList(resourceDto1);
        if (EmptyUtil.isNullOrEmpty(resourceList)) {
            throw new RuntimeException("资源信息为定义！");
        }
        //返回的树形集合
        List<TreeItemVo> treeItemVoList = new ArrayList<>();

        //找根节点
        Resource rootResource = new Resource();
        rootResource.setResourceNo(SuperConstant.ROOT_PARENT_ID);
        rootResource.setResourceName("智慧养老院");
        //构建树形结构
        recursionTreeItem(treeItemVoList, rootResource, resourceList);
        return TreeVo.builder()
                .items(treeItemVoList)
                .build();
    }

    private void recursionTreeItem(List<TreeItemVo> treeItemVoList, Resource ResourceRoot, List<Resource> resourceList) {
        TreeItemVo treeItem = TreeItemVo.builder()
                .id(ResourceRoot.getResourceNo())
                .label(ResourceRoot.getResourceName())
                .build();
        //获得当前资源下子资源
        List<Resource> childrenResource = resourceList.stream()
                .filter(n -> n.getParentResourceNo().equals(ResourceRoot.getResourceNo()) && n.getResourceType().equals(SuperConstant.MENU))
                .collect(Collectors.toList());
        if (!EmptyUtil.isNullOrEmpty(childrenResource)) {
            List<TreeItemVo> listChildren = Lists.newArrayList();

            childrenResource.forEach(resource -> {
                this.recursionTreeItem(listChildren, resource, resourceList);
            });
            treeItem.setChildren(listChildren);
        }

        treeItemVoList.add(treeItem);
    }

    @Override
    public List<ResourceVo> findResourceVoListInRoleId(List<Long> roleIds) {
        return resourceMapper.findResourceVoListInRoleId(roleIds);
    }

    @Override
    public List<ResourceVo> findResourceVoListByUserId(Long userIdSet) {
        return resourceMapper.findResourceVoListByUserId(userIdSet);
    }

    @Override
    public String createResourceNo(String parentResourceNo) {
        ResourceDto resourceDto = ResourceDto.builder()
                .parentResourceNo(parentResourceNo)
                .build();
        List<Resource> resourceList = resourceMapper.selectList(resourceDto);
        //无下属节点则创建下属节点
        if (EmptyUtil.isNullOrEmpty(resourceList)) {
            return NoProcessing.createNo(parentResourceNo, false);

        } else { //有下属节点则累加下属节点
            Long resourceNo = resourceList.stream()
                    .map(resource -> {
                        return Long.valueOf(resource.getResourceNo());
                    })
                    .max(Comparator.comparing(i -> i)).get();
            return NoProcessing.createNo(String.valueOf(resourceNo), true);
        }
    }

    @Override
    public int deleteMenuById(String menuId) {
        if (hasChildByMenuId(menuId)) {
            throw new RuntimeException("存在子菜单,不允许删除");
        }
        if (checkMenuExistRole(menuId)) {
            throw new RuntimeException("菜单已分配,不允许删除");
        }
        return resourceMapper.deleteMenuById(menuId);
    }


    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public boolean hasChildByMenuId(String menuId) {
        int result = resourceMapper.hasChildByMenuId(menuId);
        return result > 0 ? true : false;
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public boolean checkMenuExistRole(String menuId) {
        int result = roleResourceMapper.checkMenuExistRole(menuId);
        return result > 0 ? true : false;
    }

    @Autowired
    private RoleService roleService;

    @Override
    public List<MenuVo> menus() {
        Long mgtUserId = UserThreadLocal.getMgtUserId();
        //查询对应角色
        List<RoleVo> roleVoList = roleService.findRoleVoListInUserId(Arrays.asList(mgtUserId));
        List<Long> roleIds = roleVoList.stream().map(RoleVo::getId).collect(Collectors.toList());
        if (CollUtil.isEmpty(roleIds)) {
            throw new BaseException("请为用户分配角色和菜单");
        }
        //查询角色对应的所有的菜单数据
        List<ResourceVo> resources = resourceMapper.findResourceVoListInRoleId(roleIds);

        List<MenuVo> list = new ArrayList<>();

        recursionMenuVo(list, resources);
        return list;

    }

    /**
     *  递归按钮
     */
    public List<MenuVo> recursionMenuVo(List<MenuVo> list, List<ResourceVo> resources) {
        for (ResourceVo resource : resources) {
            if (resource.getParentResourceNo().equals(SuperConstant.ROOT_PARENT_ID)) {
                MenuVo menuVo = new MenuVo();
                menuVo.setPath(resource.getRequestPath());
                menuVo.setName(resource.getResourceName());
                menuVo.setPath("/" + resource.getRequestPath());
                menuVo.setComponent(SuperConstant.COMPONENT_LAYOUT);

                MenuMetaVo menuMetaVo = MenuMetaVo.builder()
                        .icon(resource.getIcon())
                        .title(resource.getResourceName())
                        .build();
                menuVo.setMeta(menuMetaVo);
                menuVo.setRedirect("/" + resource.getResourceName());

                menuVo.setChildren(getChildrens(resource, resources, resource.getRequestPath()));
                list.add(menuVo);
            }
        }
        return list;
    }

    /**
     * 递归获取菜单和按钮
     *
     * @param resource
     * @param resources
     * @return
     */
    private List<MenuVo> getChildrens(ResourceVo resource, List<ResourceVo> resources, String component) {

        List<MenuVo> childrenList = Lists.newArrayList();
        for (ResourceVo resourceVo : resources) {
            if (resourceVo.getParentResourceNo().equals(resource.getResourceNo())) {
                MenuVo menuVo = new MenuVo();
                menuVo.setPath(resourceVo.getRequestPath());
                menuVo.setName(resourceVo.getResourceName());
                menuVo.setPath(resource.getRequestPath());
                menuVo.setComponent(component + "/" + resourceVo.getRequestPath());

                MenuMetaVo menuMetaVo = MenuMetaVo.builder()
                        .icon(resourceVo.getIcon())
                        .title(resourceVo.getResourceName())
                        .build();
                menuVo.setMeta(menuMetaVo);

                menuVo.setRedirect("/" + resource.getResourceName() + "/" + resourceVo.getResourceName());

                menuVo.setChildren(getChildrens(resourceVo, resources, resource.getRequestPath()));
                childrenList.add(menuVo);
            }
        }

        return childrenList;
    }

    /**
     * 查询当前登录人的按钮
     *
     * @return
     */
    @Override
    public List<MenuVo> buttons() {
        Long mgtUserId = UserThreadLocal.getMgtUserId();
        //查询对应角色
        List<RoleVo> roleVoList = roleService.findRoleVoListInUserId(Arrays.asList(mgtUserId));
        List<Long> roleIds = roleVoList.stream().map(RoleVo::getId).collect(Collectors.toList());
        if (CollUtil.isEmpty(roleIds)) {
            throw new BaseException("请为用户分配角色和菜单");
        }
        //全部的菜单，包含了菜单和按钮
        List<ResourceVo> resources = resourceMapper.findButtonVoListInRoleId(roleIds);

        List<MenuVo> list = new ArrayList<>();
        recursionButtonVo(list, resources);
        return list;
    }

    /**
     *  递归按钮
     */
    public List<MenuVo> recursionButtonVo(List<MenuVo> list, List<ResourceVo> resources) {
        for (ResourceVo resource : resources) {
            if (resource.getParentResourceNo().equals(SuperConstant.ROOT_PARENT_ID)) {
                MenuVo menuVo = new MenuVo();
                menuVo.setPath(resource.getRequestPath());
                menuVo.setName(resource.getResourceName());
                menuVo.setChildren(getAllChildrens(resource, resources));
                list.add(menuVo);
            }
        }
        return list;
    }

    /**
     * 递归获取菜单和按钮
     *
     * @param resource
     * @param resources
     * @return
     */
    private List<MenuVo> getAllChildrens(ResourceVo resource, List<ResourceVo> resources) {

        List<MenuVo> childrenList = Lists.newArrayList();
        for (ResourceVo resourceVo : resources) {
            if (resourceVo.getParentResourceNo().equals(resource.getResourceNo())) {
                MenuVo menuVo = new MenuVo();
                menuVo.setPath(resourceVo.getRequestPath());
                menuVo.setName(resourceVo.getResourceName());
                menuVo.setChildren(getAllChildrens(resourceVo, resources));
                childrenList.add(menuVo);
            }
        }

        return childrenList;
    }


}
