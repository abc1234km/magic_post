package com.university.post.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPointDS extends Paging{
    private Long pointId;

    private String phoneNo;

    private String address;

    private String type;

    private Boolean hasGatheringPoint = false;
}
