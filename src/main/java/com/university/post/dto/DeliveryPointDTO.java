package com.university.post.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPointDTO extends AuditDTO<String>{
    protected String id;

    protected Long pointId;

    protected String name;

    protected String phoneNo;

    protected String address;

    protected String typePoint;
    
    protected User admin;

    public DeliveryPointDTO(DeliveryPoint deliveryPoint) {
        this.id = deliveryPoint.getId();
        this.pointId = deliveryPoint.getResourceId();
        this.name = deliveryPoint.getName();
        this.phoneNo = deliveryPoint.getPhoneNo();
        this.address = deliveryPoint.getAddress();
        this.typePoint = deliveryPoint.getTypeResource();
        this.admin = deliveryPoint.getStaffs().isEmpty() ? null : deliveryPoint.getStaffs().get(0);
        this.createAt = deliveryPoint.getCreateAt();
        this.createBy = deliveryPoint.getCreateBy();
        this.updateAt = deliveryPoint.getUpdateAt();
        this.updateBy = deliveryPoint.getUpdateBy();
    }
}
