package com.university.post.dto;

import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TransactionPointDTO extends DeliveryPointDTO{
    private GatheringPointDTO gatheringPoint;

    public TransactionPointDTO(DeliveryPoint transactionPoint) {
        super(transactionPoint);
        this.gatheringPoint = new GatheringPointDTO(transactionPoint.getGatheringPoint());
    }
}
