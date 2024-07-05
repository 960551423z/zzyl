package com.zzyl.mapper;

import com.zzyl.dto.DeptDto;
import com.zzyl.entity.Dept;
import com.zzyl.vo.DeptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DeptMapper {

    int insert(Dept record);

    Dept selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Dept record);

    List<Dept> selectList(@Param("deptDto") DeptDto deptDto);

    List<Dept> findDeptInDeptNos(@Param("deptNos")List<String> deptNos);

    List<DeptVo> findDeptVoListInRoleId(@Param("roleIds")List<Long> roleIds);

    Dept selectByDeptNo(@Param("deptNo") String deptNo);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(@Param("deptId") String deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(@Param("deptId") String deptId);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteByDeptNo(@Param("deptId") String deptId);

    @Update("update sys_dept set leader_id = null where leader_id = #{leaderId} and dept_no != #{deptNo} ")
    void clearOtherDeptLeader(@Param("leaderId") Long leaderId,@Param("deptNo")String deptNo);
}
