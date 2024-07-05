package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CheckInMapper {

    int deleteByPrimaryKey(Long id);

    int insert(CheckIn record);

    int insertSelective(CheckIn record);

    CheckIn selectByCheckInCode(String checkInCode);

    CheckIn selectByUserId(String userId);

    int updateByPrimaryKeySelective(CheckIn checkIn);

    int updateByPrimaryKeyWithBLOBs(CheckIn record);

    int updateByPrimaryKey(CheckIn record);

    int batchInsert(@Param("list") List<CheckIn> list);


    Page<List<CheckIn>> selectByPage(@Param("checkInCode")String checkInCode, @Param("name")String name, @Param("idCardNo")String idCardNo, @Param("startTime") LocalDateTime startTime, @Param("endTime")LocalDateTime endTime, @Param("applicatId") Long applicatId, @Param("deptNo") String deptNo);

    CheckIn selectByPrimaryKey(Long id);
}