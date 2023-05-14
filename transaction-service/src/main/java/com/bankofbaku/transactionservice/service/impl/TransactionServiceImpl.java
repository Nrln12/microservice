package com.bankofbaku.transactionservice.service.impl;

import com.bankofbaku.common.entities.Account;
import com.bankofbaku.common.entities.Transaction;
import com.bankofbaku.common.enums.OperationType;
import com.bankofbaku.common.enums.Status;
import com.bankofbaku.common.exceptions.NotFoundException;
import com.bankofbaku.common.models.AccountDto;
import com.bankofbaku.common.models.TransactionDto;
import com.bankofbaku.common.models.TransactionRequest;
import com.bankofbaku.common.repositories.TransactionRepository;
import com.bankofbaku.transactionservice.config.AccountClient;
import com.bankofbaku.transactionservice.service.TransactionService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper mapper;
    private final AccountClient accountClient;
    public TransactionServiceImpl(TransactionRepository transactionRepository, ModelMapper mapper, AccountClient accountClient){
        this.transactionRepository=transactionRepository;
        this.mapper=mapper;
        this.accountClient=accountClient;
    }

//    @Override
//    public List<TransactionDto> getTransactionByReceiverAccount(Long receiverId) {
//        List<Transaction> transactions = transactionRepository.getTransactionByReceiverAccount(receiverId);
//        if(transactions.isEmpty()) throw new NotFoundException("Invalid receiver account id");
//        List<TransactionDto> transactionDtos = transactions.stream()
//                .filter(transaction -> transaction.getStatus().equals(Status.SUCCESS))
//                .map(transaction -> mapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
//        transactionDtos.forEach(transactionDto -> transactionDto.setOperationType(OperationType.DEBIT));
//        return transactionDtos;
//    }
//
//    @Override
//    public List<TransactionDto> getTransactionBySenderAccount(Long senderId) {
//        List<Transaction> transactions = transactionRepository.getTransactionBySenderAccount(senderId);
//        if(transactions.isEmpty()) throw new NotFoundException("Invalid sender account id");
//        List<TransactionDto> transactionDtos = transactions.stream()
//                .filter(transaction -> transaction.getStatus().equals(Status.SUCCESS))
//                .map(transaction -> mapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
//        transactionDtos.forEach(transactionDto -> transactionDto.setOperationType(OperationType.CREDIT));
//        return transactionDtos;
//    }


    @Override
    @SneakyThrows
    public TransactionDto addTransaction(TransactionDto request) {
        Optional<ResponseEntity<AccountDto>> sender = Optional.ofNullable(accountClient.getByAccountId(request.getSenderAccount()));
        if(sender.isEmpty()) throw new NotFoundException("Sender account doesn't exist");
        Optional<ResponseEntity<AccountDto>> receiver = Optional.ofNullable(accountClient.getByAccountId(request.getReceiverAccount()));
        if(receiver.isEmpty()) throw new NotFoundException("Receiver account doesn't exist");
        if(sender.get().getBody().getBalance() < request.getAmount()) throw new Exception("Sender doesn't have enough money");
        Transaction transaction;
        try{
            Double senderCurrBalance = sender.get().getBody().getBalance();
            Double receiverCurrBalance = receiver.get().getBody().getBalance();
            System.out.println("receiver balance -> "+receiverCurrBalance);
            sender.get().getBody().setBalance(senderCurrBalance- request.getAmount());
            accountClient.updateAccount(request.getSenderAccount(), sender.get().getBody());
            receiver.get().getBody().setBalance(receiverCurrBalance+request.getAmount());
            accountClient.updateAccount(request.getReceiverAccount(), receiver.get().getBody());
            request.setDate(LocalDateTime.now());
            request.setStatus(Status.SUCCESS);
            transaction=mapper.map(request, Transaction.class);
//            transaction = TransactionDto.builder()
//                    .date(LocalDateTime.now())
//                    .status(Status.SUCCESS)
//                    .senderAccount(request.getSenderAccount())
//                    .receiverAccount(request.getReceiverAccount())
//                    .amount(request.getAmount())
//                    .operationType(OperationType.DEBIT)
//                    .build();
            transactionRepository.save(transaction);
        }catch (Exception ex){
            throw new Exception(ex);
        }
        return request;
    }

    @Override
    public Double getAmountByAccount(Double id) {
        return transactionRepository.getAmountByAccount(id);
    }


}