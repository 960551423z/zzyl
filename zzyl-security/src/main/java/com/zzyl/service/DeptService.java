package com.zzyl.service;

import com.zzyl.dto.DeptDto;
import com.zzyl.vo.DeptVo;
import com.zzyl.vo.TreeVo;

import java.util.List;

/**
 * 部门表服务类
 */
public interface DeptService {

    /**
     *  创建部门表
     * @param deptDto 对象信息
     * @return DeptVo
     */
    Boolean createDept(DeptDto deptDto);

    /**
     *  修改部门表
     * @param deptDto 对象信息
     * @return Boolean
     */
    Boolean updateDept(DeptDto deptDto);

    /**
     *  多条件查询部门表列表
     * @param deptDto 查询条件
     * @return: List<DeptVo>
     */
    List<DeptVo> findDeptList(DeptDto deptDto);


    /**
     *  批量查詢部門
     * @param deptNos 查询条件
     * @return: TreeVo
     */
    List<DeptVo> findDeptInDeptNos(List<String> deptNos);

    /**
     *  创建编号
     * @param parentDeptNo 父部门编号
     * @return
     */
    String createDeptNo(String parentDeptNo);

    /***
     *  角色对应部门
     * @param roleIds
     * @return
     */
    List<DeptVo> findDeptVoListInRoleId(List<Long> roleIds);

    /**
     * 根据部门ID删除部门
     * @param deptId
     * @return
     */
    int deleteDeptById(String deptId);

    /**
     * 启用-禁用部门
     * @param deptDto
     * @return
     */
    Boolean isEnable(DeptDto deptDto);

    /**
     * 组织部门树形
     * @return: deptDto
     */
    TreeVo deptTreeVo();
}
