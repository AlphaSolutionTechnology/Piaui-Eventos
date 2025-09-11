package com.alphasolutions.piauieventos.mapper;

import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.model.UserRoleModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    UserResponseDTO.RoleDTO toDto(UserRoleModel userRoleModel);
}
