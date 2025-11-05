package com.alphasolutions.piauieventos.repository;

import com.alphasolutions.piauieventos.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserModel u JOIN FETCH u.userRole WHERE u.email = :email")
    Optional<UserModel> findByEmailWithRole(@Param("email") String email);

    @Query("SELECT u FROM UserModel u JOIN FETCH u.userRole WHERE u.phoneNumber = :phone")
    Optional<UserModel> findByPhoneWithRole(@Param("phone") String phone);
}
