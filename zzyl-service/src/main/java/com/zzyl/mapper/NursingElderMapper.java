package com.zzyl.mapper;

import com.zzyl.entity.NursingElder;
import com.zzyl.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NursingElderMapper {

    int deleteByPrimaryKey(Long id);

    int deleteByElderId(Long elderId);

    int deleteByElderIds(@Param("elderIds") Long[] elderIds);

    int insert(NursingElder record);

    int insertSelective(NursingElder record);

    List<NursingElder> selectByElderId(List<Long> list);

    NursingElder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NursingElder record);

    int updateByPrimaryKey(NursingElder record);

    int batchInsert(@Param("list") List<NursingElder> list);

    @Select("select tu.real_name from nursing_elder ne , sys_user tu where ne.elder_id = #{elderId} and tu.id = ne.nursing_id and tu.data_state = 0")
    List<String> selectNameByElderId( Long elderId);

    @Select("select tu.id as uid, tu.real_name from nursing_elder ne , sys_user tu where ne.elder_id = #{elderId} and tu.id = ne.nursing_id and tu.data_state = 0")
    List<UserVo> selectUserByElderId(Long elderId);
}