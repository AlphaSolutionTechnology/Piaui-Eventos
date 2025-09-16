package com.alphasolutions.piauieventos.service.user;

import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.dto.UserCreationResultDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);
    UserCreationResultDTO createUserWithTokens(UserRequestDTO dto);
}
