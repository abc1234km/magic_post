package com.university.post.service;

import com.google.zxing.WriterException;
import com.university.post.dataset.OrderDS;
import com.university.post.dataset.OrderStatisticDS;
import com.university.post.model.Order;
import com.university.post.model.OrderDelivery;
import com.university.post.payload.request.order.OrderDeliveryRequest;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

public interface OrderDeliveryService {
    OrderDelivery addOrderDelivery(Order order) throws WriterException, IOException;

    OrderDelivery handleRegister(Order order);

    void updateOrderStatus(Order order, Integer deliveryStatus);

    OrderDelivery orderConfirmFromPoint(Order order);

    OrderDelivery orderDeliveryToPoint(Order order, OrderDeliveryRequest orderDeliveryRequest);

    OrderDelivery orderConfirmLeft(Order order);

    OrderDelivery orderDeliveryToReceiver(Order order);

    OrderDelivery orderConfirmFromReceiver(Integer status, Order order);

    List<OrderDelivery> getAllOrdersByStatus(Integer status);

    List<OrderDelivery> getOrderStatisticByConditions(OrderStatisticDS orderStatisticDS);

    Page<OrderDelivery> getAllOrderDeliveryByPage(OrderDS orderDS);

    List<OrderDelivery> getAllOrderDelivery(OrderDS orderDS);
}
