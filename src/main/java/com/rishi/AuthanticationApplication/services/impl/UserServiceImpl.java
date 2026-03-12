package com.rishi.AuthanticationApplication.services.impl;

import com.rishi.AuthanticationApplication.dtos.UserDto;
import com.rishi.AuthanticationApplication.exceptions.ResourceNotFoundException;
import com.rishi.AuthanticationApplication.helpers.UserHelper;
import com.rishi.AuthanticationApplication.model.User;
import com.rishi.AuthanticationApplication.other.Provider;
import com.rishi.AuthanticationApplication.repository.UserRepository;
import com.rishi.AuthanticationApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw  new IllegalArgumentException("Email is required");
        }

        if(userRepository.existsByEmail(userDto.getEmail())) {

        }

        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() !=  null ? userDto.getProvider() : Provider.LOCAL);
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email id.."));
        return modelMapper.map(userRepository.findByEmail(email), UserDto.class);
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
        return modelMapper.map(updatedUser, UserDto.class);
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
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {

        return userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserDto.class)
        ).toList();
    }
}
