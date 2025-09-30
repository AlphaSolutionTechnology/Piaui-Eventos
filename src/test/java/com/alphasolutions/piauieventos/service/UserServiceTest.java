package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.mapper.UserMapper;
import com.alphasolutions.piauieventos.model.UserModel;
import com.alphasolutions.piauieventos.model.UserRoleModel;
import com.alphasolutions.piauieventos.repository.UserRepository;
import com.alphasolutions.piauieventos.repository.UserRoleRepository;
import com.alphasolutions.piauieventos.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        System.out.println("=== Setup do teste iniciado ===");
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        System.out.println("=== Início do teste: deveCriarUsuarioComSucesso ===");

        // Arrange
        UserRequestDTO dto = new UserRequestDTO(null, "João", "joao@email.com",
                "123456", "999999999", null);
        System.out.println("DTO recebido: " + dto);

        UserModel entity = new UserModel();
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPassword(dto.password());
        entity.setPhoneNumber(dto.phoneNumber());

        UserRoleModel role = new UserRoleModel();
        role.setRoleId(1);
        role.setRoleName("USER");
        entity.setUserRole(role);

        UserModel saved = new UserModel();
        saved.setId(1L);
        saved.setName(entity.getName());
        saved.setEmail(entity.getEmail());
        saved.setPhoneNumber(entity.getPhoneNumber());
        saved.setUserRole(role);

        UserResponseDTO responseDTO = new UserResponseDTO(
                saved.getId(), saved.getName(), saved.getEmail(), saved.getPhoneNumber()
        );

        // Configuração dos mocks
        when(userRepository.existsByEmail(dto.email())).thenAnswer(invocation -> {
            System.out.println("Verificando se email já existe: " + dto.email());
            return false;
        });
        when(userMapper.toEntity(dto)).thenAnswer(invocation -> {
            System.out.println("Convertendo DTO para entidade");
            return entity;
        });
        when(passwordEncoder.encode(dto.password())).thenAnswer(invocation -> {
            System.out.println("Criptografando senha: " + dto.password());
            return "senhaCriptografada";
        });
        when(userRoleRepository.findByRoleNameIgnoreCase("USER")).thenAnswer(invocation -> {
            System.out.println("Buscando role padrão USER");
            return Optional.of(role);
        });
        when(userRepository.save(entity)).thenAnswer(invocation -> {
            System.out.println("Salvando usuário no repositório: " + entity.getName());
            return saved;
        });
        when(userMapper.toDto(saved)).thenAnswer(invocation -> {
            System.out.println("Convertendo entidade salva para DTO de resposta");
            return responseDTO;
        });

        // Act
        UserResponseDTO resultado = userService.createUser(dto);
        System.out.println("Resultado do createUser: " + resultado);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("João", resultado.name());
        assertEquals("joao@email.com", resultado.email());

        // Verificações
        verify(userRepository).existsByEmail(dto.email());
        verify(userMapper).toEntity(dto);
        verify(passwordEncoder).encode(dto.password());
        verify(userRoleRepository).findByRoleNameIgnoreCase("USER");
        verify(userRepository).save(entity);
        verify(userMapper).toDto(saved);

        System.out.println("=== Fim do teste: deveCriarUsuarioComSucesso ===\n");
    }

    @Test
    void deveFalharQuandoEmailJaExiste() {
        System.out.println("=== Início do teste: deveFalharQuandoEmailJaExiste ===");

        UserRequestDTO dto = new UserRequestDTO(null, "Maria", "maria@email.com",
                "123456", "888888888", null);

        when(userRepository.existsByEmail(dto.email())).thenAnswer(invocation -> {
            System.out.println("Verificando se email já existe: " + dto.email());
            return true;
        });

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(dto));

        System.out.println("Exceção lançada: " + ex.getMessage());

        assertEquals("Email já cadastrado", ex.getMessage());
        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository, never()).save(any());

        System.out.println("=== Fim do teste: deveFalharQuandoEmailJaExiste ===\n");
    }
}
