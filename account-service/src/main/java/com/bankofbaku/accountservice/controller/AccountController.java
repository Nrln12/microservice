package com.bankofbaku.accountservice.controller;
import com.bankofbaku.common.models.AccountDto;
import com.bankofbaku.common.enums.AccountType;
import com.bankofbaku.accountservice.service.AccountService;
import com.bankofbaku.common.models.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("add-account")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createAccount(@RequestBody AccountDto accountDto) throws Exception {
        return ResponseEntity.ok().body(accountService.createAccount(accountDto));
    }
    @GetMapping("/accountId/{accountId}")
    public ResponseEntity<AccountDto> getByAccountId(@PathVariable Long accountId){
        return ResponseEntity.ok().body(accountService.getByAccountId(accountId));
    }
    @GetMapping("/accountCode/{accountCode}")
    public ResponseEntity getAccountByAccountCode(@PathVariable Long accountCode){
        return ResponseEntity.ok().body(accountService.getAccountByAccountCode(accountCode));
    }

    @GetMapping("/accountType")
    public ResponseEntity getAccountByType(@RequestParam(name = "accountType") AccountType accountType){
        return ResponseEntity.ok().body(accountService.getAccountByAccountType(accountType));
    }
    @GetMapping("/allAccounts")
    public ResponseEntity getAllAccounts(){
        return ResponseEntity.ok().body(accountService.getAllAccounts());
    }

//    @GetMapping("/clientId/{clientId}")
//    public ResponseEntity getByClientId(@PathVariable Long clientId){
//        return ResponseEntity.ok().body(accountService.getByClientId(clientId));
//    }

    @PutMapping("/updateAccount/{accountId}")
    public ResponseEntity updateAccount(@PathVariable Long accountId, @RequestBody AccountDto accountDto){
        return ResponseEntity.ok().body(accountService.updateAccount(accountId,accountDto));
    }
}