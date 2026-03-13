package com.rishi.AuthanticationApplication.services.impl;

import com.rishi.AuthanticationApplication.dtos.UserDto;
import com.rishi.AuthanticationApplication.exceptions.ResourceNotFoundException;
import com.rishi.AuthanticationApplication.helpers.UserHelper;
import com.rishi.AuthanticationApplication.mappers.UserMapper;
import com.rishi.AuthanticationApplication.model.User;
import com.rishi.AuthanticationApplication.other.Provider;
import com.rishi.AuthanticationApplication.repository.UserRepository;
import com.rishi.AuthanticationApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw  new IllegalArgumentException("Email is required");
        }

        if(userRepository.existsByEmail(userDto.getEmail())) {

        }

        User user = userMapper.toEntity(userDto);
        user.setProvider(userDto.getProvider() !=  null ? userDto.getProvider() : Provider.LOCAL);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email id.."));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id.."));

        if (userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        if (userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.isEnable());
        existingUser.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String id) {
        User user = userRepository.findById(UserHelper.parseUUID(id)).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        return userMapper.toDto(user);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }
}
