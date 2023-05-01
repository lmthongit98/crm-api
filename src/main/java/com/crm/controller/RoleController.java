package com.crm.controller;

import com.crm.dto.RoleDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.RoleService;
import com.crm.common.util.ErrorHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("api/v1/roles")
//@Tag(name = "REST APIs for Role Resource")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Find all roles", description = "Find all roles in the database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = {Permission.ROLE_VIEW, Permission.ROLE_EDIT})
    @GetMapping
    public Object findAll() {
        List<RoleDto> roles = roleService.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @Operation(summary = "Find role by id", description = "Find role by id in the database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = {Permission.ROLE_VIEW, Permission.ROLE_EDIT})
    @GetMapping("/{role-id}")
    public Object findRoleById(@PathVariable("role-id") Long id) {
        RoleDto role = roleService.findById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @Operation(summary = "Crate role", description = "Create a new role and save to database")
    @ApiResponse(responseCode = "201", description = "Http Status 201 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.ROLE_EDIT)
    @PostMapping
    public Object createNewRole(@RequestBody @Valid RoleDto roleDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        RoleDto savedRole = roleService.save(roleDto);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @Operation(summary = "Update role by id", description = "Update role by id and save to the database")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @SecurityRequirement(name = "Bear Authentication")
    @HasAnyPermissions(permissions = Permission.ROLE_EDIT)
    @PutMapping("/{role-id}")
    public Object updateRole(@PathVariable("role-id") Long id, @RequestBody @Valid RoleDto roleDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        RoleDto updatedRole = roleService.update(id, roleDto);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

}
