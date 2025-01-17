package com.imooc.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.ecommerce.dto.OrderInfoDTO;
import com.imooc.ecommerce.dto.OrderInfoDetailDTO;
import com.imooc.ecommerce.entity.OrderInfo;
import com.imooc.ecommerce.request.OrderFilterRequest;
import com.imooc.ecommerce.request.OrderRequest;
import com.imooc.ecommerce.request.OrderStatusRequest;
import com.imooc.ecommerce.service.IOrderInfoService;
import com.imooc.ecommerce.vo.CommonListResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yfk
 * @since 2022-08-14
 */
@RestController
@RequestMapping("/orderInfo")
public class OrderInfoController {

    @Resource
    private IOrderInfoService orderInfoService;

    /**
     * @Description: 获取订单列表
     * @Author: yfk
     * @Date:  2022-9-5
     * @param orderFilterRequest:
     * @return: com.imooc.ecommerce.vo.CommonListResponse
     **/
    @GetMapping("/orderList")
    public CommonListResponse getOrderList(OrderFilterRequest orderFilterRequest){
        List<OrderInfoDTO> orderInfoDTOS = orderInfoService.listOrderInfo(orderFilterRequest);
        return new CommonListResponse<>(orderInfoDTOS.size(), orderInfoDTOS);
    }

    /**
     * @Description: 获取订单详情
     * @Author: yfk
     * @Date: 2022-9-6
     * @param orderRequest:
     * @return: com.imooc.ecommerce.vo.CommonListResponse
     **/
    @GetMapping("/orderDetail")
    public OrderInfoDetailDTO getOrderDetail(OrderRequest orderRequest) {

        OrderInfoDetailDTO orderDetail = orderInfoService.getOrderDetail(orderRequest);
        return orderDetail;
    }

    /**
     * @Description: 更新订单状态
     * @Author: yfk
     * @Date: 2022-9-6
     * @param orderStatusRequest:
     * @return: void
     **/
    @PostMapping("/updateOrder")
    public void updateOrderInfo(@RequestBody OrderStatusRequest orderStatusRequest) {

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_sn",orderStatusRequest.getOrderSn());

        OrderInfo one = orderInfoService.getOne(queryWrapper);
        if (one == null) {
            throw new RuntimeException("不存在订单编号为" + orderStatusRequest.getOrderSn() + "的商品");
        }
        one.setStatus(orderStatusRequest.getStatus());
        orderInfoService.saveOrUpdate(one,queryWrapper);

    }
}
