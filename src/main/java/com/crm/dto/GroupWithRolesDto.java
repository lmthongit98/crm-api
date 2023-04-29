package com.crm.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GroupWithRolesDto {
    private Long id;
    private String name;
    private String description;
    private Set<RoleDto> roles;
}
