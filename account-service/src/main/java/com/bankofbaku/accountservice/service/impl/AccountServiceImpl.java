package com.bankofbaku.accountservice.service.impl;

import com.bankofbaku.accountservice.config.ClientServiceClient;
import com.bankofbaku.common.entities.Account;
import com.bankofbaku.common.exceptions.BadRequestException;
import com.bankofbaku.common.exceptions.IsNotValidException;
import com.bankofbaku.common.exceptions.NotFoundException;
import com.bankofbaku.common.models.AccountDto;

import com.bankofbaku.common.models.ClientDto;
import com.bankofbaku.common.repositories.AccountRepository;
import com.bankofbaku.accountservice.service.AccountService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.bankofbaku.common.enums.AccountType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final ModelMapper mapper;
    private final AccountRepository accountRepository;
    private final ClientServiceClient client;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper mapper, ClientServiceClient client) {
        this.accountRepository = accountRepository;
        this.mapper = mapper;
        this.client=client;
    }

    @Override
    @SneakyThrows
    public AccountDto createAccount(AccountDto accountDto) {
       Optional<Account> checkAccount = Optional.ofNullable(accountRepository.findByAccountCode(accountDto.getAccountCode()));
        if (checkAccount.isPresent()) throw new BadRequestException("Account has already existed");
        Optional<ResponseEntity<ClientDto>> response = Optional.ofNullable(client.getClientById(accountDto.getClientId()));
        if(response.isEmpty()) throw new NotFoundException("You cannot create account with this id, client doesn't exist");

        try {
            accountDto.setAccountCode(randomNumberGenerator());
            accountDto.setOpenDate(LocalDateTime.now());
            accountRepository.save(mapper.map(accountDto, Account.class));
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return accountDto;
    }
    @Override
    @SneakyThrows
    public AccountDto getAccountByAccountCode(Long accountCode) {
        Optional<Account> checkAccount = Optional.ofNullable(accountRepository.findByAccountCode(accountCode));
        if (checkAccount.isEmpty()) throw new NotFoundException("Account has not found");
        return mapper.map(checkAccount.get(), AccountDto.class);
    }
    @Override
    public AccountDto getByAccountId(Long accountId){
        Optional<Account> account = Optional.ofNullable(accountRepository.findByAccountId(accountId));
        if(account.isEmpty()) throw new NotFoundException("Account has not found");
        return mapper.map(account.get(), AccountDto.class);
    }

    @Override // DUZELT
    public List<AccountDto> getAccountByAccountType(AccountType type) {
        if (!isValidAccountType(type)) throw new IsNotValidException("This account type doesn't exist");
        if (accountRepository.findByAccountType(type).isEmpty())
            throw new NotFoundException("We have no users in this type.");
        return accountRepository.findByAccountType(type).stream().map(account -> mapper.map(account, AccountDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(account -> mapper.map(account, AccountDto.class)).collect(Collectors.toList());
    }

//    @Override
//    public List<AccountDto> getByClientId(Long clientId) {
//        List<Account> accountDtoList = accountRepository.findByClientId(clientId);
//        if (accountDtoList.isEmpty()) throw new NotFoundException("This client doesn't have account");
//        return accountDtoList.stream().map(account -> mapper.map(account, AccountDto.class)).collect(Collectors.toList());
//    }

    @Override
    @SneakyThrows
    public AccountDto updateAccount(Long accountId, AccountDto accountDto) {
        Optional<Account> checkAccount = Optional.ofNullable(accountRepository.findByAccountId(accountId));
        if(checkAccount.isEmpty()) throw new NotFoundException("Account has not found");
        try{
            checkAccount.get().setAccountCode(accountDto.getAccountCode());
            checkAccount.get().setAccountType(accountDto.getAccountType());
            checkAccount.get().setBalance(accountDto.getBalance());
            accountRepository.save(checkAccount.get());
        }catch (Exception ex){
            throw new Exception(ex);
        }
        return mapper.map(checkAccount, AccountDto.class);
    }

    public boolean isValidAccountType(AccountType type) {
        return type.getClass().equals(AccountType.class);
    }

    public Long randomNumberGenerator() {
        Random random = new Random();
        return Math.abs(random.nextLong());
    }
}