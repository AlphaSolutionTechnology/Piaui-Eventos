package com.alphasolutions.piauieventos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    @NonNull
    private String number;
    @NonNull
    private String text;

    private Integer delay;

    @JsonProperty("linkPreview")
    private Boolean linkPreview;

}
