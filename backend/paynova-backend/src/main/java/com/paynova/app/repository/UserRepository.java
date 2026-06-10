package com.paynova.app.repository;

import com.paynova.app.entity.User;
import com.paynova.app.entity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByUpiId(String upiId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Page<User> findAllByStatus(UserStatus status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:query% OR u.email LIKE %:query% OR u.phone LIKE %:query%")
    Page<User> searchUsers(@Param("query") String query, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    long countByStatus(@Param("status") UserStatus status);
}
