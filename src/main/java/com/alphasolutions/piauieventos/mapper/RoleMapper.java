package com.alphasolutions.piauieventos.mapper;

import com.alphasolutions.piauieventos.dto.RoleDTO;
import com.alphasolutions.piauieventos.model.UserRoleModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDto(UserRoleModel userRoleModel);
}
