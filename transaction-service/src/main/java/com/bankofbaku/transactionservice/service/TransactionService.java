package com.bankofbaku.transactionservice.service;

import com.bankofbaku.common.entities.Transaction;
import com.bankofbaku.common.models.TransactionDto;
import com.bankofbaku.common.models.TransactionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {
    @Transactional
    TransactionDto addTransaction(TransactionDto request);
    List<TransactionDto> getTransactionByReceiverAccount(Long receiverId);
    List<TransactionDto> getTransactionBySenderAccount(Long senderId);
    List<TransactionDto> getReceiverAccountWaitingTransactions (Long receiverId);
    Transaction reverseTransaction(Long transactionId);
    Transaction acceptTransaction(Long transactionId);
    Optional<Transaction> getByTransactionId(Long transactionId);
    Double getAmountByAccount(Double id);
}