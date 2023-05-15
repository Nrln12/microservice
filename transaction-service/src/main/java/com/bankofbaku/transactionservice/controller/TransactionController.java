package com.bankofbaku.transactionservice.controller;

import com.bankofbaku.common.models.TransactionDto;
import com.bankofbaku.common.models.TransactionRequest;
import com.bankofbaku.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    @PostMapping("/add-transaction")
    public ResponseEntity addTransaction(@RequestBody TransactionDto request){
        return ResponseEntity.ok().body(transactionService.addTransaction(request));
    }

    @GetMapping("/receiverId/{receiverId}")
    public ResponseEntity getTransactionByReceiverId(@PathVariable("receiverId") Long receiverId){
        return ResponseEntity.ok().body(transactionService.getTransactionByReceiverAccount(receiverId));
    }
    @GetMapping("/senderId/{senderId}")
    public ResponseEntity getTransactionBySenderAccountAccountId(@PathVariable("senderId") Long senderId){
        return ResponseEntity.ok().body(transactionService.getTransactionBySenderAccount(senderId));
    }
    @GetMapping("/getAmount/{id}")
    public ResponseEntity getCirc(@PathVariable Double id){
        return ResponseEntity.ok().body(transactionService.getAmountByAccount(id));
    }
    @GetMapping("/receiver-waiting-transactions/{receiverId}")
    public ResponseEntity getReceiverAccountWaitingTransactions(@PathVariable Long receiverId){
        return ResponseEntity.ok().body(transactionService.getReceiverAccountWaitingTransactions(receiverId));
    }
    @PutMapping("/reverse-money/{transactionId}")
    public ResponseEntity reverseMoney(@PathVariable Long transactionId){
        return ResponseEntity.ok().body(transactionService.reverseTransaction(transactionId));
    }

    @PutMapping("/accept-money/{transactionId}")
    public ResponseEntity acceptTransaction (@PathVariable Long transactionId){
        return ResponseEntity.ok().body(transactionService.acceptTransaction(transactionId));
    }
    @GetMapping("/{transactionId}")
    public ResponseEntity getByTransactionId(@PathVariable Long transactionId){
        return ResponseEntity.ok().body(transactionService.getByTransactionId(transactionId));
    }

}
