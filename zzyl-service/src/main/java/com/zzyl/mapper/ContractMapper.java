/**
 * ContractMapper接口
 */
package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.Contract;
import com.zzyl.vo.ContractVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ContractMapper {
    /**
     * 根据id删除合同
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入合同
     */
    int insert(Contract record);

    /**
     * 选择性插入合同
     */
    int insertSelective(Contract record);

    /**
     * 根据id选择合同
     */
    Contract selectByPrimaryKey(Long id);

    /**
     * 选择性更新合同
     */
    int updateByPrimaryKeySelective(Contract record);

    /**
     * 选择性更新合同
     */
    int batchUpdateByPrimaryKeySelective(@Param("ids") List<Long> ids, @Param("status")Integer status);

    /**
     * 更新合同
     */
    int updateByPrimaryKey(Contract record);




    /**
     * 分页查询合同信息
     *
     * @param contractNo 合同编号
     * @param elderName 老人姓名
     * @param status 合同状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    Page<List<Contract>> selectByPage(@Param("memberPhone") String memberPhone, @Param("contractNo") String contractNo, @Param("elderName") String elderName,
                                      @Param("status") Integer status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    List<Contract> listAllContracts();

    List<ContractVo> listByMemberPhone(@Param("memberPhone") String memberPhone);

    @Update("update contract set status = #{status} where elder_id = #{elderId}")
    void updateStatusByElderId(@Param("elderId") Long elderId,@Param("status")Integer status);


    @Update("update contract set status = #{status} where elder_id = #{elderId}")
    void updateByElderId(@Param("elderId") Long elderId,@Param("status")Integer status);

    @Select("select * from contract where elder_id = #{elderId}")
    Contract selectByElderId(@Param("elderId") Long elderId);
}

