package com.rishi.AuthanticationApplication.services;

import com.rishi.AuthanticationApplication.dtos.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByEmail(String email);

    UserDto updateUser(UserDto userDto, String userId);

    


}
