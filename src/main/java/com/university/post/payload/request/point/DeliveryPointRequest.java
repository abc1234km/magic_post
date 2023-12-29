package com.university.post.payload.request.point;

import lombok.Data;

@Data
public class DeliveryPointRequest {
    private String name;

    private String phoneNo;

    private String address;

    private String adminId;

    private String gatheringPointId;

    private String type;
}
