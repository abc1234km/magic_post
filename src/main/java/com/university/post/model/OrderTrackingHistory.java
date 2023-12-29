package com.university.post.model;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="order_tracking_history")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OrderTrackingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Order order;

    private String messageStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date createAt;
}
