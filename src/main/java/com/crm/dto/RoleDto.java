package com.crm.dto;

import com.crm.validation.anotation.UniqueRoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Long id;

    @Size(min = 3, max = 20, message = "url is not valid.")
    @NotBlank(message = "Role name must not be blank")
    @UniqueRoleName
    private String name;

    @NotBlank(message = "Description must not be blank.")
    private String description;
}
