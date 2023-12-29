package com.university.post.repository;

import com.university.post.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query(value = "SELECT nextval('customers_id_seq')", nativeQuery = true)
    Long getNextId();
}
