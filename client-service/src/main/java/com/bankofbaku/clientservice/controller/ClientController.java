package com.bankofbaku.clientservice.controller;
import com.bankofbaku.common.models.ClientDto;
import com.bankofbaku.clientservice.service.ClientService;
import com.bankofbaku.common.models.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;
    @PostMapping("/add-client")
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto clientDto) throws Exception {
        return ResponseEntity.ok().body(service.addClient(clientDto));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ClientDto>> getAll(){
        return ResponseEntity.ok().body(service.getAllClients());
    }
    @GetMapping("/clientId/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClientDto> getClientById(@PathVariable(name = "clientId") Long id) {
        return ResponseEntity.ok().body(service.getClientById(id));
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<ClientDto> getClientByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(service.getClientByUsername(username));
    }

}
