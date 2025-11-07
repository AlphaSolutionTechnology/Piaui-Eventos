package com.alphasolutions.piauieventos.service.user;

import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.dto.UserCreationResultDTO;
import com.alphasolutions.piauieventos.dto.UserUpdateDTO;
import com.alphasolutions.piauieventos.dto.PasswordUpdateDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);
    UserCreationResultDTO createUserWithTokens(UserRequestDTO dto);
    UserResponseDTO getCurrentUser();
    UserResponseDTO updateUser(UserUpdateDTO dto);
    void updatePassword(PasswordUpdateDTO dto);
}
