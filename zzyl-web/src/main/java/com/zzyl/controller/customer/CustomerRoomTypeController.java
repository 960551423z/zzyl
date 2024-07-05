package com.zzyl.controller.customer;

import com.zzyl.base.ResponseResult;
import com.zzyl.controller.BaseController;
import com.zzyl.service.RoomTypeService;
import com.zzyl.vo.RoomTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer/roomTypes")
@Api(tags = "客户房型管理")
public class CustomerRoomTypeController extends BaseController {
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping
    @ApiOperation("根据状态查询房型")
    public ResponseResult<List<RoomTypeVo>> findRoomTypeListByStatus(@RequestParam("status") Integer status) {
        List<RoomTypeVo> roomTypeVoList = roomTypeService.findRoomTypeListByStatus(status);
        return success(roomTypeVoList);
    }

}
