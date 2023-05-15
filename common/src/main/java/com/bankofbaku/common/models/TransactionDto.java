package com.bankofbaku.common.models;

import com.bankofbaku.common.enums.OperationType;
import com.bankofbaku.common.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TransactionDto {
    private LocalDateTime date;
    private Double amount;
    private Status status;
    private Long senderAccountId;
    private Long receiverAccountId;
    private OperationType operationType;
}