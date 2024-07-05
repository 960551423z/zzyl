package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.dto.OrderDto;
import com.zzyl.entity.Order;
import com.zzyl.vo.OrderVo;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 订单服务接口
 */
public interface OrderService {
    /**
     * 创建订单
     * @param orderDto 订单信息
     * @return 订单信息
     */
    OrderVo createOrder(OrderDto orderDto);

    /**
     * 取消订单
     * @param orderId 订单ID
     * @param reason 取消原因
     * @param createType
     * @return 订单信息
     */
    OrderVo cancelOrder(Long orderId, String reason, Integer createType);

    /**
     * 执行订单
     * @param orderId 订单ID
     * @return 订单信息
     */
    OrderVo doOrder(Long orderId);

    /**
     * 执行订单
     * @param orderNo 订单编号
     * @return 订单信息
     */
    void doOrder(String orderNo);

    /**
     * 根据订单ID获取订单信息
     * @param orderId 订单ID
     * @return 订单信息
     */
    OrderVo getOrderById(Long orderId);

    /**
     * 搜索订单
     * @param status 订单状态
     * @param orderNo 订单编号
     * @param elderlyName 老人姓名
     * @param creator 创建人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param pageSize 每页数量
     * @return 订单信息列表
     */
    PageResponse<OrderVo> searchOrders(Integer status, String orderNo, String elderlyName, String creator, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer pageSize);

    /**
     * 根据客户id查询订单
     * @param id
     * @return
     */
    List<OrderVo> listByMemberId(Long id);

    /**
     * 保存订单
     * @param order
     */
    void save(Order order);

    /**
     * 根据状态查询订单
     * @param status
     * @return
     */
    List<Order> selectByStatus(Integer status);

    /**
     * 线上支付
     * @param tradingOrderNos 交易号
     */
    void payOrder(List<Long> tradingOrderNos);

    /**
     * 线上退款
     * @param tradingOrderNos 交易号
     */

    void refundOrder(List<Long> tradingOrderNos);

    /**
     * 根据老人id删除订单
     * @param orderId
     */
    void delete(Long orderId);

    /**
     * 查询订单状态，检查数据
     * @param orderDto
     * @return
     */
    OrderVo createOrderCheck(OrderDto orderDto);
}

