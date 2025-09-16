package com.alphasolutions.piauieventos.service.auth;

import com.alphasolutions.piauieventos.dto.LoginRequestDTO;
import com.alphasolutions.piauieventos.dto.LoginResponseDTO;
import com.alphasolutions.piauieventos.dto.RefreshRequestDTO;
import com.alphasolutions.piauieventos.dto.RefreshResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
    RefreshResponseDTO refresh(RefreshRequestDTO request);
}
