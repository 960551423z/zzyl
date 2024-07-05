package com.zzyl.client.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AmountResponse.java
 *  金额信息
 */
@Data
@NoArgsConstructor
public class AmountResponse {

    //订单总金额【分】
    private Integer total;

    //退款总金额【分】
    private Integer refund;
}
