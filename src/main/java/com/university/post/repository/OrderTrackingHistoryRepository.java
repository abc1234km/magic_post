package com.university.post.repository;

import com.university.post.model.OrderTrackingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OrderTrackingHistoryRepository extends JpaRepository<OrderTrackingHistory, Long> {
    @Query(value = "SELECT ot FROM OrderTrackingHistory ot INNER JOIN ot.order o WHERE o.orderId = :orderId")
    List<OrderTrackingHistory> findMessageStatusByOrderId(String orderId);
}
