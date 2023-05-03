package com.crm.service;

import com.crm.dto.PasswordDto;
import com.crm.dto.request.UserRequestDto;
import com.crm.dto.UserToUpdateDto;
import com.crm.dto.UserWithRolesDto;
import com.crm.dto.response.UserResponseDto;
import com.crm.model.User;

import java.util.List;

public interface UserService {
    UserResponseDto save(UserRequestDto userRequestDto);

    UserWithRolesDto findUserWithRolesByUsername(String username);

    User findByUsername(String username);

    List<UserResponseDto> findAll();

    UserResponseDto update(Long id, UserToUpdateDto userToUpdateDto);

    void changePassword(Long userId, PasswordDto passwordDto);

    User findUserById(Long id);

    void delete(Long id);
}
