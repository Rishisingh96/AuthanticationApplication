package com.rishi.AuthanticationApplication.mappers;

import com.rishi.AuthanticationApplication.dtos.UserDto;
import com.rishi.AuthanticationApplication.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

    List<UserDto> toDtoList(List<User> users);
}

