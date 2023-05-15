package com.bankofbaku.transactionservice.service.impl;

import com.bankofbaku.common.entities.Account;
import com.bankofbaku.common.entities.Transaction;
import com.bankofbaku.common.enums.OperationType;
import com.bankofbaku.common.enums.Status;
import com.bankofbaku.common.exceptions.BadRequestException;
import com.bankofbaku.common.exceptions.NotFoundException;
import com.bankofbaku.common.models.AccountDto;
import com.bankofbaku.common.models.TransactionDto;
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

    @Override
    public List<TransactionDto> getTransactionByReceiverAccount(Long receiverId) {
        List<Transaction> transactions = transactionRepository.getTransactionByReceiverAccountAccountId(receiverId);
        if(transactions.isEmpty()) throw new NotFoundException("Invalid receiver account id");
        List<TransactionDto> transactionDtos = transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(Status.SUCCESS))
                .map(transaction -> mapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
        transactionDtos.forEach(transactionDto -> transactionDto.setOperationType(OperationType.DEBIT));
        return transactionDtos;
    }

    @Override
    public List<TransactionDto> getTransactionBySenderAccount(Long senderId) {
        List<Transaction> transactions = transactionRepository.getTransactionBySenderAccountAccountId(senderId);
        if(transactions.isEmpty()) throw new NotFoundException("Invalid sender account id");
        List<TransactionDto> transactionDtos = transactions.stream()
                .filter(transaction -> transaction.getStatus().equals(Status.SUCCESS))
                .map(transaction -> mapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
        transactionDtos.forEach(transactionDto -> transactionDto.setOperationType(OperationType.CREDIT));
        return transactionDtos;
    }

    @Override
    @SneakyThrows
    public TransactionDto addTransaction(TransactionDto transactionDto) {
        Optional<ResponseEntity<AccountDto>> sender = Optional.ofNullable(accountClient.getByAccountId(transactionDto.getSenderAccountId()));
        Account senderAccount = mapper.map(sender.get().getBody(), Account.class);
        senderAccount.setAccountId(transactionDto.getSenderAccountId());
        if(sender.isEmpty()) throw new NotFoundException("Sender account doesn't exist");
        Optional<ResponseEntity<AccountDto>> receiver = Optional.ofNullable(accountClient.getByAccountId(transactionDto.getReceiverAccountId()));
        Account receiverAccount = mapper.map(receiver.get().getBody(), Account.class);
        receiverAccount.setAccountId(transactionDto.getReceiverAccountId());
        if(receiver.isEmpty()) throw new NotFoundException("Receiver account doesn't exist");
        if(sender.get().getBody().getBalance() < transactionDto.getAmount()) throw new Exception("Sender doesn't have enough money");
        Transaction transaction;
        try{
           System.out.println("sender: "+ sender);
            System.out.println("sender account: "+ senderAccount);
            System.out.println("receiver account: "+ receiverAccount);
                transaction = Transaction.builder()
                    .date(LocalDateTime.now())
                    .status(Status.WAITING)
                    .senderAccount(senderAccount)
                    .receiverAccount(receiverAccount)
                    .amount(transactionDto.getAmount())
                    .build();
            transactionRepository.save(transaction);
        }catch (Exception ex){
            throw new Exception(ex);
        }
        TransactionDto response = mapper.map(transaction, TransactionDto.class);
        response.setSenderAccountId(transaction.getSenderAccount().getAccountId());
        response.setReceiverAccountId(transaction.getReceiverAccount().getAccountId());
        return response;
    }
    @Override
    public List<TransactionDto> getReceiverAccountWaitingTransactions(Long receiverId) {
        List<Transaction> transactions = transactionRepository.getTransactionByReceiverAccountAccountIdAndStatus(receiverId, Status.WAITING);
        List<TransactionDto> transactionDtos = transactions.stream().map(transaction -> mapper.map(transaction, TransactionDto.class)).collect(Collectors.toList());
        transactionDtos.forEach(transactionDto -> transactionDto.setOperationType(OperationType.DEBIT));
        return transactionDtos;
    }

    @Override
    public Optional<Transaction> getByTransactionId(Long transactionId){
        Optional<Transaction> transaction = Optional.ofNullable(transactionRepository.getByTransactionId(transactionId));
        if(transaction.isEmpty()) throw new NotFoundException("Transaction has not found.");
        return transaction;
    }

    @SneakyThrows
    @Override
    public Transaction reverseTransaction(Long transactionId){
       Optional<Transaction> transaction = getByTransactionId(transactionId);
       if(transaction.isEmpty()) throw new NotFoundException("Transaction has not found.");
        try{
            if(transaction.get().getStatus()==Status.REVERSED) throw new BadRequestException("Transaction has already reversed.");
           transaction.get().setStatus(Status.REVERSED);
           transactionRepository.save(transaction.get());
        }catch (Exception ex){
            throw new Exception(ex);
        }
        return transaction.get();
    }

    @Override
    @SneakyThrows
    public Transaction acceptTransaction(Long transactionId) {
        Optional<Transaction> transaction = getByTransactionId(transactionId);
        if(transaction.isEmpty()) throw new NotFoundException("Transaction has not found.");
        if(transaction.get().getStatus()!= Status.WAITING) throw new BadRequestException("This transaction is not waiting");
        try{
            Account senderAccount = transaction.get().getSenderAccount();
            Account receiverAccount = transaction.get().getReceiverAccount();
            // sender-den pul chixma
            Double senderCurrBalance = senderAccount.getBalance();
            senderAccount.setBalance(senderCurrBalance-transaction.get().getAmount());
            AccountDto senderAcc = mapper.map(senderAccount, AccountDto.class);
            accountClient.updateAccount(senderAccount.getAccountId(), senderAcc);
            // receiver-e pul kochurme
            Double receiverCurrBalance = receiverAccount.getBalance();
            receiverAccount.setBalance(receiverCurrBalance+transaction.get().getAmount());
            AccountDto receiverAcc = mapper.map(receiverAccount, AccountDto.class);
            accountClient.updateAccount(receiverAccount.getAccountId(), receiverAcc);
            transactionRepository.save(transaction.get());
            // update transaction status
            transaction.get().setStatus(Status.SUCCESS);
            transactionRepository.save(transaction.get());
        }catch (Exception ex){
            throw new Exception(ex);
        }
        return transaction.get();
    }


    //    public void AcceptMoney(TransactionDto transactionDto){
//        Double senderCurrBalance = sender.get().getBody().getBalance();
//        Double receiverCurrBalance = receiver.get().getBody().getBalance();
//        sender.get().getBody().setBalance(senderCurrBalance- transactionDto.getAmount());
//        accountClient.updateAccount(transactionDto.getSenderAccountId(), sender.get().getBody());
//        receiver.get().getBody().setBalance(receiverCurrBalance+transactionDto.getAmount());
//        accountClient.updateAccount(transactionDto.getReceiverAccountId(), receiver.get().getBody());
//    }
    @Override
    public Double getAmountByAccount(Double id) {
        return transactionRepository.getAmountByAccount(id);
    }


}