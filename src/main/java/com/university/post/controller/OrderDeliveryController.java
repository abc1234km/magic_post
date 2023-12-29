package com.university.post.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.university.post.dto.OrderDTO;
import com.university.post.model.Order;
import com.university.post.model.OrderDelivery;
import com.university.post.payload.request.order.OrderDeliveryRequest;
import com.university.post.payload.response.data.DataRepsonse;
import com.university.post.service.OrderDeliveryService;
import com.university.post.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/order-delivery")
@SecurityRequirement(name = "Xác thực Bearer")
public class OrderDeliveryController {
    @Autowired
    private OrderDeliveryService orderDeliveryService;

    @Autowired
    private OrderService orderService;
    
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public DataRepsonse<List> updateOrderDelivery(@RequestBody OrderDeliveryRequest orderDeliveryRequest) {
        List<String> orderList = Arrays.asList(orderDeliveryRequest.getOrderIds());
        List<OrderDTO> orders = new ArrayList<>();
        orderList.stream().forEach(orderId -> {
            Order order = orderService.getOrderById(orderId);
            orderDeliveryService.updateOrderStatus(order, orderDeliveryRequest.getDeliveryStatus());
            orders.add(new OrderDTO(order));
        });

        return new DataRepsonse<>(HttpStatus.OK, orders, "Cập nhật đơn hàng thành công");
    }
}
