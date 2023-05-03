package com.crm.service;

import com.crm.dto.GroupDto;
import com.crm.dto.GroupWithRolesDto;

import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();

    GroupWithRolesDto findById(Long groupId);

    GroupWithRolesDto addRole(Long groupId, Long roleId);

    GroupDto save(GroupDto groupDto);

    GroupWithRolesDto removeRole(Long groupId, Long roleId);

    GroupDto update(Long groupId, GroupDto groupDto);

    void addUser(Long groupId, Long userId);

    void removeUser(Long groupId, Long userId);

    void deleteGroup(Long id);
}
