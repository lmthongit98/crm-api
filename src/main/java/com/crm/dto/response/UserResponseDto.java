package com.crm.dto.response;

import com.crm.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto extends AbstractResponseDto<UserResponseDto> {
    private Long id;
    private String username;
    private String displayName;
    private String email;
    private UserStatus status;
    private String avatar;
}
