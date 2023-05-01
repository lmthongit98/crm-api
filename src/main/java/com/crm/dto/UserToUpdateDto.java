package com.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UserToUpdateDto {
    private Long id;

    @Size(min = 3, max = 100, message = "Username length must be between 3 and 100")
    @NotBlank
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank
    private String password;

    @NotBlank
    private String displayName;

    @NotBlank
    private String email;
}
