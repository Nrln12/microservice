package com.bankofbaku.transactionservice.config;

import com.bankofbaku.common.entities.Account;
import com.bankofbaku.common.models.AccountDto;
import com.bankofbaku.common.models.ClientDto;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "account-service")
public interface AccountClient {
    @GetMapping("api/account/accountId/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountDto> getByAccountId(@PathVariable(name = "accountId") Long accountId);

    @PutMapping("api/account/updateAccount/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateAccount(@PathVariable Long accountId, @RequestBody AccountDto accountDto);
}

