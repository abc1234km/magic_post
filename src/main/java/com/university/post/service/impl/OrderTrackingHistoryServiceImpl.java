package com.university.post.service.impl;

import com.university.post.model.OrderDelivery;
import com.university.post.model.OrderTrackingHistory;
import com.university.post.repository.OrderTrackingHistoryRepository;
import com.university.post.service.OrderTrackingHistoryService;

import javax.validation.OverridesAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTrackingHistoryServiceImpl implements OrderTrackingHistoryService {

    @Autowired
    private OrderTrackingHistoryRepository orderTrackingHistoryRepository;

    @Override
    public void addOrderTrackingHistory(OrderTrackingHistory orderTrackingHistory) {
        orderTrackingHistoryRepository.save(orderTrackingHistory);
    }

    @Override
    public OrderTrackingHistory handleRegister(OrderDelivery orderDelivery) {
        return null;
    }

    @Override
    public List<OrderTrackingHistory> getAllMessageStatusByOrderId(String orderId) {
        return orderTrackingHistoryRepository.findMessageStatusByOrderId(orderId);
    }
}
