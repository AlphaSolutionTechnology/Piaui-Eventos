package com.alphasolutions.piauieventos.repository;

import com.alphasolutions.piauieventos.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleModel, Integer> {
}
