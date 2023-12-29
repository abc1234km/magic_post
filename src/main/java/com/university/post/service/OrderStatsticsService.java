package com.university.post.service;

import java.util.List;

import com.university.post.dataset.OrderStatisticDS;
import com.university.post.payload.response.statistic.OrderStatisticPointResponse;

public interface OrderStatsticsService {
    void getOrderStastisByOrderDelivery(OrderStatisticDS orderStatisticDS);
    List<OrderStatisticPointResponse> getOrderStastic(String typePoint, String pointId);
}
