package com.university.post.service.impl;

import com.google.zxing.WriterException;
import com.university.post.dataset.OrderDS;
import com.university.post.dataset.OrderStatisticDS;
import com.university.post.enums.MessageResponse;
import com.university.post.enums.OrderStatus;
import com.university.post.enums.OrderTrackingStatus;
import com.university.post.enums.Status;
import com.university.post.enums.Type;
import com.university.post.exception.entity.EntityException;
import com.university.post.exception.entity.EntityNotFound;
import com.university.post.model.*;
import com.university.post.payload.request.order.OrderDeliveryRequest;
import com.university.post.repository.OrderDeliveryRepository;
import com.university.post.service.*;
import com.university.post.specification.OrderDeliverySpecification;
import com.university.post.utils.Constant;
import com.university.post.utils.QRCodeGenerator;
import com.university.post.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class OrderDeliveryServiceImpl implements OrderDeliveryService {

    @Autowired
    private OrderDeliveryRepository orderDeliveryRepository;

    @Autowired
    private OrderTrackingHistoryService orderTrackingHistoryService;

    @Autowired
    private DeliveryPointService deliveryPointService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public OrderDelivery addOrderDelivery(Order order) throws WriterException, IOException {
        OrderDelivery orderDelivery = new OrderDelivery();
        orderDelivery.setOrder(order);
        orderDelivery.setFromLocation(order.getSender());

        User user = Utils.getUserAuthentication();
        orderDelivery.setCurrentLocation(user.getWorkAt());

        orderDelivery.setStatus(OrderStatus.NEW_ORDER);
        orderDelivery.setTypeOrder(Type.CUSTOMER_ORDER);

        order.setOrderDelivery(orderDelivery);
        byte[] files = QRCodeGenerator.getQRCodeImage("https://magic-post.pages.dev/tracking/" + order.getOrderId(),
                125, 125);

        order.setUrlQRCode(cloudinaryService.upload(files));

        OrderTrackingHistory orderTrackingHistory = new OrderTrackingHistory();
        orderTrackingHistory.setOrder(order);
        orderTrackingHistory.setMessageStatus(Status.ORDER_CREATED.getMessage());

        orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);

        return orderDeliveryRepository.save(orderDelivery);
    }

    @Override
    public OrderDelivery handleRegister(Order order) {
        OrderDelivery orderDelivery = new OrderDelivery();
        orderDelivery.setOrder(order);
        orderDelivery.setFromLocation(order.getSender());

        User user = Utils.getUserAuthentication();
        orderDelivery.setCurrentLocation(user.getWorkAt());

        orderDelivery.setStatus(Constant.CONFIRM_STATUS);

        return orderDelivery;
    }

    @Override
    public void updateOrderStatus(Order order, Integer deliveryStatus) {
        User user = Utils.getUserAuthentication();
        DeliveryPoint workAt = user.getWorkAt();
        String typeWorkAt = workAt.getTypeResource();

        OrderDelivery orderDelivery = order.getOrderDelivery();
        Integer typeOrder = orderDelivery.getTypeOrder();

        OrderTrackingHistory orderTrackingHistory = new OrderTrackingHistory();
        orderTrackingHistory.setOrder(order);

        Integer status = orderDelivery.getStatus();

        if (typeWorkAt.equals(Type.TRANSACTION_POINT_LOCATION)) {
            if (typeOrder.equals(Type.CUSTOMER_ORDER)) {
                if (status == OrderStatus.NEW_ORDER) {
                    orderDelivery.setStatus(OrderStatus.PROCESSING_ORDER);

                    DeliveryPoint gatheringPoint = workAt.getGatheringPoint();
                    orderDelivery.setToLocation(gatheringPoint);
                } else if (status == OrderStatus.PROCESSING_ORDER) {
                    orderDelivery.setStatus(OrderStatus.CONFIRM_SEND);

                    OrderDelivery newOrderDelivery = new OrderDelivery();
                    newOrderDelivery.setFromLocation(orderDelivery.getCurrentLocation());
                    newOrderDelivery.setCurrentLocation(orderDelivery.getToLocation());

                    newOrderDelivery.setStatus(OrderStatus.CONFIRM_RECEIVER);
                    newOrderDelivery.setTypeOrder(Type.TRANSACTION_ORDER);
                    newOrderDelivery.setOrder(order);

                    order.setOrderDelivery(newOrderDelivery);
                    orderDeliveryRepository.save(newOrderDelivery);
                }
            } else if (typeOrder.equals(Type.GATHERING_ORDER)) {
                if (status == OrderStatus.CONFIRM_RECEIVER) {
                    orderDelivery.setStatus(OrderStatus.PROCESSING_ORDER);

                    orderTrackingHistory.setMessageStatus(
                            OrderTrackingStatus.TO_TRANSACTION_POINT.getMessage() + workAt.getAddress());
                    orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);
                } else if (status == OrderStatus.PROCESSING_ORDER) {
                    orderDelivery.setTypeOrder(Type.DELIVERY_ORDER);
                    orderDelivery.setToLocation(order.getReceiver());
                }
            } else if (typeOrder.equals(Type.DELIVERY_ORDER)) {
                if (status == OrderStatus.PROCESSING_ORDER) {
                    orderDelivery.setStatus(OrderStatus.CONFIRM_SEND);

                    orderTrackingHistory.setMessageStatus(OrderTrackingStatus.DELIVERY_RECEIVER.getMessage());
                } else if (status == OrderStatus.CONFIRM_SEND) {
                    orderDelivery.setStatus(deliveryStatus);
                    if (deliveryStatus.equals(OrderStatus.SUCCESS_DELIVERY)) {
                        orderTrackingHistory.setMessageStatus(OrderTrackingStatus.SUCCESS_RECEIVER.getMessage());
                    } else {
                        orderTrackingHistory.setMessageStatus(OrderTrackingStatus.UNSUCCESS_RECEIVER.getMessage());
                    }
                }
                orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);
            }
        } else {
            if (typeOrder.equals(Type.TRANSACTION_ORDER)) {
                if (status == OrderStatus.CONFIRM_RECEIVER) {
                    orderDelivery.setStatus(OrderStatus.PROCESSING_ORDER);

                    Customer receiver = order.getReceiver();
                    DeliveryPoint transactionPoint = receiver.getTransactionPoint();
                    DeliveryPoint gatheringPoint = transactionPoint.getGatheringPoint();

                    orderDelivery.setToLocation(gatheringPoint);

                    orderTrackingHistory.setMessageStatus(
                            OrderTrackingStatus.TO_GATHERING_POINT.getMessage() + workAt.getAddress());
                    orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);
                } else if (status == OrderStatus.PROCESSING_ORDER) {
                    orderDelivery.setStatus(OrderStatus.CREATE_SEND_ORDER);
                } else if (status == OrderStatus.CREATE_SEND_ORDER) {
                    orderDelivery.setStatus(OrderStatus.CONFIRM_SEND);

                    OrderDelivery newOrderDelivery = new OrderDelivery();
                    newOrderDelivery.setFromLocation(orderDelivery.getCurrentLocation());
                    newOrderDelivery.setCurrentLocation(orderDelivery.getToLocation());

                    newOrderDelivery.setStatus(OrderStatus.CONFIRM_RECEIVER);
                    newOrderDelivery.setTypeOrder(Type.GATHERING_ORDER);
                    newOrderDelivery.setOrder(order);
                    orderDeliveryRepository.save(newOrderDelivery);

                    order.setOrderDelivery(newOrderDelivery);

                    orderTrackingHistory.setMessageStatus(OrderTrackingStatus.LEAVE_GATHERING_POINT.getMessage());
                    orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);
                }
            } else if (typeOrder.equals(Type.GATHERING_ORDER)) {
                if (status == OrderStatus.CONFIRM_RECEIVER) {
                    orderDelivery.setStatus(OrderStatus.PROCESSING_ORDER);

                    Customer receiver = order.getReceiver();
                    DeliveryPoint transactionPoint = receiver.getTransactionPoint();

                    orderDelivery.setToLocation(transactionPoint);

                    orderTrackingHistory.setMessageStatus(
                            OrderTrackingStatus.TO_GATHERING_POINT.getMessage() + workAt.getAddress());
                    orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);
                } else if (status == OrderStatus.PROCESSING_ORDER) {
                    orderDelivery.setStatus(OrderStatus.CREATE_SEND_ORDER);
                } else if (status == OrderStatus.CREATE_SEND_ORDER) {
                    orderDelivery.setStatus(OrderStatus.CONFIRM_SEND);

                    OrderDelivery newOrderDelivery = new OrderDelivery();
                    newOrderDelivery.setFromLocation(orderDelivery.getCurrentLocation());
                    newOrderDelivery.setCurrentLocation(orderDelivery.getToLocation());

                    newOrderDelivery.setStatus(OrderStatus.CONFIRM_RECEIVER);
                    newOrderDelivery.setTypeOrder(Type.GATHERING_ORDER);
                    newOrderDelivery.setOrder(order);
                    orderDeliveryRepository.save(newOrderDelivery);
                    order.setOrderDelivery(newOrderDelivery);

                    orderTrackingHistory.setMessageStatus(OrderTrackingStatus.LEAVE_GATHERING_POINT.getMessage());
                    orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);
                }
            }
        }
        orderDeliveryRepository.save(orderDelivery);
    }

    @Override
    public OrderDelivery orderConfirmFromPoint(Order order) {
        OrderDelivery orderDelivery = order.getOrderDelivery();
        Integer status = orderDelivery.getStatus();

        String typePoint = orderDelivery.getToLocation().getTypeResource();
        if (!status.equals(Constant.LEFT_STATUS) || typePoint.equals(Type.RECEIVER_CUSTOMER_LOCATION)) {
            throw new EntityException(MessageResponse.ERROR_REGISTER.getMessage());
        }

        OrderDelivery orderConfirm = new OrderDelivery();
        OrderTrackingHistory orderTrackingHistory = new OrderTrackingHistory();

        orderConfirm.setOrder(order);
        orderConfirm.setFromLocation(orderDelivery.getCurrentLocation());
        orderConfirm.setCurrentLocation(orderDelivery.getToLocation());

        orderConfirm.setStatus(Constant.CONFIRM_STATUS);

        String typeCurrent = orderConfirm.getCurrentLocation().getTypeResource();
        DeliveryPoint deliveryPoint = deliveryPointService
                .getDeliveryPointById(orderConfirm.getCurrentLocation().getId());
        if (typeCurrent.equals(Type.TRANSACTION_POINT_LOCATION)) {
            orderTrackingHistory
                    .setMessageStatus(Status.TRANSACTION_POINT_ARRIVED.getMessage() + deliveryPoint.getAddress());
        } else if (typeCurrent.equals(Type.GATHERING_POINT_LOCATION)) {
            orderTrackingHistory
                    .setMessageStatus(Status.GATHERING_POINT_ARRIVED.getMessage() + deliveryPoint.getAddress());
        }
        orderTrackingHistory.setOrder(order);
        orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);

        return orderConfirm;
    }

    @Override
    public OrderDelivery orderDeliveryToPoint(Order order, OrderDeliveryRequest orderDeliveryRequest) {
        OrderDelivery orderDelivery = order.getOrderDelivery();
        Integer status = orderDelivery.getStatus();
        if (!status.equals(Constant.CONFIRM_STATUS)) {
            throw new EntityException(MessageResponse.ERROR_REGISTER.getMessage());
        }
        orderDelivery.setStatus(Constant.CREATED_STATUS);

        User user = Utils.getUserAuthentication();
        DeliveryPoint transactionPoint = user.getWorkAt();
        if (transactionPoint.getTypeResource().equals(Type.TRANSACTION_POINT_LOCATION)) {
            DeliveryPoint gatheringPoint = transactionPoint.getGatheringPoint();
            orderDelivery.setToLocation(gatheringPoint);
        } else {
            // ResourcePoint deliveryPoint = deliveryPointService
            // .getDeliveryPointById(orderDeliveryRequest.getGatheringPointId());
            // orderDelivery.setToLocation(deliveryPoint);
        }

        return orderDelivery;
    }

    @Override
    public OrderDelivery orderConfirmLeft(Order order) {
        OrderDelivery orderDelivery = order.getOrderDelivery();
        Integer status = orderDelivery.getStatus();
        if (!status.equals(Constant.CREATED_STATUS)) {
            throw new EntityException(MessageResponse.ERROR_REGISTER.getMessage());
        }

        orderDelivery.setStatus(Constant.LEFT_STATUS);
        // orderDelivery.setCompleted(1);

        OrderTrackingHistory orderTrackingHistory = new OrderTrackingHistory();
        String typeCurrent = orderDelivery.getCurrentLocation().getTypeResource();
        if (typeCurrent.equals(Type.TRANSACTION_POINT_LOCATION)) {
            orderTrackingHistory.setMessageStatus(Status.TRANSACTION_POINT_LEFT.getMessage());
        } else if (typeCurrent.equals(Type.GATHERING_POINT_LOCATION)) {
            orderTrackingHistory.setMessageStatus(Status.GATHERING_POINT_LEFT.getMessage());
        }
        orderTrackingHistory.setOrder(order);
        orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);

        return orderDelivery;
    }

    @Override
    public OrderDelivery orderDeliveryToReceiver(Order order) {
        OrderDelivery orderDelivery = order.getOrderDelivery();
        Integer status = orderDelivery.getStatus();

        String typeToReceiver = orderDelivery.getToLocation().getTypeResource();
        if (!status.equals(Constant.LEFT_STATUS) && !typeToReceiver.equals(Type.RECEIVER_CUSTOMER_LOCATION)) {
            throw new EntityException(MessageResponse.ERROR_REGISTER.getMessage());
        }
        orderDelivery.setStatus(Constant.DELIVERY_STATUS);

        OrderTrackingHistory orderTrackingHistory = new OrderTrackingHistory();

        orderTrackingHistory.setOrder(order);
        orderTrackingHistory.setMessageStatus(Status.RECEIVER_DELIVERY.getMessage());

        orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);

        return orderDelivery;
    }

    @Override
    public OrderDelivery orderConfirmFromReceiver(Integer status, Order order) {
        OrderDelivery orderDelivery = order.getOrderDelivery();
        Integer statusCurrentLocation = orderDelivery.getStatus();
        if (!statusCurrentLocation.equals(Constant.DELIVERY_STATUS)) {
            throw new EntityException(MessageResponse.ERROR_REGISTER.getMessage());
        }

        orderDelivery.setStatus(status);

        OrderTrackingHistory orderTrackingHistory = new OrderTrackingHistory();

        orderTrackingHistory.setOrder(order);
        if (status.equals(Status.SUCCESS_DELIVERY)) {
            orderTrackingHistory.setMessageStatus(Status.SUCCESS_DELIVERY.getMessage());
        } else {
            orderTrackingHistory.setMessageStatus(Status.UNSUCESS_DELIVERY.getMessage());
        }

        orderTrackingHistoryService.addOrderTrackingHistory(orderTrackingHistory);

        return orderDelivery;
    }

    @Override
    public List<OrderDelivery> getAllOrdersByStatus(Integer status) {
        return orderDeliveryRepository.findAllByStatus(status);
    }

    public OrderDelivery getOrderDeliveryById(Long id) {
        return orderDeliveryRepository.findById(id).orElseThrow(() -> new EntityNotFound("Không tìm thấy"));
    }

    @Override
    public List<OrderDelivery> getOrderStatisticByConditions(OrderStatisticDS orderStatisticDS) {
        return orderDeliveryRepository.findAll(OrderDeliverySpecification.getOrderStatistic(orderStatisticDS));
    }

    @Override
    public Page<OrderDelivery> getAllOrderDeliveryByPage(OrderDS orderDS) {
        return orderDeliveryRepository.findAll(OrderDeliverySpecification.getAllOrderDeliveryByPage(orderDS),
                PageRequest.of(orderDS.getPageNumber() - 1, orderDS.getPageSize(), Sort.by("order.createAt").descending()));
    }

    @Override
    public List<OrderDelivery> getAllOrderDelivery(OrderDS orderDS) {
        return orderDeliveryRepository.findAll(OrderDeliverySpecification.getAllOrderDeliveryByPage(orderDS));
    }

}
