package com.zzyl.dto;

import com.zzyl.base.BaseDto;
import com.zzyl.vo.retreat.DueBack;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RetreatClearingBillDto extends BaseDto {

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 实际退住时间
     */
    private LocalDateTime localDateTime;

    /**
     * 押金扣减金额 （元）
     */
    private BigDecimal depositDeductions;

    /**
     * 账单扣减金额  （元）
     */
    private BigDecimal billDeductions;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 操作人id
     */
    private Long userId;

    /**
     * 退住天数
     */
    private Integer day;

    /**
     * 账单备注
     */
    private String remark;

    /**
     * 押金备注
     */
    private String depositRemark;

    private List<DueBack> dueBackList;

}
