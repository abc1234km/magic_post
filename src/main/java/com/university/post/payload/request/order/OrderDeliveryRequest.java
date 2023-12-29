package com.university.post.payload.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeliveryRequest {
    private String[] orderIds;

    private Integer deliveryStatus;;
}
