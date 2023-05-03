package com.crm.controller;

import com.crm.common.util.ErrorHelper;
import com.crm.dto.GroupDto;
import com.crm.dto.GroupWithRolesDto;
import com.crm.security.anotations.HasAnyPermissions;
import com.crm.security.enums.Permission;
import com.crm.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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

    @Operation(summary = "Find all groups")
    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping
    @HasAnyPermissions(permissions = {Permission.GROUP_VIEW, Permission.GROUP_EDIT})
    public Object findAllGroups() {
        List<GroupDto> groups = groupService.findAll();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @Operation(summary = "Find group by id")
    @SecurityRequirement(name = "Bear Authentication")
    @GetMapping("/{group-id}")
    @HasAnyPermissions(permissions = {Permission.GROUP_VIEW, Permission.GROUP_EDIT})
    public Object findGroupById(@PathVariable("group-id") Long groupId) {
        GroupWithRolesDto group = groupService.findById(groupId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @Operation(summary = "Create a new group")
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

    @Operation(summary = "Update group by id")
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

    @Operation(summary = "Add a role into group")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/add-role/{group-id}/{role-id}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public Object addRole(@PathVariable(name = "group-id") @NotNull Long groupId,
                          @PathVariable(name = "role-id") @NotNull Long roleId) {
        GroupWithRolesDto group = groupService.addRole(groupId, roleId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @Operation(summary = "Remove a role from group")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/remove-role/{group-id}/{role-id}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public Object removeRole(@PathVariable(name = "group-id") @NotNull Long groupId,
                             @PathVariable(name = "role-id") @NotNull Long roleId) {
        GroupWithRolesDto group = groupService.removeRole(groupId, roleId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @Operation(summary = "Add a user into group")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/add-user/{group-id}/{user-id}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public void addUser(@PathVariable(name = "group-id") @NotNull Long groupId,
                        @PathVariable(name = "user-id") @NotNull Long userId) {
        groupService.addUser(groupId, userId);
    }

    @Operation(summary = "Remove a user from group")
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/remove-user/{group-id}/{user-id}")
    @HasAnyPermissions(permissions = Permission.GROUP_EDIT)
    public void removeUser(@PathVariable(name = "group-id") @NotNull Long groupId,
                           @PathVariable(name = "user-id") @NotNull Long userId) {
        groupService.removeUser(groupId, userId);
    }

    @Operation(summary = "Delete group by id")
    @SecurityRequirement(name = "Bear Authentication")
    @DeleteMapping("/{id}")
    @HasAnyPermissions(permissions = Permission.GROUP_DELETION)
    public void deleteGroup(@PathVariable("id") @NotNull Long id) {
        groupService.deleteGroup(id);
    }


}
