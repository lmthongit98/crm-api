package com.crm.controller;

import com.crm.dto.GroupDto;
import com.crm.dto.GroupWithRolesDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.GroupService;
import com.crm.util.ErrorHelper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/groups")
public class GroupController {

    public final GroupService groupService;

    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping
    @HasAnyPermissions(permissions = {Permission.GROUP_VIEW, Permission.GROUP_EDIT})
    public Object findAllGroups() {
        List<GroupDto> groups = groupService.findAll();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping("/{group-id}")
    @HasAnyPermissions(permissions = {Permission.GROUP_VIEW, Permission.GROUP_EDIT})
    public Object findGroupById(@PathVariable("group-id") Long groupId) {
        GroupWithRolesDto group = groupService.findById(groupId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public Object createNewGroup(@Valid @RequestBody GroupDto groupDto,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(result), HttpStatus.BAD_REQUEST);
        }
        GroupDto group = groupService.save(groupDto);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PutMapping("/{group-id}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public Object updateGroup(@PathVariable("group-id") Long groupId, @Valid @RequestBody GroupDto groupDto,
                              BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(result), HttpStatus.BAD_REQUEST);
        }
        GroupDto group = groupService.update(groupId, groupDto);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/add-role/{group-id}/{role-id}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public Object addRole(@PathVariable(name = "group-id") @NotNull Long groupId,
                          @PathVariable(name = "role-id") @NotNull Long roleId) {
        GroupWithRolesDto group = groupService.addRole(groupId, roleId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/remove-role/{group-id}/{role-id}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public Object removeRole(@PathVariable(name = "group-id") @NotNull Long groupId,
                             @PathVariable(name = "role-id") @NotNull Long roleId) {
        GroupWithRolesDto group = groupService.removeRole(groupId, roleId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/add-user/{group-id}/{username}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public void addUser(@PathVariable(name = "group-id") @NotNull Long groupId,
                        @PathVariable(name = "username") @NotBlank String username) {
        groupService.addUser(groupId, username);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/remove-user/{group-id}/{username}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public void removeUser(@PathVariable(name = "group-id") @NotNull Long groupId,
                           @PathVariable(name = "username") @NotBlank String username) {
        groupService.removeUser(groupId, username);
    }


}
