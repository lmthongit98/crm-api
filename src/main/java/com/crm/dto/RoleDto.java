package com.crm.dto;

import com.crm.security.enums.SecurityAuthority;
import com.crm.validation.anotation.UniqueRoleName;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Long id;

    @UniqueRoleName
    private SecurityAuthority name;

    @NotBlank(message = "Description must not be blank.")
    private String description;
}
