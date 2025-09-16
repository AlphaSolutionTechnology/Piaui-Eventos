package com.alphasolutions.piauieventos.repository;

import com.alphasolutions.piauieventos.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleModel, Integer> {
    Optional<UserRoleModel> findByRoleNameIgnoreCase(String roleName);
}
