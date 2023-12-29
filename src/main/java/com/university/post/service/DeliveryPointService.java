package com.university.post.service;

import com.university.post.dataset.DeliveryPointDS;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import com.university.post.payload.request.point.DeliveryPointRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeliveryPointService {
    public DeliveryPoint addDeliveryPoint(DeliveryPoint deliveryPoint);

    public DeliveryPoint updateDeliveryPoint(DeliveryPoint deliveryPoint);

    public void deleteDeliveryPoint(DeliveryPoint deliveryPoint);

    public Page<DeliveryPoint> getAllDeliveryPointByPage(DeliveryPointDS deliveryPointDS);

    List<DeliveryPoint> getAllDeliveryPoint(DeliveryPointDS deliveryPointDS);

    public DeliveryPoint handleRegister(DeliveryPointRequest deliveryPointRequest);

    DeliveryPoint handleUpdate(String id, DeliveryPointRequest deliveryPointRequest);

    public void handleDeliveryPoint(DeliveryPointRequest deliveryPointRequest, DeliveryPoint deliveryPoint);

    public DeliveryPoint getDeliveryPointByIdAndTypePoint(String id, String typePoint);

    public DeliveryPoint getDeliveryPointById(String id);

    public Boolean isValidationAdminInRegister(DeliveryPointRequest deliveryPointRequest);
}
