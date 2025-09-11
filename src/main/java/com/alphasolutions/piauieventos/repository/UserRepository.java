package com.alphasolutions.piauieventos.repository;

import com.alphasolutions.piauieventos.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
