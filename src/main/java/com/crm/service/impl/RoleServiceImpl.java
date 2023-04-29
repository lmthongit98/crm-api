package com.crm.service.impl;

import com.crm.dto.RoleDto;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.model.role.Role;
import com.crm.repository.RoleRepository;
import com.crm.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public RoleDto save(RoleDto roleDto) {
        roleDto.setName(roleDto.getName().toUpperCase());
        checkIsRoleExist(roleDto.getName());
        Role savedRole = roleRepository.save(mapToEntity(roleDto));
        return mapToDto(savedRole);
    }

    @Override
    public RoleDto findById(Long id) {
        return mapToDto(getRoleEntityById(id));
    }

    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        Role role = getRoleEntityById(id);
        if (!role.getName().equalsIgnoreCase(roleDto.getName())) {
            checkIsRoleExist(roleDto.getName());
            role.setName(roleDto.getName().toUpperCase());
        }
        role.setDescription(roleDto.getDescription());
        Role updatedRole = roleRepository.save(role);
        return mapToDto(updatedRole);
    }

    private void checkIsRoleExist(String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName.toUpperCase());
        if (roleOpt.isPresent()) {
            throw new BadRequestException("Role name is already exist!");
        }
    }

    private Role getRoleEntityById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found for id: " + id));
    }

    private RoleDto mapToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

    private Role mapToEntity(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }

}
