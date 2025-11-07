package com.alphasolutions.piauieventos.service.user;

import com.alphasolutions.piauieventos.dto.UserCreationResultDTO;
import com.alphasolutions.piauieventos.dto.UserCreatedResponseDTO;
import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.dto.UserUpdateDTO;
import com.alphasolutions.piauieventos.dto.PasswordUpdateDTO;
import com.alphasolutions.piauieventos.mapper.RoleMapper;
import com.alphasolutions.piauieventos.mapper.UserMapper;
import com.alphasolutions.piauieventos.model.UserModel;
import com.alphasolutions.piauieventos.model.UserRoleModel;
import com.alphasolutions.piauieventos.repository.UserRepository;
import com.alphasolutions.piauieventos.repository.UserRoleRepository;
import com.alphasolutions.piauieventos.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UsernameNotFoundException("Usuário não autenticado");
        }

        String email = authentication.getName();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UserUpdateDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UsernameNotFoundException("Usuário não autenticado");
        }

        String currentEmail = authentication.getName();
        UserModel user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + currentEmail));

        // Validar se o email já existe (caso esteja sendo alterado)
        if (dto.email() != null && !dto.email().equals(currentEmail)) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new IllegalArgumentException("Email já cadastrado");
            }
            user.setEmail(dto.email());
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            user.setName(dto.name());
        }

        if (dto.phoneNumber() != null) {
            user.setPhoneNumber(dto.phoneNumber());
        }

        UserModel updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void updatePassword(PasswordUpdateDTO dto) {
        if (dto.currentPassword() == null || dto.currentPassword().isBlank()) {
            throw new IllegalArgumentException("Senha atual é obrigatória");
        }
        if (dto.newPassword() == null || dto.newPassword().isBlank()) {
            throw new IllegalArgumentException("Nova senha é obrigatória");
        }
        if (dto.newPassword().length() < 6) {
            throw new IllegalArgumentException("Nova senha deve ter no mínimo 6 caracteres");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UsernameNotFoundException("Usuário não autenticado");
        }

        String email = authentication.getName();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Verificar se a senha atual está correta
        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        // Verificar se a nova senha é diferente da atual
        if (passwordEncoder.matches(dto.newPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Nova senha deve ser diferente da senha atual");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }
}
