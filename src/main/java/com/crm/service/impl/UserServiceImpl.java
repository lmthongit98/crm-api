package com.crm.service.impl;

import com.crm.dto.*;
import com.crm.dto.request.UserRequestDto;
import com.crm.dto.response.UserResponseDto;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.User;
import com.crm.repository.UserRepository;
import com.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User user = mapToEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    public UserWithRolesDto findUserWithRolesByUsername(String username) {
        User user = findByUsername(username);
        UserWithRolesDto userWithRolesDto = UserWithRolesDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .build();
        if (!Objects.isNull(user.getGroup()) && !CollectionUtils.isEmpty(user.getGroup().getRoles())) {
            List<RoleDto> roleDtos = user.getGroup().getRoles().stream().map(role -> modelMapper.map(role, RoleDto.class)).toList();
            userWithRolesDto.setRoles(roleDtos);
        }
        return userWithRolesDto;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found for username: " + username));
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto update(Long id, UserToUpdateDto userToUpdateDto) {
        User user = findUserById(id);
        user.setDisplayName(userToUpdateDto.getUsername());
        if (!user.getUsername().equals(userToUpdateDto.getUsername())) {
            if (userRepository.findByUsername(userToUpdateDto.getUsername()).isPresent()) {
                throw new BadRequestException("Username is already used.");
            }
            user.setUsername(userToUpdateDto.getUsername());
        }
        if (!user.getEmail().equals(userToUpdateDto.getEmail())) {
            if (userRepository.findByEmail(userToUpdateDto.getEmail()).isPresent()) {
                throw new BadRequestException("Email is already used.");
            }
            user.setEmail(userToUpdateDto.getEmail());
        }
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public void changePassword(Long userId, PasswordDto passwordDto) {
        User user = findUserById(userId);
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is not correct!");
        }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            throw new BadRequestException("Confirm password is not correct!");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + id));
    }

    private UserResponseDto mapToDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }

    private User mapToEntity(UserRequestDto userRequestDto) {
        return modelMapper.map(userRequestDto, User.class);
    }

}
