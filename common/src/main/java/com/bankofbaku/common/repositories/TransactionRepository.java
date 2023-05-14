package com.bankofbaku.common.repositories;

import com.bankofbaku.common.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//    List<Transaction> getTransactionByReceiverAccount(Long receiverId);
//    List<Transaction> getTransactionBySenderAccount(Long senderId);
    @Query(value = "select getCirc(?)", nativeQuery = true)
    Double getAmountByAccount(Double id);
}
