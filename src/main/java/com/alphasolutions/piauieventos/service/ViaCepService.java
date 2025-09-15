package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.ViaCepResponse;
import org.springframework.stereotype.Service;

@Service
public interface ViaCepService {
    ViaCepResponse getAddressByCep(String cep);
}
