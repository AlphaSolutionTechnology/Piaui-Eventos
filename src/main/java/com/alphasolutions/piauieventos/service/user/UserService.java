package com.alphasolutions.piauieventos.service.user;

import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.dto.UserCreationResultDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    UserCreationResultDTO createUserWithTokens(UserRequestDTO dto);

    @Transactional(readOnly = true)
    Map<String, Object> getCurrentUserInfo();
}
