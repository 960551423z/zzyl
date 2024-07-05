package com.zzyl.mapper;

import com.zzyl.entity.AccraditationRecord;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 阿庆
 */
@Mapper
public interface AccraditationRecordMapper {


    @Select("select * from accraditation_record where bussniess_id = #{bussniessId} and type = #{type} order by step_no")
    public List<AccraditationRecord> getAccraditationRecordByBuisId(@Param("bussniessId")long bussniessId, @Param("type")Integer type);

    @Select("select * from accraditation_record where bussniess_id = #{bussniessId} and type = #{type} order by step_no desc limit 1")
    public AccraditationRecord getLastByBuisId(@Param("bussniessId")long bussniessId, @Param("type")Integer type);

    public void insert(AccraditationRecord accraditationRecord);

    @Delete("delete from accraditation_record where id =  #{id}")
    void delete(long id);

    @Select("select * from accraditation_record where bussniess_id = #{bussniessId} and type = #{type} order by step_no ASC limit 1")
    AccraditationRecord getFirstByBuisId(@Param("bussniessId")Long bussniessId, @Param("type")Integer type);
}
