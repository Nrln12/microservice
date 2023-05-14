package com.bankofbaku.common.models;

import com.bankofbaku.common.enums.OperationType;
import com.bankofbaku.common.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionRequest {
    private Double amount;
    private Long senderAccount;
    private Long receiverAccount;
}