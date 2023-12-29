package com.university.post.service.impl;

import com.university.post.dataset.OrderDS;
import com.university.post.enums.Type;
import com.university.post.exception.entity.EntityNotFound;
import com.university.post.exception.user.UserNotFound;
import com.university.post.model.Customer;
import com.university.post.model.Order;
import com.university.post.payload.request.order.OrderRequest;
import com.university.post.repository.OrderRepository;
import com.university.post.service.CustomerService;
import com.university.post.service.OrderService;
import com.university.post.specification.OrderSpecification;
import com.university.post.specification.UserSpecification;
import com.university.post.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Override
    @Transactional
    public Order addOrder(OrderRequest orderRequest) {
        Order order = new Order();
        Long nextId = orderRepository.getNextId();
        String nowDate = LocalDate.now().toString();
        nowDate = nowDate.replaceAll("-", "");
        Long infixOrder = Long.valueOf(nowDate);
        String orderId = Type.PREFIX_ORDER + infixOrder + nextId + Type.POSTFIX_ORDER;

        order.setOrderId(orderId);
        Customer sender = customerService.handleCustomerRegister(orderRequest.getSenderCustomer(),
                Type.SENDER_CUSTOMER);

        Customer receiver = customerService.handleCustomerRegister(orderRequest.getReceiverCustomer(),
                Type.RECEIVER_CUSTOMER_LOCATION);

        order.setSender(sender);
        order.setReceiver(receiver);

        order.setCategority(orderRequest.getCategority());
        order.setContextOrders(orderRequest.getContextOrders());
        // order.setCodCharge(orderRequest.getCodCharge());
        // order.setGTGTCharge(orderRequest.getGTGTCharge());
        Long distance = Utils.getLocationDistance(sender.getAddress(), receiver.getAddress());
        Long mainCharge = Math.floorDiv(distance, 20) * 9000;
        order.setMainCharge(mainCharge);
        // order.setOtherCharge(orderRequest.getOtherCharge());
        // order.setOtherReceiverCharge(orderRequest.getOtherReceiverCharge());
        // order.setSender(senderCustomer);
        // order.setReceiver(receiverCustomer);
        // order.setTempCharge(orderRequest.getTempCharge());
        // order.setVatCharge(orderRequest.getVatCharge());
        order.setWeight(orderRequest.getWeight());
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(String id, OrderRequest orderRequest) {
        Order order = getOrderById(id);
        order.setCategority(orderRequest.getCategority());
        order.setContextOrders(orderRequest.getContextOrders());
        order.setWeight(orderRequest.getWeight());

        Customer sender = order.getSender();
        sender = customerService.updateCustomer(sender, orderRequest.getSenderCustomer(), Type.SENDER_CUSTOMER);
        order.setSender(sender);

        Customer receiver = order.getReceiver();
        receiver = customerService.updateCustomer(receiver, orderRequest.getReceiverCustomer(),
                Type.RECEIVER_CUSTOMER_LOCATION);
        order.setReceiver(receiver);

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Không tìm thấy đơn hàng với id " + id));
    }

    @Override
    public Page<Order> getAllOrders(OrderDS orderDS) {
        return orderRepository.findAll(OrderSpecification.getAllOrderByConditions(orderDS),
                PageRequest.of(orderDS.getPageNumber() - 1, orderDS.getPageSize(), Sort.by("createAt").descending()));
    }

    @Override
    public Order handleOrderRegister(OrderRequest orderRequest) {
        Order order = new Order();
        Long CurrentLocationId = orderRepository.findMaxOrderId();
        if (CurrentLocationId != null) {
            // order.setOrderId(CurrentLocationId + 1);
        }
        // Customer sender =
        // customerService.handleCustomerRegister(orderRequest.getSenderCustomer());

        // Customer receiver =
        // customerService.handleCustomerRegister(orderRequest.getReceiverCustomer());

        // order.setSender(sender);
        // order.setReceiver(receiver);

        order.setCategority(orderRequest.getCategority());
        order.setContextOrders(orderRequest.getContextOrders());
        // order.setCodCharge(orderRequest.getCodCharge());
        // order.setGTGTCharge(orderRequest.getGTGTCharge());
        // order.setMainCharge(orderRequest.getMainCharge());
        // order.setOtherCharge(orderRequest.getOtherCharge());
        // order.setOtherReceiverCharge(orderRequest.getOtherReceiverCharge());
        // order.setSender(senderCustomer);
        // order.setReceiver(receiverCustomer);
        // order.setTempCharge(orderRequest.getTempCharge());
        // order.setVatCharge(orderRequest.getVatCharge());
        order.setWeight(orderRequest.getWeight());
        return order;
    }
}
