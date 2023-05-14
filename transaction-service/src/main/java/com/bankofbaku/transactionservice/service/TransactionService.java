package com.bankofbaku.transactionservice.service;

import com.bankofbaku.common.models.TransactionDto;
import com.bankofbaku.common.models.TransactionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface TransactionService {
    @Transactional
    TransactionDto addTransaction(TransactionDto request);
//
//    List<TransactionDto> getTransactionByReceiverAccount(Long receiverId);
//    List<TransactionDto> getTransactionBySenderAccount(Long senderId);
    Double getAmountByAccount(Double id);
}