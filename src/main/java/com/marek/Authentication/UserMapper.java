package com.marek.Authentication;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    public UserDTO toDTO(UserEntity user);
    public UserEntity toEntity(UserDTO userDTO);
    public List<UserDTO> toDTO(List<UserEntity> userEntities);
    public List<UserEntity> toEntity(List<UserDTO> userDTOs);
}
