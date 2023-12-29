package com.university.post.specification;

import com.university.post.dataset.DeliveryPointDS;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import com.university.post.utils.Utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryPointSpecification {
    public static Specification<DeliveryPoint> getAllDeliveryPointByPage(DeliveryPointDS deliveryPointDS) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (deliveryPointDS.getPointId() != null) {
                predicates.add(cb.like(root.get("pointId").as(String.class), "%" + deliveryPointDS.getPointId() + "%"));
            }

            if (deliveryPointDS.getPhoneNo() != null) {
                predicates.add(cb.like(root.get("phoneNo"), "%" + deliveryPointDS.getPhoneNo() + "%"));
            }

            if (deliveryPointDS.getAddress() != null) {
                predicates.add(cb.like(root.get("address"), "%" + deliveryPointDS.getAddress() + "%"));
            }

            if (deliveryPointDS.getType() != null) {
                predicates.add(cb.equal(root.get("typeResource"), String.valueOf(deliveryPointDS.getType())));
            }

            if (deliveryPointDS.getHasGatheringPoint()) {
                Join<DeliveryPoint, DeliveryPoint> joinGatheringPoint = root.join("gatheringPoint");
                User user = Utils.getUserAuthentication();
                DeliveryPoint gatheringPointWork = user.getWorkAt();
                predicates.add(cb.equal(joinGatheringPoint.get("id"), gatheringPointWork.getId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
