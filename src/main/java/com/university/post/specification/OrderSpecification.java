package com.university.post.specification;

import com.university.post.dataset.OrderDS;
import com.university.post.model.*;
import com.university.post.utils.Constant;
import com.university.post.utils.Utils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Order> getAllOrderByConditions(OrderDS orderDS) {

        return (root, query, cb) -> {
            User user = Utils.getUserAuthentication();
            ResourcePoint deliveryPoint = user.getWorkAt();

            List<Predicate> predicates = new ArrayList<>();

            Join<Order, OrderDelivery> orderDeliveryJoin = root.join("orderDelivery", JoinType.INNER);

            if (orderDS.getStatus() != null) {
                predicates.add(cb.equal(orderDeliveryJoin.get("status"), orderDS.getStatus()));
            }

            Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
            return predicate;
        };
    }
}
