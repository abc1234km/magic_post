package com.university.post.repository;

import com.university.post.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findByUserId(Integer userId);

    @Query(value = "SELECT nextval('users_id_seq')", nativeQuery = true)
    Long getNextId();
}
