package com.alphasolutions.piauieventos.service.event;

import com.alphasolutions.piauieventos.dto.EventLocationDTO;
import com.alphasolutions.piauieventos.model.EventLocation;
import org.springframework.stereotype.Service;

@Service
public interface EventLocationService {

    EventLocation addLocation(EventLocationDTO eventLocationDTO);
}
