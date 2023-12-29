package com.university.post.service;

import com.university.post.dataset.OrderDS;
import com.university.post.model.Customer;
import com.university.post.model.Order;
import com.university.post.payload.request.order.OrderRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Order addOrder(OrderRequest orderRequest);

    Order updateOrder(String id, OrderRequest orderRequest);

    void deleteOrder(String id);

    Order getOrderById(String id);

    Page<Order> getAllOrders(OrderDS orderDS);

    Order handleOrderRegister(OrderRequest orderRequest);
}
