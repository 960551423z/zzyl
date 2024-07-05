package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.dto.UserDto;
import com.zzyl.entity.User;
import com.zzyl.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User record);

    int insertSelective(User record);

    int updateByPrimaryKeySelective(User record);

    Page<List<User>> selectPage(@Param("userDto") UserDto userDto);

    List<User> selectList(@Param("userVo") UserVo userVo);

    List<UserVo> findUserVoListByDeptNo(@Param("deptNo") String deptNo);

    List<UserVo> findUserVoListByPostNo(@Param("postNo") String postNo);

    List<UserVo> findUserVoListByRoleId(@Param("roleId") Long roleId);

    User findUserVoForLogin(@Param("userVo") UserVo userVo);

    int deleteUserByIds(@Param("userIds")List<Long> userIds);

    List<User> selectUserByIds(@Param("userIds")List<Long> userIds);

    @Select("select id from sys_user where dept_no = #{deptCode}")
    List<Long> selectByDeptNo(String deptCode);

    @Select("select * from sys_user where dept_no = #{deptNo} and is_leader = 1")
    User selectLeaderByDeptNo(String deptNo);

    @Update("update sys_user set is_leader = 0 where id = #{id}")
    void clearIsLeader(Long id);

    @Update("update sys_user set is_leader =  1 where id = #{leaderId} and dept_no = #{deptNo}")
    void updateByUserIdAndLeaderId(@Param("leaderId")Long leaderId,@Param("deptNo")String deptNo);


}
