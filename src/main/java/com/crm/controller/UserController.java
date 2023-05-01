package com.crm.controller;

import com.crm.dto.PasswordDto;
import com.crm.dto.request.UserRequestDto;
import com.crm.dto.UserToUpdateDto;
import com.crm.dto.UserWithRolesDto;
import com.crm.dto.response.UserResponseDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.UserService;
import com.crm.common.util.ErrorHelper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_EDIT)
    @PostMapping
    public Object createNewUser(@RequestBody @Valid UserRequestDto userRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        UserResponseDto savedUser = userService.save(userRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_EDIT)
    @PutMapping("/{id}")
    public Object updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserToUpdateDto userToUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        UserResponseDto updatedUser = userService.update(id, userToUpdateDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_EDIT)
    @PutMapping("/change-password/{user-id}")
    public Object changePassword(@PathVariable("user-id") Long userId, @RequestBody @Valid PasswordDto passwordDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(userId, passwordDto);
        return new ResponseEntity<>("Changed password successfully!", HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_VIEW)
    @GetMapping
    public Object getAllUsers() {
        List<UserResponseDto> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.USER_VIEW)
    @GetMapping ("/{username}/roles")
    public Object findRoleByUsername(@PathVariable("username") String username) {
        UserWithRolesDto userWithRolesDto = userService.findUserWithRolesByUsername(username);
        return new ResponseEntity<>(userWithRolesDto, HttpStatus.OK);
    }

}
