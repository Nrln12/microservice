package com.bankofbaku.accountservice.service;
import com.bankofbaku.common.models.AccountDto;
import com.bankofbaku.common.enums.AccountType;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface AccountService {
    @Transactional
    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountByAccountCode(Long accountCode);
    AccountDto getByAccountId(Long accountId);
    List<AccountDto> getAccountByAccountType(AccountType type);
    List<AccountDto> getAllAccounts();
    List<AccountDto> getByClientId(Long clientId);
    @Transactional
    AccountDto updateAccount(Long accountId, AccountDto accountDto);
}
