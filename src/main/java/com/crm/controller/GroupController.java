package com.crm.controller;

import com.crm.dto.GroupDto;
import com.crm.dto.GroupWithRolesDto;
import com.crm.service.GroupService;
import com.crm.util.ErrorHelper;
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

    @GetMapping
    public Object findAllGroups() {
        List<GroupDto> groups = groupService.findAll();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/{group-id}")
    public Object findGroupById(@PathVariable("group-id") Long groupId) {
        GroupWithRolesDto group = groupService.findById(groupId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PostMapping
    public Object createNewGroup(@Valid @RequestBody GroupDto groupDto,
                                 BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(result), HttpStatus.BAD_REQUEST);
        }
        GroupDto group = groupService.save(groupDto);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PutMapping("/{group-id}")
    public Object updateGroup(@PathVariable("group-id") Long groupId, @Valid @RequestBody GroupDto groupDto,
                              BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ErrorHelper.getAllError(result), HttpStatus.BAD_REQUEST);
        }
        GroupDto group = groupService.update(groupId, groupDto);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PostMapping("/add-role/{group-id}/{role-id}")
    public Object addRole(@PathVariable(name = "group-id") @NotNull Long groupId,
                          @PathVariable(name = "role-id") @NotNull Long roleId) {
        GroupWithRolesDto group = groupService.addRole(groupId, roleId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PostMapping("/remove-role/{group-id}/{role-id}")
    public Object removeRole(@PathVariable(name = "group-id") @NotNull Long groupId,
                          @PathVariable(name = "role-id") @NotNull Long roleId) {
        GroupWithRolesDto group = groupService.removeRole(groupId, roleId);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

}
