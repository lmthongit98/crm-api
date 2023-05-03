package com.crm.repository;

import com.crm.common.enums.UserStatus;
import com.crm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.group g LEFT JOIN FETCH g.roles WHERE u.username = ?1")
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndStatus(Long id, UserStatus active);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.group g LEFT JOIN FETCH g.roles WHERE u.username = ?1 AND u.status = ?2")
    Optional<User> findByUsernameAndStatus(String username, UserStatus active);
}
