package com.crm.controller;

import com.crm.dto.RoleDto;
import com.crm.service.RoleService;
import com.crm.util.ErrorHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public Object findAll() {
        List<RoleDto> roles = roleService.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{role-id}")
    public Object findRoleById(@PathVariable("role-id") Long id) {
        RoleDto role = roleService.findById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping
    public Object createNewRole(@RequestBody @Valid RoleDto roleDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(bindingResult), HttpStatus.BAD_REQUEST);
        }
        RoleDto savedRole = roleService.save(roleDto);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

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
