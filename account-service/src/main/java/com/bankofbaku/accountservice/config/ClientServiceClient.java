package com.bankofbaku.accountservice.config;

import com.bankofbaku.common.models.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name="client-service")
public interface ClientServiceClient {
    @GetMapping(value = "api/client/clientId/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClientDto> getClientById(@PathVariable(name = "clientId") Long id);
}
