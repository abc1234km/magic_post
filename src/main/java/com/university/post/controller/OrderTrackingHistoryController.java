package com.university.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.university.post.payload.response.data.DataRepsonse;
import com.university.post.service.OrderTrackingHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-status")
public class OrderTrackingHistoryController {

    @Autowired
    private OrderTrackingHistoryService orderTrackingHistoryService;

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public DataRepsonse<List> getOrderStatus(@PathVariable String orderId) {
        return new DataRepsonse<List>(HttpStatus.OK, orderTrackingHistoryService.getAllMessageStatusByOrderId(orderId),
                "Lấy thông tin trạng thái đơn hàng thành công");
    }
}
