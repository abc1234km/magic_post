package com.university.post.service;

import com.university.post.model.OrderDelivery;
import com.university.post.model.OrderTrackingHistory;

import java.util.List;

public interface OrderTrackingHistoryService {
    void addOrderTrackingHistory(OrderTrackingHistory orderTrackingHistory);

    OrderTrackingHistory handleRegister(OrderDelivery orderDelivery);

    List<OrderTrackingHistory> getAllMessageStatusByOrderId(String orderId);
}
