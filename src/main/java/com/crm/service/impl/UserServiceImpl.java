package com.crm.service.impl;

import com.crm.dto.RoleDto;
import com.crm.dto.UserDto;
import com.crm.dto.UserWithRolesDto;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.user.User;
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
    public UserDto save(UserDto userDto) {
        User user = mapToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        UserDto dto = mapToDto(savedUser);
        dto.setPassword(null);
        return dto;
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
        if(!Objects.isNull(user.getGroup()) && !CollectionUtils.isEmpty(user.getGroup().getRoles())) {
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
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDto userDto = mapToDto(user);
            userDto.setPassword(null);
            return userDto;
        }).collect(Collectors.toList());
    }

    private UserDto mapToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

}
