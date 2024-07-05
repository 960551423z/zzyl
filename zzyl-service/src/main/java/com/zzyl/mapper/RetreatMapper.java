package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.dto.RetreatReqDto;
import com.zzyl.entity.Retreat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 阿庆
 */
@Mapper
public interface RetreatMapper {

    void createRetreat(Retreat retreat);

    @Select("select * from retreat where retreat_code = #{code}")
    Retreat getRetreatByCode(String code);

    @Update("update retreat set flow_status = #{flowStatus} where id = #{id} ")
    void updateRetreatByFlowStatus(@Param("id") long id, @Param("flowStatus") Integer flowStatus);

    @Update("update retreat set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id,@Param("status") Integer status);

    void update(Retreat retreat);

    Page<List<Retreat>> selectByPage(RetreatReqDto retreatReqDto);

    @Select("select * from retreat where elder_id = #{elderId} and status = 1")
    Retreat selectByElderId(Long elderId);

    @Select("select * from retreat where retreat_code = #{code} and status = 1")
    Retreat selectByCode(String code);

}
