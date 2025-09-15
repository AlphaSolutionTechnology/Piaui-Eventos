package com.alphasolutions.piauieventos.dto;


public record EventLocationDTO(Long id,
                               String placeName,
                               String latitude,
                               String longitude,
                               String fullAddress,
                               String zipCode,
                               String category) {

}
