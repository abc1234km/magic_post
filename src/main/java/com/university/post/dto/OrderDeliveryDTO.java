package com.university.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.university.post.model.OrderDelivery;
import com.university.post.serializer.Serializer;
import com.university.post.utils.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDeliveryDTO {
    private Long id;

    private Object fromLocation;

    @JsonSerialize(using = Serializer.class)
    private Object currentLocation;

    private Object toLocation;

    private Integer status;

    public OrderDeliveryDTO(OrderDelivery orderDelivery) {
        this.id = orderDelivery.getId();
        this.fromLocation = Utils.typeOrderDelivery(orderDelivery.getFromLocation());
        this.currentLocation = Utils.typeOrderDelivery(orderDelivery.getCurrentLocation());
        this.toLocation = Utils.typeOrderDelivery(orderDelivery.getToLocation());
        this.status = orderDelivery.getStatus();
    }
}
