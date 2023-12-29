package com.university.post.dto;

import com.university.post.model.ContextOrder;
import com.university.post.model.Order;
import com.university.post.model.OrderDelivery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO extends AuditDTO<String>{
    private String id;

    private String orderId;

    private Integer categority;

    private Double weight;

    private List<ContextOrder> contextOrders;

    private Long mainCharge;

    private Long tempCharge;

    private Long gtgtCharge;

    private Long vatCharge;

    private Long otherCharge;

    private Long codCharge;

    private Long otherReceiverCharge;

    private String urlQrCode;

    private CustomerDTO senderCustomer;

    private CustomerDTO receiverCustomer;

    private OrderDeliveryDTO orderDelivery;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.orderId = order.getOrderId();
        this.categority = order.getCategority();
        this.weight = order.getWeight();
        this.contextOrders = order.getContextOrders();
        this.mainCharge = order.getMainCharge();
        this.tempCharge = order.getTempCharge();
        this.gtgtCharge = order.getGTGTCharge();
        this.vatCharge = order.getVatCharge();
        this.otherCharge = order.getOtherCharge();
        this.codCharge = order.getCodCharge();
        this.otherReceiverCharge = order.getOtherReceiverCharge();
        this.urlQrCode = order.getUrlQRCode();
        this.senderCustomer = new CustomerDTO(order.getSender());
        this.receiverCustomer = new CustomerDTO(order.getReceiver());
        this.orderDelivery = new OrderDeliveryDTO(order.getOrderDelivery());
        this.createBy = order.getCreateBy();
        this.createAt = order.getCreateAt();
        this.updateBy = order.getUpdateBy();
        this.updateAt = order.getUpdateAt();
    }
}
