
package com.zzyl.mapper;

import com.github.pagehelper.Page;
import com.zzyl.entity.Order;
import com.zzyl.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    // 根据主键删除订单
    int deleteByPrimaryKey(Long id);

    // 插入订单
    int insert(Order record);

    // 选择性地插入订单
    int insertSelective(Order record);

    // 根据主键选择订单
    Order selectByPrimaryKey(Long id);

    // 选择性地更新订单
    int updateByPrimaryKeySelective(Order record);

    // 选择性地更新订单
    @Update("update `order` set status = #{status} where trading_order_no = (select trading_order_no from bill where bill_no = #{orderNo})")
    int updateStatusBycode(@Param("status") Integer status, @Param("orderNo") String orderNo);

    // 更新订单
    int updateByPrimaryKey(Order record);


    /**
     * 搜索订单
     * @param status 订单状态
     * @param orderNo 交易订单号
     * @param elderlyName 老人姓名
     * @param creator 创建者
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param userId
     * @return 订单列表
     */
    Page<List<Order>> searchOrders(@Param("status") Integer status, @Param("orderNo") String orderNo, @Param("elderlyName") String elderlyName, @Param("creator") String creator, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,  @Param("userId")Long userId);

    /**
     * 根据会员ID查询订单
     * @param memberId 会员ID
     * @return 订单列表
     */
    List<OrderVo> listByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据状态查询订单
     * @param status 订单状态
     * @return 订单列表
     */
    List<Order> selectByStatus(@Param("status")Integer status);

    /**
     * 批量选择性地更新订单
     * @param list 订单号列表
     * @param status 订单状态
     * @return 更新数量
     */
    int batchUpdateByTradingOrderNoSelective(@Param("list")List<Long> list, @Param("status")int status);

    List<Order> selectByTradingOrderNo(@Param("list")List<Long> list);
}


