package com.bankofbaku.common.models;

import com.bankofbaku.common.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long clientId;
    private Long accountCode;
    private LocalDateTime openDate;
    private AccountType accountType;
    private Double balance;
}
