package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.mapper.RoleMapper;
import com.alphasolutions.piauieventos.mapper.UserMapper;
import com.alphasolutions.piauieventos.model.UserModel;
import com.alphasolutions.piauieventos.model.UserRoleModel;
import com.alphasolutions.piauieventos.repository.UserRepository;
import com.alphasolutions.piauieventos.repository.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, UserMapper userMapper, RoleMapper roleMapper) {
        this.userRepository    = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userMapper        = userMapper;
        this.roleMapper        = roleMapper;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        //

    }


}
