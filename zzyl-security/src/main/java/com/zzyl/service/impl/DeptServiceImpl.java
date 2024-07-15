package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.constant.DeptCacheConstant;
import com.zzyl.constant.SuperConstant;
import com.zzyl.dto.DeptDto;
import com.zzyl.entity.Dept;
import com.zzyl.exception.BaseException;
import com.zzyl.mapper.DeptMapper;
import com.zzyl.service.DeptService;
import com.zzyl.utils.BeanConv;
import com.zzyl.utils.NoProcessing;
import com.zzyl.vo.DeptVo;
import com.zzyl.vo.TreeItemVo;
import com.zzyl.vo.TreeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import springfox.documentation.service.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门表服务实现类
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    @CachePut(value = DeptCacheConstant.LIST,key = "#deptDto.hashCode()")
    public List<DeptVo> findDeptList(DeptDto deptDto) {
        return deptMapper.getList(deptDto);
    }

    @Override
    @CachePut(value = DeptCacheConstant.TREE)
    public TreeVo deptTree() {
        // 父节点常量
        String rootDeptParentId = SuperConstant.ROOT_DEPT_PARENT_ID;

        //封装DTO
        DeptDto deptDto = DeptDto.builder().parentDeptNo(NoProcessing.processString(rootDeptParentId)).build();

        // 先找所有元素
        List<DeptVo> list = deptMapper.getList(deptDto);


        // 找到父节点(目前父级只有一个)
        DeptVo deptVo = list.stream().filter(d -> d.getParentDeptNo()
                        .equals(rootDeptParentId))
                .collect(Collectors.toList()).get(0);


        //递归封装数据
        List<TreeItemVo> rootDept = new ArrayList<>();
        recursionTreeItem(rootDept, deptVo, list);


        return TreeVo.builder().items(rootDept).build();
    }

    /**
     * 递归封装
     */
    private void recursionTreeItem(List<TreeItemVo> rootDept, DeptVo deptVo, List<DeptVo> list) {
        // 先封装父级
        TreeItemVo treeItemVo = TreeItemVo.builder()
                .label(deptVo.getDeptName())
                .id(deptVo.getDeptNo()).build();

        // 封装子级(先找子级,自己的父编号，等于当前节点的编号)
        List<DeptVo> deptVos = list.stream().filter(d ->
                        d.getParentDeptNo().equals(deptVo.getDeptNo()))
                .collect(Collectors.toList());

        // 若存在子级
        if (!deptVos.isEmpty()) {

            // 创建一个TreeItemVo集合，因为子元素可能不止一个
            List<TreeItemVo> treeItemVos = new ArrayList<>();

            // 找到每一个子元素的下面的孙元素，进行递归封装
            deptVos.forEach(dept -> {
                recursionTreeItem(treeItemVos, dept, list);
            });

            // 封装好就可以设置节点
            treeItemVo.setChildren(treeItemVos);
        }
        // 父元素封装
        rootDept.add(treeItemVo);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)
    })
    public boolean createDept(DeptDto deptDto) {

        Dept dept = BeanUtil.copyProperties(deptDto, Dept.class);

        //根据父部门编号，构建部门编号
        String deptNo = createDeptNo(deptDto.getParentDeptNo());
        dept.setDeptNo(deptNo);
        int flag = deptMapper.insert(dept);

        if (flag !=1 )
            throw new BaseException("保存部门失败");

        return true;
    }

    /**
     * 构建部门编号
     * @param parentDeptNo
     */
    private String createDeptNo(String parentDeptNo) {
        // 部门层级不能超过四级(也就是只能有一个最大的那一级)
        if (NoProcessing.processString(parentDeptNo).length() / 3 == 5)
            throw new BaseException("部门创建不能超过4级");

        /* 构建部门编号:  1、当前父部门编号有子部门，在子部门上累加
         *              2、没有子部门，则新建子部门
         */
        DeptDto deptDto = DeptDto.builder().parentDeptNo(parentDeptNo).build();
        List<DeptVo> deptList = findDeptList(deptDto);
        if (deptList.isEmpty()) {
            // 没有子部门
            return NoProcessing.createNo(parentDeptNo,false);
        } else {
            // 有子部门
            Long deptNo = deptList.stream().map(dept -> Long.valueOf(parentDeptNo)).max(Comparator.comparing(i -> i)).get();
            return NoProcessing.createNo(String.valueOf(deptNo),true);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)
    })
    public boolean updateDept(DeptDto deptDto) {
        Dept dept = BeanUtil.copyProperties(deptDto, Dept.class);
        int flag = deptMapper.update(dept);


        if (flag == 0)
            throw new BaseException("部门修改失败");
        return true;
    }

        /**
         * 启用-禁用部门
         */
    @Override
    @Caching(evict = {
            @CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)
    })
    public boolean isEnable(DeptDto deptDto) {
        Dept dept = BeanUtil.copyProperties(deptDto, Dept.class);


        /*
            问题，禁用父部门的时候，子部门还在，不合理
            方法一：若该部门下面存在子部门，则提示不能禁用
            方法二：禁用父部门的同时，把子部门也禁用掉
                方法一更合理
         */
        //
        String deptNo = dept.getDeptNo();
        List<Dept> list = deptMapper.selectNode(deptNo);
        if (!list.isEmpty()) {
            throw new BaseException("存在子部门,暂时无法禁用");
        }


        int count = deptMapper.update(dept);
        if (count == 0) {
            throw new RuntimeException("修改部门信息出错");
        }
        return true;
    }


    /**
     * 部门删除
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)
    })
    public int removeById(Long deptId) {
        Dept dept = deptMapper.selectByDeptId(deptId);
        String deptNo = dept.getDeptNo();
        List<Dept> depts = deptMapper.selectByDept(deptNo);
        if (!depts.isEmpty()) {
            throw new RuntimeException("存在子部门,暂时无法删除");
        }

        return deptMapper.delete(deptId);
    }

    @Override
    public List<DeptVo> findDeptToDeptNosList(List<String> deptNos) {
        // 根据部门id来查询部门
        List<Dept> depts = deptMapper.selectByBatchDeptNos(deptNos);
        return depts.stream().map(dept -> BeanUtil.copyProperties(dept, DeptVo.class)).collect(Collectors.toList());
    }





    //    @Autowired
//    DeptMapper deptMapper;
//
//    @Resource
//    private PostMapper postMapper;
//
//    @Autowired
//    private UserService userService;
//
//    /**
//     * @param deptDto 对象信息
//     * @return DeptVo
//     *  创建部门表
//     */
//    @Transactional
//    @Override
//    @Caching(evict = {@CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
//            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)})
//    public Boolean createDept(DeptDto deptDto) {
//        //转换deptDto为Dept
//        Dept dept = BeanUtil.toBean(deptDto, Dept.class);
//
//        //根据传递过来的父部门编号创建当前部门编号
//        String deptNo = createDeptNo(dept.getParentDeptNo());
//        dept.setDeptNo(deptNo);
//
//        //保存
//        int flag = deptMapper.insert(dept);
//        if (flag != 1) {
//            throw new RuntimeException("保存部门信息出错");
//        }
//
//        //如果当前leader也是其他其他部门的负责人，则清空其他部门的leader数据
//        //一个人只能是一个部门的leader
//        if (ObjectUtil.isNotEmpty(deptDto.getLeaderId())) {
//            //根据leader查询，如果存在，则清空
//            deptMapper.clearOtherDeptLeader(deptDto.getLeaderId(), deptNo);
//            //在用户表设置标识，表明当前部门的leader
//            userService.updateIsLeaderByUserIdAndDeptNo(deptDto.getLeaderId(), dept.getDeptNo());
//        }
//
//        return true;
//    }
//
//    /**
//     * @param deptDto 对象信息
//     * @return Boolean
//     *  修改部门表
//     */
//    @Transactional
//    @Caching(evict = {@CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
//            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)})
//    @Override
//    public Boolean updateDept(DeptDto deptDto) {
//        //转换DeptVo为Dept
//        Dept dept = BeanUtil.toBean(deptDto, Dept.class);
//
//        //检验是否可以修改
//        if (dept.getDataState().equals("1")) {
//            if (hasChildByDeptId(dept.getDeptNo())) {
//                throw new RuntimeException("存在下级部门,不允许禁用");
//            }
//            if (checkDeptExistUser(dept.getDeptNo())) {
//                throw new RuntimeException("部门存在用户,不允许禁用");
//            }
//        }
//
//        //修改
//        int flag = deptMapper.updateByPrimaryKey(dept);
//        if (flag == 0) {
//            throw new RuntimeException("修改部门信息出错");
//        }
//
//        //如果当前leader也是其他其他部门的负责人，则清空其他部门的leader数据
//        //一个人只能是一个部门的leader
//        if (ObjectUtil.isNotEmpty(deptDto.getLeaderId())) {
//            //根据leader查询，如果存在，则清空
//            deptMapper.clearOtherDeptLeader(deptDto.getLeaderId(), deptDto.getDeptNo());
//            //在用户表设置标识，表明当前部门的leader
//            userService.updateIsLeaderByUserIdAndDeptNo(deptDto.getLeaderId(), dept.getDeptNo());
//        }
//
//        return true;
//    }
//
//    /**
//     * @param deptDto 查询条件
//     *  多条件查询部门表列表
//     * @return: List<DeptVo>
//     */
//    @Cacheable(value = DeptCacheConstant.LIST,key ="#deptDto.hashCode()")
//    @Override
//    public List<DeptVo> findDeptList(DeptDto deptDto) {
//        List<Dept> deptList = deptMapper.selectList(deptDto);
//        List<DeptVo> deptVos = BeanConv.toBeanList(deptList, DeptVo.class);
//        deptVos.forEach(v -> v.setCreateDay(LocalDateTimeUtil.format(v.getCreateTime(), "yyyy-MM-dd")));
//        return deptVos;
//    }
//

    @Override
    public List<DeptVo> findDeptInDeptNos(List<String> deptNos) {
        List<Dept> depts = deptMapper.findDeptInDeptNos(deptNos);
        return BeanConv.toBeanList(depts, DeptVo.class);
    }
//
//
//    @Override
//    public String createDeptNo(String parentDeptNo) {
//        if (NoProcessing.processString(parentDeptNo).length() / 3 == 5) {
//            throw new BaseException("部门最多4级");
//        }
//        DeptDto deptDto = DeptDto.builder().parentDeptNo(parentDeptNo).build();
//        List<Dept> deptList = deptMapper.selectList(deptDto);
//        //无下属节点则创建下属节点
//        if (EmptyUtil.isNullOrEmpty(deptList)) {
//            return NoProcessing.createNo(parentDeptNo, false);
//            //有下属节点则累加下属节点
//        } else {
//            Long deptNo = deptList.stream()
//                    .map(dept -> {
//                        return Long.valueOf(dept.getDeptNo());
//                    })
//                    .max(Comparator.comparing(i -> i)).get();
//            return NoProcessing.createNo(String.valueOf(deptNo), true);
//        }
//    }
//
    @Override
    public List<DeptVo> findDeptVoListInRoleId(List<Long> roleIdSet) {
        return deptMapper.findDeptVoListInRoleId(roleIdSet);
    }
//
//    @Transactional
//    @Caching(evict = {@CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
//            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)})
//    @Override
//    public int deleteDeptById(String deptId) {
//        if (hasChildByDeptId(deptId)) {
//            throw new RuntimeException("存在下级部门,不允许删除");
//        }
//        if (checkDeptExistUser(deptId)) {
//            throw new RuntimeException("部门存在用户,不允许删除");
//        }
//
//        postMapper.deletePostByDeptNo(deptId);
//        return deptMapper.deleteByDeptNo(deptId);
//    }
//
//    /**
//     * 启用-禁用部门
//     *
//     * @param deptDto
//     * @return
//     */
//    @Caching(evict = {@CacheEvict(value = DeptCacheConstant.LIST,allEntries = true),
//            @CacheEvict(value = DeptCacheConstant.TREE,allEntries = true)})
//    @Override
//    public Boolean isEnable(DeptDto deptDto) {
//
//        //查询部门
//        Dept dept = deptMapper.selectByDeptNo(deptDto.getDeptNo());
//        if (dept == null) {
//            throw new BaseException("部门不存在");
//        }
//        //设置状态
//        dept.setDataState(deptDto.getDataState());
//        //修改
//        int count = deptMapper.updateByPrimaryKey(dept);
//        if (count == 0) {
//            throw new RuntimeException("修改部门信息出错");
//        }
//        return true;
//    }
//
//    /**
//     * 是否存在子节点
//     *
//     * @param deptId 部门ID
//     * @return 结果
//     */
//    public boolean hasChildByDeptId(String deptId) {
//        int result = deptMapper.hasChildByDeptId(deptId);
//        return result > 0 ? true : false;
//    }
//
//    /**
//     * 查询部门是否存在用户
//     *
//     * @param deptId 部门ID
//     * @return 结果 true 存在 false 不存在
//     */
//    public boolean checkDeptExistUser(String deptId) {
//        int result = deptMapper.checkDeptExistUser(deptId);
//        return result > 0 ? true : false;
//    }
//
//    /**
//     * 组织部门树形
//     * @return: deptDto
//     */
//    @Override
//    @Cacheable(value = DeptCacheConstant.TREE)
//    public TreeVo deptTreeVo() {
//        //根节点查询树形结构
//        String parentDeptNo = SuperConstant.ROOT_DEPT_PARENT_ID;
//        //指定节点查询树形结构
//        DeptDto deptDto = DeptDto.builder()
//                .dataState(SuperConstant.DATA_STATE_0)
//                .parentDeptNo(NoProcessing.processString(parentDeptNo))
//                .build();
//        //查询部门列表数据
//        List<Dept> deptList =  deptMapper.selectList(deptDto);
//
//        if (EmptyUtil.isNullOrEmpty(deptList)) {
//            throw new RuntimeException("部门数据没有定义！");
//        }
//        //返回的部门列表
//        List<TreeItemVo> treeItemVoList = new ArrayList<>();
//        //找根节点
//        Dept rootDept = deptList.stream()
//                .filter(d -> SuperConstant.ROOT_DEPT_PARENT_ID.equals(d.getParentDeptNo()))
//                .collect(Collectors.toList()).get(0);
//        //递归调用
//        recursionTreeItem(treeItemVoList, rootDept, deptList);
//        //返回
//        return TreeVo.builder()
//                .items(treeItemVoList)
//                .build();
//    }
//
//    /**
//     * 构建树形结构，递归调用
//     * @param treeItemVoList   封装返回的对象
//     * @param deptRoot  当前部门
//     * @param deptList  部门列表（全部数据）
//     */
//    private void recursionTreeItem(List<TreeItemVo> treeItemVoList, Dept deptRoot, List<Dept> deptList) {
//        //构建item对象
//        TreeItemVo treeItem = TreeItemVo.builder()
//                .id(deptRoot.getDeptNo())
//                .label(deptRoot.getDeptName())
//                .build();
//        //获得当前部门下子部门
//        List<Dept> childrenDept = deptList.stream()
//                .filter(n -> n.getParentDeptNo().equals(deptRoot.getDeptNo()))
//                .collect(Collectors.toList());
//        //如果子部门不为空，则继续递归查询
//        if (!EmptyUtil.isNullOrEmpty(childrenDept)) {
//            //子部门列表
//            List<TreeItemVo> listChildren = Lists.newArrayList();
//            //继续找子部门，找不到为止
//            childrenDept.forEach(dept -> {
//                this.recursionTreeItem(listChildren, dept, deptList);
//            });
//            treeItem.setChildren(listChildren);
//        }
//        treeItemVoList.add(treeItem);
//    }
}
