package com.university.post.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.university.post.model.Customer;
import com.university.post.model.DeliveryPoint;
import com.university.post.serializer.Serializer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private String id;

    private Long customerId;

    private String name;

    private String phoneNo;

    private String address;

    @JsonSerialize(using = Serializer.class)
    private DeliveryPointDTO transactionPoint;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.customerId = customer.getResourceId();
        this.name = customer.getName();
        this.phoneNo = customer.getPhoneNo();
        this.address = customer.getAddress();
        this.transactionPoint = new TransactionPointDTO(customer.getTransactionPoint());
    }
}
