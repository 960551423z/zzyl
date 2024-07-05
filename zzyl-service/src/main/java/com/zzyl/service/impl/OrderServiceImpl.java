
package com.zzyl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zzyl.base.PageResponse;
import com.zzyl.dto.BillDto;
import com.zzyl.dto.OrderDto;
import com.zzyl.entity.Bill;
import com.zzyl.entity.Member;
import com.zzyl.entity.NursingTask;
import com.zzyl.entity.Order;
import com.zzyl.enums.BillStatus;
import com.zzyl.enums.OrderStatus;
import com.zzyl.exception.BaseException;
import com.zzyl.handler.wechat.WechatWapPayHandler;
import com.zzyl.mapper.BillMapper;
import com.zzyl.mapper.NursingTaskMapper;
import com.zzyl.mapper.OrderMapper;
import com.zzyl.service.*;
import com.zzyl.utils.CodeUtil;
import com.zzyl.utils.UserThreadLocal;
import com.zzyl.vo.NursingProjectVo;
import com.zzyl.vo.OrderVo;
import com.zzyl.vo.TradingVo;
import com.zzyl.vo.retreat.ElderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Resource
    private WechatWapPayHandler wechatWapPayHandler;

    @Resource
    private MemberService memberService;

    @Resource
    private BillService billService;

    @Resource
    private BillMapper billMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private NursingTaskService nursingTaskService;

    /*@Resource
    private NursingProjectService nursingProjectService;*/

    @Resource
    private ElderService elderService;

    /**
     * 创建订单
     * @param orderDto 订单dto
     * @return 订单vo
     */
    @Transactional
    @Override
    public OrderVo createOrder(OrderDto orderDto) {
        ElderVo elderVo = elderService.selectByPrimaryKey(orderDto.getElderId());
        if (elderVo.getStatus() == 3) {
            throw new BaseException("退住中，不可下单");
        }
        if (elderVo.getStatus() == 5) {
            throw new BaseException("已退住，不可下单");
        }

        Order order = BeanUtil.toBean(orderDto, Order.class);
        order.setStatus(OrderStatus.PENDING_PAY.getCode());
        String dd = CodeUtil.generateCode("DD", stringRedisTemplate, 5);
        order.setOrderNo(dd);
        Long userId = UserThreadLocal.getUserId();
        order.setPaymentStatus(1);
        order.setMemberId(userId);
        orderMapper.insert(order);

        TradingVo tradingVo = new TradingVo();
        Member member = memberService.getById(userId);
        tradingVo.setOpenId(member.getOpenId());
        tradingVo.setMemo("服务下单");
        tradingVo.setTradingAmount(order.getAmount());
        tradingVo.setProductOrderNo(order.getId());
        tradingVo.setTradingType("2");
        TradingVo tradingVo1 = wechatWapPayHandler.wapTrading(tradingVo);

        order.setTradingOrderNo(tradingVo1.getTradingOrderNo());
        orderMapper.updateByPrimaryKey(order);

        BillDto billDto = new BillDto();
        billDto.setBillAmount(order.getAmount());
        billDto.setPayableAmount(order.getAmount());
        billDto.setTradingOrderNo(tradingVo1.getTradingOrderNo());
        billDto.setElderId(order.getElderId());
        //TODO 护理项目开发完成后调用
//        NursingProjectVo byId = nursingProjectService.getById(order.getProjectId());
//        billDto.setRemark(byId.getName());
        billService.createProjectBill(billDto);

        OrderVo orderVo = BeanUtil.toBean(order, OrderVo.class);
        orderVo.setTradingVo(tradingVo1);
        return orderVo;
    }

    /**
     * 取消订单
     * @param orderId 订单id
     * @param reason 取消原因
     * @param createType
     * @return 订单vo
     */
    @Override
    public OrderVo cancelOrder(Long orderId, String reason, Integer createType) {

        Order order1 = orderMapper.selectByPrimaryKey(orderId);
        if (order1.getStatus().equals(OrderStatus.CLOSE.getCode())) {
            throw new BaseException("订单已关闭");
        }

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CLOSE.getCode());
        order.setReason(reason);
        order.setPaymentStatus(3);
        order.setCreateType(createType);
        orderMapper.updateByPrimaryKeySelective(order);

        BillDto billDto = new BillDto();
        billDto.setTradingOrderNo(order1.getTradingOrderNo());
        billDto.setTransactionStatus(BillStatus.CLOSE.getOrdinal());
        billService.updateBytradingOrderNoSelective(billDto);

        TradingVo tradingVo = new TradingVo();
        tradingVo.setProductOrderNo(order1.getId());
        tradingVo.setTradingOrderNo(order1.getTradingOrderNo());
        tradingVo.setEnterpriseId(1561414331L);
        wechatWapPayHandler.closeTrading(tradingVo);
        return BeanUtil.toBean(order1, OrderVo.class);
    }

    @Override
    public void doOrder(String orderNo) {
        orderMapper.updateStatusBycode(OrderStatus.DONE.getCode(), orderNo);
    }

    @Override
    public OrderVo doOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.DONE.getCode());
        orderMapper.updateByPrimaryKeySelective(order);
        return BeanUtil.toBean(order, OrderVo.class);
    }
    /**
     * 根据订单id获取订单详情
     * @param orderId 订单id
     * @return 订单vo
     */
    @Override
    public OrderVo getOrderById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        return BeanUtil.toBean(order, OrderVo.class);
    }

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
     * @return 订单分页vo
     */
    @Override
    public PageResponse<OrderVo> searchOrders(Integer status, String orderNo, String elderlyName, String creator, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Long userId = UserThreadLocal.getUserId();
        Page<List<Order>> lists =
                orderMapper.searchOrders(status, orderNo, elderlyName, creator, startTime, endTime, userId);
        return PageResponse.of(lists, OrderVo.class);
    }

    @Override
    public List<OrderVo> listByMemberId(Long id) {
        return orderMapper.listByMemberId(id);
    }

    @Override
    public void save(Order order) {
        orderMapper.updateByPrimaryKey(order);
    }

    @Override
    public List<Order> selectByStatus(Integer status) {
        return orderMapper.selectByStatus(status);
    }


    /**
     * 线上支付
     * @param tradingOrderNos 交易号
     */
    @Override
    public void payOrder(List<Long> tradingOrderNos) {
        orderMapper.batchUpdateByTradingOrderNoSelective(tradingOrderNos, OrderStatus.PENDING_DO.getCode());
        //  生成护理任务 创建时间是支付完成时间
        List<Order> orders = orderMapper.selectByTradingOrderNo(tradingOrderNos);
        List<Bill> bills = billMapper.selectBytradingOrderNo(tradingOrderNos);
        Map<Long, String> billMap = bills.stream().collect(Collectors.toMap(Bill::getTradingOrderNo, Bill::getBillNo));
        List<NursingTask> nursingTasks = new ArrayList<>();
        orders.forEach(v -> {
            NursingTask nursingTask = BeanUtil.toBean(v, NursingTask.class);
            nursingTask.setStatus(1);
            nursingTask.setTaskType((byte) 1);
            nursingTask.setRelNo(billMap.get(v.getTradingOrderNo()));
            nursingTask.setEstimatedServerTime(v.getEstimatedArrivalTime());
            nursingTask.setCreateTime(v.getUpdateTime());
            nursingTask.setCreateBy(v.getCreateBy());
            nursingTask.setRemark(v.getRemark());
            nursingTasks.add(nursingTask);
        });
        if (CollUtil.isEmpty(nursingTasks)) {
            return;
        }
        nursingTaskService.batchInsert(nursingTasks);
    }

    @Resource
    private NursingTaskMapper nursingTaskMapper;

    /**
     * 线上退款
     * @param tradingOrderNos 交易号
     */
    @Override
    public void refundOrder(List<Long> tradingOrderNos) {
        orderMapper.batchUpdateByTradingOrderNoSelective(tradingOrderNos, OrderStatus.REFUND.getCode());

        List<Bill> bills = billMapper.selectBytradingOrderNo(tradingOrderNos);
        List<String> collect = bills.stream().map(Bill::getBillNo).collect(Collectors.toList());
        nursingTaskMapper.updateByBillNoSelective(collect);
    }

    @Override
    public void delete(Long orderId) {
        orderMapper.deleteByPrimaryKey(orderId);
    }

    @Override
    public OrderVo createOrderCheck(OrderDto orderDto) {
        ElderVo elderVo = elderService.selectByPrimaryKey(orderDto.getElderId());
        if (elderVo.getStatus() == 3) {
            throw new BaseException("退住中，不可下单");
        }
        if (elderVo.getStatus() == 5) {
            throw new BaseException("已退住，不可下单");
        }
        return null;
    }
}

