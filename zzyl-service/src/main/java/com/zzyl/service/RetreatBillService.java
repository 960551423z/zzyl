package com.zzyl.service;

import com.zzyl.base.ResponseResult;
import com.zzyl.dto.RefundVoucherDto;

public interface RetreatBillService {

    /**
     * 上传退款凭证
     * @param refundVoucherDto
     * @return
     */
    ResponseResult uploadRefundVoucher(RefundVoucherDto refundVoucherDto);

    /**
     * 查询退住账单数据
     * @param retreatId
     * @return
     */
    ResponseResult getRetreatBill(Long retreatId);
}
