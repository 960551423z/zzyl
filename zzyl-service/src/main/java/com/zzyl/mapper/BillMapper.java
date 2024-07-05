package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.Bill;
import com.zzyl.vo.BillVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * BillMapper
 * 账单Mapper
 */
@Mapper
public interface BillMapper {
    /**
     * 根据主键删除
     * @param id 主键
     * @return 删除结果
     */
    int deleteByElderId(Long id);

    /**
     * 插入账单
     * @param bill 账单实体
     * @return 插入结果
     */
    int insert(Bill bill);

    /**
     * 选择性插入账单
     * @param bill 账单实体
     * @return 插入结果
     */
    int insertSelective(Bill bill);

    /**
     * 根据主键选择账单
     * @param id 主键
     * @return 账单实体
     */
    Bill selectByPrimaryKey(Long id);

    /**
     * 选择账单
     * @param list
     * @return 账单实体
     */
    List<Bill> selectBytradingOrderNo(@Param("list")List<Long> list);

    /**
     * 选择性更新账单
     * @param bill 账单实体
     * @return 更新结果
     */
    int updateBytradingOrderNoSelective(Bill bill);


    /**
     * 选择性更新账单
     *
     * @param bill 账单实体
     * @return 更新结果
     */
    int updateByIdSelective(Bill bill);

    /**
     * 更新账单
     * @param record 账单实体
     * @return 更新结果
     */
    int updateByPrimaryKey(Bill record);

    @Select("select * from bill where elder_id = #{elderId} and bill_month = #{month} and bill_type = 0")
    Bill selectByElderAndMonth(@Param("elderId") Long elderId, @Param("month")String month);

    @Select("select * from bill where elder_id = #{elderId} and bill_type = 0 order by  bill_end_time desc limit 1")
    Bill selectLastByElder(@Param("elderId") Long elderId);

    @Select("select * from bill where elder_id = #{elderId} and bill_type = 0 order by  bill_end_time ASC limit 1")
    Bill selectFirstByElder(@Param("elderId") Long elderId);

    @Select("select * from bill where elder_id = #{elderId} and transaction_status >= #{status} and deposit_amount > 0 and bill_type = 0 limit 1 ")
    Bill selectDepositByElderAndStatus(@Param("elderId")Long elderId, @Param("status")int status);


    List<Bill> selectDepositByEldersAndStatus(@Param("elderIds")List<Long> elderIds, @Param("status")int status);

    /**
     * 查询应退 （账单）
     * @param elderId
     * @param status
     * @param time
     * @return
     */
    @Select("select * from bill where elder_id = #{elderId} and bill_type = 0 and transaction_status = #{status} and bill_end_time > #{time} ORDER BY create_time DESC ")
    List<Bill> selectDueBackByElder(@Param("elderId")Long elderId, @Param("status")int status, @Param("time") LocalDateTime time);


    /**
     * 查询应退 （订单）
     * @param elderId
     * @param status
    * @return
     */
    @Select("select b.* from bill b left join `order` o  on o.trading_order_no = b.trading_order_no where b.elder_id = #{elderId} and o.status = 1 and b.bill_type = 1 and b.transaction_status = #{status} ORDER BY b.create_time DESC ")
    List<Bill> selectOrderDueBackByElder(@Param("elderId")Long elderId, @Param("status")int status);

    /**
     * 查询欠费 (月度)
     * @param elderId
     * @param status
     * @param time
     * @return
     */
    @Select("select * from bill where elder_id = #{elderId} and transaction_status = #{status} and bill_type = 0 and bill_start_time < #{time} ORDER BY id asc ")
    List<Bill> selectArrearageByElder(@Param("elderId")Long elderId, @Param("status")int status, @Param("time") LocalDateTime time);


    /**
     * 查询未缴 （订单）
     * @param elderId
     * @param status
     * @return
     */
    @Select("select * from bill where elder_id = #{elderId} and transaction_status = #{status} and bill_type = 1 ORDER BY create_time DESC ")
    List<Bill> selectUnpaidByElder(@Param("elderId")Long elderId, @Param("status")int status);

    /**
     * 分页查询账单
     *
     * @param billNo      账单编号
     * @param elderName   老人姓名
     * @param elderIdCard 老人身份证号
     * @param startTime   账单月份
     * @return 分页结果
     */
    Page<BillVo> page(@Param("billNo")String billNo, @Param("elderName")String elderName, @Param("elderIdCard")String elderIdCard, @Param("startTime")LocalDateTime startTime, @Param("endTime")LocalDateTime endTime, @Param("transactionStatus")Integer transactionStatus, @Param("elderIds")List<Long> elderIds);

    /**
     * 分页查询欠费账单
     *
     * @param bedNo     床位号
     * @param elderName 老人姓名
     * @return 分页结果
     */
    Page<BillVo> arrears(@Param("bedNo")String bedNo, @Param("elderName")String elderName);

    /**
     * 批量选择性更新账单
     * @param list 账单主键列表
     * @param transactionStatus 交易状态
     * @return 更新结果
     */
    int batchUpdateByTradingOrderNoSelective(@Param("list")List<Long> list, @Param("transactionStatus")int transactionStatus);

    /**
     * 批量选择性更新账单
     * @param list 账单主键列表
     * @param transactionStatus 交易状态
     * @return 更新结果
     */
    int obatchUpdateByTradingOrderNoSelective(@Param("list")List<Long> list, @Param("transactionStatus")int transactionStatus, @Param("amount")BigDecimal amount);

    @Update("update bill set transaction_status = 2 where elder_id = #{elderId} and transaction_status = 0")
    void close(@Param("elderId")Long elderId);


    @Update("delete  from bill where elder_id = #{elderId} and bill_start_time > #{time}")
    void delete(@Param("elderId")Long elderId, @Param("time")LocalDateTime time);


    @Select("select * from bill where bill_no = #{relNo} and bill_type = 1 order by  bill_end_time desc limit 1")
    Bill selectByBillNo(@Param("relNo")String relNo);
}

