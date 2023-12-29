package com.university.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.university.post.dataset.DeliveryPointDS;
import com.university.post.dataset.OrderDS;
import com.university.post.enums.OrderStatus;
import com.university.post.enums.Type;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.OrderDelivery;
import com.university.post.payload.response.data.DataRepsonse;
import com.university.post.service.DeliveryPointService;
import com.university.post.service.OrderDeliveryService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors; 

@RestController
@RequestMapping("/api/v1/gathering-points")
public class GatheringPointController {

    @Autowired
    private DeliveryPointService deliveryPointService;

    @Autowired
    private OrderDeliveryService orderDeliveryService;
    
    @GetMapping("/transaction-points")
    @ResponseStatus(HttpStatus.OK)
    public DataRepsonse<List> getAllTransactionPoint() {
        DeliveryPointDS deliveryPointDS = new DeliveryPointDS();
        deliveryPointDS.setType(Type.TRANSACTION_POINT_LOCATION);
        deliveryPointDS.setHasGatheringPoint(true);

        List<DeliveryPoint> transactionPoints = deliveryPointService.getAllDeliveryPoint(deliveryPointDS);
        List<String> addressList = transactionPoints.parallelStream().map(point -> point.getAddress()).collect(Collectors.toList());


        return new DataRepsonse<List>(HttpStatus.OK, addressList, "Lấy thông tin tất cả địa chỉ điểm giao dịch liên kết thành công");
    }

    @GetMapping("/order-gatherings")
    @ResponseStatus(HttpStatus.OK)
    public DataRepsonse<Set> getAllGatheringPoint(Integer deliveryStatus, Integer typeOrder) {
        OrderDS orderDS = new OrderDS();
        orderDS.setDeliveryStatus(deliveryStatus);
        orderDS.setTypeOrder(typeOrder);

        List<OrderDelivery> orderDeliveries = orderDeliveryService.getAllOrderDelivery(orderDS);
        List<String> addressList = orderDeliveries.parallelStream().map(
            orderDelivery -> {
                return deliveryStatus == OrderStatus.CONFIRM_RECEIVER 
                ? orderDelivery.getFromLocation().getAddress() 
                : orderDelivery.getToLocation().getAddress();
            }
        ).collect(Collectors.toList());

        Set<String> address = new HashSet<>(addressList);

        return new DataRepsonse<Set>(HttpStatus.OK, address, "Lấy thông tin tất cả địa chỉ điểm tập kết thành công");
    }

    
}
