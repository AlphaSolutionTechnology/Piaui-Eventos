package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.ViaCepResponse;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
public class ViaCepServiceImpl implements  ViaCepService {
    private final RestTemplate restTemplate;

    public ViaCepServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ViaCepResponse getAddressByCep(String cep) {
        if(cep.length() != 8){;
            throw new IllegalArgumentException("CEP deve conter exatamente 8 dígitos.");
        }
        return restTemplate.getForObject("https://viacep.com.br/ws/" + cep + "/json/", ViaCepResponse.class);


    }
}
