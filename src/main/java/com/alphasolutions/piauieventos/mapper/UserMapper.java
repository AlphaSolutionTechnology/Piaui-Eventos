package com.alphasolutions.piauieventos.mapper;

import com.alphasolutions.piauieventos.dto.UserRequestDTO;
import com.alphasolutions.piauieventos.dto.UserResponseDTO;
import com.alphasolutions.piauieventos.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {RoleMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "userRole", ignore = true)
    UserModel toEntity(UserRequestDTO dto);

    UserResponseDTO toDto(UserModel entity);

    List<UserResponseDTO> toDtoList(List<UserModel> users);

}