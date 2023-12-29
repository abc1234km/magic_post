package com.university.post.payload.request.point;

import com.university.post.enums.Type;
import lombok.Data;

@Data
public class TransactionPointRequest extends DeliveryPointRequest {
    private Long gatheringPoint;
}
