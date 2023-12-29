package com.university.post.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="customers")
@Data
@DiscriminatorValue("Customer")
public class Customer extends ResourcePoint{
    @OneToOne
    @JoinColumn(name="transaction_id")
    private DeliveryPoint transactionPoint;
}
