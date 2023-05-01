package com.crm.dto;

import com.crm.security.enums.Permission;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Long id;

    private Permission name;

    @NotBlank(message = "Description must not be blank.")
    private String description;
}
