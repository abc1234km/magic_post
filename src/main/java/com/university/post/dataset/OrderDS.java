package com.university.post.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDS extends Paging{

    private String orderId;

    private Integer categorityOrder;

    private Integer status;

    private String senderName;

    private String senderAddress;

    private String receiverName;

    private String receiverAddress;

    private String fromAddress;

    private String toAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAtFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAtTo;

    private Integer deliveryStatus;

    private Integer typeOrder;
}
