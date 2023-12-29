package com.university.post.payload.request.order;

import com.university.post.model.ContextOrder;
import com.university.post.payload.request.customer.CustomerRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Integer categority;

    private Double weight;

    private CustomerRequest senderCustomer;

    private CustomerRequest receiverCustomer;

    private List<ContextOrder> contextOrders;
}
