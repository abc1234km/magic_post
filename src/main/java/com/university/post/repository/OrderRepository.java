package com.university.post.repository;

import com.university.post.model.DeliveryPoint;
import com.university.post.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    @Query("SELECT MAX(o.id) FROM Order o")
    Long findMaxOrderId();

    @Query(value = "SELECT nextval('orders_id_seq')", nativeQuery = true)
    Long getNextId();

}
