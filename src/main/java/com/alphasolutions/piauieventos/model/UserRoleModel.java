package com.alphasolutions.piauieventos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_role")
@NoArgsConstructor
public class UserRoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role", nullable = false)
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;
}
