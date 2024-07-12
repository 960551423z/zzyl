package com.zzyl.mapper;


import com.zzyl.dto.DeptDto;
import com.zzyl.entity.Dept;
import com.zzyl.vo.DeptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeptMapper {


    /**
     * 部门列表
     *
     */
    List<DeptVo> getList(DeptDto deptDto);

    /**
     * 新增部门
     */
    int insert(Dept dept);

    /**
     * 部门修改
     */
    int update(Dept dept);

    /**
     * 部门删除
     */
    int delete(Long deptId);


    /**
     * 查询禁用状态下是否还有其他子节点
     */
    List<Dept> selectNode(String deptNo);

    @Select("select * from sys_dept where parent_dept_no = #{deptNo}")
    List<Dept> selectByDept(String deptNo);

    @Select("select * from sys_dept where id = #{deptId}")
    Dept selectByDeptId(Long deptId);


    /**
     * 根据部门id批量查询
     */
    List<Dept> selectByBatchDeptNos(@Param("deptNos") List<String> deptNos);
}
