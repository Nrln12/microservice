package com.bankofbaku.common.models;

import com.bankofbaku.common.enums.OperationType;
import com.bankofbaku.common.enums.Status;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDto {
    private LocalDateTime date;
    private Double amount;
    private Status status;
    private Long senderAccount;
    private Long receiverAccount;
    private OperationType operationType;
}