package com.umatrix.example.mapstruct;

import com.umatrix.example.dto.UserDto;
import com.umatrix.example.models.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "name", source = "userDto.name")
    @Mapping(target = "username", source = "userDto.username")
    @Mapping(target = "password", source = "userDto.password")
    Users toUsers(UserDto userDto);

    UserDto toUserDto(Users users);

}
