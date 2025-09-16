package com.alphasolutions.piauieventos.service.user;

import com.alphasolutions.piauieventos.dto.UserCreationResultDTO;
import com.alphasolutions.piauieventos.dto.UserCreatedResponseDTO;
import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.mapper.RoleMapper;
import com.alphasolutions.piauieventos.mapper.UserMapper;
import com.alphasolutions.piauieventos.model.UserModel;
import com.alphasolutions.piauieventos.model.UserRoleModel;
import com.alphasolutions.piauieventos.repository.UserRepository;
import com.alphasolutions.piauieventos.repository.UserRoleRepository;
import com.alphasolutions.piauieventos.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, UserMapper userMapper, RoleMapper roleMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository     = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userMapper         = userMapper;
        this.roleMapper         = roleMapper;
        this.passwordEncoder    = passwordEncoder;
        this.jwtUtil           = jwtUtil;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Payload inválido");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (dto.password() == null || dto.password().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        UserModel entity = userMapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.password()));

        UserRoleModel role;
        if (dto.roleId() != null) {
            role = userRoleRepository.findById(dto.roleId().intValue())
                    .orElseThrow(() -> new IllegalArgumentException("Role não encontrada"));
        } else {
            role = userRoleRepository.findByRoleNameIgnoreCase("USER")
                    .orElseThrow(() -> new IllegalArgumentException("Perfil padrão USER não encontrado"));
        }
        entity.setUserRole(role);

        UserModel saved = userRepository.save(entity);
        return userMapper.toDto(saved);
    }

    @Override
    @Transactional
    public UserCreationResultDTO createUserWithTokens(UserRequestDTO dto) {
        UserResponseDTO userResponse = createUser(dto);

        String accessToken = jwtUtil.generateToken(dto.email());
        String refreshToken = jwtUtil.generateRefreshToken(dto.email());

        UserCreatedResponseDTO userData = new UserCreatedResponseDTO(
                userResponse.id(),
                userResponse.name(),
                userResponse.email(),
                userResponse.phoneNumber()
        );

        return new UserCreationResultDTO(userData, accessToken, refreshToken);
    }
}
