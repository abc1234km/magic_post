package com.university.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.zxing.WriterException;
import com.university.post.dataset.OrderDS;
import com.university.post.dataset.OrderStatisticDS;
import com.university.post.dto.CustomerDTO;
import com.university.post.dto.DeliveryPointDTO;
import com.university.post.dto.GatheringPointDTO;
import com.university.post.dto.OrderDTO;
import com.university.post.dto.OrderDeliveryDTO;
import com.university.post.enums.OrderStatus;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.Order;
import com.university.post.model.OrderDelivery;
import com.university.post.model.User;
import com.university.post.payload.request.order.OrderRequest;
import com.university.post.payload.response.data.DataRepsonse;
import com.university.post.payload.response.data.PageResponse;
import com.university.post.payload.response.statistic.OrderStatisticPointResponse;
import com.university.post.service.CloudinaryService;
import com.university.post.service.CustomerService;
import com.university.post.service.DeliveryPointService;
import com.university.post.service.OrderDeliveryService;
import com.university.post.service.OrderService;
import com.university.post.service.OrderStatsticsService;
import com.university.post.utils.CloudinaryUtils;
import com.university.post.utils.QRCodeGenerator;
import com.university.post.utils.Utils;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.io.IOException;
import java.util.function.Function;
import java.util.*;

@RestController
@RequestMapping("/api/v1/delivery-points")
@SecurityRequirement(name = "Xác thực Bearer")
public class DeliveryPointController {
    @Autowired
    private DeliveryPointService deliveryPointService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderDeliveryService orderDeliveryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderStatsticsService orderStatsticsService;

    @GetMapping("/orders")
    @ResponseStatus(HttpStatus.OK)
    public DataRepsonse<PageResponse> getAllOrders(OrderDS orderDS) {
        Page<OrderDelivery> orderDeliveryList = orderDeliveryService.getAllOrderDeliveryByPage(orderDS);
        Page<OrderDTO> orderDTOS = orderDeliveryList.map(new Function<OrderDelivery, OrderDTO>() {
            @Override
            public OrderDTO apply(OrderDelivery orderDelivery) {
                Integer deliveryStatus = orderDelivery.getStatus();
                OrderDTO orderDTO = new OrderDTO(orderDelivery.getOrder());
                if (deliveryStatus == OrderStatus.CONFIRM_SEND) {
                    orderDTO.setOrderDelivery(new OrderDeliveryDTO(orderDelivery));
                }
                return orderDTO;
            }
        });
        return new DataRepsonse<>(HttpStatus.OK, new PageResponse<>(orderDTOS),
                "Lấy thông tin các đơn hàng thành công");
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public DataRepsonse<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest)
            throws WriterException, IOException {
        Order order = orderService.addOrder(orderRequest);

        OrderDelivery orderDelivery = orderDeliveryService.addOrderDelivery(order);

        return new DataRepsonse<>(HttpStatus.CREATED, new OrderDTO(order), "Xác nhận đơn hàng thành công");
    }

    @GetMapping("/orders/{id}")
    public DataRepsonse<OrderDTO> getOrder(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        return new DataRepsonse<OrderDTO>(HttpStatus.OK, new OrderDTO(order), "Lấy thông tin đơn hàng thành công");
    }

    @PutMapping("/orders/{id}")
    public DataRepsonse<OrderDTO> updateOrder(@PathVariable String id, @RequestBody OrderRequest orderRequest) {
        Order order = orderService.updateOrder(id, orderRequest);
        return new DataRepsonse<>(HttpStatus.OK, new OrderDTO(order), "Cập nhật đơn hàng thành công");
    }

    @DeleteMapping("/orders/{id}")
    public DataRepsonse<String> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return new DataRepsonse<>(HttpStatus.OK, "", "Xóa đơn hàng thành công");
    }

    @GetMapping("order-statistics")
    public DataRepsonse<List> getOrderStatistics() {
        List<OrderStatisticPointResponse> orderStastics = orderStatsticsService.getOrderStastic(null, null);

        return new DataRepsonse<>(HttpStatus.OK,
                orderStastics, "Thống kê đơn hàng thành công");
    }
}
