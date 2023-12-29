package com.university.post.specification;

import com.university.post.dataset.UserDS;
import com.university.post.dto.UserDTO;
import com.university.post.enums.Permission;
import com.university.post.enums.Type;
import com.university.post.model.DeliveryPoint;
import com.university.post.model.Role;
import com.university.post.model.User;
import com.university.post.utils.Utils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.*;

public class UserSpecification {
    public static Specification<User> getAllUserByConditions(UserDS userDS) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userDS.getUserId() != null) {
                predicates.add(cb.like(root.get("userId").as(String.class), "%" + userDS.getUserId() + "%"));
            }

            if (userDS.getFullName() != null) {
                predicates.add(cb.like(root.get("fullName"),"%" + userDS.getFullName() + "%"));
            }

            if (userDS.getPhoneNo() != null) {
                predicates.add(cb.like(root.get("phoneNo"), "%" + userDS.getPhoneNo() + "%"));
            }

            if (userDS.getAddress() != null) {
                predicates.add(cb.like(root.get("address"), "%" + userDS.getAddress() + "%"));
            }

            if (userDS.getEmail() != null) {
                predicates.add(cb.like(root.get("email"), "%" + userDS.getEmail() + "%"));
            }

            User user = Utils.getUserAuthentication();
            Join<User, DeliveryPoint> userDeliveryPoint = root.join("workAt", JoinType.INNER);
            predicates.add(cb.equal(userDeliveryPoint.get("id"), user.getWorkAt().getId()));

            if (userDS.getHasWorkAt() != null) {
                if (userDS.getHasWorkAt()) {
                    if (userDS.getWorkAt() != null) {
                        predicates.add(cb.like(userDeliveryPoint.get("pointId").as(String.class),
                                "%" + userDS.getWorkAt() + "%"));
                    }
                } else {
                    predicates.add(cb.isNull(root.get("workAt")));
                }
            }

            Join<User, Role> userRole = root.join("role", JoinType.INNER);

            Long roleId = user.getRole().getId();

            if (userDS.getRole() != null && !userDS.getRole().equals(roleId)) {
                predicates.add(cb.equal(userRole.get("id"), userDS.getRole()));
            } else {
                if (roleId.equals(Permission.COMPANY_ADMIN)) {
                    if (userDS.getHasWorkAt() != null) {
                        if (userDS.getHasWorkAt()) {
                            // Join<User, DeliveryPoint> userDeliveryPoint = root.join("workAt", JoinType.INNER);
                            if (userDS.getWorkAt() != null) {
                                predicates.add(cb.like(userDeliveryPoint.get("pointId").as(String.class),
                                        "%" + userDS.getWorkAt() + "%"));
                            }
                        } else {
                            predicates.add(cb.isNull(root.get("workAt")));
                        }
                    }

                    if (userDS.getTypePoint() != null) {
                        if (userDS.getTypePoint().equals(Type.TRANSACTION_POINT_LOCATION)) {
                            predicates.add(cb.equal(userRole.get("id"), Permission.TRANSACTION_ADMIN));
                        } else {
                            predicates.add(cb.equal(userRole.get("id"), Permission.GATHERING_ADMIN));
                        }
                    } else {
                        predicates.add(cb.or(cb.equal(userRole.get("id"), Permission.TRANSACTION_ADMIN),
                                cb.equal(userRole.get("id"), Permission.GATHERING_ADMIN)));
                    }
                } else if (roleId.equals(Permission.TRANSACTION_ADMIN)) {
                    predicates.add(cb.equal(userRole.get("id"), Permission.TRANSACTION_STAFF));
                } else if (roleId.equals(Permission.GATHERING_ADMIN)) {
                    predicates.add(cb.equal(userRole.get("id"), Permission.GATHERING_STAFF));
                }
            }
            Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
            return predicate;
        };
    }
}
