package com.bankofbaku.clientservice.service;

import com.bankofbaku.common.models.ClientDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ClientService {
    ClientDto addClient(ClientDto clientDto) throws Exception;
    @Transactional
    ClientDto getClientById(Long clientId);
    ClientDto getClientByUsername(String username);
    List<ClientDto> getAllClients();
}
