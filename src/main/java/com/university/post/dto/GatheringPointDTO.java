package com.university.post.dto;

import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GatheringPointDTO extends DeliveryPointDTO{
    public GatheringPointDTO(DeliveryPoint gatheringPoint) {
        super(gatheringPoint);
    }

}
