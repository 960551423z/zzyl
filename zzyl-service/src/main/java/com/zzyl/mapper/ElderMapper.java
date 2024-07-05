
package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.Elder;
import com.zzyl.vo.ChoiceElderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 老人Mapper接口
 */
@Mapper
public interface ElderMapper {

    /**
     * 根据主键删除老人信息
     * @param id 主键
     * @return 删除结果
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入老人信息
     * @param record 老人信息
     * @return 插入结果
     */
    int insert(Elder record);

    /**
     * 选择性插入老人信息
     * @param record 老人信息
     * @return 插入结果
     */
    int insertSelective(Elder record);

    /**
     * 根据主键选择老人信息
     * @param id 主键
     * @return 老人信息
     */
    Elder selectByPrimaryKey(Long id);

    /**
     * 根据主键选择性更新老人信息
     * @param record 老人信息
     * @return 更新结果
     */
    int updateByPrimaryKeySelective(Elder record);

    /**
     * 根据主键更新老人信息
     * @param record 老人信息
     * @return 更新结果
     */
    int updateByPrimaryKey(Elder record);



    /**
     * 根据身份证号和姓名选择老人信息
     * @param idCard 身份证号
     * @param name 姓名
     * @return 老人信息
     */
    Elder selectByIdCardAndName(@Param("idCard") String idCard, @Param("name") String name);

    List<Elder> selectList();

    List<Elder> selectByIds(List<Long> ids);

    Page<List<ChoiceElderVo>> selectListByPage(@Param("name") String name, @Param("idCardNo") String idCardNo, @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    Elder selectByIdCard(String idCard);

    @Update("UPDATE elder SET bed_number = null ,bed_id = null WHERE id = #{elderId}")
    void clearBedNum(Long elderId);
}

