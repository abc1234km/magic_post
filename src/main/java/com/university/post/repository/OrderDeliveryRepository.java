package com.university.post.repository;

import com.university.post.model.OrderDelivery;
import com.university.post.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderDeliveryRepository extends JpaRepository<OrderDelivery, Long>, JpaSpecificationExecutor<OrderDelivery> {
    List<OrderDelivery> findAllByStatus(Integer status);
}
