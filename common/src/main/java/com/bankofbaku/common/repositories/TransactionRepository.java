package com.bankofbaku.common.repositories;

import com.bankofbaku.common.entities.Transaction;
import com.bankofbaku.common.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
     List<Transaction> getTransactionByReceiverAccountAccountId(Long receiverId);
    List<Transaction> getTransactionBySenderAccountAccountId(Long senderId);
    Transaction getByTransactionId(Long transactionId);
    List<Transaction> getTransactionByReceiverAccountAccountIdAndStatus(Long receiverId, Status status);
    @Query(value = "select getCirc(?)", nativeQuery = true)
    Double getAmountByAccount(Double id);
}
