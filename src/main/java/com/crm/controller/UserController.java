package com.crm.controller;

import com.crm.dto.UserDto;
import com.crm.dto.UserWithRolesDto;
import com.crm.security.anotations.HasEndpointAuthority;
import com.crm.security.enums.SecurityAuthority;
import com.crm.service.UserService;
import com.crm.util.ErrorHelper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @SecurityRequirement(name = "Bear Authentication")
    @HasEndpointAuthority(SecurityAuthority.CREATE_USER)
    @PostMapping
    public Object createNewUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        UserDto savedUser = userService.save(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping ("/{username}/roles")
    public Object findRoleByUsername(@PathVariable("username") String username) {
        UserWithRolesDto userWithRolesDto = userService.findUserWithRolesByUsername(username);
        return new ResponseEntity<>(userWithRolesDto, HttpStatus.OK);
    }

}
