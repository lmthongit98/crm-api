package com.crm.service;

import com.crm.dto.RoleDto;
import com.crm.model.Role;

import java.util.List;

public interface RoleService {
    List<RoleDto> findAll();

    RoleDto save(RoleDto roleDto);

    RoleDto findById(Long id);

    RoleDto update(Long id, RoleDto roleDto);

    Role getRoleEntityById(Long id);
}
