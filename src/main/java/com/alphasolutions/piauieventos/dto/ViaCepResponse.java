package com.alphasolutions.piauieventos.dto;

public record ViaCepResponse(String cep,
                             String logradouro,
                             String complemento,
                             String bairro,
                             String localidade,
                             String uf
                             )
{}
