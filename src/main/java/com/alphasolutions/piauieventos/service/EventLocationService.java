package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.model.EventLocation;
import org.springframework.stereotype.Service;

@Service
public interface EventLocationService {
    EventLocation addLocation(EventLocation eventLocation);
}
