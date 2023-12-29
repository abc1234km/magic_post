package com.university.post.specification;

import com.university.post.dataset.OrderDS;
import com.university.post.dataset.OrderStatisticDS;
import com.university.post.enums.OrderStatus;
import com.university.post.enums.Permission;
import com.university.post.enums.Type;
import com.university.post.model.Customer;
import com.university.post.model.Order;
import com.university.post.model.OrderDelivery;
import com.university.post.model.ResourcePoint;
import com.university.post.model.User;
import com.university.post.utils.Utils;

import ch.qos.logback.classic.pattern.Util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.SpelQueryContext;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;

public class OrderDeliverySpecification {
   public static Specification<OrderDelivery> getOrderStatistic(OrderStatisticDS orderStatisticDS) {
      return (root, query, cb) -> {
         List<Predicate> predicates = new ArrayList<>();
         Join<OrderDelivery, ResourcePoint> join = root.join(orderStatisticDS.getTypeStatistic(), JoinType.INNER);
         User user = Utils.getUserAuthentication();
         Long role = user.getRole().getId();

         if (role == Permission.COMPANY_ADMIN) {
            if (orderStatisticDS.getTypePoint().equals(Type.TRANSACTION_POINT_LOCATION)) {
               if (orderStatisticDS.getPointId() != null) {
                  Join<OrderDelivery, ResourcePoint> joinCurrentLocation = root.join("currentLocation", JoinType.INNER);
                  predicates.add(cb.equal(joinCurrentLocation.get("id"), orderStatisticDS.getPointId()));

                  if (orderStatisticDS.getTypeResource().equals("sender")) {
                     predicates.add(cb.equal(join.get("typeResource"), Type.SENDER_CUSTOMER));
                  } else {
                     predicates.add(cb.notEqual(join.get("typeResource"), Type.SENDER_CUSTOMER));
                  }
               } else {
                  Join<OrderDelivery, ResourcePoint> joinCurrentLocation = root.join("currentLocation", JoinType.INNER);
                  predicates.add(cb.equal(joinCurrentLocation.get("typeResource"), Type.TRANSACTION_POINT_LOCATION));

                  if (orderStatisticDS.getTypeResource().equals("sender")) {
                     predicates.add(cb.equal(join.get("typeResource"), Type.SENDER_CUSTOMER));
                  } else {
                     predicates.add(cb.notEqual(join.get("typeResource"), Type.SENDER_CUSTOMER));
                  }
               }
            } else if (orderStatisticDS.getTypePoint().equals(Type.GATHERING_POINT_LOCATION)) {
               if (orderStatisticDS.getPointId() != null) {
                  predicates.add(cb.equal(join.get("id"), orderStatisticDS.getPointId()));
               } else {
                  predicates.add(cb.equal(join.get("typeResource"), Type.GATHERING_POINT_LOCATION));
               }
            }
         } else if (role == Permission.TRANSACTION_ADMIN) {
            Join<OrderDelivery, ResourcePoint> joinCurrentLocation = root.join("currentLocation", JoinType.INNER);
            predicates.add(cb.equal(joinCurrentLocation.get("id"), user.getWorkAt().getId()));

            if (orderStatisticDS.getTypeResource().equals("sender")) {
               predicates.add(cb.equal(join.get("typeResource"), Type.SENDER_CUSTOMER));
            } else {
               predicates.add(cb.notEqual(join.get("typeResource"), Type.SENDER_CUSTOMER));
            }
         } else if (role == Permission.TRANSACTION_STAFF) {
            Join<OrderDelivery, ResourcePoint> joinCurrentLocation = root.join("currentLocation", JoinType.INNER);
            predicates.add(cb.equal(joinCurrentLocation.get("id"), user.getWorkAt().getId()));
            predicates.add(cb.equal(root.get("status"), orderStatisticDS.getStatusComplete()));
         } else if (role == Permission.GATHERING_ADMIN) {
            predicates.add(cb.equal(join.get("id"), user.getWorkAt().getId()));
         }

         Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
         return predicate;
      };
   }

   public static Specification<OrderDelivery> getAllOrderDeliveryByPage(OrderDS orderDS) {
      return (root, query, cb) -> {
         List<Predicate> predicates = new ArrayList();

         User user = Utils.getUserAuthentication();
         ResourcePoint resourcePoint = user.getWorkAt();
         Join<OrderDelivery, Order> joinOrder = root.join("order", JoinType.INNER);

         if (orderDS.getOrderId() != null) {
            predicates.add(cb.like(joinOrder.get("orderId").as(String.class), "%" + orderDS.getOrderId() + "%"));
         }

         if (orderDS.getCategorityOrder() != null) {
            predicates.add(cb.equal(joinOrder.get("categority"), orderDS.getCategorityOrder()));
         }

         if (orderDS.getSenderName() != null) {
            Join<Order, Customer> joinSender = joinOrder.join("sender", JoinType.INNER);
            predicates.add(cb.like(joinSender.get("name"), "%" + orderDS.getSenderName() + "%"));
         }

         if (orderDS.getSenderAddress() != null) {
            Join<Order, Customer> joinSender = joinOrder.join("sender", JoinType.INNER);
            predicates.add(cb.like(joinSender.get("address"), "%" + orderDS.getSenderAddress() + "%"));
         }

         if (orderDS.getReceiverName() != null) {
            Join<Order, Customer> joinReceiver = joinOrder.join("receiver", JoinType.INNER);
            predicates.add(cb.like(joinReceiver.get("name"), "%" + orderDS.getReceiverName() + "%"));
         }

         if (orderDS.getReceiverAddress() != null) {
            Join<Order, Customer> joinReceiver = joinOrder.join("receiver", JoinType.INNER);
            predicates.add(cb.like(joinReceiver.get("address"), "%" + orderDS.getReceiverAddress() + "%"));
         }

         if (orderDS.getCreateAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(joinOrder.get("createAt"), orderDS.getCreateAtFrom()));
         }

         if (orderDS.getCreateAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(joinOrder.get("createAt"), orderDS.getCreateAtTo()));
         }
         predicates.add(cb.equal(root.get("typeOrder"), orderDS.getTypeOrder()));
         if (orderDS.getDeliveryStatus() == OrderStatus.COMPLETED_ORDER) {
            Predicate predicateForSucccessOrder = cb.equal(root.get("status"), OrderStatus.SUCCESS_DELIVERY);
            Predicate predicateForUnSucccessOrder = cb.equal(root.get("status"), OrderStatus.UNSUCESS_DELIVERY);
            predicates.add(cb.or(predicateForSucccessOrder, predicateForUnSucccessOrder));
         } else {
            predicates.add(cb.equal(root.get("status"), orderDS.getDeliveryStatus()));
         }

         if (orderDS.getFromAddress() != null) {
            Join<OrderDelivery, ResourcePoint> joinPoint = root.join("fromLocation", JoinType.INNER);
            predicates.add(cb.like(
                  cb.function("unaccent", String.class, cb.lower(joinPoint.get("address"))),
                  "%" + StringUtils.stripAccents(orderDS.getFromAddress().toLowerCase()) + "%"));
         }

         if (orderDS.getToAddress() != null) {
            Join<OrderDelivery, ResourcePoint> joinPoint = root.join("toLocation", JoinType.INNER);
            predicates.add(cb.like(
                  cb.function("unaccent", String.class, cb.lower(joinPoint.get("address"))),
                  "%" + StringUtils.stripAccents(orderDS.getToAddress().toLowerCase()) + "%"));
         }

         Join<OrderDelivery, ResourcePoint> joinResourcePoint = root.join("currentLocation", JoinType.INNER);
         predicates.add(cb.equal(joinResourcePoint.get("id"), resourcePoint.getId()));

         Predicate predicate = cb.and(predicates.toArray(new Predicate[0]));
         return predicate;
      };
   }
}
