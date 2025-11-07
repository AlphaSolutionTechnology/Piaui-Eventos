package com.alphasolutions.piauieventos.controller;

import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.dto.UserUpdateDTO;
import com.alphasolutions.piauieventos.dto.PasswordUpdateDTO;
import com.alphasolutions.piauieventos.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        UserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserResponseDTO response = userService.getCurrentUser();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserUpdateDTO dto) {
        UserResponseDTO response = userService.updateUser(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordUpdateDTO dto) {
        userService.updatePassword(dto);
        return ResponseEntity.noContent().build();
    }
}
