package com.bankofbaku.clientservice.service.impl;

import com.bankofbaku.clientservice.service.ClientService;
import com.bankofbaku.common.entities.Client;
import com.bankofbaku.common.exceptions.BadRequestException;
import com.bankofbaku.common.exceptions.IsNotValidException;
import com.bankofbaku.common.exceptions.NotFoundException;
import com.bankofbaku.common.models.ClientDto;
import com.bankofbaku.common.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapper mapper;
    public ClientServiceImpl(ClientRepository clientRepository,ModelMapper mapper){
        this.clientRepository=clientRepository;
        this.mapper=mapper;
    }
    public ClientDto getClientById(Long id){
        Optional<Client> client = clientRepository.findById(id);
        if(!client.isPresent()) throw new NotFoundException("User has not found");
        return mapper.map(client, ClientDto.class);
    }
    @Override
    public ClientDto getClientByUsername(String username) {
        Optional<Client> client = Optional.ofNullable(clientRepository.findClientByUsername(username));
        if(!client.isPresent()) throw new NotFoundException("User has not found");
        return mapper.map(client, ClientDto.class);
    }

    @Override
    public List<ClientDto> getAllClients() {
        List<ClientDto> clientDtos = clientRepository.findAll().stream().map(client -> mapper.map(client, ClientDto.class)).collect(Collectors.toList());
        if (clientDtos.isEmpty()) throw new NotFoundException("There is no client");
        return clientDtos;
    }

    @Override
    public ClientDto addClient(ClientDto clientDto) throws Exception {
        Optional<Client> checkClient = Optional.ofNullable(clientRepository.findClientByUsername(clientDto.getUsername()));
        if (checkClient.isPresent()) throw new BadRequestException("The username has already taken.");
        try {
            if (!isValidUsername(clientDto.getUsername())) throw new IsNotValidException("The username is not valid");
            if (!isValidPassword(clientDto.getPassword())) throw new IsNotValidException("The password is not valid");
            clientDto.setPassword(encodePassword(clientDto.getPassword()));
            clientRepository.save(mapper.map(clientDto, Client.class));
        }catch (Exception ex){
            throw new Exception(ex);
        }
        return clientDto;
    }
    public boolean isValidUsername(String username){
        String regex = "^[a-zA-Z0-9._-]{3,}$";
        return username.matches(regex) ? true : false;
    }
    public boolean isValidPassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        return password.matches(regex) ? true : false;
    }
    public String encodePassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}