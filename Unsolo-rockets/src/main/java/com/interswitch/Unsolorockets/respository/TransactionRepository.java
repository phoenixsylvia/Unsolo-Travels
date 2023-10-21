package com.interswitch.Unsolorockets.respository;

import com.interswitch.Unsolorockets.service.payment.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByIdAndUserId(long id, Long userId);

    @Query(value = "select * from transactions where status = ?1 ", nativeQuery = true)
    List<Transaction> findByStatus(String name);
}
