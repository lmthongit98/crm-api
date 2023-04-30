package com.crm.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class UserWithRolesDto {
    private Long id;
    private String username;
    private String displayName;
    private String email;
    private List<RoleDto> roles;
}
