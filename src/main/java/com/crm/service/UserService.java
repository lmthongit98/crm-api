package com.crm.service;

import com.crm.dto.UserDto;
import com.crm.dto.UserWithRolesDto;
import com.crm.model.user.User;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);

    UserWithRolesDto findUserWithRolesByUsername(String username);

    User findByUsername(String username);
}
