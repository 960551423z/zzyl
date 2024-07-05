package com.zzyl.service;

import com.zzyl.base.PageResponse;
import com.zzyl.entity.Balance;
import com.zzyl.vo.BalanceVo;

import java.math.BigDecimal;

/**
* <p>
* balance Service 接口
* </p>
*
* @author 阿庆
*/
public interface BalanceService {

    /**
     * 根据老人id查询余额信息
     * @param elderId 老人id
     * @return 余额信息
     */
    Balance selectByElderId(Long elderId);

    /**
     * 保存余额信息
     * @param balance 余额信息
     */
    void save(Balance balance);

    /**
     * 分页查询余额信息
     * @param bedNo 床位号
     * @param elderName 老人姓名
     * @param pageNum 当前页码
     * @param pageSize 每页显示数量
     * @return 分页结果
     */
    PageResponse<BalanceVo> page(String bedNo, String elderName, Integer pageNum, Integer pageSize);


    /**
     * 扣除余额
     * @param elderId
     * @param deductions
     */
    void close(Long elderId, BigDecimal deductions);

}

