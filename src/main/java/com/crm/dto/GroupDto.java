package com.crm.dto;

import com.crm.validation.anotation.UniqueGroupName;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDto {
    private Long id;

    @NotBlank(message = "Group name must not be blank.")
    @UniqueGroupName
    private String name;

    @NotBlank(message = "Description must not be blank.")
    private String description;
}
