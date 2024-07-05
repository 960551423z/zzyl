package com.zzyl.mapper;

import com.zzyl.entity.RoleResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleResourceMapper {

    int insertSelective(RoleResource record);

    int updateByPrimaryKeySelective(RoleResource record);

    int batchInsert(@Param("list") List<RoleResource> list);

    int deleteRoleResourceByRoleId(@Param("roleId")Long roleId);

    int deleteRoleResourceInRoleId(@Param("roleIds")List<Long> roleIds);

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int checkMenuExistRole(@Param("menuId")String menuId);
}
