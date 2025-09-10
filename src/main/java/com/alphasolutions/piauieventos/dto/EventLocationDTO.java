package com.alphasolutions.piauieventos.dto;


public record EventLocationDTO(
                               String placeName,
                               String latitude,
                               String longitude,
                               String fullAddress,
                               String zipCode,
                               String category) {

}
