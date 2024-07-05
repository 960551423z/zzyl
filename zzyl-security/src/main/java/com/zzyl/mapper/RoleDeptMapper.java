package com.zzyl.mapper;

import com.zzyl.entity.RoleDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDeptMapper {

    int insertSelective(RoleDept record);

    int updateByPrimaryKeySelective(RoleDept record);

    int batchInsert(@Param("list") List<RoleDept> list);

    int deleteRoleDeptByRoleId(@Param("roleId") Long roleId);

    int deleteRoleDeptInRoleId(@Param("roleIds") List<Long> roleIds);

    List<RoleDept> selectInRoleIds(@Param("roleIds") List<Long> roleIds);
}
