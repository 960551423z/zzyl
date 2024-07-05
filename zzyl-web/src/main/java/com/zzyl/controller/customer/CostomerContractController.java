
package com.zzyl.controller.customer;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zzyl.base.PageResponse;
import com.zzyl.base.ResponseResult;
import com.zzyl.service.ContractService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.ContractVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 合同 Controller
 */
@RestController
@RequestMapping("/customer/contract")
@Api(tags = "客户合同")
public class CostomerContractController {

    @Autowired
    private ContractService contractService;

    /**
     * 分页查询合同信息
     * @return 分页结果
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页查询合同信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "contractNo", value = "合同编号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "elderName", value = "老人姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "long", paramType = "query")
    })
    public ResponseResult<PageResponse<ContractVo>> selectByPage(Integer pageNum,
                                                                 Integer pageSize,
                                                                 String contractNo,
                                                                 String elderName,
                                                                 Integer status,
                                                                 Long startTime,
                                                                 Long endTime) {
        PageResponse<ContractVo> pageInfo = contractService.selectByPage(pageNum, pageSize, contractNo, elderName, status, ObjectUtil.isEmpty(startTime)? null : LocalDateTimeUtil.of(startTime), ObjectUtil.isEmpty(endTime)? null : LocalDateTimeUtil.of(endTime));
        return ResponseResult.success(pageInfo);
    }
}

