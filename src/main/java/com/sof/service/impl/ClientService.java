package com.sof.service.impl;

import com.sof.dto.ClientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final RestTemplate restTemplate;

    public ClientDTO findClientById(String clientId) {
        String url = "http://localhost:8080/api/v1/clientes/clientId/" + clientId;
        return restTemplate.getForObject(url, ClientDTO.class);
    }
}
