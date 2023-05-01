package com.crm.dto.request;

import com.crm.model.user.UserStatus;
import com.crm.validation.anotation.UniqueEmail;
import com.crm.validation.anotation.UniqueUsername;
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
public class UserRequestDto {
    @Size(min = 3, max = 100, message = "Username length must be between 3 and 100")
    @UniqueUsername(message = "Username is already existed.")
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String displayName;

    @UniqueEmail(message = "Email is already existed.")
    @NotBlank
    private String email;

    private UserStatus status;
}
