package com.zzyl.client.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PagePayResponse.java
 */
@Data
@NoArgsConstructor
public class WapPayResponse extends BasicResponse {

    @JSONField(name="prepay_id")
    private String prepayId;

}
