package com.bankofbaku.common.repositories;

import com.bankofbaku.common.entities.Account;
import com.bankofbaku.common.enums.AccountType;
import com.bankofbaku.common.models.AccountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountCode(Long accountCode);

    Account findByAccountId(Long accountId);
    List<Account> findByClientClientId(Long clientId);
    List<Account> findByAccountType(AccountType accountType);
    List<Account> findAll();
}
