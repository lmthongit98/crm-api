package com.crm.service.impl;

import com.crm.dto.GroupDto;
import com.crm.dto.GroupWithRolesDto;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.Group;
import com.crm.model.Role;
import com.crm.model.User;
import com.crm.repository.GroupRepository;
import com.crm.service.GroupService;
import com.crm.service.RoleService;
import com.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final UserService userService;

    @Override
    public List<GroupDto> findAll() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public GroupWithRolesDto findById(Long groupId) {
        Group group = getGroupEntityById(groupId);
        return mapToDtoWithRoles(group);
    }

    @Override
    public GroupWithRolesDto addRole(Long groupId, Long roleId) {
        Group group = getGroupEntityById(groupId);
        Role role = roleService.getRoleEntityById(roleId);
        group.addRole(role);
        Group updatedGroup = groupRepository.save(group);
        return mapToDtoWithRoles(updatedGroup);
    }

    @Override
    public GroupWithRolesDto removeRole(Long groupId, Long roleId) {
        Group group = getGroupEntityById(groupId);
        Role role = roleService.getRoleEntityById(roleId);
        group.removeRole(role);
        Group updatedGroup = groupRepository.save(group);
        return mapToDtoWithRoles(updatedGroup);
    }

    @Override
    public GroupDto update(Long groupId, GroupDto groupDto) {
        Group group = getGroupEntityById(groupId);
        group.setName(group.getName());
        group.setDescription(groupDto.getDescription());
        Group updatedGroup = groupRepository.save(group);
        return mapToDto(updatedGroup);
    }

    @Override
    public void addUser(Long groupId, String username) {
        User user = userService.findByUsername(username);
        Group group = getGroupEntityById(groupId);
        group.addUser(user);
        groupRepository.save(group);
    }

    @Override
    public void removeUser(Long groupId, String username) {
        User user = userService.findByUsername(username);
        Group group = getGroupEntityById(groupId);
        group.removeUser(user);
        groupRepository.save(group);
    }

    @Override
    public GroupDto save(GroupDto groupDto) {
        Group group = mapToEntity(groupDto);
        Group savedGroup = groupRepository.save(group);
        return mapToDto(savedGroup);
    }

    private Group getGroupEntityById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group not found for id: " + groupId));
    }

    private GroupDto mapToDto(Group group) {
        return modelMapper.map(group, GroupDto.class);
    }

    private GroupWithRolesDto mapToDtoWithRoles(Group group) {
        return modelMapper.map(group, GroupWithRolesDto.class);
    }


    private Group mapToEntity(GroupDto groupDto) {
        return modelMapper.map(groupDto, Group.class);
    }

}
