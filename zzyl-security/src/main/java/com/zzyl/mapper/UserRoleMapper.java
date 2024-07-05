package com.zzyl.mapper;

import com.zzyl.entity.UserRole;
import com.zzyl.vo.UserRoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    int insertSelective(UserRole record);

    int updateByPrimaryKeySelective(UserRole record);

    int batchInsert(@Param("list") List<UserRole> list);

    int deleteUserRoleByUserId(Long userId);

    int deleteUserRoleInUserId(@Param("userIds") List<Long> userIds);

    int countUserRoleByRoleId(Long roleId);

    @Select("select tu.id,tu.real_name userName,tr.role_name roleName from sys_user tu ,sys_user_role tur ,sys_role tr\n" +
            "where tu.id = tur.user_id and tr.id = tur.role_id\n" +
            "and  tu.id = #{userId}")
    List<UserRoleVo> selectByUserId(@Param("userId")Long userId);
}
