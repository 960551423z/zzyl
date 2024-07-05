package com.zzyl.mapper;

import com.zzyl.dto.ResourceDto;
import com.zzyl.entity.Resource;
import com.zzyl.vo.ResourceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceMapper {

    int insert(Resource record);

    int insertSelective(Resource record);

    int updateByPrimaryKeySelective(Resource record);

    List<Resource> selectList(@Param("resourceDto") ResourceDto resourceDto);

    List<ResourceVo> findResourceVoListInRoleId(@Param("roleIds") List<Long> roleIds);

    List<ResourceVo> findButtonVoListInRoleId(@Param("roleIds") List<Long> roleIds);

    List<ResourceVo> findResourceVoListByUserId(@Param("userId") Long userId);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int hasChildByMenuId(@Param("menuId") String menuId);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int deleteMenuById(@Param("menuId") String menuId);
}
