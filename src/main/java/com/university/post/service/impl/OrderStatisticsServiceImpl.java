package com.university.post.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.post.dataset.OrderStatisticDS;
import com.university.post.enums.OrderStatus;
import com.university.post.enums.Permission;
import com.university.post.enums.Type;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.OrderDelivery;
import com.university.post.model.User;
import com.university.post.payload.response.statistic.OrderStatisticPointResponse;
import com.university.post.service.OrderDeliveryService;
import com.university.post.service.OrderStatsticsService;
import com.university.post.utils.Utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class OrderStatisticsServiceImpl implements OrderStatsticsService {
    private Map<String, Integer> mapDate;

    private List<OrderStatisticPointResponse> orderStatstics;

    @Autowired
    private OrderDeliveryService orderDeliveryService;

    public void getOrderStastisByOrderDelivery(OrderStatisticDS orderStatisticDS) {
        List<OrderDelivery> orderInComing = orderDeliveryService.getOrderStatisticByConditions(orderStatisticDS);
        Long role = Utils.getUserAuthentication().getRole().getId();

        orderInComing.parallelStream().forEach(order -> {
            Date orderDate = order.getCreateAt();
            LocalDate date = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            OrderStatisticPointResponse orderStatisticPointResponse = this.orderStatstics
                    .get(mapDate.get(date.toString()));
            if ((role == Permission.COMPANY_ADMIN
                    && orderStatisticDS.getTypePoint().equals(Type.TRANSACTION_POINT_LOCATION)) ||
                    role == Permission.TRANSACTION_ADMIN) {
                if (orderStatisticDS.getTypeResource().equals("sender")) {
                    orderStatisticPointResponse
                            .setNumberOrderIncoming(orderStatisticPointResponse.getNumberOrderIncoming() + 1);
                } else {
                    orderStatisticPointResponse
                            .setNumberOrderLeave(orderStatisticPointResponse.getNumberOrderLeave() + 1);
                }
            } else if (role == Permission.TRANSACTION_STAFF) {
                if (orderStatisticDS.getStatusComplete() == OrderStatus.SUCCESS_DELIVERY) {
                    orderStatisticPointResponse
                            .setNumberOrderIncoming(orderStatisticPointResponse.getNumberOrderIncoming() + 1);
                } else {
                    orderStatisticPointResponse
                            .setNumberOrderLeave(orderStatisticPointResponse.getNumberOrderLeave() + 1);
                }
            } else {
                if (orderStatisticDS.getTypeStatistic().equals("fromLocation")) {
                    orderStatisticPointResponse
                            .setNumberOrderIncoming(orderStatisticPointResponse.getNumberOrderIncoming() + 1);
                } else {
                    orderStatisticPointResponse
                            .setNumberOrderLeave(orderStatisticPointResponse.getNumberOrderLeave() + 1);
                }
            }
        });
    }

    public List<OrderStatisticPointResponse> getOrderStastic(String typePoint, String pointId) {
        this.mapDate = new HashMap();
        this.orderStatstics = Utils.getOrderStasticByDates(this.mapDate);
        Long role = Utils.getUserAuthentication().getRole().getId();
        OrderStatisticDS orderStatisticDS = new OrderStatisticDS();
        if (role == Permission.COMPANY_ADMIN) {
            orderStatisticDS.setPointId(pointId);
            orderStatisticDS.setTypePoint(typePoint);
            if (typePoint.equals(Type.TRANSACTION_POINT_LOCATION)) {
                orderStatisticDS.setTypeResource("sender");
                orderStatisticDS.setTypeStatistic("fromLocation");
                this.getOrderStastisByOrderDelivery(orderStatisticDS);
                orderStatisticDS.setTypeResource("noSender");
                this.getOrderStastisByOrderDelivery(orderStatisticDS);
            } else {
                orderStatisticDS.setTypeStatistic("fromLocation");
                this.getOrderStastisByOrderDelivery(orderStatisticDS);
                orderStatisticDS.setTypeStatistic("toLocation");
                this.getOrderStastisByOrderDelivery(orderStatisticDS);
            }
        } else if (role == Permission.TRANSACTION_ADMIN) {
            orderStatisticDS.setTypeStatistic("fromLocation");
            orderStatisticDS.setTypeResource("sender");
            this.getOrderStastisByOrderDelivery(orderStatisticDS);
            orderStatisticDS.setTypeResource("noSender");
            this.getOrderStastisByOrderDelivery(orderStatisticDS);
        } else if (role == Permission.TRANSACTION_STAFF) {
            orderStatisticDS.setStatusComplete(OrderStatus.SUCCESS_DELIVERY);
            this.getOrderStastisByOrderDelivery(orderStatisticDS);
            orderStatisticDS.setStatusComplete(OrderStatus.UNSUCESS_DELIVERY);
            this.getOrderStastisByOrderDelivery(orderStatisticDS);
        } else if (role == Permission.GATHERING_ADMIN) {
            orderStatisticDS.setTypeStatistic("fromLocation");
            this.getOrderStastisByOrderDelivery(orderStatisticDS);
            orderStatisticDS.setTypeStatistic("toLocation");
            this.getOrderStastisByOrderDelivery(orderStatisticDS);
        }
        Collections.reverse(this.orderStatstics);
        return this.orderStatstics;
    }
}
