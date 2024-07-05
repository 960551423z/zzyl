package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.NursingTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NursingTaskMapper {

    int deleteByExample(NursingTask example);

    int deleteByPrimaryKey(Long id);

    int insert(NursingTask record);

    int insertSelective(NursingTask record);


    NursingTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NursingTask record);

    @Update("update nursing_task set nursing_id = #{elderId} where elder_id = #{elderId} ")
    int updateByElderId(Long elderId, Long nursingId);

    List<NursingTask> selectByIds(List<Long> ids);

    int updateByPrimaryKey(NursingTask record);

    int batchInsert(@Param("list") List<NursingTask> list);

    Page<NursingTask> selectByParams(@Param("elderName") String elderName, @Param("nurseId") Long nurseId, @Param("projectId") Long projectId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") Integer status);

    void updateByBillNoSelective(@Param("roleIds") List<String> roleIds);
}