package com.university.post.service.impl;

import com.university.post.dataset.DeliveryPointDS;
import com.university.post.enums.Permission;
import com.university.post.enums.Type;
import com.university.post.exception.entity.EntityNotFound;
import com.university.post.exception.user.UserNotFound;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import com.university.post.payload.request.point.DeliveryPointRequest;
import com.university.post.payload.request.user.UserRequest;
import com.university.post.repository.DeliveryPointRepository;
import com.university.post.service.DeliveryPointService;
import com.university.post.service.UserService;
import com.university.post.specification.DeliveryPointSpecification;
import com.university.post.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryPointServiceImpl implements DeliveryPointService {

    @Autowired
    private DeliveryPointRepository deliveryPointRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public DeliveryPoint addDeliveryPoint(DeliveryPoint deliveryPoint) {
        return deliveryPointRepository.save(deliveryPoint);
    }

    @Override
    public DeliveryPoint updateDeliveryPoint(DeliveryPoint deliveryPoint) {
        return deliveryPointRepository.save(deliveryPoint);
    }

    @Override
    @Transactional
    public void deleteDeliveryPoint(DeliveryPoint deliveryPoint) {
        String type = deliveryPoint.getTypeResource();
        // if (type.equals(Type.TRANSACTION_POINT_LOCATION)) {
        // deliveryPoint.setGatheringPoint(null);
        // }
        if (type.equals(Type.GATHERING_POINT_LOCATION)) {
            deliveryPointRepository.deleteGatheringPointInTransactionPoint(deliveryPoint);
        }
        deliveryPoint.getStaffs().stream().forEach(staff -> staff.setWorkAt(null));
        deliveryPointRepository.delete(deliveryPoint);
    }

    @Override
    public Page<DeliveryPoint> getAllDeliveryPointByPage(DeliveryPointDS deliveryPointDS) {
        return deliveryPointRepository.findAll(DeliveryPointSpecification.getAllDeliveryPointByPage(deliveryPointDS),
                PageRequest.of(deliveryPointDS.getPageNumber() - 1, deliveryPointDS.getPageSize(),
                        Sort.by("createAt").descending()));
    }

    @Override
    public List<DeliveryPoint> getAllDeliveryPoint(DeliveryPointDS deliveryPointDS) {
        return deliveryPointRepository.findAll(DeliveryPointSpecification.getAllDeliveryPointByPage(deliveryPointDS));
    }

    @Override
    public DeliveryPoint handleRegister(DeliveryPointRequest deliveryPointRequest) {
        DeliveryPoint deliveryPoint = new DeliveryPoint();
        // deliveryPoint.setId(UUID.randomUUID().toString());
        this.handleDeliveryPoint(deliveryPointRequest, deliveryPoint);
        Long nextId = deliveryPointRepository.getNextId();
        deliveryPoint.setResourceId(Constant.POINT_PREFIX_ID + nextId);

        String adminId = deliveryPointRequest.getAdminId();
        User admin = userService.getUserById(adminId);
        deliveryPoint.getStaffs().add(admin);
        admin.setWorkAt(deliveryPoint);

        String typePoint = deliveryPointRequest.getType();
        if (typePoint.equals(Type.TRANSACTION_POINT_LOCATION)) {
            String gatheringPointId = deliveryPointRequest.getGatheringPointId();
            DeliveryPoint gatheringPoint = this.getDeliveryPointById(gatheringPointId);
            if (gatheringPoint != null) {
                deliveryPoint.setGatheringPoint(gatheringPoint);
            }
        }
        return deliveryPoint;
    }

    @Override
    public DeliveryPoint handleUpdate(String id, DeliveryPointRequest deliveryPointRequest) {
        String typePoint = deliveryPointRequest.getType();
        DeliveryPoint deliveryPoint = this.getDeliveryPointByIdAndTypePoint(id, typePoint);
        this.handleDeliveryPoint(deliveryPointRequest, deliveryPoint);
        // User adminOld = deliveryPoint.getStaffs().get(0);
        // String adminIdOld = adminOld.getId();
        String adminIdNew = deliveryPointRequest.getAdminId();
        User adminNew = userService.getUserById(adminIdNew);
        deliveryPoint.getStaffs().set(0, adminNew);
        adminNew.setWorkAt(deliveryPoint);
        // if (!adminIdOld.equals(adminIdNew)) {
        // User adminNew = userService.getUserById(adminIdNew);
        // adminOld.setWorkAt(null);
        // deliveryPoint.getStaffs().set(0, adminNew);
        // adminNew.setWorkAt(deliveryPoint);
        // }

        if (typePoint.equals(Type.TRANSACTION_POINT_LOCATION)) {
            String gatheringPointIdNew = deliveryPointRequest.getGatheringPointId();
            DeliveryPoint gatheringPointNew = this.getDeliveryPointById(gatheringPointIdNew);
            deliveryPoint.setGatheringPoint(gatheringPointNew);
        }
        return deliveryPoint;
    }

    public void handleDeliveryPoint(DeliveryPointRequest deliveryPointRequest, DeliveryPoint deliveryPoint) {
        deliveryPoint.setName(deliveryPointRequest.getName());
        deliveryPoint.setAddress(deliveryPointRequest.getAddress());
        deliveryPoint.setPhoneNo(deliveryPointRequest.getPhoneNo());
        deliveryPoint.setTypeResource(deliveryPointRequest.getType());
    }

    @Override
    public DeliveryPoint getDeliveryPointByIdAndTypePoint(String id, String typePoint) {
        return deliveryPointRepository.findByIdAndTypeResource(id, typePoint)
                .orElseThrow(() -> new UserNotFound("Không tìm thấy điểm chuyển phát"));
    }

    @Override
    public DeliveryPoint getDeliveryPointById(String id) {
        return deliveryPointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Không tìm thấy điểm chuyển phát với id " + id));
    }

    @Override
    public Boolean isValidationAdminInRegister(DeliveryPointRequest deliveryPointRequest) {
        String adminId = deliveryPointRequest.getAdminId();
        User user = userService.getUserById(adminId);
        Long roleOfUser = user.getRole().getId();
        String typePoint = deliveryPointRequest.getType();
        return ((roleOfUser.equals(Permission.TRANSACTION_ADMIN) && typePoint.equals(Type.TRANSACTION_POINT_LOCATION))
                || (roleOfUser.equals(Permission.GATHERING_ADMIN) && typePoint.equals(Type.GATHERING_POINT_LOCATION)));
    }
}
