package com.university.post.repository;

import com.university.post.model.DeliveryPoint;
import com.university.post.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, String>, JpaSpecificationExecutor<DeliveryPoint> {
    Optional<DeliveryPoint> findByIdAndTypeResource(String id, String typePoint);

    @Query(value = "SELECT nextval('delivery_points_id_seq')", nativeQuery = true)
    Long getNextId();
    @Query("SELECT MAX(d.resourceId) FROM DeliveryPoint d")
    Long findMaxPointId();

    List<DeliveryPoint> findAllByTypeResource(String typeResource);

    @Modifying
    @Query("UPDATE DeliveryPoint dp SET dp.gatheringPoint = null WHERE dp.gatheringPoint = :gatheringPoint")
    void deleteGatheringPointInTransactionPoint(DeliveryPoint gatheringPoint);
}
