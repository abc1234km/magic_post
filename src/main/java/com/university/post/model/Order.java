package com.university.post.model;

import com.university.post.enums.Type;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order extends Audit<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(250)")
    private String id;

    private String orderId;

    private Integer categority;

    private Double weight;

    @ElementCollection(targetClass = ContextOrder.class)
    private List<ContextOrder> contextOrders = new ArrayList<>();

    private Long mainCharge;

    private Long tempCharge;

    private Long GTGTCharge;

    private Long vatCharge;

    private Long otherCharge;

    private Long codCharge;

    private Long otherReceiverCharge;

    private String urlQRCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private Customer sender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id")
    private Customer receiver;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_delivery_id")
    private OrderDelivery orderDelivery;
}
