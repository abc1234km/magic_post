package com.university.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "order_delivery")
@Data
public class OrderDelivery extends Audit<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Order order;

    @ManyToOne
    private ResourcePoint fromLocation;

    @ManyToOne
    private ResourcePoint currentLocation;

    @ManyToOne
    private ResourcePoint toLocation;

    private Integer status;

    // private Integer completed = 0;

    private Integer typeOrder;
}
