package com.paynova.app.repository;

import com.paynova.app.entity.Transaction;
import com.paynova.app.entity.enums.TransactionStatus;
import com.paynova.app.entity.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);

    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :userId OR t.receiver.id = :userId ORDER BY t.createdAt DESC")
    Page<Transaction> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.sender.id = :userId OR t.receiver.id = :userId) AND t.type = :type ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.sender.id = :userId OR t.receiver.id = :userId) AND t.createdAt BETWEEN :from AND :to ORDER BY t.createdAt DESC")
    Page<Transaction> findByUserIdAndDateRange(@Param("userId") Long userId,
                                               @Param("from") LocalDateTime from,
                                               @Param("to") LocalDateTime to,
                                               Pageable pageable);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = :status")
    long countByStatus(@Param("status") TransactionStatus status);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.status = 'SUCCESS'")
    BigDecimal sumByTypeAndSuccess(@Param("type") TransactionType type);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.createdAt >= :since")
    long countTransactionsSince(@Param("since") LocalDateTime since);

    Page<Transaction> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
